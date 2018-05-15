package com.microwise.tattletale.service.impl;

import com.microwise.tattletale.dao.SensorinfoMapper;
import com.microwise.tattletale.model.Sensorinfo;
import com.microwise.tattletale.service.SensorinfoService;
import com.microwise.tattletale.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by li.jianfei on 2017/12/19.
 */
@Service
@Transactional
public class SensorinfoServiceImpl extends AbstractService<Sensorinfo> implements SensorinfoService {
    @Resource
    private SensorinfoMapper sensorinfoMapper;

}
