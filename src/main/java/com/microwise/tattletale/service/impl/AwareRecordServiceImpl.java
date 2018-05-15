package com.microwise.tattletale.service.impl;

import com.microwise.tattletale.core.AbstractService;
import com.microwise.tattletale.dao.AlarmRecordMapper;
import com.microwise.tattletale.dao.AwareRecordMapper;
import com.microwise.tattletale.model.AlarmRecord;
import com.microwise.tattletale.model.AwareRecord;
import com.microwise.tattletale.service.AwareRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 知晓记录mapper
 */
@Service
@Transactional
public class AwareRecordServiceImpl extends AbstractService<AwareRecord> implements AwareRecordService {
    @Resource
    private AwareRecordMapper awareRecordMapper;
    @Resource
    private AlarmRecordMapper alarmRecordMapper;

    @Override
    public List<AwareRecord> findAwareRecords(String sourceId, String userId) {
        Map paramMap = new HashMap();
        paramMap.put("sourceId", sourceId);
        paramMap.put("userId", userId);
        return awareRecordMapper.findAwareRecords(paramMap);
    }

    @Override
    public List<AwareRecord> findAwareRecordsBlurry(String sourceId, List<String> alarmPoints,
                                                    List<String> users, Date begin, Date end) {
        Map paramMap = new HashMap();
        paramMap.put("sourceId", sourceId);
        paramMap.put("alarmPoints", alarmPoints);
        paramMap.put("users", users);
        paramMap.put("begin", begin);
        paramMap.put("end", end);
        List<AwareRecord> awareRecords = awareRecordMapper.findAwareRecordsBlurry(paramMap);
        for (AwareRecord awareRecord : awareRecords) {
            AlarmRecord alarmRecord = alarmRecordMapper.selectByPrimaryKey(awareRecord.getAlarmRecordId());
            awareRecord.setAlarmRecord(alarmRecord);
        }
        return awareRecords;
    }
}
