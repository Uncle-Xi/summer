/*
 Navicat Premium Data Transfer

 Source Server         : Local
 Source Server Type    : MariaDB
 Source Server Version : 100213
 Source Host           : localhost:3306
 Source Schema         : mybatis

 Target Server Type    : MariaDB
 Target Server Version : 100213
 File Encoding         : 65001

 Date: 04/02/2020 07:04:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(20) NOT NULL COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名',
  `sex` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '性别',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `birthday` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '就是这么掉渣', '嫩', '优秀', NULL);
INSERT INTO `user` VALUES (3, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (4, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (5, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (6, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (7, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (8, '牛逼', NULL, NULL, NULL);
INSERT INTO `user` VALUES (9, '牛逼', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
