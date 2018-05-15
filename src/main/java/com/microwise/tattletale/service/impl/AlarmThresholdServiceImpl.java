package com.microwise.tattletale.service.impl;

import com.microwise.tattletale.dao.AlarmThresholdMapper;
import com.microwise.tattletale.model.AlarmStrategy;
import com.microwise.tattletale.model.AlarmThreshold;
import com.microwise.tattletale.service.AlarmThresholdService;
import com.microwise.tattletale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by li.jianfei on 2017/11/16.
 */
@Service
@Transactional
public class AlarmThresholdServiceImpl extends AbstractService<AlarmThreshold> implements AlarmThresholdService {
    @Resource
    private AlarmThresholdMapper alarmThresholdMapper;

    @Override
    public List<AlarmThreshold> findThresholdByStrategyId(String strategyId) {
        Condition condition = new Condition(AlarmThreshold.class);
        condition.createCriteria().andEqualTo("alarmstrategyid", strategyId);
        return findByCondition(condition);
    }
}
