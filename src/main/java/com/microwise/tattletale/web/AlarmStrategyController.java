package com.microwise.tattletale.web;

import com.microwise.tattletale.core.Result;
import com.microwise.tattletale.core.ResultGenerator;
import com.microwise.tattletale.model.AlarmStrategy;
import com.microwise.tattletale.service.AlarmStrategyService;
import com.microwise.tattletale.service.AlarmThresholdService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报警策略controller
 *
 * @author bai.weixing
 * @since 2017/11/10.
 */
@RestController
@RequestMapping("/alarmStrategies")
public class AlarmStrategyController {

    private static final Logger logger = LoggerFactory.getLogger(AlarmStrategyController.class);

    @Resource
    private AlarmStrategyService alarmStrategyService;

    @Resource
    private AlarmThresholdService alarmThresholdService;

    @ApiOperation(value = "添加报警策略")
    @PostMapping
    public Result add(@RequestBody AlarmStrategy alarmStrategy) {
        try {
            alarmStrategyService.save(alarmStrategy);
        } catch (Exception e) {
            logger.error("添加报警策略失败", e);
            return ResultGenerator.genFailResult("添加报警策略失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "通过id删除报警策略")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        try {
            alarmStrategyService.delete(id);
        } catch (Exception e) {
            logger.error("删除报警策略失败", e);
            return ResultGenerator.genFailResult("删除报警策略失败");
        }
        return ResultGenerator.genSuccessResult();
    }
    @ApiOperation(value = "更新报警策略")
    @PostMapping("/update")
    public Result update(@RequestBody AlarmStrategy alarmStrategy) {
        try {
            alarmStrategyService.update(alarmStrategy);
        } catch (Exception e) {
            logger.error("更新报警策略失败", e);
            return ResultGenerator.genFailResult("更新报警策略失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "通过id查询报警策略")
    @GetMapping("/{id}")
    public Result edit(@PathVariable String id) {
        AlarmStrategy alarmStrategy = alarmStrategyService.findById(id);
        return ResultGenerator.genSuccessResult(alarmStrategy);
    }

    @ApiOperation(value = "查询报警策略列表")
    @GetMapping("/{systemId}/{sourceId}/{alarmPointType}/{alarmPointId}")
    public Result list(@PathVariable String systemId, @PathVariable String sourceId, @PathVariable Integer alarmPointType,
                       @PathVariable String alarmPointId) {
        Condition condition = new Condition(AlarmStrategy.class);
        condition.createCriteria().andEqualTo("systemid", systemId).andEqualTo("sourceid", sourceId)
                .andEqualTo("alarmpointtype", alarmPointType).andEqualTo("alarmpointid", alarmPointId);
        condition.setOrderByClause("updatetime desc");
        List<AlarmStrategy> alarmStrategys = alarmStrategyService.findByCondition(condition);
        return ResultGenerator.genSuccessResult(alarmStrategys);
    }

}
