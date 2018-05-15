package com.microwise.tattletale.dao;

import com.microwise.tattletale.core.TattletaleMapper;
import com.microwise.tattletale.model.AlarmThreshold;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmThresholdMapper extends TattletaleMapper<AlarmThreshold> {
}