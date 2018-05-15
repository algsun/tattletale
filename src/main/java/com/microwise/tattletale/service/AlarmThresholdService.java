package com.microwise.tattletale.service;

import com.microwise.tattletale.model.AlarmThreshold;
import com.microwise.tattletale.core.Service;

import java.util.List;


/**
 * Created by li.jianfei on 2017/11/16.
 */
public interface AlarmThresholdService extends Service<AlarmThreshold> {

    /**
     * 查找报警阈值
     *
     * @param strategyId 报警策略id
     * @return
     */
    List<AlarmThreshold> findThresholdByStrategyId(String strategyId);
}
