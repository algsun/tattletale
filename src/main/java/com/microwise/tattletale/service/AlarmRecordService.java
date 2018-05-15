package com.microwise.tattletale.service;

import com.microwise.tattletale.model.*;
import com.microwise.tattletale.core.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;


/**
 * 报警记录Service
 */
public interface AlarmRecordService extends Service<AlarmRecord> {

    /**
     * 查询报警历史记录
     *
     * @param sourceId    站点id或机构id
     * @param alarmPoints 区域、文物或位置点ID集合
     * @param state       处理状态 0-待处理；1-已处理; -1-全部
     * @param begin       开始时间
     * @param end         结束时间
     * @return
     */
    List<AlarmRecord> findAlarmRecords(String sourceId, List<String> alarmPoints, int state, Date begin, Date end);

    /**
     * 查询报警历史记录
     *
     * @param sourceId 站点id或机构id
     * @param state    处理状态 0-待处理；1-已处理; -1-全部
     * @return
     */
    List<AlarmRecord> findAlarmRecords(String sourceId, int state);

    /**
     * 查询报警历史记录
     *
     * @param sourceId     站点id或机构id
     * @param alarmPointId 区域、文物或位置点ID
     * @param state        处理状态 0-待处理；1-已处理; -1-全部
     * @return
     */
    List<AlarmRecord> findAlarmRecords(String sourceId, String alarmPointId, int state);

    /**
     * 查询用户的报警任务
     *
     * @return
     */
    List<AlarmRecord> findTasks(String sourceId, String userId);

    /**
     * 指定时间内已处理的报警任务
     *
     * @param sourceId
     * @param begin
     * @param end
     * @return
     */
    int processedCount(String sourceId, Date begin, Date end);

    /**
     * 指定时间内待处理的报警任务
     *
     * @param sourceId
     * @param begin
     * @param end
     * @return
     */
    int pendingCount(String sourceId, Date begin, Date end);

    /**
     * 所有未处理的报警记录
     *
     * @param sourceId
     * @return
     */
    int totalPendingCount(String sourceId);

    /**
     * 指定用户待处理的任务
     *
     * @param sourceId
     * @param userId
     * @return
     */
    int pendingCount(String sourceId, String userId);

    AlarmRecord findTask(String id);

    /**
     * 根据条件查询报警记录
     *
     * @param alarmRecord
     * @return
     */
    List<AlarmRecord> findAlarmRecords(AlarmRecord alarmRecord);

    /**
     * 生成阈值报警记录
     *
     * @param dataReceived
     * @param alarmStrategy
     * @param thresholds
     * @param alarmLocations
     * @return
     */
    AlarmRecord buildAlarmRecord(DataReceived dataReceived, AlarmStrategy alarmStrategy, List<AlarmThreshold> thresholds, List<Location> alarmLocations);

    /**
     * 插入报警记录
     *
     * @param alarmHistory
     */
    void insertAlarmRecord(AlarmRecord alarmHistory);

    /**
     * 生成设备超时报警记录
     *
     * @param dataReceived
     * @param alarmStrategies
     * @return
     */
    AlarmRecord buildAlarmRecord(DataReceived dataReceived, List<AlarmStrategy> alarmStrategies);

    /**
     * 插入报警记录前预处理
     *
     * @param dataReceived
     * @return
     */
    AlarmRecord preInsertAlarmRecord(DataReceived dataReceived);

    /**
     * 生成监测指标邮件并发送
     *
     * @throws MessagingException
     */
    void sendAlarmThresholdEmail(DataReceived dataReceived, AlarmStrategy alarmStrategy, List<AlarmThreshold>
            thresholds, List<Location> alarmLocations) throws Exception;

    /**
     * 生成设备超时邮件并发送
     *
     * @param dataReceived
     * @throws MessagingException
     */
    void sendAlarmDeviceEmail(DataReceived dataReceived, List<AlarmStrategy> alarmStrategies) throws Exception;
}
