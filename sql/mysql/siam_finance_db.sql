SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_capital_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_capital_record`;
CREATE TABLE `tb_capital_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `capital_type_id` int(11) NOT NULL COMMENT '资金分类id',
  `transaction_category_id` bigint(11) DEFAULT NULL COMMENT '交易分类id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=收入 1=支出 2=借入 3=借出 4=待转收入 5=待转支出 6=借入还款 7=借出归还',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `remarks` varchar(10240) DEFAULT NULL COMMENT '备注',
  `related_record_id` int(11) DEFAULT '0' COMMENT '操作关联的收支记录id 默认为0=无',
  `status` int(2) DEFAULT '0' COMMENT '状态 0=无 1=部分还款 2=部分归还 3=部分收入 4=部分支出 5=已还款 6=已归还 7=已收入 8=已支出',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_capital_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_capital_type`;
CREATE TABLE `tb_capital_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '资金分类名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '介绍说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_convertible_bond
-- ----------------------------
DROP TABLE IF EXISTS `tb_convertible_bond`;
CREATE TABLE `tb_convertible_bond` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '可转债名称',
  `code` varchar(10) NOT NULL COMMENT '可转债代码',
  `lastest_price` decimal(10,4) DEFAULT '0.0000' COMMENT '最新价格',
  `lastest_price_date` date DEFAULT NULL COMMENT '最新价格对应的日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `bond_py` varchar(50) DEFAULT NULL COMMENT '拼音',
  `increase_rt` decimal(10,4) DEFAULT '0.0000' COMMENT '涨跌幅(%)',
  `stock_id` varchar(50) DEFAULT NULL COMMENT '正股代码',
  `stock_nm` varchar(50) DEFAULT NULL COMMENT '正股名称',
  `stock_py` varchar(50) DEFAULT NULL COMMENT '正股拼音',
  `sprice` decimal(10,4) DEFAULT '0.0000' COMMENT '正股价',
  `sincrease_rt` decimal(10,4) DEFAULT '0.0000' COMMENT '正股涨跌',
  `pb` decimal(10,4) DEFAULT '0.0000' COMMENT '正股PB',
  `convert_price` decimal(10,4) DEFAULT '0.0000' COMMENT '转股价',
  `convert_value` decimal(10,4) DEFAULT '0.0000' COMMENT '转股价值',
  `premium_rt` decimal(10,4) DEFAULT '0.0000' COMMENT '溢价率',
  `rating_cd` varchar(50) DEFAULT NULL COMMENT '债券评级',
  `put_convert_price` decimal(10,4) DEFAULT '0.0000' COMMENT '回售触发价',
  `force_redeem_price` decimal(10,4) DEFAULT '0.0000' COMMENT '强赎触发价',
  `convert_amt_ratio` decimal(10,4) DEFAULT '0.0000' COMMENT '转债占比(%)',
  `fund_rt` varchar(50) DEFAULT NULL COMMENT '基金持仓',
  `short_maturity_dt` varchar(50) DEFAULT NULL COMMENT '到期时间',
  `year_left` varchar(50) DEFAULT NULL COMMENT '剩余年限',
  `curr_iss_amt` decimal(10,4) DEFAULT '0.0000' COMMENT '剩余规模(亿元)',
  `volume` decimal(10,4) DEFAULT '0.0000' COMMENT '成交额(万元)',
  `turnover_rt` decimal(10,4) DEFAULT '0.0000' COMMENT '换手率(%)',
  `ytm_rt` decimal(10,4) DEFAULT '0.0000' COMMENT '到期税前收益(%)',
  `convert_price_tips` varchar(50) DEFAULT NULL COMMENT '转股提示',
  `is_optional` tinyint(1) DEFAULT '0' COMMENT '是否为自选 0=否 1=是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_convertible_bond_fixed_investment
