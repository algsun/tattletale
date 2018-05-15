package com.microwise.tattletale.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 设备
 *
 * @author sun.cong
 * @create 2017-11-30 16:30
 **/
@Data
public class Device implements Serializable {
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备类型(1：节点 2：中继 3:节点-主模块(可控) 4:节点-从模块(可控) 7：网关)
     */
    private int deviceType;
    /**
     * 是否正常 (-1:异常 0：正常 1：低电压 2：设备掉电)
     */
    private int anomaly;
}
