package com.microwise.tattletale.model;

import com.microwise.tattletale.entity.LocationSensor;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 位置点实体类
 *
 * @author lijianfei
 * @date 2016-07-13
 */
@Data
public class Location implements Serializable {

    /**
     * 位置点ID
     */
    private String id;

    /**
     * 位置点名称
     */
    private String locationName;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 区域ID
     */
    private String zoneId;

    /**
     * 站点编号
     */
    private String siteId;


    /**
     * 位置点创建时间
     */
    private Date createTime;

    /**
     * 位置点类型 0:设备位置点，1：批次位置点.
     */
    private int type;
    /**
     * 备注
     */
    private String remark;

    @Transient
    private Map<Integer, LocationSensor> sensorData;

    @Transient
    private Device device;
}
