package com.microwise.tattletale.service.impl;

import com.microwise.tattletale.core.AbstractService;
import com.microwise.tattletale.dao.AlarmStrategyMapper;
import com.microwise.tattletale.dao.AlarmThresholdMapper;
import com.microwise.tattletale.model.AlarmRecord;
import com.microwise.tattletale.model.AlarmStrategy;
import com.microwise.tattletale.model.AlarmThreshold;
import com.microwise.tattletale.service.AlarmStrategyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by li.jianfei on 2017/11/10.
 */
@Service
@Transactional
public class AlarmStrategyServiceImpl extends AbstractService<AlarmStrategy> implements AlarmStrategyService {
    @Resource
    private AlarmStrategyMapper alarmStrategyMapper;

    @Resource
    private AlarmThresholdMapper alarmThresholdMapper;

    @Override
    public void save(AlarmStrategy alarmStrategy) {
        super.save(alarmStrategy);
        Float target = null;
        Float floating = null;
        Integer conditionType = null;
        List<AlarmThreshold> alarmThresholds = alarmStrategy.getAlarmthresholds();
        for (int i = alarmThresholds.size() - 1; i >= 0; i--) {
            AlarmThreshold alarmThreshold = alarmThresholds.get(i);
            if (alarmThreshold.getSensorphysicalid() == null) {
                alarmThresholds.remove(alarmThreshold);
            } else {
                if (alarmThreshold.getSensorphysicalid() == 111) {
                    target = alarmThreshold.getTarget();
                    floating = alarmThreshold.getFloating();
                    conditionType = alarmThreshold.getConditiontype();
                }
            }
            alarmThreshold.setAlarmstrategyid(alarmStrategy.getId());
        }
        for (AlarmThreshold alarmThreshold : alarmThresholds) {
            AlarmThreshold threshold = alarmThreshold;
            if (threshold.getSensorphysicalid() == 112 || threshold.getSensorphysicalid() == 113) {
                alarmThreshold.setTarget(target);
                alarmThreshold.setFloating(floating);
                alarmThreshold.setConditiontype(conditionType);
            }
        }
        alarmThresholdMapper.insertList(alarmThresholds);
    }

    @Override
    public void delete(String id) {
        alarmStrategyMapper.deleteByPrimaryKey(id);
        Condition condition = new Condition(AlarmThreshold.class);
        condition.createCriteria().andEqualTo("alarmstrategyid", id);
        alarmThresholdMapper.deleteByCondition(condition);
    }

    @Override
    public AlarmStrategy findById(String id) {
        AlarmStrategy alarmStrategy = alarmStrategyMapper.selectByPrimaryKey(id);
        AlarmThreshold alarmThreshold = new AlarmThreshold();
        alarmThreshold.setAlarmstrategyid(id);
        List<AlarmThreshold> alarmThresholds = alarmThresholdMapper.select(alarmThreshold);
        alarmStrategy.setAlarmthresholds(alarmThresholds);
        return alarmStrategy;
    }

    @Override
    public void update(AlarmStrategy alarmStrategy) {
        /*List<Notifier> notifiers = alarmStrategy.getNotifiers();
        Gson gson = new Gson();
        String notifier = gson.toJson(notifiers);
        alarmStrategy.setNotifier(notifier);*/
        super.update(alarmStrategy);
        List<AlarmThreshold> alarmThresholds = alarmStrategy.getAlarmthresholds();
        Float target = null;
        Float floating = null;
        for (AlarmThreshold alarmThreshold : alarmThresholds) {
            if (alarmThreshold.getSensorphysicalid() == 111) {
                target = alarmThreshold.getTarget();
                floating = alarmThreshold.getFloating();
                break;
            }
        }
        for (AlarmThreshold alarmThreshold : alarmThresholds) {
            if (alarmThreshold.getSensorphysicalid() == 112 || alarmThreshold.getSensorphysicalid() == 113) {
                alarmThreshold.setFloating(floating);
                alarmThreshold.setTarget(target);
            }
            alarmThresholdMapper.updateByPrimaryKey(alarmThreshold);
        }
    }

    @Override
    public List<AlarmStrategy> findStrategiesByTargetId(String unitId, String targetId) {
        Condition condition = new Condition(AlarmStrategy.class);
        condition.createCriteria().andEqualTo("sourceid", unitId).andEqualTo("alarmpointid", targetId);
        return findByCondition(condition);
    }

    @Override
    public List<AlarmStrategy> findStrategies(AlarmRecord alarmRecord) {
        Condition condition = new Condition(AlarmStrategy.class);
        condition.createCriteria().andEqualTo("systemid", alarmRecord.getSystemtype())
                .andEqualTo("sourceid", alarmRecord.getSourceid())
                .andEqualTo("alarmpointtype", alarmRecord.getAlarmpointtype())
                .andEqualTo("alarmpointid", alarmRecord.getAlarmpointid());
        return super.findByCondition(condition);
    }

    @Override
    public List<AlarmStrategy> findByCondition(Condition condition) {
        List<AlarmStrategy> alarmStrategies = super.findByCondition(condition);
        for(AlarmStrategy alarmStrategy : alarmStrategies) {
            Condition alarmThresholdCondition = new Condition(AlarmThreshold.class);
            alarmThresholdCondition.createCriteria().andEqualTo("alarmstrategyid", alarmStrategy.getId());
            alarmStrategy.setAlarmthresholds(alarmThresholdMapper.selectByCondition(alarmThresholdCondition));
        }
        return alarmStrategies;
    }
}
