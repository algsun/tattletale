--alarm_record 表添加alarmType列
--bai.weixing

ALTER TABLE alarm_record
  ADD COLUMN alarmType  tinyint(1) NOT NULL DEFAULT 1 COMMENT "报警类型：1-监测指标阈值报警,2-设备超时报警"




