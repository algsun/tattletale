package com.microwise.tattletale.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microwise.tattletale.core.AbstractService;
import com.microwise.tattletale.core.EmailUtil;
import com.microwise.tattletale.core.Global;
import com.microwise.tattletale.dao.AlarmRecordMapper;
import com.microwise.tattletale.model.*;
import com.microwise.tattletale.service.AlarmRecordService;
import com.microwise.tattletale.service.SensorinfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;


/**
 * 报警记录Service实现
 */
@Service
@Transactional
public class AlarmRecordServiceImpl extends AbstractService<AlarmRecord> implements AlarmRecordService {
    @Resource
    private AlarmRecordMapper alarmRecordMapper;

    @Resource
    private SensorinfoService sensorinfoService;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private TemplateEngine templateEngine;

    /**
     * 设备异常的通知人
     */
    @Value("${recipient}")
    private String recipient;

    @Override
    public List<AlarmRecord> findAlarmRecords(String sourceId, List<String> alarmPoints, int state, Date begin, Date end) {
        Condition condition = new Condition(AlarmRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("sourceid", sourceId);
        if (alarmPoints != null) {
            if (!alarmPoints.isEmpty()) {
                criteria.andIn("alarmpointid", alarmPoints);
            }
        }
        if (state == 1 || state == 0) {
            criteria.andEqualTo("state", state);
        }
        criteria.andBetween("alarmtime", begin, end);
        return findByCondition(condition);
    }

    @Override
    public List<AlarmRecord> findAlarmRecords(String sourceId, int state) {
        Condition condition = new Condition(AlarmRecord.class);
        Example.Criteria criteria = condition.createCriteria()
                .andEqualTo("sourceid", sourceId);
        if (state == 1 || state == 0) {
            criteria.andEqualTo("state", state);
        }
        return findByCondition(condition);
    }

    @Override
    public List<AlarmRecord> findAlarmRecords(String sourceId, String alarmPointId, int state) {
        Condition condition = new Condition(AlarmRecord.class);
        Example.Criteria criteria = condition.createCriteria()
                .andEqualTo("sourceid", sourceId)
                .andEqualTo("alarmpointid", alarmPointId);
        if (state == 1 || state == 0) {
            criteria.andEqualTo("state", state);
        }
        return findByCondition(condition);
    }

    @Override
    public List<AlarmRecord> findTasks(String sourceId, String userId) {
        Condition condition = new Condition(AlarmRecord.class);
        condition.createCriteria().andEqualTo("sourceid", sourceId).andEqualTo("state", 0);
        List<AlarmRecord> alarmHistories = findByCondition(condition);

        // 根据用户ID过滤
        Iterator<AlarmRecord> iterator = alarmHistories.iterator();
        while (iterator.hasNext()) {
            AlarmRecord alarmHistory = iterator.next();
            String notifierJson = alarmHistory.getNotifier();
            Gson gson = new Gson();
            List<Notifier> notifiers = gson.fromJson(notifierJson, new TypeToken<List<Notifier>>() {
            }.getType());
            //List<String> notifiers = Arrays.asList(alarmHistory.getNotifier().split(","));
            List<String> notifierIds = Lists.newArrayList();
            for (Notifier notifier : notifiers) {
                notifierIds.add(notifier.getId());
            }
            if (!notifierIds.contains(userId)) {
                iterator.remove();
            }
        }
        return alarmHistories;
    }


    @Override
    public int processedCount(String sourceId, Date begin, Date end) {
        return countAlarmRecords(sourceId, 1, begin, end);
    }

    @Override
    public int pendingCount(String sourceId, Date begin, Date end) {
        return countAlarmRecords(sourceId, 0, begin, end);
    }

    @Override
    public int pendingCount(String sourceId, String userId) {
        return findTasks(sourceId, userId).size();
    }

    @Override
    public AlarmRecord findTask(String id) {
        return alarmRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AlarmRecord> findAlarmRecords(AlarmRecord alarmRecord) {
        Condition condition = new Condition(AlarmRecord.class);
        condition.createCriteria().andEqualTo("systemtype", alarmRecord.getSystemtype())
                .andEqualTo("sourceid", alarmRecord.getSourceid())
                .andEqualTo("alarmpointtype", alarmRecord.getAlarmpointtype())
                .andEqualTo("alarmpointid", alarmRecord.getAlarmpointid())
                .andEqualTo("alarmtype", AlarmRecord.ALARM_TYPE_THRESHOLD)
                .andEqualTo("factor", alarmRecord.getFactor());
        condition.setOrderByClause("alarmtime desc");
        return super.findByCondition(condition);
    }

    @Override
    public AlarmRecord buildAlarmRecord(DataReceived dataReceived, AlarmStrategy alarmStrategy, List<AlarmThreshold> thresholds, List<Location> alarmLocations) {
        AlarmRecord alarmHistory = preInsertAlarmRecord(dataReceived);
        alarmHistory.setFactor(getFactor(dataReceived, thresholds, alarmLocations));
        alarmHistory.setNotifier(alarmStrategy.getNotifier());
        alarmHistory.setAlarmtype(AlarmRecord.ALARM_TYPE_THRESHOLD);
        return alarmHistory;
    }

    @Override
    public void insertAlarmRecord(AlarmRecord alarmHistory) {
        alarmRecordMapper.insertSelective(alarmHistory);
    }

    @Override
    public AlarmRecord buildAlarmRecord(DataReceived dataReceived, List<AlarmStrategy> alarmStrategies) {
        AlarmRecord alarmHistory = preInsertAlarmRecord(dataReceived);
        alarmHistory.setFactor(getFactor(dataReceived));
        Set<Notifier> notifierSet = new HashSet<>();
        Gson gson = new Gson();
        for (AlarmStrategy alarmStrategy : alarmStrategies) {
            List<Notifier> notifiers = gson.fromJson(alarmStrategy.getNotifier(), new TypeToken<List<Notifier>>() {
            }.getType());
            for (Notifier notifier : notifiers) {
                notifierSet.add(notifier);
            }
        }
        alarmHistory.setNotifier(gson.toJson(notifierSet));
        alarmHistory.setAlarmtype(AlarmRecord.ALARM_TYPE_TIMEOUT);
        return alarmHistory;
    }

    private int countAlarmRecords(String sourceId, int state, Date begin, Date end) {
        Condition condition = new Condition(AlarmRecord.class);
        condition.createCriteria().andEqualTo("sourceid", sourceId)
                .andEqualTo("state", state)
                .andBetween("alarmtime", begin, end);
        return alarmRecordMapper.selectCountByCondition(condition);
    }

    @Override
    public int totalPendingCount(String sourceId) {
        Condition condition = new Condition(AlarmRecord.class);
        condition.createCriteria().andEqualTo("sourceid", sourceId)
                .andEqualTo("state", 0);
        return alarmRecordMapper.selectCountByCondition(condition);
    }

    @Override
    public AlarmRecord preInsertAlarmRecord(DataReceived dataReceived) {
        Integer systemType = dataReceived.getSystemType();
        AlarmRecord alarmHistory = new AlarmRecord();
        alarmHistory.setSystemtype(systemType);
        alarmHistory.setSourceid(dataReceived.getUnit().getId());
        if (dataReceived.getLocation() == null) {
            alarmHistory.setAlarmpointid(dataReceived.getTarget().getId());
        } else {
            alarmHistory.setAlarmpointid(dataReceived.getLocation().getId());
        }
        alarmHistory.setAlarmtime(dataReceived.getTimeStamp());
        alarmHistory.setState(AlarmRecord.STATE_UNTREATED);
        if (systemType == Global.SystemFlag.GALAXY) {
            if (dataReceived.getLocation() == null) {
                alarmHistory.setAlarmpointtype(AlarmRecord.ALARM_POINT_ZONE);
            } else {
                alarmHistory.setAlarmpointtype(AlarmRecord.ALARM_POINT_LOCATION);
            }
        } else if (systemType == Global.SystemFlag.TERMINATOR) {
            alarmHistory.setAlarmpointtype(AlarmRecord.ALARM_POINT_RELIC);
        }
        return alarmHistory;
    }

    @Override
    public void sendAlarmThresholdEmail(DataReceived dataReceived, AlarmStrategy alarmStrategy,
                                        List<AlarmThreshold> thresholds, List<Location> alarmLocations) throws Exception {
        Timestamp timeStamp = dataReceived.getTimeStamp();
        Context context = EmailUtil.getContextInstance();
        if (isDisturbTime(timeStamp, alarmStrategy.getAlarmbegintime(), alarmStrategy.getAlarmendtime())) {
            context.setVariable("text", getFactor(dataReceived, thresholds, alarmLocations) + "请立即前往处理。");
            for (Notifier notifier : alarmStrategy.getNotifiers()) {
                context.setVariable("name", notifier.getName());
                emailUtil.prepareAndSend(notifier.getEmail(), "警报!警报!", templateEngine.process("emailtemplates/alarmTemplate", context));
            }
        }
    }

    @Override
    public void sendAlarmDeviceEmail(DataReceived dataReceived, List<AlarmStrategy> alarmStrategies) throws Exception {
        Context context = EmailUtil.getContextInstance();
        context.setVariable("name", recipient.substring(0, recipient.indexOf("@")));
        context.setVariable("text", getFactor(dataReceived) + "。请立即前往处理。");
        emailUtil.prepareAndSend(recipient, "警报!警报!", templateEngine.process("emailtemplates/alarmTemplate", context));
        Set<Notifier> notifierSet = new HashSet<>();
        for (AlarmStrategy alarmStrategy : alarmStrategies) {
            notifierSet.addAll(alarmStrategy.getNotifiers());
        }
        for (Notifier notifier : notifierSet) {
            context.setVariable("name", notifier.getName());
            emailUtil.prepareAndSend(notifier.getEmail(), "警报!警报!", templateEngine.process("emailtemplates/alarmTemplate", context));
        }
    }

    /**
     * 生成阈值报警原因
     *
     * @return
     */
    private String getFactor(DataReceived dataReceived, List<AlarmThreshold> thresholds, List<Location> alarmLocations) {
        String cnName = null;
        String conditionType = null;
        StringBuilder factor = new StringBuilder();
        for (AlarmThreshold alarmThreshold : thresholds) {
            Condition condition = new Condition(Sensorinfo.class);
            condition.createCriteria().andEqualTo("sensorphysicalid", alarmThreshold.getSensorphysicalid());
            Sensorinfo sensorinfo = sensorinfoService.findByCondition(condition).get(0);
            cnName = sensorinfo.getCnName();
            conditionType = alarmThreshold.parseConditionType();
        }
        for (Location location : alarmLocations) {
            factor.append(location.getLocationName()).append(cnName).append("(").append(conditionType).append(")").append(";");
        }

        if (dataReceived.getLocation() == null) {
            return dataReceived.getTarget().getName() + ":" + factor;
        } else {
            return dataReceived.getLocation().getLocationName() + ":" + factor;
        }
    }

    /**
     * 判断是否是免打扰时段
     *
     * @return 返回true表示不是免打扰时段
     */
    private Boolean isDisturbTime(Date alarmDate, Date beginTime, Date endTime) {
        if (beginTime.equals(endTime)) {
            return true;
        } else if (beginTime.before(endTime)) {
            if (alarmDate.before(beginTime) || alarmDate.after(endTime)) {
                return true;
            }
        } else {
            if (alarmDate.before(beginTime) && alarmDate.after(endTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成设备报警原因
     *
     * @param dataReceived
     * @return
     */
    private String getFactor(DataReceived dataReceived) {
        Location location;
        Target target = dataReceived.getTarget();
        if (target == null) {
            location = dataReceived.getLocation();
        } else {
            location = target.getLocations().get(0);
        }
        Device device = location.getDevice();
        String deviceNum = device.getDeviceId().substring(8);
        String deviceType = parseDeviceType(device.getDeviceType());
        String anomaly = parseAnomalyType(device.getAnomaly());
        StringBuilder factor = new StringBuilder();
        if (target == null) {
            factor.append(deviceNum).append("号").append(deviceType).append(anomaly);
        } else {
            factor.append(target.getName()).append(location.getLocationName()).append(deviceNum).append("号")
                    .append(deviceType).append(anomaly);
        }
        return factor.toString();
    }

    private String parseDeviceType(int deviceType) {
        switch (deviceType) {
            case 1:
                return "节点";
            case 2:
                return "中继";
            case 3:
                return "主模块";
            case 4:
                return "从模块";
            case 7:
                return "网关";
            default:
                return "设备";
        }
    }

    private String parseAnomalyType(int anomalyType) {
        switch (anomalyType) {
            case -1:
                return "超时";
            case 2:
                return "低电压";
            case 3:
                return "掉电";
            default:
                return "异常";
        }
    }
}
