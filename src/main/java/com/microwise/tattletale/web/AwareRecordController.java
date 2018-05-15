package com.microwise.tattletale.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.tattletale.core.Result;
import com.microwise.tattletale.core.ResultGenerator;
import com.microwise.tattletale.model.AwareRecord;
import com.microwise.tattletale.service.AwareRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 知晓记录接口
 *
 * @author sun.cong
 * @create 2018-1-9 16:25
 */
@RestController
@RequestMapping("/aware/record")
public class AwareRecordController {
    @Resource
    private AwareRecordService awareRecordService;

    @PostMapping
    public Result add(@RequestBody AwareRecord awareRecord) {
        awareRecordService.save(awareRecord);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/addAll")
    public Result addAll(@RequestBody List<AwareRecord> awareRecordList) {
        for (AwareRecord awareRecord : awareRecordList) {
            awareRecordService.save(awareRecord);
        }
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        awareRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    public Result update(@RequestBody AwareRecord awareRecord) {
        awareRecordService.update(awareRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        AwareRecord awareRecord = awareRecordService.findById(id);
        return ResultGenerator.genSuccessResult(awareRecord);
    }

    @GetMapping
    public Result list(@RequestParam String sourceId, @RequestParam String userId) {
        List<AwareRecord> list = awareRecordService.findAwareRecords(sourceId, userId);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/aware-record")
    @ApiOperation("模糊查询知晓记录")
    public Result<PageInfo<AwareRecord>> list(@RequestParam String sourceId,
                                              @RequestParam(required = false) List<String> alarmPoints,
                                              @RequestParam(required = false) List<String> users,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                              @RequestParam(defaultValue = "0") int pageNum,
                                              @RequestParam(defaultValue = "0") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AwareRecord> list = awareRecordService.findAwareRecordsBlurry(sourceId, alarmPoints, users, begin, end);
        return ResultGenerator.genSuccessResult(new PageInfo<>(list));
    }
}