-- ----------------------------
DROP TABLE IF EXISTS `tb_convertible_bond_fixed_investment`;
CREATE TABLE `tb_convertible_bond_fixed_investment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `plan_id` int(11) NOT NULL COMMENT '可转债定投计划id',
  `convertible_bond_id` int(11) NOT NULL COMMENT '可转债id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `quantity` decimal(10,2) DEFAULT '0.00' COMMENT '数量(张)',
  `price` decimal(10,4) DEFAULT '0.0000' COMMENT '价格',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '定投结束卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '买入操作关联的卖出交易id',
  `status` int(2) DEFAULT '0' COMMENT '状态 0=无 1=进行中 2=已完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_convertible_bond_fixed_investment_plan
-- ----------------------------
DROP TABLE IF EXISTS `tb_convertible_bond_fixed_investment_plan`;
CREATE TABLE `tb_convertible_bond_fixed_investment_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `convertible_bond_id_comma` varchar(50) NOT NULL COMMENT '可转债id(有多个值时以逗号分隔)',
  `type` int(1) NOT NULL COMMENT '操作类型 0=多可转债混合定投 1=单可转债定投',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '定投金额(元)',
  `cycle` int(11) DEFAULT '0' COMMENT '定投周期(月)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `expected_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '期望收益率(百分比)',
  `status` int(2) DEFAULT '0' COMMENT '定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成',
  `actual_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '实际收益率(百分比)',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '收益(元)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注/定投方法',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_convertible_bond_shortterm_trading
-- ----------------------------
DROP TABLE IF EXISTS `tb_convertible_bond_shortterm_trading`;
CREATE TABLE `tb_convertible_bond_shortterm_trading` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `convertible_bond_id` int(11) NOT NULL COMMENT '可转债id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `quantity` decimal(10,2) DEFAULT '0.00' COMMENT '数量(张)',
  `price` decimal(10,4) DEFAULT '0.0000' COMMENT '价格',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '做T卖出操作关联的做T买入交易id',
  `status` int(2) DEFAULT '0' COMMENT '做T状态 0=无 1=进行中 2=已完成',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '做T卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(10240) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_fund
-- ----------------------------
DROP TABLE IF EXISTS `tb_fund`;
CREATE TABLE `tb_fund` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '基金名称',
  `code` varchar(10) NOT NULL COMMENT '基金代码',
  `lastest_net_value` decimal(10,4) DEFAULT '0.0000' COMMENT '最新净值',
  `lastest_increase_rate` decimal(10,4) DEFAULT '0.0000' COMMENT '最新涨跌幅(%)',
  `lastest_net_value_date` date DEFAULT NULL COMMENT '最新净值对应的日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `is_optional` tinyint(1) DEFAULT '0' COMMENT '是否为自选 0=否 1=是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_fund_fixed_investment
-- ----------------------------
DROP TABLE IF EXISTS `tb_fund_fixed_investment`;
CREATE TABLE `tb_fund_fixed_investment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `plan_id` int(11) NOT NULL COMMENT '基金定投计划id',
  `fund_id` int(11) NOT NULL COMMENT '基金id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `shares` decimal(10,2) DEFAULT '0.00' COMMENT '份额(份)',
  `net_value` decimal(10,4) DEFAULT '0.0000' COMMENT '净值',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '定投结束卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '买入操作关联的卖出交易id',
  `status` int(2) DEFAULT '0' COMMENT '状态 0=无 1=进行中 2=已完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_fund_fixed_investment_plan
-- ----------------------------
DROP TABLE IF EXISTS `tb_fund_fixed_investment_plan`;
CREATE TABLE `tb_fund_fixed_investment_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `fund_id_comma` varchar(50) NOT NULL COMMENT '基金id(有多个值时以逗号分隔)',
  `type` int(1) NOT NULL COMMENT '操作类型 0=多基金混合定投 1=单基金定投',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '定投金额(元)',
  `cycle` int(11) DEFAULT '0' COMMENT '定投周期(月)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `expected_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '期望收益率(百分比)',
  `status` int(2) DEFAULT '0' COMMENT '定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成',
  `actual_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '实际收益率(百分比)',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '收益(元)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注/定投方法',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_fund_shortterm_trading
-- ----------------------------
DROP TABLE IF EXISTS `tb_fund_shortterm_trading`;
CREATE TABLE `tb_fund_shortterm_trading` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `fund_id` int(11) NOT NULL COMMENT '基金id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `shares` decimal(10,2) DEFAULT '0.00' COMMENT '份额(份)',
  `net_value` decimal(10,4) DEFAULT '0.0000' COMMENT '净值',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '做T卖出操作关联的做T买入交易id',
  `status` int(2) DEFAULT '0' COMMENT '做T状态 0=无 1=进行中 2=已完成',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '做T卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(10240) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_global_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_global_setting`;
