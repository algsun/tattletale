package com.microwise.tattletale.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 监测指标
 *
 * @author suncong
 * @create 2017-11-15 16:43
 */
@Data
public class Sensor implements Serializable {
    /**
     * 传感量标识
     */
    private int sensorPhysicalId;
    /**
     * 英文名
     */
    private String enName;

    /**
     * 中文名
     */
    private String cnName;

    /**
     * 精度
     */
    private int precision;

    /**
     * 显示拉(监测指标显示顺序)
     */
    private int position;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否有效
     */
    private int isActive;
    /**
     * 显示类型
     */
    private int showType;
    /**
     * 最小值
     */
    private double minValue;
    /**
     * 最大值
     */
    private double maxValue;
    /**
     * 无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     */
    private int rangeType;
    /**
     * 原始数据符号类型(0 无符号, 1 有符号)
     */
    private int signType;
    /**
     * 条件类型 1-数值范围；2-大于；3-小于；4-大于等于；5-小于等于
     */
    private int conditionType;
    /**
     * 目标值
     */
    private float target;
    /**
     * 浮动值
     */
    private float floating;
}
