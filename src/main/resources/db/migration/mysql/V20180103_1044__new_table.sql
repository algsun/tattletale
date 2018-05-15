--创建表aware_record
--author sun.cong

CREATE TABLE IF NOT EXISTS `aware_record` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `alarm_record_id` varchar(64) NOT NULL COMMENT '报警记录id',
  `aware_notifier` varchar(64) NOT NULL COMMENT '人员',
  `aware_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`),
  KEY `alarm_aware_foreign_key` (`alarm_record_id`),
  CONSTRAINT `alarm_aware_foreign_key` FOREIGN KEY (`alarm_record_id`) REFERENCES `alarm_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报警知晓表'













