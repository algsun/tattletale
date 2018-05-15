package com.microwise.tattletale.dao;

import com.microwise.tattletale.core.TattletaleMapper;
import com.microwise.tattletale.model.AwareRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AwareRecordMapper extends TattletaleMapper<AwareRecord> {
    /**
     * 查找知晓记录
     *
     * @param paramMap
     * @return
     */
    List<AwareRecord> findAwareRecords(Map paramMap);

    /**
     * 查找知晓记录(模糊查询)
     *
     * @param paramMap
     * @return
     */
    List<AwareRecord> findAwareRecordsBlurry(Map paramMap);
}