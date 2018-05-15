package com.microwise.tattletale.core;

/**
 * 系统常量
 *
 * @author sun.cong
 * @create 2017-12-05 11:17
 **/
public class Global {

    public static final class Devices {
        /**
         * 设备正常
         */
        public static final Integer DEVICE_NOMAL = 0;
    }

    public static final class ShowTypes {
        /**
         * 默认showType
         */
        public static final Integer SHOWTYPE_DEFAULT = 0;
        /**
         * 开关量
         */
        public static final Integer SHOWTYPE_SWITCH = 3;
    }

    public static final class ConditionTypes {
        /**
         * 范围
         */
        public static final Integer CONDITIONTYPE_RANGE = 1;
        /**
         * 大于
         */
        public static final Integer CONDITIONTYPE_GREATER = 2;
        /**
         * 小于
         */
        public static final Integer CONDITIONTYPE_SMALLER = 3;
        /**
         * 大于等于
         */
        public static final Integer CONDITIONTYPE_GREATER_EQUAL = 4;
        /**
         * 小于等于
         */
        public static final Integer CONDITIONTYPE_SMALLER_EQUAL = 5;
    }

    public static final class SystemFlag {
        /**
         * 银河
         */
        public static final Integer GALAXY = 1;
        /**
         * 终结者
         */
        public static final Integer TERMINATOR = 2;
    }

    public static final class TemplateType {
        /**
         * 设备
         */
        public static final Integer TEMPLATE_DEVICE = 1;
        /**
         * 阈值
         */
        public static final Integer TEMPLATE_THRESHOLD = 2;
    }
}
