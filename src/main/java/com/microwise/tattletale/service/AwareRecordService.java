package com.microwise.tattletale.service;

import com.microwise.tattletale.model.AwareRecord;
import com.microwise.tattletale.core.Service;

import java.util.Date;
import java.util.List;


/**
 * 知晓记录Service
 */
public interface AwareRecordService extends Service<AwareRecord> {
    /**
     * 查找知晓记录
     *
     * @param sourceId
     * @param userId
     * @return
     */
    List<AwareRecord> findAwareRecords(String sourceId, String userId);

    /**
     * 查找知晓记录(模糊查询)
     *
     * @param sourceId
     * @param alarmPoints
     * @param users
     * @param begin
     * @param end
     * @return
     */
    List<AwareRecord> findAwareRecordsBlurry(String sourceId, List<String> alarmPoints,
                                             List<String> users, Date begin, Date end);
}
