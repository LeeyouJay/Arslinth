/*
 Navicat Premium Data Transfer

 Source Server         : Arslinth-MySQL
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : thyme

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 15/09/2020 04:32:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_details
-- ----------------------------
DROP TABLE IF EXISTS `order_details`;
CREATE TABLE `order_details`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品种类型',
  `pd_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品种名称',
  `value` double(10, 1) NULL DEFAULT NULL COMMENT '数量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `cost` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '进货价',
  `subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `is_deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 266 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_list
-- ----------------------------
DROP TABLE IF EXISTS `order_list`;
CREATE TABLE `order_list`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `consumer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `pay_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `region` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户地址',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `checker` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收银员',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '总价',
  `total_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '成本价',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '0代表未删除 1代表已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_record`;
CREATE TABLE `pay_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `principal_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司或负责人',
  `pay` decimal(10, 2) NULL DEFAULT NULL COMMENT '付款金额',
  `pay_date` date NULL DEFAULT NULL COMMENT '付款日期',
  `payment` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_used` timestamp(0) NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for principal
-- ----------------------------
DROP TABLE IF EXISTS `principal`;
CREATE TABLE `principal`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公司负责人id',
  `pcp_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `tel` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `PSBC` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政卡',
  `RCU` varchar(19) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '农村信用社',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of principal
-- ----------------------------
INSERT INTO `principal` VALUES ('1eb8348a3de14375bf4123e862eead08', '工具人', '', '', '', '', '2020-08-22 11:02:36');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '33ff9fb9e11a454dbaa388e8332a8ac4' COMMENT '品种类型',
  `pd_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '进货价',
  `num` double(10, 1) NULL DEFAULT 0.0 COMMENT '剩余库存(包)',
  `period` int(0) NULL DEFAULT 0 COMMENT '生育期（天）',
  `yield` double(10, 1) NULL DEFAULT 0.0 COMMENT '亩产量（公斤）',
  `height` double(10, 1) NULL DEFAULT 0.0 COMMENT '株高（cm）',
  `is_show` tinyint(0) NULL DEFAULT 1 COMMENT '显示状态 1为显示',
  `from_app` tinyint(0) NULL DEFAULT 0 COMMENT '添加方式1为app中添加',
  `characters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '性状特征',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('170ecf8990ff40eaaadfe346327040f1', '33ff9fb9e11a454dbaa388e8332a8ac4', '昌两优丝苗', 50.00, 43.00, 30.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('47e870bc6e9c49f083c37d07425bcc74', '33ff9fb9e11a454dbaa388e8332a8ac4', '野香优703', 45.00, 38.00, 40.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('5fd84258a2304a96bf99bb8754618354', '33ff9fb9e11a454dbaa388e8332a8ac4', '和两优1086', 45.00, 38.00, 40.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('800298e6af8b4569839266925434ce2f', '33ff9fb9e11a454dbaa388e8332a8ac4', '精品619', 50.00, 40.00, 10.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 12:29:42');
INSERT INTO `product` VALUES ('9fa3b07ca2b24b92946577aa6a85dd13', '33ff9fb9e11a454dbaa388e8332a8ac4', '百优1191', 35.00, 28.00, 210.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('af27c0cc0dc94bf0bc2710a4491ae1b7', '33ff9fb9e11a454dbaa388e8332a8ac4', '科德优3号', 40.00, 33.00, 20.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('be1913b1eb06498eaff91b2dda9bcce3', '33ff9fb9e11a454dbaa388e8332a8ac4', '天目19', 35.00, 28.00, 6.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('c98a7a9257dc488daa7d0c41c5d36141', '33ff9fb9e11a454dbaa388e8332a8ac4', '正大808', 60.00, 53.00, 40.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 12:29:42');
INSERT INTO `product` VALUES ('cf29f4e267984f2893667b5922779e34', '33ff9fb9e11a454dbaa388e8332a8ac4', '中浙优8号', 50.00, 43.00, 60.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('ea43f7706c994536992cace7eed9374e', '33ff9fb9e11a454dbaa388e8332a8ac4', '野香优莉丝', 50.00, 43.00, 20.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 11:02:44');
INSERT INTO `product` VALUES ('ed3b7adee98e41dab4c019b1861e1779', '33ff9fb9e11a454dbaa388e8332a8ac4', '美玉13号', 20.00, 15.00, 20.0, 0, 0.0, 0.0, 1, 0, '', '2020-08-22 12:29:42');

-- ----------------------------
-- Table structure for product_img
-- ----------------------------
DROP TABLE IF EXISTS `product_img`;
CREATE TABLE `product_img`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `pd_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品种名称',
  `url` varchar(47) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_img
-- ----------------------------
INSERT INTO `product_img` VALUES (51, '水稻', '/uploadimg/水稻.jpg');
INSERT INTO `product_img` VALUES (92, '中浙优8号', NULL);
INSERT INTO `product_img` VALUES (93, '百优1191', NULL);
INSERT INTO `product_img` VALUES (94, '天目19', NULL);
INSERT INTO `product_img` VALUES (95, '和两优1086', NULL);
INSERT INTO `product_img` VALUES (96, '野香优莉丝', NULL);
INSERT INTO `product_img` VALUES (97, '野香优703', NULL);
INSERT INTO `product_img` VALUES (98, '昌两优丝苗', NULL);
INSERT INTO `product_img` VALUES (99, '科德优3号', NULL);
INSERT INTO `product_img` VALUES (100, '正大808', NULL);
INSERT INTO `product_img` VALUES (101, '精品619', NULL);
INSERT INTO `product_img` VALUES (102, '美玉13号', NULL);

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `district` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of region
-- ----------------------------
INSERT INTO `region` VALUES (1, '周鹿街');
INSERT INTO `region` VALUES (2, '里龙村');
INSERT INTO `region` VALUES (3, '武平村');
INSERT INTO `region` VALUES (4, '三星村');
INSERT INTO `region` VALUES (5, '马周村');
INSERT INTO `region` VALUES (6, '琴马村');
INSERT INTO `region` VALUES (7, '黎村');
INSERT INTO `region` VALUES (8, '六周村');
INSERT INTO `region` VALUES (9, '爱旗村');
INSERT INTO `region` VALUES (10, '妙圩村');
INSERT INTO `region` VALUES (11, '武平村');

-- ----------------------------
-- Table structure for remittance
-- ----------------------------
DROP TABLE IF EXISTS `remittance`;
CREATE TABLE `remittance`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `principal_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司或负责人',
  `total_count` int(0) NULL DEFAULT NULL COMMENT '总进货量',
  `total_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '总进货价',
  `total_pay` decimal(10, 2) NULL DEFAULT NULL COMMENT '已付款金额',
  `debt` decimal(10, 2) NULL DEFAULT NULL COMMENT '尚欠款',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of remittance
-- ----------------------------
INSERT INTO `remittance` VALUES ('43bead42e3d145908bd881abb9b1b4be', '1eb8348a3de14375bf4123e862eead08', 496, 17298.00, 0.00, -17298.00);

-- ----------------------------
-- Table structure for stock
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `product_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品种id',
  `principal_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司负责人id',
  `count` int(0) NULL DEFAULT NULL COMMENT '数量',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '进货单价',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '零售价',
  `in_date` date NULL DEFAULT NULL COMMENT '进货日期',
  `unit` int(0) NULL DEFAULT NULL COMMENT '规格数量',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `single` tinyint(0) NULL DEFAULT NULL COMMENT '区别导入方式',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stock
-- ----------------------------
INSERT INTO `stock` VALUES ('1e0f2e92bc7443e89d2df2a4ead97150', '47e870bc6e9c49f083c37d07425bcc74', '1eb8348a3de14375bf4123e862eead08', 20, 38.00, 45.00, '2019-06-07', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('2300fac8f10e40c1b0f5891304ade2a6', '170ecf8990ff40eaaadfe346327040f1', '1eb8348a3de14375bf4123e862eead08', 20, 43.00, 50.00, '2019-06-25', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('27386216c30a4380a91d1747d3df4c89', '5fd84258a2304a96bf99bb8754618354', '1eb8348a3de14375bf4123e862eead08', 30, 38.00, 45.00, '2019-06-16', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('387a2a2e2b824bdbadafa0dd58793e83', '9fa3b07ca2b24b92946577aa6a85dd13', '1eb8348a3de14375bf4123e862eead08', 20, 28.00, 35.00, '2019-06-21', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('3cedce2006a3492db76cc428bac0ad4f', '170ecf8990ff40eaaadfe346327040f1', '1eb8348a3de14375bf4123e862eead08', 10, 43.00, 50.00, '2019-06-22', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('3e66e5568bc4439fa287a92953a0f8c7', 'ea43f7706c994536992cace7eed9374e', '1eb8348a3de14375bf4123e862eead08', 20, 43.00, 50.00, '2019-06-07', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('542e63dae4324afe8664d4ca937ea754', '9fa3b07ca2b24b92946577aa6a85dd13', '1eb8348a3de14375bf4123e862eead08', 60, 28.00, 35.00, '2019-06-05', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('60b6afd846a748158c175db3d88d910e', 'ed3b7adee98e41dab4c019b1861e1779', '1eb8348a3de14375bf4123e862eead08', 20, 15.00, 20.00, '2020-02-15', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('6ae277ec8f044fff93c6da2c778d0b30', '5fd84258a2304a96bf99bb8754618354', '1eb8348a3de14375bf4123e862eead08', 10, 38.00, 45.00, '2019-06-05', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('7886a5f168294df8a173a64013d3ed9d', 'af27c0cc0dc94bf0bc2710a4491ae1b7', '1eb8348a3de14375bf4123e862eead08', 10, 33.00, 40.00, '2019-06-28', 1, '', 0, '2020-08-22 12:29:32');
INSERT INTO `stock` VALUES ('7b68f155750945349ca3d92d517e13f3', 'c98a7a9257dc488daa7d0c41c5d36141', '1eb8348a3de14375bf4123e862eead08', 10, 53.00, 60.00, '2020-02-15', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('8e58a868d8924e659e5f2b64220fdf14', 'be1913b1eb06498eaff91b2dda9bcce3', '1eb8348a3de14375bf4123e862eead08', 6, 28.00, 35.00, '2019-06-05', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('9ad0f2b266ae448db5382456f1ed6b4d', '9fa3b07ca2b24b92946577aa6a85dd13', '1eb8348a3de14375bf4123e862eead08', 120, 28.00, 35.00, '2019-06-09', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('9ec5da0a1bb84153a98190629406a4c8', 'c98a7a9257dc488daa7d0c41c5d36141', '1eb8348a3de14375bf4123e862eead08', 30, 53.00, 60.00, '2020-02-06', 1, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('ab247ee7f47942f2b3ca4dc59fe3247f', '47e870bc6e9c49f083c37d07425bcc74', '1eb8348a3de14375bf4123e862eead08', 20, 38.00, 45.00, '2019-06-16', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('b03cb4f38e984909bf35552e61273d45', 'cf29f4e267984f2893667b5922779e34', '1eb8348a3de14375bf4123e862eead08', 60, 43.00, 50.00, '2019-06-05', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('d5fcb3ac6abc43f281c954f85936fd60', 'af27c0cc0dc94bf0bc2710a4491ae1b7', '1eb8348a3de14375bf4123e862eead08', 10, 33.00, 40.00, '2019-07-01', 1, '', 0, '2020-08-22 12:21:48');
INSERT INTO `stock` VALUES ('ee2f510427e14934b8182514a2de4911', '800298e6af8b4569839266925434ce2f', '1eb8348a3de14375bf4123e862eead08', 10, 40.00, 50.00, '2020-02-15', 0, NULL, 0, NULL);
INSERT INTO `stock` VALUES ('f10a23bea34f488bbb876a7845fe1e7e', '9fa3b07ca2b24b92946577aa6a85dd13', '1eb8348a3de14375bf4123e862eead08', 10, 28.00, 35.00, '2019-06-22', 0, NULL, 0, NULL);

-- ----------------------------
-- Table structure for stu_score
-- ----------------------------
DROP TABLE IF EXISTS `stu_score`;
CREATE TABLE `stu_score`  (
  `id` int(0) NOT NULL,
  `stu_id` int(0) NULL DEFAULT NULL COMMENT '学生学号',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `chinese` double(10, 1) NULL DEFAULT NULL COMMENT '语文',
  `math` double(10, 1) NULL DEFAULT NULL COMMENT '数学',
  `english` double(10, 1) NULL DEFAULT NULL COMMENT '英语',
  `total` double(10, 1) NULL DEFAULT NULL COMMENT '总分',
  `class_num` int(0) NULL DEFAULT NULL COMMENT '班级',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stu_score
-- ----------------------------
INSERT INTO `stu_score` VALUES (1, 1001, '黄诗漫', 89.5, 95.5, 96.0, 281.0, 154, NULL, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP地址',
  `ip_source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'IP来源',
  `message` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `browser_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器名称',
  `system_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '系统名称',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('082b973377ba4a32834dc9cf1d01fe16', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-09-15 02:42:37');
INSERT INTO `sys_log` VALUES ('1f5304f340df4fa381f92496b582e5a1', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-19 20:45:58');
INSERT INTO `sys_log` VALUES ('22ebc00345404c5aad21a7906b8fa90f', 'admin', '127.0.0.1', '内网IP', '验证码不匹配！', 'Chrome 8', 'Windows', '2020-08-19 17:03:53');
INSERT INTO `sys_log` VALUES ('2a2ea902316e40ec9ef16a782f52758a', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-17 14:05:45');
INSERT INTO `sys_log` VALUES ('3f2bc526d6584af988e8d7a0dafa9718', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-19 19:43:34');
INSERT INTO `sys_log` VALUES ('67d82b05e08c4953ad0b219452bd9905', 'hua', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-19 17:54:15');
INSERT INTO `sys_log` VALUES ('7e1f0336243c43d49d0148ad6ed007e9', 'arslinth', '127.0.0.1', '内网IP', '用户不存在', 'Chrome 8', 'Windows', '2020-09-15 02:42:28');
INSERT INTO `sys_log` VALUES ('804a15f3e2f647038698d80706648022', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-22 16:32:54');
INSERT INTO `sys_log` VALUES ('8e61e2d39f6442c2a42ac0dde5a4d4a5', 'hua', '127.0.0.1', '内网IP', '退出成功', 'Chrome 8', 'Windows', '2020-08-19 17:54:26');
INSERT INTO `sys_log` VALUES ('977eeebf58fe4eaeae3479b65971657e', 'admin', '127.0.0.1', '内网IP', '退出成功', 'Chrome 8', 'Windows', '2020-08-19 17:54:06');
INSERT INTO `sys_log` VALUES ('991ff18bb450454ba7656a4ab74a3b13', 'admin', '127.0.0.1', '内网IP', '验证码不匹配！', 'Chrome 8', 'Windows', '2020-08-19 17:03:56');
INSERT INTO `sys_log` VALUES ('99b7253799b445639afc4b11e2fcd4cc', 'admin', '127.0.0.1', '内网IP', '验证码不匹配！', 'Chrome 8', 'Windows', '2020-08-19 17:54:31');
INSERT INTO `sys_log` VALUES ('c924cc27622a4300b45c7c85d80df378', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-22 12:21:37');
INSERT INTO `sys_log` VALUES ('e6b903de9a9c4f22a0d9aa75bcfc3a18', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-19 17:54:35');
INSERT INTO `sys_log` VALUES ('f1c8a8bc709b47af893d1ff3238f6071', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-19 17:04:01');
INSERT INTO `sys_log` VALUES ('f3d545e7ceb6431b89f30efd95db84a5', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-20 17:58:38');
INSERT INTO `sys_log` VALUES ('fdf7d92cc47e40a797dc99bfa9149326', 'admin', '127.0.0.1', '内网IP', '登录成功', 'Chrome 8', 'Windows', '2020-08-22 11:02:20');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单主键',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父菜单主键',
  `menu_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `menu_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单别名',
  `menu_href` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单链接',
  `menu_icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `menu_level` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单级别',
  `menu_weight` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单权重',
  `is_show` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否显示（true显示 false隐藏）',
  `create_date` datetime(0) NOT NULL COMMENT '创建时间',
  `create_by` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('18220e0290bb4e2295ec139202012315', '64cffea0ee16412087d196aec95634b6', '品种信息', 'product', 'product/list', '&#xe6fa;', '2', '3', 'true', '2020-06-17 20:36:09', 'admin');
INSERT INTO `sys_menu` VALUES ('3a0c076693384168a01b0b177d8154e7', '64cffea0ee16412087d196aec95634b6', '汇款记录', 'remittance', 'Import/remittance', '&#xe69e;', '2', '1', 'true', '2020-06-14 11:55:31', 'admin');
INSERT INTO `sys_menu` VALUES ('3bc6a24d1c5d4196b4d7bc3a732d2208', '3e7d54f2bd82464484defcb4709b3142', '登录日志', 'syslog', 'sys_log/list', '&#xe69e;', '2', '4', 'true', '2020-01-13 11:25:29', 'admin');
INSERT INTO `sys_menu` VALUES ('3c2363839f584216b643e6bd3c05695d', '3e7d54f2bd82464484defcb4709b3142', '用户管理', 'user', 'user/list', '&#xe6b8;', '2', '1', 'true', '2019-12-24 15:04:59', 'admin');
INSERT INTO `sys_menu` VALUES ('3e7d54f2bd82464484defcb4709b3142', '0', '系统管理', 'system', NULL, '&#xe6ae;', '1', '1', 'true', '2019-12-24 15:02:32', 'admin');
INSERT INTO `sys_menu` VALUES ('5c2f6c5c80084a99a9d7166ba328bfdd', 'e3c575455f1a4af683b26b3f56a27815', '数据源监控', 'druid', 'druid', '&#xe6e1;', '2', '1', 'true', '2019-12-29 20:17:10', 'admin');
INSERT INTO `sys_menu` VALUES ('64cffea0ee16412087d196aec95634b6', '0', '进货管理', 'Import', NULL, '&#xe6a2;', '1', '2', 'true', '2020-06-09 20:30:06', 'admin');
INSERT INTO `sys_menu` VALUES ('7fd7aae9c9504a7b8971e668ed6055e5', 'd2d353a687e74a338b7390a5c0c396a1', '销售列表', 'SalesManagement', 'sales/management', '&#xe698;', '2', '0', 'true', '2020-07-11 09:58:58', 'admin');
INSERT INTO `sys_menu` VALUES ('893c49dd5fdb44d79bb2897db9472517', '8f1eb86b09354635b3857222d54991d3', 'v-charts图表', 'vCharts', 'devUtils/vCharts', '&#xe6a8;', '2', '1', 'true', '2020-01-16 16:16:48', 'admin');
INSERT INTO `sys_menu` VALUES ('8db930130a1e4b2b9fd479d1f9a4ed45', '3e7d54f2bd82464484defcb4709b3142', '菜单管理', 'menu', 'menu/list', '&#xe6a2;', '2', '2', 'true', '2019-12-24 15:07:12', 'admin');
INSERT INTO `sys_menu` VALUES ('8f1eb86b09354635b3857222d54991d3', '0', '研发工具', 'devUtils', NULL, '&#xe6fc;', '1', '4', 'true', '2020-01-15 16:33:27', 'admin');
INSERT INTO `sys_menu` VALUES ('ba90c64234a44202818e10868ab9da91', '8f1eb86b09354635b3857222d54991d3', '菜单图标', 'menuIcon', 'devUtils/menuIcon', '&#xe761;', '2', '0', 'true', '2020-01-15 16:34:17', 'admin');
INSERT INTO `sys_menu` VALUES ('be0a8e5ec52c4f0baa2a3edf8194f7f2', 'e3c575455f1a4af683b26b3f56a27815', '服务器监控', 'server', 'system/serverInfo', '&#xe6b3;', '2', '0', 'true', '2019-12-27 17:08:56', 'admin');
INSERT INTO `sys_menu` VALUES ('d02b96546bc64f10a35e57f96963e1af', '64cffea0ee16412087d196aec95634b6', '公司或负责人', 'principal', 'Import/principal', '&#xe6af;', '2', '0', 'true', '2020-06-16 04:24:42', 'admin');
INSERT INTO `sys_menu` VALUES ('d2d353a687e74a338b7390a5c0c396a1', '0', '出货管理', 'Sales', NULL, '&#xe702;', '1', '3', 'true', '2020-06-25 12:04:39', 'admin');
INSERT INTO `sys_menu` VALUES ('d9c0da20937e4511b23860a557ac3a6a', '64cffea0ee16412087d196aec95634b6', '进货统计', 'Import', 'Import/list', '&#xe6a2;', '2', '0', 'true', '2020-06-09 20:43:44', 'admin');
INSERT INTO `sys_menu` VALUES ('dc6933ce2f434d6b972ebafe78f871b7', 'd2d353a687e74a338b7390a5c0c396a1', '销售统计', 'Analysis', 'sales/analysis', '&#xe6b3;', '2', '1', 'true', '2020-07-22 15:44:57', 'admin');
INSERT INTO `sys_menu` VALUES ('e3c575455f1a4af683b26b3f56a27815', '0', '系统监控', 'monitor', NULL, '&#xe806;', '1', '5', 'true', '2019-12-24 15:37:04', 'admin');
INSERT INTO `sys_menu` VALUES ('ed8df2ffe77645cdb7a1b2b10f5d89e4', '3e7d54f2bd82464484defcb4709b3142', '角色管理', 'role', 'role/list', '&#xe6f5;', '2', '3', 'true', '2019-12-24 15:08:17', 'admin');

-- ----------------------------
-- Table structure for sys_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_role`;
CREATE TABLE `sys_menu_role`  (
  `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单主键',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色主键'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_role
-- ----------------------------
INSERT INTO `sys_menu_role` VALUES ('3e7d54f2bd82464484defcb4709b3142', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('3bc6a24d1c5d4196b4d7bc3a732d2208', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('3c2363839f584216b643e6bd3c05695d', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('7c3195059e954531909f6b31c91826b9', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('8db930130a1e4b2b9fd479d1f9a4ed45', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('ed8df2ffe77645cdb7a1b2b10f5d89e4', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('64cffea0ee16412087d196aec95634b6', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('18220e0290bb4e2295ec139202012315', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('3a0c076693384168a01b0b177d8154e7', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('d02b96546bc64f10a35e57f96963e1af', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('d9c0da20937e4511b23860a557ac3a6a', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('8f1eb86b09354635b3857222d54991d3', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('893c49dd5fdb44d79bb2897db9472517', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('ba90c64234a44202818e10868ab9da91', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('d2d353a687e74a338b7390a5c0c396a1', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('7fd7aae9c9504a7b8971e668ed6055e5', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('dc6933ce2f434d6b972ebafe78f871b7', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('e3c575455f1a4af683b26b3f56a27815', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('5c2f6c5c80084a99a9d7166ba328bfdd', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('be0a8e5ec52c4f0baa2a3edf8194f7f2', 'b8174920f33f4b17ad5f415c700aacd2');
INSERT INTO `sys_menu_role` VALUES ('3e7d54f2bd82464484defcb4709b3142', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('3bc6a24d1c5d4196b4d7bc3a732d2208', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('3c2363839f584216b643e6bd3c05695d', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('ed8df2ffe77645cdb7a1b2b10f5d89e4', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('64cffea0ee16412087d196aec95634b6', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('18220e0290bb4e2295ec139202012315', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('3a0c076693384168a01b0b177d8154e7', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('d02b96546bc64f10a35e57f96963e1af', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('d9c0da20937e4511b23860a557ac3a6a', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('d2d353a687e74a338b7390a5c0c396a1', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('7fd7aae9c9504a7b8971e668ed6055e5', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('dc6933ce2f434d6b972ebafe78f871b7', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('e3c575455f1a4af683b26b3f56a27815', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('be0a8e5ec52c4f0baa2a3edf8194f7f2', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_menu_role` VALUES ('3e7d54f2bd82464484defcb4709b3142', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('3bc6a24d1c5d4196b4d7bc3a732d2208', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('3c2363839f584216b643e6bd3c05695d', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('ed8df2ffe77645cdb7a1b2b10f5d89e4', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('64cffea0ee16412087d196aec95634b6', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('18220e0290bb4e2295ec139202012315', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('3a0c076693384168a01b0b177d8154e7', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('d02b96546bc64f10a35e57f96963e1af', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('d9c0da20937e4511b23860a557ac3a6a', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('d2d353a687e74a338b7390a5c0c396a1', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('7fd7aae9c9504a7b8971e668ed6055e5', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('dc6933ce2f434d6b972ebafe78f871b7', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('e3c575455f1a4af683b26b3f56a27815', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_menu_role` VALUES ('be0a8e5ec52c4f0baa2a3edf8194f7f2', 'ac0cbf8c158047969b9969763a726d0e');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `authority` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('6d496c7c577f4955bee6f54c88e95a12', 'ROLE_MANAGER', '管理员', '2020-06-21 16:35:18');
INSERT INTO `sys_role` VALUES ('ac0cbf8c158047969b9969763a726d0e', 'ROLE_VIEWER', '观光者', '2020-07-23 02:28:11');
INSERT INTO `sys_role` VALUES ('b8174920f33f4b17ad5f415c700aacd2', 'ROLE_DEVELOPER', '开发者', '2019-12-12 21:42:43');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `birthday` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `hobby` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爱好',
  `live_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '现居地',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('0fa91357d81d43359cd85f64a0719713', 'hua', '$2a$10$0PdWLfad9p7ZvdVdJQJUueajyVfDKq/XB05rB6jgQpHSzLqJDXQ7m', '何', 'uploadimg/hua.jpg', '男', '', '', '', '', '', '2020-06-21 16:33:01', '2020-08-02 14:17:05');
INSERT INTO `sys_user` VALUES ('727c8fbe492d4431866334d65e13b16a', 'admin', '$2a$10$PACWCKvNiANNOAUBhuvLieLOdb1RA5w9k4aRPkRvwBry2khhCiRCm', 'Jay', 'uploadimg/admin.jpg', '男', '18677952830', '752279593@qq.com', '2020-06-01', '', '', '2020-08-04 22:53:57', '2020-07-15 17:07:06');
INSERT INTO `sys_user` VALUES ('9ad7f31b797b4536a187d6f5dad01264', 'view', '$2a$10$9EjlaOuAeINRRyCFkZ4K.OLvJlhmT9IsLJyp8gTT01EErswy8QU.e', '观光者', NULL, '女', '', '', '', '', '', '2020-07-23 02:47:06', '2020-07-24 19:53:50');
INSERT INTO `sys_user` VALUES ('c35fe3ff8879476aaf5df2bd096313ef', 'ces', '$2a$10$gPIJR9X2A29XMdmdrcfX9eLWH.49GhYPt/2YpKWOcLXHQcLoqtj.S', '测试用户', NULL, '男', '', '', '', '', '', '2020-07-23 02:46:37', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户主键',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色主键'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('c35fe3ff8879476aaf5df2bd096313ef', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_user_role` VALUES ('9ad7f31b797b4536a187d6f5dad01264', 'ac0cbf8c158047969b9969763a726d0e');
INSERT INTO `sys_user_role` VALUES ('0fa91357d81d43359cd85f64a0719713', '6d496c7c577f4955bee6f54c88e95a12');
INSERT INTO `sys_user_role` VALUES ('727c8fbe492d4431866334d65e13b16a', 'b8174920f33f4b17ad5f415c700aacd2');

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES ('29b0fa94f74e47e78e864af8df364cf1', '农药');
INSERT INTO `type` VALUES ('310a1579867a441dbb475be0ee0e73bc', '花生');
INSERT INTO `type` VALUES ('33ff9fb9e11a454dbaa388e8332a8ac4', '玉米');
INSERT INTO `type` VALUES ('c8234e4c8e764e1f982eca0c3be925df', '水稻');

SET FOREIGN_KEY_CHECKS = 1;
