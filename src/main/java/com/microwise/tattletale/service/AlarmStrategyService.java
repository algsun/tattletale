package com.microwise.tattletale.service;

import com.microwise.tattletale.core.Service;
import com.microwise.tattletale.model.AlarmRecord;
import com.microwise.tattletale.model.AlarmStrategy;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


/**
 * Created by li.jianfei on 2017/11/10.
 */
public interface AlarmStrategyService extends Service<AlarmStrategy> {


    /**
     * 添加报警策略
     *
     * @param alarmStrategy
     */
    void save(AlarmStrategy alarmStrategy);

    /**
     * 删除报警策略
     *
     * @param id 策略id
     */
    void delete(String id);

    /**
     * 根据策略id查询策略
     *
     * @param id
     * @return
     */
    AlarmStrategy findById(String id);

    /**
     * 更新报警策略
     *
     * @param alarmStrategy
     */
    void update(AlarmStrategy alarmStrategy);

    /**
     * 查找报警对象的报警策略
     *
     * @param unitId   区域/机构id
     * @param targetId 报警对象id
     * @return
     */
    List<AlarmStrategy> findStrategiesByTargetId(String unitId, String targetId);

    /**
     * 根据条件查询报警策略
     *
     * @param alarmRecord
     * @return
     */
    List<AlarmStrategy> findStrategies(AlarmRecord alarmRecord);
}
