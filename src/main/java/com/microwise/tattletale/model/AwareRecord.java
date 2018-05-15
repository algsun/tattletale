package com.microwise.tattletale.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "aware_record")
@Data
public class AwareRecord {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 报警记录id
     */
    @Column(name = "alarm_record_id")
    private String alarmRecordId;

    /**
     * 人员
     */
    @Column(name = "aware_notifier")
    private String awareNotifier;

    /**
     * 时间
     */
    @Column(name = "aware_time")
    @Transient
    private Date awareTime;

    /**
     * 报警记录
     */
    @Transient
    private AlarmRecord alarmRecord;
}