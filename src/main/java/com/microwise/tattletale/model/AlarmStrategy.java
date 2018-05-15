package com.microwise.tattletale.model;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Data
@Table(name = "alarm_strategy")
public class AlarmStrategy {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 系统id
     */
    @Column(name = "systemId")
    private String systemid;

    /**
     * 系统来源id
     */
    @Column(name = "sourceId")
    private String sourceid;

    /**
     * 报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警
     */
    @Column(name = "alarmPointType")
    private Integer alarmpointtype;

    /**
     * 报警点id
     */
    @Column(name = "alarmPointId")
    private String alarmpointid;

    /**
     * 报警通知人
     */
    private String notifier;


    /**
     * 策略名称
     */
    private String name;

    /**
     * 报警方式：1.邮件 2.短信 3.语音短信
     */
    @Column(name = "alarmApproach")
    private Integer alarmapproach;

    /**
     * 免打扰时间段：开始时间
     */
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "alarmBeginTime")
    private Date alarmbegintime;

    /**
     * 免打扰时间段：结束时间
     */
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "alarmEndTime")
    private Date alarmendtime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createTime")
    private Date createtime = new Date();
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updateTime")
    private Date updatetime = new Date();

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

    @Transient
    private List<AlarmThreshold> alarmthresholds;

}