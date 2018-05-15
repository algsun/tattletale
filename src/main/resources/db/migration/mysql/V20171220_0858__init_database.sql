--初始化数据库
--bai.weixing



/*Table structure for table `alarm_record` */

DROP TABLE IF EXISTS `alarm_record`;

CREATE TABLE `alarm_record` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `systemType` tinyint(1) NOT NULL COMMENT '系统类型',
  `sourceId` varchar(64) NOT NULL COMMENT '系统来源id',
  `alarmPointType` tinyint(1) NOT NULL COMMENT '报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警',
  `alarmPointId` varchar(64) NOT NULL COMMENT '报警点id',
  `factor` varchar(500) NOT NULL COMMENT '因素',
  `alarmTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报警时间',
  `state` tinyint(1) DEFAULT NULL COMMENT '处理状态：0：待处理 1：已处理',
  `transactor` varchar(64) DEFAULT NULL COMMENT '处理人',
  `handleTime` datetime DEFAULT NULL COMMENT '处理时间',
  `handleMeasure` varchar(500) DEFAULT NULL COMMENT '处理措施',
  `notifier` varchar(6000) NOT NULL COMMENT '通知人,多个通知人用逗号隔开 例：10001,1002,1003',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报警历史记录表';

/*Table structure for table `alarm_strategy` */

DROP TABLE IF EXISTS `alarm_strategy`;

CREATE TABLE `alarm_strategy` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `systemId` varchar(64) NOT NULL COMMENT '系统id',
  `sourceId` varchar(64) NOT NULL COMMENT '系统来源id',
  `alarmPointId` varchar(64) NOT NULL COMMENT '报警点id',
  `notifier` varchar(6000) NOT NULL COMMENT '报警通知人',
  `name` varchar(50) NOT NULL COMMENT '策略名称',
  `alarmApproach` tinyint(1) NOT NULL COMMENT '报警方式：1.邮件 2.短信 3.语音短信',
  `alarmPointType` tinyint(1) NOT NULL COMMENT '报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警',
  `alarmBeginTime` time NOT NULL COMMENT '正常报警时间段：开始时间',
  `alarmEndTime` time NOT NULL COMMENT '常报警时间段：结束时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报警策略';

/*Table structure for table `alarm_threshold` */

DROP TABLE IF EXISTS `alarm_threshold`;

CREATE TABLE `alarm_threshold` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `alarmStrategyId` varchar(64) NOT NULL COMMENT '报警策略id',
  `sensorPhysicalId` int(11) NOT NULL COMMENT '监测指标id',
  `conditionType` int(1) DEFAULT NULL COMMENT '达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于; 与目标值/浮动值有关',
  `target` float DEFAULT NULL COMMENT '文保行业监测调控预期目标值',
  `floating` float DEFAULT NULL COMMENT '浮动值：以目标值为中心的浮动范围',
  `showType` int(1) NOT NULL COMMENT '0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类; 3.开关量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=914 DEFAULT CHARSET=utf8 COMMENT='报警阈值表';

/*Table structure for table `sensorinfo` */

DROP TABLE IF EXISTS `sensorinfo`;

CREATE TABLE `sensorinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensorPhysicalid` int(11) NOT NULL COMMENT '传感量标识',
  `escape_sensor_id` int(11) NOT NULL DEFAULT '0' COMMENT '转义传感量标识',
  `en_name` varchar(20) NOT NULL COMMENT '传感量缩写',
  `cn_name` varchar(50) NOT NULL COMMENT '监测量中文名',
  `sensorPrecision` int(4) NOT NULL DEFAULT '2' COMMENT '传感量精度',
  `units` varchar(20) NOT NULL COMMENT '计量单位',
  `positions` int(4) NOT NULL DEFAULT '0' COMMENT '显示位',
  `isActive` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效 1：有效    0：无效',
  `showType` int(11) NOT NULL COMMENT '0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;',
  `minValue` double NOT NULL DEFAULT '0' COMMENT '允许的最小值',
  `maxValue` double NOT NULL DEFAULT '0' COMMENT '允许的最大值',
  `rangeType` int(11) NOT NULL DEFAULT '0' COMMENT '无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;',
  `signType` int(11) NOT NULL DEFAULT '0' COMMENT '原始值是否有符号。无符号 0; 有符号 1;',
  `conditionType` int(11) DEFAULT NULL COMMENT '达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关',
  `target` float DEFAULT NULL COMMENT '文保行业监测调控预期目标值',
  `floating` float DEFAULT NULL COMMENT '浮动值：以目标值为中心的浮动范围',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensorPhysicalid` (`sensorPhysicalid`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 COMMENT='传感信息表';

insert  into `sensorinfo`(`id`,`sensorPhysicalid`,`escape_sensor_id`,`en_name`,`cn_name`,`sensorPrecision`,`units`,`positions`,`isActive`,`showType`,`minValue`,`maxValue`,`rangeType`,`signType`,`conditionType`,`target`,`floating`) values
(1,32,0,'HUM','湿度',1,'%',0,1,0,0,100,3,0,1,50,20),
(2,33,0,'TMT','温度',1,'℃',1,1,0,-40,120,3,1,1,20,4),
(3,34,0,'HCHO','甲醛',2,'ppm',2,1,0,0,10,3,0,NULL,NULL,NULL),
(4,35,0,'DST','粉尘',1,'mg/m³',3,1,0,0,0.4,3,0,NULL,NULL,NULL),
(5,36,0,'CO2','二氧化碳',0,'ppm',4,1,0,0,50000,3,0,5,1000,NULL),
(6,37,0,'H2S','硫化氢',0,'ppm',5,1,0,0,0,0,0,NULL,NULL,NULL),
(7,38,0,'O3','臭氧',0,'ppm',6,1,0,0,50,3,0,NULL,NULL,NULL),
(8,39,0,'NO2','二氧化氮',0,'ppm',7,1,0,0,0,0,0,NULL,NULL,NULL),
(9,40,0,'ACC','加速度',1,'g',8,1,0,0,0,0,0,NULL,NULL,NULL),
(10,41,0,'LUX','光照',2,'lx',9,1,0,0,200000,3,1,5,300,NULL),
(11,42,0,'UV','紫外',2,'uw/c㎡',10,1,0,0,10000,3,1,3,20,NULL),
(12,43,0,'DT/FT','露点/霜点',1,'℃',11,1,0,0,120,3,0,NULL,NULL,NULL),
(13,44,0,'STMT','土壤温度',3,'℃',12,1,0,0,65535,3,0,NULL,NULL,NULL),
(14,45,0,'SHUM','土壤含水率',1,'%',13,1,0,0,100,3,0,NULL,NULL,NULL),
(15,46,0,'VOC','VOC',2,'ppm',14,1,0,0,20,3,1,NULL,NULL,NULL),
(16,47,0,'RB','降雨量',2,'mm',15,1,0,0,0,0,0,NULL,NULL,NULL),
(17,48,0,'WDD','风向',1,'°',16,1,1,0,360,3,1,NULL,NULL,NULL),
(18,49,0,'WDP','风速',1,'m/s',17,1,0,0,30,3,1,NULL,NULL,NULL),
(19,50,0,'WDF','风力',0,'级',18,1,0,0,0,0,0,NULL,NULL,NULL),
(20,51,0,'LTMT','导线温度',1,'℃',19,1,0,0,0,0,0,NULL,NULL,NULL),
(21,52,0,'TSN','拉力',1,'KN',20,1,0,0,0,0,0,NULL,NULL,NULL),
(22,53,0,'FIT','绝缘子泄漏电流',0,'μa',21,1,0,0,0,0,0,NULL,NULL,NULL),
(23,54,0,'SWD','摆角(横向)',1,'°',22,1,0,0,0,0,0,NULL,NULL,NULL),
(24,55,0,'AOL','线上电流',0,'a',23,1,0,0,0,0,0,NULL,NULL,NULL),
(25,56,0,'WT','水温',1,'℃',24,1,0,-60,100,3,1,NULL,NULL,NULL),
(26,57,0,'PH','PH值',2,'~',25,1,0,0.1,14,3,1,NULL,NULL,NULL),
(27,58,0,'DO','溶氧',0,'mg/L',26,1,0,0,20,3,1,NULL,NULL,NULL),
(28,59,0,'SWDH','摆角(纵向)',1,'°',27,1,0,0,0,0,0,NULL,NULL,NULL),
(29,60,0,'BTMT','表面温度',1,'℃',28,1,0,0,0,0,0,NULL,NULL,NULL),
(30,61,0,'PA','大气压强',1,'hPa',29,1,0,0,0,0,0,NULL,NULL,NULL),
(31,62,0,'COND','电导率',1,'dS/m',30,1,0,0,0,0,0,NULL,NULL,NULL),
(32,63,0,'RRB','降雨强度',1,'mm/h',31,1,0,0,0,0,0,NULL,NULL,NULL),
(33,65,0,'MWDD','微风风向',1,'°',32,1,1,0,0,0,0,NULL,NULL,NULL),
(34,66,0,'MWDP','微风风速',2,'m/s',33,1,0,0,60,3,0,NULL,NULL,NULL),
(35,67,0,'SO2','二氧化硫',1,'ppm',34,1,0,0,20,3,1,NULL,NULL,NULL),
(36,68,0,'STMT','5TE土壤温度',1,'℃',35,1,0,-40,50,3,0,NULL,NULL,NULL),
(37,69,0,'SHUM','5TE容积含水率',2,'%m³/m³',36,1,0,0,100,3,0,NULL,NULL,NULL),
(38,70,0,'COND','5TE电导率',2,'dS/m',37,1,0,0,23,3,0,NULL,NULL,NULL),
(39,71,0,'USD','距离',3,'mm',38,1,0,100,1000,3,0,NULL,NULL,NULL),
(40,72,0,'RM','辐射度',0,'W/㎡',39,1,0,0,1800,3,0,NULL,NULL,NULL),
(41,73,0,'EC','EC值',2,'mS/m',40,1,0,0,0,0,0,NULL,NULL,NULL),
(42,74,0,'RTMT','雨水温度',2,'℃',41,1,0,0,0,0,0,NULL,NULL,NULL),
(43,75,0,'PWL','液面高度',1,'mm',42,1,0,0,100,3,0,NULL,NULL,NULL),
(44,76,0,'SGRX','X方向裂隙',5,'mm',43,1,0,-1,1,3,1,NULL,NULL,NULL),
(45,77,0,'SGRY','Y方向裂隙',5,'mm',44,1,0,-1,1,3,1,NULL,NULL,NULL),
(46,78,0,'SGRZ','Z方向裂隙',5,'mm',45,1,0,-1,1,3,1,NULL,NULL,NULL),
(47,79,0,'LVDT','位移量',4,'mm',46,1,0,-50,50,3,1,NULL,NULL,NULL),
(48,80,0,'EVAP','蒸发量',1,'mm',47,1,0,-100,100,3,1,NULL,NULL,NULL),
(49,81,0,'DR','液位增量',1,'mm',48,1,0,0,0,0,0,NULL,NULL,NULL),
(50,82,0,'LEVEL','液位',0,'mm',49,1,0,0,10000,3,1,NULL,NULL,NULL),
(51,83,0,'VOC_HS','VOC-高灵敏度',0,'ppb',50,1,0,0,50000,3,1,3,300,NULL),
(52,84,0,'PA_HS','大气压强-高灵敏度',2,'hPa',51,1,0,300,1200,3,0,NULL,NULL,NULL),
(53,85,0,'SO2_HS','二氧化硫-高灵敏度',0,'ppb',52,1,0,0,10000,3,1,3,4,NULL),
(54,12287,0,'LONGITUDE','经度',5,'°',53,1,2,0,0,0,1,NULL,NULL,NULL),
(55,12286,0,'LATITUDE','纬度',5,'°',54,1,2,0,0,0,1,NULL,NULL,NULL),
(56,12285,0,'ALTITUDE','海拔',1,'m',56,1,2,0,0,0,1,NULL,NULL,NULL),
(57,12284,0,'SPEED','速率',2,'km/h',57,1,2,0,0,0,0,NULL,NULL,NULL),
(58,12283,0,'DIRECTION','航向',1,'°',58,1,2,0,0,0,0,NULL,NULL,NULL),
(59,86,0,'VSP','土压力',2,'kPa',53,1,0,-200,200,3,1,NULL,NULL,NULL),
(63,87,0,'ACCL','加速度',2,'g',54,1,0,-10,10,3,1,NULL,NULL,NULL),
(64,88,0,'SHAKE','震动',2,'g',55,1,0,-10,10,3,1,NULL,NULL,NULL),
(65,89,0,'SWH','开关量',0,' ',56,1,0,0,1,3,0,NULL,NULL,NULL),
(69,2048,0,'O3-HS','臭氧-高灵敏度',0,'ppb',56,1,0,0,500,3,0,3,5,NULL),
(70,90,0,'PULSE','水速',2,'m/s',57,1,0,0,9.14,3,0,NULL,NULL,NULL),
(71,91,0,'CH20-HS','甲醛-高灵敏度',0,'ppb',0,1,0,0,10000,3,1,3,80,NULL),
(72,2049,0,'NO2-HS','二氧化氮-高灵敏度',0,'ppb',0,1,0,0,1000,3,0,3,5,NULL),
(73,2050,85,'SO2-HS','二氧化硫-高灵敏度',0,'ppb',0,1,0,0,10000,3,0,NULL,NULL,NULL),
(74,2051,83,'VOC-HS','VOC-高灵敏度',0,'ppb',0,1,0,0,20000,3,0,NULL,NULL,NULL),
(75,2052,0,'PM2.5','PM2.5',1,'ug/m³',0,1,0,0,1000,3,0,3,75,NULL),
(76,2053,0,'PM10','PM10',1,'ug/m³',0,1,0,0,1000,3,0,NULL,NULL,NULL),
(77,2054,0,'PM0.5','PM0.5',1,'ug/m³',0,1,0,0,1000,3,0,NULL,NULL,NULL),
(78,2055,0,'PM1','PM1',1,'ug/m³',0,1,0,0,1000,3,0,NULL,NULL,NULL),
(79,92,0,'SGRX-DIF','X方向裂隙差值',5,'mm',0,1,0,0,1,3,1,NULL,NULL,NULL),
(80,93,0,'SGRY-DIF','Y方向裂隙差值',5,'mm',0,1,0,0,1,3,1,NULL,NULL,NULL),
(81,94,0,'SGRZ-DIF','Z方向裂隙差值',5,'mm',0,1,0,0,1,3,1,NULL,NULL,NULL),
(82,2056,0,'CO-HS','一氧化碳-高灵敏度',0,'ppb',0,1,0,0,25000,3,0,NULL,NULL,NULL),
(83,95,0,'COND-HS','电导率-高灵敏度',0,'us/cm',0,1,0,0,0,0,0,NULL,NULL,NULL),
(84,2060,0,'HUM','湿度-气站',4,'%',0,1,0,0,0,0,1,NULL,NULL,NULL),
(85,2061,0,'TMT','温度-气站',5,'℃',0,1,0,0,0,0,1,NULL,NULL,NULL),
(86,2062,0,'WDD','风向-气站',4,'°',0,1,1,0,0,0,1,NULL,NULL,NULL),
(87,2063,0,'WDP','风速-气站',5,'m/s',0,1,0,0,0,0,1,NULL,NULL,NULL),
(88,2064,0,'PA','压力-气站',3,'kPa',0,1,0,0,0,0,1,NULL,NULL,NULL),
(89,2065,0,'SO2','二氧化硫-气站',4,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(90,2066,0,'CO','一氧化碳-气站',4,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(91,2067,0,'O3','臭氧-气站',3,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(92,2068,0,'PM2.5','PM2.5-气站',4,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(93,2069,0,'PM10','PM10-气站',4,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(94,2070,0,'NOX','氮氧化物-气站',4,'mg/m³',0,1,0,0,0,0,1,NULL,NULL,NULL),
(99,2057,0,'ORGANIC_POL','有机污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(100,2058,0,'INORGANIC_POL','无机污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(101,2059,0,'SULFUROUS_POL','含硫污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(102,2071,0,'ORGANIC_POL_DIF','有机污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(103,2072,0,'INORGANIC_POL_DIF','无机污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(104,2073,0,'SULFUROUS_POL_DIF','含硫污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(105,3072,0,'ORGANIC_POL','有机污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(106,3073,0,'INORGANIC_POL','无机污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(107,3074,0,'SULFUROUS_POL','含硫污染物',3,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(108,3075,0,'ORGANIC_POL_DIF','有机污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(109,3076,0,'INORGANIC_POL_DIF','无机污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(110,3077,0,'SULFUROUS_POL_DIF','含硫污染物差值',2,'Hz',0,1,0,0,0,0,0,NULL,NULL,NULL),
(111,98,0,'UV','紫外',2,'μW/lm',0,1,0,0,0,0,0,NULL,NULL,NULL),
(112,99,0,'Noise','噪声',0,'dB',0,1,0,0,0,0,0,NULL,NULL,NULL),
(113,100,0,'HCOOH（O3）','甲酸（臭氧）',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(114,101,0,'CH3COOH（O3）','乙酸（臭氧）',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(115,102,0,'O3','臭氧',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(116,103,0,'HCOOH','甲酸',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(117,104,0,'CH3COOH','乙酸',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(118,105,0,'NH3','氨气',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(119,106,0,'SO2','二氧化硫',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(120,107,0,'NO2','二氧化氮',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(121,108,0,'HCHO','甲醛',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(122,109,0,'F+','氟离子',2,'μg/m³',0,1,0,0,0,0,0,NULL,NULL,NULL),
(126,2074,0,'TMT','非接触式表面温度',1,'℃',0,1,0,-40,600,3,1,NULL,NULL,NULL),
(127,0,0,'','',0,'',0,0,0,0,0,0,0,NULL,NULL,NULL),
(128,110,0,'MC','含水率',1,'%',0,1,0,0,100,3,1,NULL,NULL,NULL),
(129,111,0,'A-X','X轴加速度',1,'mg',0,1,0,-8000,8000,3,1,NULL,NULL,NULL),
(130,112,0,'A-Y','Y轴加速度',1,'mg',0,1,0,-8000,8000,3,1,NULL,NULL,NULL),
(131,113,0,'A-Z','Z轴加速度',1,'mg',0,1,0,-8000,8000,3,1,NULL,NULL,NULL),
(132,114,0,'Vibration','设备振动',1,'',0,1,3,0,1,3,1,NULL,NULL,NULL),
(133,115,0,'IR','红外开关',1,'',0,1,3,0,1,3,1,NULL,NULL,NULL),
(134,116,0,'SM','微动开关',1,'',0,1,3,0,1,3,1,NULL,NULL,NULL);