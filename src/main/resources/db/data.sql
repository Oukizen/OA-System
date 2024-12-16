/*
 Navicat Premium Data Transfer

 Source Server         : oa
 Source Server Type    : MySQL
 Source Server Version : 80039
 Source Host           : localhost:3306
 Source Schema         : oa

 Target Server Type    : MySQL
 Target Server Version : 80039
 File Encoding         : 65001

 Date: 17/12/2024 02:23:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `syokui` int NOT NULL,
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `position_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10000004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (10000001, '一郎', 'admin', '784512545@gmail.com', '080952644554', '女', 1, 24, '日本', NULL);
INSERT INTO `user` VALUES (10000002, '二郎', 'admin', '784512545@gmail.com', '13888888888', '女', 1, 18, '日本東京文京区千駄木', NULL);
INSERT INTO `user` VALUES (10000003, '三朗', 'admin', '784512545@gmail.com', '080952644554', '女', 1, 22, '東京都千駄木4-7-10', NULL);

SET FOREIGN_KEY_CHECKS = 1;