CREATE TABLE `tb_global_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_service_phone` varchar(11) DEFAULT NULL COMMENT '客服电话',
  `customer_service_wechat` varchar(50) DEFAULT NULL COMMENT '客服微信',
  `customer_service_wechat_qrcode` varchar(1024) DEFAULT NULL COMMENT '客服微信二维码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础数据设置表';

-- ----------------------------
-- Table structure for tb_gold
-- ----------------------------
DROP TABLE IF EXISTS `tb_gold`;
CREATE TABLE `tb_gold` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '黄金名称',
  `lastest_price` decimal(10,4) DEFAULT '0.0000' COMMENT '最新金价(元)',
  `lastest_price_date` date DEFAULT NULL COMMENT '最新金价对应的日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_gold_shortterm_trading
-- ----------------------------
DROP TABLE IF EXISTS `tb_gold_shortterm_trading`;
CREATE TABLE `tb_gold_shortterm_trading` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `gold_id` int(11) NOT NULL COMMENT '基金id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `grams` decimal(10,4) DEFAULT '0.0000' COMMENT '克数(克)',
  `price` decimal(10,4) DEFAULT '0.0000' COMMENT '金价(元)',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '做T卖出操作关联的做T买入交易id',
  `status` int(2) DEFAULT '0' COMMENT '做T状态 0=无 1=进行中 2=已完成',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '做T卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_member
