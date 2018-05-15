package com.microwise.tattletale.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "alarm_record")
@Data
public class AlarmRecord {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 系统类型
     */
    @Column(name = "systemType")
    private Integer systemtype;

    /**
     * 系统来源id
     */
    @Column(name = "sourceId")
    private String sourceid;

    /**
     * 报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警
     */
    public static final Integer ALARM_POINT_ZONE = 1;
    public static final Integer ALARM_POINT_RELIC = 2;
    public static final Integer ALARM_POINT_LOCATION = 3;
    @Column(name = "alarmPointType")
    private Integer alarmpointtype;

    /**
     * 报警点id
     */
    @Column(name = "alarmPointId")
    private String alarmpointid;

    /**
     * 因素
     */
    private String factor;

    /**
     * 报警时间
     */
    @Column(name = "alarmTime")
    private Date alarmtime;

    /**
     * 处理状态：0：待处理 1：已处理
     */
    public static final Integer STATE_UNTREATED = 0;
    public static final Integer STATE_TREATED = 1;
    private Integer state;

    /**
     * 处理人
     */
    private String transactor;

    /**
     * 处理时间
     */
    @Column(name = "handleTime")
    private Date handletime;

    /**
     * 处理措施
     */
    @Column(name = "handleMeasure")
    private String handlemeasure;
    /**
     * 报警类型 1-阈值报警 2-设备超时报警
     */
    public static final Integer ALARM_TYPE_THRESHOLD = 1;
    public static final Integer ALARM_TYPE_TIMEOUT = 2;
    @Column(name = "alarmType")
    private Integer alarmtype;

    /**
     * 通知人json字符串
     */
    private String notifier;

    /**
     * 通知人json对象 => 通知人实体对象
     *
     * @return
     */
    public List<Notifier> getNotifiers() {
        Gson gson = new Gson();
        return gson.fromJson(getNotifier(), new TypeToken<List<Notifier>>() {
        }.getType());
    }
}