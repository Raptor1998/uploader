/*
 Navicat Premium Data Transfer

 Source Server         : TDSQL-C
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : sh-cynosdbmysql-grp-rjz587zw.sql.tencentcdb.com:27204
 Source Schema         : uploader

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 08/12/2021 14:52:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for block_file
-- ----------------------------
DROP TABLE IF EXISTS `block_file`;
CREATE TABLE `block_file`  (
  `block_file_id` int(4) NOT NULL AUTO_INCREMENT,
  `block_file_chunk` int(11) NOT NULL COMMENT '第几块',
  `block_file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'md5',
  `upload_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上传时间',
  `temp_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '临时文件的存储位置',
  PRIMARY KEY (`block_file_id`) USING BTREE,
  UNIQUE INDEX `block_file_chunk`(`block_file_chunk`, `block_file_md5`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件详情',
  `file_original_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原文件名',
  `file_union_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识',
  `file_real_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实路径',
  `file_suffix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '拓展名',
  `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络地址',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `file_upload_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上传时间',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件的md5值',
  PRIMARY KEY (`file_id`) USING BTREE,
  UNIQUE INDEX `file_union_name`(`file_union_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
