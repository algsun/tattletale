package com.microwise.tattletale.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列数据
 *
 * @author sun.cong
 * @create 2017-11-15 16:43
 **/
@Data
public class DataReceived implements Serializable {
    /**
     * 系统类型
     * 1-银河
     * 2-终结者
     */
    private int systemType;
    /**
     * 银河的站点/终结者的机构
     */
    private Unit unit;
    /**
     * 银河的区域/终结者的文物
     */
    private Target target;

    /**
     * 位置点
     */
    private Location location;

    /**
     * 时间戳
     */
    private Timestamp timeStamp;

}