-- ----------------------------
DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `password_salt` varchar(100) DEFAULT NULL COMMENT '密码盐值',
  `nickname` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '昵称(废弃字段)',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登陆次数',
  `invite_code` varchar(10) DEFAULT NULL COMMENT '邀请码',
  `head_img` varchar(256) DEFAULT NULL COMMENT '头像',
  `roles` varchar(128) DEFAULT NULL COMMENT '权限',
  `sex` int(2) DEFAULT '0' COMMENT '性别 0=无 1=男 2=女',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用 0=启用 1=禁用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0=正常 1=删除',
  `open_id` varchar(50) DEFAULT NULL COMMENT '微信小程序openId',
  `is_bind_wx` tinyint(1) DEFAULT '0' COMMENT '是否绑定微信 0=否 1=是',
  `points` decimal(10,2) DEFAULT '0.00' COMMENT '积分',
  `vip_status` int(2) DEFAULT '0' COMMENT '会员状态 0=无/非会员 1=正常 2=禁用 3=已过期(预留字段)',
  `vip_type` int(2) DEFAULT '0' COMMENT '会员类型 0=无 1=超级会员 2=黄金会员 3=钻石会员(预留字段)',
  `vip_start_time` datetime DEFAULT NULL COMMENT '会员开始时间(预留字段)',
  `vip_end_time` datetime DEFAULT NULL COMMENT '会员结束时间(预留字段)',
  `type` int(2) DEFAULT '1' COMMENT '用户类型 1=普通用户 2=VIP会员',
  `vip_no` varchar(50) DEFAULT NULL COMMENT '会员编号',
  `is_new_people` tinyint(1) DEFAULT '1' COMMENT '是否为新用户 0=否 1=是',
  `is_remind_new_people` tinyint(1) DEFAULT '1' COMMENT '是否需要弹出新人引导提示 0=否 1=是',
  `last_use_time` datetime DEFAULT NULL COMMENT '最后使用/进入小程序的时间',
  `last_use_address` varchar(200) DEFAULT NULL COMMENT '最后使用/进入小程序的定位地址',
  `register_way` int(2) DEFAULT NULL COMMENT '注册方式 1=微信一键登录 2=手机验证码 3=邀请注册',
  `wx_public_platform_open_id` varchar(50) DEFAULT NULL COMMENT '微信公众号openId',
  `is_request_wx_notify` tinyint(1) DEFAULT '1' COMMENT '是否需要请求授权服务通知 0=否 1=是',
  `last_request_wx_notify_time` datetime DEFAULT NULL COMMENT '上一次请求授权服务通知时间',
  `invite_reward_amount` decimal(10,2) DEFAULT '0.00' COMMENT '邀请新用户注册奖励金额',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `total_balance` decimal(10,2) DEFAULT '0.00' COMMENT '累计余额',
  `total_consume_balance` decimal(10,2) DEFAULT '0.00' COMMENT '累计消费余额',
  `total_points` decimal(10,2) DEFAULT '0.00' COMMENT '累计积分',
  `total_consume_points` decimal(10,2) DEFAULT '0.00' COMMENT '累计消费积分',
  `total_withdraw_invite_reward_amount` decimal(10,2) DEFAULT '0.00' COMMENT '累积已提邀请新用户注册奖励金额',
  `payment_password` varchar(100) DEFAULT NULL COMMENT '支付密码',
  `payment_password_salt` varchar(100) DEFAULT NULL COMMENT '支付密码盐值',
  `invite_suncode` varchar(256) DEFAULT NULL COMMENT '邀请分享-微信小程序太阳码',
  `unreceived_points` decimal(10,2) DEFAULT '0.00' COMMENT '未到账-积分',
  `unreceived_invite_reward_amount` decimal(10,2) DEFAULT '0.00' COMMENT '未到账-邀请新用户注册奖励金额',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_type` int(2) NOT NULL COMMENT '消息用户类型 1=用户 2=商家 3=管理员',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读 0=未读 1=已读',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统消息表';

-- ----------------------------
-- Table structure for tb_scheduled_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_scheduled_task`;
CREATE TABLE `tb_scheduled_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(50) NOT NULL COMMENT '任务名称',
  `code` varchar(256) NOT NULL COMMENT '任务代码',
  `frequency` varchar(50) NOT NULL COMMENT '执行频率',
  `state` int(2) DEFAULT '1' COMMENT '执行状态 1=未执行 2=正在执行',
  `last_start_time` datetime DEFAULT NULL COMMENT '最后执行开始时间',
  `last_end_time` datetime DEFAULT NULL COMMENT '最后执行结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务表';

-- ----------------------------
-- Table structure for tb_scheduled_task_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_scheduled_task_log`;
CREATE TABLE `tb_scheduled_task_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `scheduled_task_code` varchar(256) NOT NULL COMMENT '任务代码',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '执行状态 1=执行成功 2=执行出错',
  `error` varchar(1024) DEFAULT NULL COMMENT '错误描述',
  `host_name` varchar(50) NOT NULL COMMENT '执行主机名称',
  `host_ip_address` varchar(50) NOT NULL COMMENT '执行主机ip地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务执行日志表';

-- ----------------------------
-- Table structure for tb_setting
-- ----------------------------
DROP TABLE IF EXISTS `tb_setting`;
CREATE TABLE `tb_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `fund_short_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '基金-短期仓金额(元)',
  `fund_long_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '基金-长期仓金额(元)',
  `stock_short_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '股票-短期仓金额(元)',
  `stock_long_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '股票-长期仓金额(元)',
  `convertible_bond_short_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '可转债-短期仓金额(元)',
  `convertible_bond_long_term_amount` decimal(12,2) DEFAULT '0.00' COMMENT '可转债-长期仓金额(元)',
  `push_stock_price_email_address` varchar(1024) DEFAULT NULL COMMENT '需推送股价信息的邮箱地址(多个以逗号分隔)',
  `accumulation_fund_balance` decimal(10,2) DEFAULT '0.00' COMMENT '公积金余额(元)',
  `medical_insurance_balance` decimal(10,2) DEFAULT '0.00' COMMENT '医保余额(元)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_sms_log`;
