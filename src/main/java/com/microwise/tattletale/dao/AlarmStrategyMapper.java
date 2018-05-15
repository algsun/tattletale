package com.microwise.tattletale.dao;

import com.microwise.tattletale.core.TattletaleMapper;
import com.microwise.tattletale.model.AlarmStrategy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmStrategyMapper extends TattletaleMapper<AlarmStrategy> {
}