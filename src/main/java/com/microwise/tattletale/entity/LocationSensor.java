package com.microwise.tattletale.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 位置点传感信息类
 *
 * @author liuzhu
 * @date 2014-12-11
 */
@Data
public class LocationSensor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * UUID
     */
    private String id;
    /**
     * 节点号
     */
    private String locationId;
    /**
     * 传感标识
     */
    private int sensorPhysicalid;
    /**
     * 传感值
     */
    private double sensorPhysicalValue;
    /**
     * <pre>
     * 传感量状态:
     * 0：采样失败(0xFFFF为采样失败) 1：采样正常 2：低于低阀值 3：超过高阀值 4：空数据
     * </pre>
     */
    private int state;
    /**
     * 实时数据时间戳
     */
    private Timestamp stamp;
    /**
     * 数据版本号
     */
    private int dataVersion;
}