CREATE TABLE `tb_sms_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) DEFAULT NULL COMMENT '用户id',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `type` varchar(20) DEFAULT NULL COMMENT '短信类型 注册=register 登录=login 验证手机号=verification 找回密码=findpwd 自提提醒=extractRemind',
  `verify_code` varchar(10) DEFAULT NULL COMMENT '短信验证码',
  `ip` varchar(15) NOT NULL COMMENT '请求ip',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '发送状态 1=发送成功 2=发送失败',
  `description` varchar(50) DEFAULT NULL COMMENT 'API调用返回信息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='短信验证码记录表';

-- ----------------------------
-- Table structure for tb_stock
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock`;
CREATE TABLE `tb_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `name` varchar(50) NOT NULL COMMENT '股票名称',
  `code` varchar(10) NOT NULL COMMENT '股票代码',
  `lastest_price` decimal(10,4) DEFAULT '0.0000' COMMENT '最新价格',
  `lastest_increase_rate` decimal(10,4) DEFAULT '0.0000' COMMENT '最新涨跌幅(%)',
  `lastest_price_date` date DEFAULT NULL COMMENT '最新价格对应的日期',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `market` varchar(50) DEFAULT NULL COMMENT '市场简写。支持 sh、sz、hk',
  `state` int(11) DEFAULT NULL COMMENT '1为上市，其他下市',
  `currcapital` decimal(20,6) DEFAULT '0.000000' COMMENT '流通股本，万股',
  `profit_four` decimal(20,6) DEFAULT '0.000000' COMMENT '四季度净利润（亿元）',
  `listing_date` date DEFAULT NULL COMMENT '上市日期',
  `totalcapital` decimal(20,6) DEFAULT '0.000000' COMMENT '总股本，万股',
  `mgjzc` decimal(20,6) DEFAULT '0.000000' COMMENT '每股净资产（元）',
  `pinyin` varchar(50) DEFAULT NULL COMMENT '拼音',
  `is_optional` tinyint(1) DEFAULT '0' COMMENT '是否为自选 0=否 1=是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_stock_fixed_investment
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock_fixed_investment`;
CREATE TABLE `tb_stock_fixed_investment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `plan_id` int(11) NOT NULL COMMENT '股票定投计划id',
  `stock_id` int(11) NOT NULL COMMENT '股票id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `quantity` decimal(10,2) DEFAULT '0.00' COMMENT '数量(股)',
  `price` decimal(10,4) DEFAULT '0.0000' COMMENT '价格',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '定投结束卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '买入操作关联的卖出交易id',
  `status` int(2) DEFAULT '0' COMMENT '状态 0=无 1=进行中 2=已完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_stock_fixed_investment_plan
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock_fixed_investment_plan`;
CREATE TABLE `tb_stock_fixed_investment_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `stock_id_comma` varchar(50) NOT NULL COMMENT '股票id(有多个值时以逗号分隔)',
  `type` int(1) NOT NULL COMMENT '操作类型 0=多股票混合定投 1=单股票定投',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '定投金额(元)',
  `cycle` int(11) DEFAULT '0' COMMENT '定投周期(月)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `expected_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '期望收益率(百分比)',
  `status` int(2) DEFAULT '0' COMMENT '定投计划状态 0=未开始 1=进行中 2=已清仓 3=已完成',
  `actual_return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '实际收益率(百分比)',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '收益(元)',
  `remarks` varchar(1024) DEFAULT NULL COMMENT '备注/定投方法',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_stock_shortterm_trading
-- ----------------------------
DROP TABLE IF EXISTS `tb_stock_shortterm_trading`;
CREATE TABLE `tb_stock_shortterm_trading` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `stock_id` int(11) NOT NULL COMMENT '股票id',
  `type` int(1) NOT NULL COMMENT '操作类型 0=买入 1=卖出',
  `amount` decimal(10,2) DEFAULT '0.00' COMMENT '金额(元)',
  `service_charge` decimal(10,2) DEFAULT '0.00' COMMENT '手续费(元)',
  `quantity` decimal(10,2) DEFAULT '0.00' COMMENT '数量(股)',
  `price` decimal(10,4) DEFAULT '0.0000' COMMENT '价格',
  `related_trading_id` int(11) DEFAULT NULL COMMENT '做T卖出操作关联的做T买入交易id',
  `status` int(2) DEFAULT '0' COMMENT '做T状态 0=无 1=进行中 2=已完成',
  `income` decimal(10,2) DEFAULT '0.00' COMMENT '做T卖出操作所获收益(元)',
  `return_rate` decimal(10,2) DEFAULT '0.00' COMMENT '收益率(百分比)',
  `remarks` varchar(10240) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tb_transaction_category
-- ----------------------------
DROP TABLE IF EXISTS `tb_transaction_category`;
CREATE TABLE `tb_transaction_category` (
  `id` bigint(11) NOT NULL COMMENT '主键id',
  `member_id` int(11) NOT NULL COMMENT '用户id',
  `parent_id` bigint(11) DEFAULT NULL COMMENT '父级id',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `type` int(1) NOT NULL COMMENT '类型 0=收入 1=支出 2=本人资金往来',
  `sort_number` int(11) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
