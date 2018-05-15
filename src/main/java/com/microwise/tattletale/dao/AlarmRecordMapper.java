package com.microwise.tattletale.dao;

import com.microwise.tattletale.core.TattletaleMapper;
import com.microwise.tattletale.model.AlarmRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmRecordMapper extends TattletaleMapper<AlarmRecord> {

}