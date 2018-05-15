package com.microwise.tattletale.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.microwise.tattletale.core.Result;
import com.microwise.tattletale.core.ResultGenerator;
import com.microwise.tattletale.model.AlarmRecord;
import com.microwise.tattletale.service.AlarmRecordService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报警记录接口
 *
 * @author sun.cong
 * @create 2017-11-28 13:07
 */
@RestController
@RequestMapping("/alarm-records")
public class AlarmRecordController {
    @Resource
    private AlarmRecordService alarmHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(AlarmRecordController.class);

    @PostMapping
    public Result add(@RequestBody AlarmRecord alarmHistory) {
        alarmHistoryService.save(alarmHistory);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        alarmHistoryService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody AlarmRecord alarmHistory) {
        alarmHistoryService.update(alarmHistory);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable String id) {
        AlarmRecord alarmHistory = alarmHistoryService.findTask(id);
        return ResultGenerator.genSuccessResult(alarmHistory);
    }

    @GetMapping
    @ApiOperation(value = "查询报警记录")
    public Result<PageInfo<AlarmRecord>> list(@RequestParam String sourceId,
                                              @RequestParam(required = false) List<String> alarmPoints,
                                              @RequestParam(defaultValue = "-1") int state,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                              @RequestParam(defaultValue = "0") int pageNum,
                                              @RequestParam(defaultValue = "0") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AlarmRecord> list = alarmHistoryService.findAlarmRecords(sourceId, alarmPoints, state, begin, end);
        return ResultGenerator.genSuccessResult(new PageInfo<>(list));
    }

    @GetMapping("/alarm-point")
    @ApiOperation(value = "查询报警点报警记录")
    public Result<List<AlarmRecord>> list(@RequestParam String sourceId, @RequestParam String alarmPointId,
                                          @RequestParam(defaultValue = "-1") int state) {
        List<AlarmRecord> list = alarmHistoryService.findAlarmRecords(sourceId, alarmPointId, state);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/daily-report")
    @ApiOperation(value = "统计日报")
    public Result dailyReport(@RequestParam String sourceId, @RequestParam String userId,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        Map<String, Object> data = Maps.newHashMap();
        try {
            int processedCount = alarmHistoryService.processedCount(sourceId, begin, end);
            int pendingCount = alarmHistoryService.pendingCount(sourceId, begin, end);
            int totalCount = processedCount + pendingCount;
            int totalPendingCount = alarmHistoryService.totalPendingCount(sourceId);
            int userPendingCount = alarmHistoryService.pendingCount(sourceId, userId);

            data.put("totalCount", totalCount);
            data.put("totalPendingCount", totalPendingCount);
            data.put("userPendingCount", userPendingCount);

        } catch (Exception e) {
            logger.error("生成统计日报失败", e);
        }
        return ResultGenerator.genSuccessResult(data);
    }

    @GetMapping("/daily-report/alarm-history")
    @ApiOperation(value = "统计日报中的报警记录")
    public Result<List<AlarmRecord>> dailyReport(@RequestParam String sourceId, @RequestParam int state) {
        List<AlarmRecord> alarmRecords = alarmHistoryService.findAlarmRecords(sourceId, state);
        return ResultGenerator.genSuccessResult(alarmRecords);
    }

    @GetMapping("user-pending-count")
    @ApiOperation(value = "统计用户待处理任务")
    public Result userPendingCount(@RequestParam String sourceId, @RequestParam String userId) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("userPendingCount", alarmHistoryService.pendingCount(sourceId, userId));
        return ResultGenerator.genSuccessResult(data);
    }


    @GetMapping("/user-task")
    @ApiOperation(value = "查询用户待处理报警记录")
    public Result list(@RequestParam String sourceId, @RequestParam String userId,
                       @RequestParam(defaultValue = "0") int pageNum,
                       @RequestParam(defaultValue = "0") int pageSize) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<AlarmRecord> userTasks = alarmHistoryService.findTasks(sourceId, userId);
            logger.info("查询报警任务成功");
            return ResultGenerator.genSuccessResult(new PageInfo<>(userTasks));
        } catch (Exception e) {
            logger.error("查询报警任务失败", e);
            return ResultGenerator.genFailResult("查询报警任务失败");
        }
    }
}
