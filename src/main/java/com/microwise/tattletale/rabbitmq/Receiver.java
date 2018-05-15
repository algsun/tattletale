package com.microwise.tattletale.rabbitmq;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.microwise.tattletale.config.SocketSessionRegistry;
import com.microwise.tattletale.core.Global;
import com.microwise.tattletale.entity.LocationSensor;
import com.microwise.tattletale.model.*;
import com.microwise.tattletale.service.AlarmRecordService;
import com.microwise.tattletale.service.AlarmStrategyService;
import com.microwise.tattletale.service.AlarmThresholdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by sun.cong on 2017/11/15.
 *
 * @author sun.cong
 * @since 2017/11/15
 */
@Component
public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    private AlarmStrategyService alarmStrategyService;
    @Autowired
    private AlarmThresholdService alarmThresholdService;
    @Autowired
    private AlarmRecordService alarmRecordService;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private SocketSessionRegistry socketSessionRegistry;

    /**
     * 设备异常的通知人
     */
    @Value("${recipient}")
    private String recipient;

    @RabbitListener(queues = "device-state")
    public void processDeviceState(String deviceState) {
        try {
            Gson gson = new Gson();
            DataReceived dataReceived = gson.fromJson(deviceState, DataReceived.class);
            AlarmRecord alarmRecord = alarmRecordService.preInsertAlarmRecord(dataReceived);
            List<AlarmStrategy> alarmStrategies = alarmStrategyService.findStrategies(alarmRecord);
            List<AlarmRecord> alarmRecords = alarmRecordService.findAlarmRecords(alarmRecord);
            if ((alarmRecords.size() > 0 && (alarmRecords.get(0).getState() == AlarmRecord.STATE_TREATED)) || alarmRecords.size() == 0) {
                AlarmRecord alarmHistory = alarmRecordService.buildAlarmRecord(dataReceived, alarmStrategies);
                this.sendAlarmMessage(alarmHistory.getNotifiers(), alarmHistory);
                alarmRecordService.insertAlarmRecord(alarmHistory);
                alarmRecordService.sendAlarmDeviceEmail(dataReceived, alarmStrategies);
            }
        } catch (MessagingException e) {
            logger.error("邮件发送失败", e);
        } catch (NullPointerException e) {
            logger.error("空指针异常", e);
        } catch (ClassCastException e) {
            logger.error("类型转换异常", e);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }
    }

    @RabbitListener(queues = "sensor-data")
    public void processSensorData(String sensorData) {
        List<Location> alarmLocations = Lists.newArrayList();
        try {
            Gson gson = new Gson();
            DataReceived dataReceived = gson.fromJson(sensorData, DataReceived.class);
            List<AlarmStrategy> strategies;
            if (dataReceived.getLocation() == null) {
                strategies = alarmStrategyService.findStrategiesByTargetId(dataReceived.getUnit().getId(),
                        dataReceived.getTarget().getId());
            } else {
                strategies = alarmStrategyService.findStrategiesByTargetId(dataReceived.getUnit().getId(),
                        dataReceived.getLocation().getId());
            }
            Boolean alarm = false;
            List<AlarmThreshold> alarmThresholds = Lists.newArrayList();
            AlarmStrategy strategy = new AlarmStrategy();
            if (!strategies.isEmpty()) {
                strategy:
                for (AlarmStrategy alarmStrategy : strategies) {
                    List<AlarmThreshold> thresholds = alarmThresholdService.findThresholdByStrategyId(alarmStrategy.getId());
                    strategy = alarmStrategy;
                    for (AlarmThreshold alarmThreshold : thresholds) {
                        alarm = thresholdCompare(dataReceived, alarmThreshold, alarmLocations);
                        if (alarm) {
                            alarmThresholds.add(alarmThreshold);
                            continue;
                        }
                    }
                }
                AlarmRecord alarmRecord = alarmRecordService.preInsertAlarmRecord(dataReceived);
                List<AlarmRecord> alarmRecords = alarmRecordService.findAlarmRecords(alarmRecord);
                if (alarmRecords.size() == 0 || (alarm && (alarmRecords.size() > 0 && alarmRecords.get(0).getState() == AlarmRecord.STATE_TREATED))) {
                    AlarmRecord alarmHistory = alarmRecordService.buildAlarmRecord(dataReceived, strategy, alarmThresholds, alarmLocations);
                    sendAlarmMessage(strategy.getNotifiers(), alarmHistory);
                    alarmRecordService.insertAlarmRecord(alarmHistory);
                    alarmRecordService.sendAlarmThresholdEmail(dataReceived, strategy, alarmThresholds, alarmLocations);
                }
            }
        } catch (NullPointerException e) {
            logger.error("空指针异常", e);
        } catch (ClassCastException e) {
            logger.error("类型转换异常", e);
        } catch (Exception e) {
            logger.error("处理阈值报警异常", e);
        }
    }

    /**
     * 阈值比较
     *
     * @param alarmThreshold
     * @return 返回true表示此监测指标满足报警条件
     */
    private Boolean thresholdCompare(DataReceived dataReceived, AlarmThreshold alarmThreshold, List<Location> alarmLocations) {
        Integer conditionType = alarmThreshold.getConditiontype();
        double target = alarmThreshold.getTarget().doubleValue();
        double floating = 0;
        Integer showType = alarmThreshold.getShowtype();
        if (showType == Global.ShowTypes.SHOWTYPE_DEFAULT) {
            if (Global.ConditionTypes.CONDITIONTYPE_RANGE == alarmThreshold.getConditiontype()) {
                floating = alarmThreshold.getFloating();
            }
        }
        List<Location> locations;
        if (dataReceived.getLocation() == null) {
            locations = dataReceived.getTarget().getLocations();
        } else {
            locations = Lists.newArrayList(dataReceived.getLocation());
        }
        for (Location location : locations) {
            Map<Integer, LocationSensor> sensorDataMap = location.getSensorData();
            for (Integer sensorPhysicalId : sensorDataMap.keySet()) {
                Double sensorPhysicalValue;
                if (Objects.equals(alarmThreshold.getSensorphysicalid(), sensorPhysicalId)) {
                    sensorPhysicalValue = sensorDataMap.get(sensorPhysicalId).getSensorPhysicalValue();
                    if (showType == Global.ShowTypes.SHOWTYPE_DEFAULT) {
                        if (conditionType == Global.ConditionTypes.CONDITIONTYPE_RANGE) {
                            if (sensorPhysicalValue > target + floating || sensorPhysicalValue < target - floating) {
                                alarmLocations.add(location);
                                return true;
                            } else {
                                return false;
                            }
                        } else if (conditionType == Global.ConditionTypes.CONDITIONTYPE_GREATER) {
                            if (sensorPhysicalValue > target) {
                                alarmLocations.add(location);
                                return true;
                            } else {
                                return false;
                            }
                        } else if (conditionType == Global.ConditionTypes.CONDITIONTYPE_SMALLER) {
                            if (sensorPhysicalValue < target) {
                                alarmLocations.add(location);
                                return true;
                            } else {
                                return false;
                            }
                        } else if (conditionType == Global.ConditionTypes.CONDITIONTYPE_GREATER_EQUAL) {
                            if (sensorPhysicalValue >= target) {
                                alarmLocations.add(location);
                                return true;
                            } else {
                                return false;
                            }
                        } else if (conditionType == Global.ConditionTypes.CONDITIONTYPE_SMALLER_EQUAL) {
                            if (sensorPhysicalValue <= target) {
                                alarmLocations.add(location);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else if (showType == Global.ShowTypes.SHOWTYPE_SWITCH) {
                        if (sensorPhysicalValue == target) {
                            alarmLocations.add(location);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 向通知人推送报警信息
     *
     * @param notifiers
     * @param alarmRecord
     */
    private void sendAlarmMessage(List<Notifier> notifiers, AlarmRecord alarmRecord) throws ExecutionException {
        Gson gson = new Gson();
        String alarmMessage = gson.toJson(alarmRecord);
        for (Notifier notifier : notifiers) {
            Set<String> sessionIds = socketSessionRegistry.getSessionIds(notifier.getId());
            for (String sessionId : sessionIds) {
                template.convertAndSendToUser(sessionId, "/topic/greetings", alarmMessage);
            }
        }
    }
}
