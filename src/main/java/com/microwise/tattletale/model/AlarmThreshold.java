package com.microwise.tattletale.model;

import com.microwise.tattletale.core.Global;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "alarm_threshold")
public class AlarmThreshold {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 报警策略id
     */
    @Column(name = "alarmStrategyId")
    private String alarmstrategyid;

    /**
     * 监测指标id
     */
    @Column(name = "sensorPhysicalId")
    private Integer sensorphysicalid;

    /**
     * 达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于; 与目标值/浮动值有关
     */
    @Column(name = "conditionType")
    private Integer conditiontype;

    /**
     * 文保行业监测调控预期目标值
     */
    private Float target;

    /**
     * 浮动值：以目标值为中心的浮动范围
     */
    private Float floating;
    /**
     * 0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类; 3.开关
     */
    @Column(name = "showType")
    private Integer showtype;

    /**
     * 解析达标条件类型
     *
     * @return
     */
    public String parseConditionType() {
        if (Global.ShowTypes.SHOWTYPE_SWITCH.equals(this.showtype)) {
            return "触发";
        }
        switch (this.conditiontype) {
            case 1:
                return "超出范围";
            case 2:
                return "过大";
            case 3:
                return "过小";
            case 4:
                return "过大";
            case 5:
                return "过小";
            default:
                return "异常";
        }
    }
}