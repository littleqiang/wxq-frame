/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : wxq_code_manage

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 09/07/2023 17:27:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cm_branch
-- ----------------------------
DROP TABLE IF EXISTS `cm_branch`;
CREATE TABLE `cm_branch` (
  `id` bigint(50) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `branch_owner` varchar(255) DEFAULT NULL COMMENT '分支所属',
  `branch_code` varchar(255) DEFAULT NULL COMMENT '分支代码',
  `branch_name` varchar(255) DEFAULT NULL COMMENT '分支名称',
  `branch_desc` varchar(255) DEFAULT NULL COMMENT '分支描述',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cm_branch
-- ----------------------------
BEGIN;
INSERT INTO `cm_branch` (`id`, `project_id`, `branch_owner`, `branch_code`, `branch_name`, `branch_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677723559369138177, 1677723558601580546, '多租户', 'test1', '基础分支', '多租户基础分支', 0, '2023-07-09 00:57:22', NULL);
INSERT INTO `cm_branch` (`id`, `project_id`, `branch_owner`, `branch_code`, `branch_name`, `branch_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677723559377526786, 1677723558601580546, '西康', 'test2', '发布分支', '西康发布分支', 0, '2023-07-09 00:57:22', NULL);
INSERT INTO `cm_branch` (`id`, `project_id`, `branch_owner`, `branch_code`, `branch_name`, `branch_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677723559398498305, 1677723558601580546, '盐城', 'test3', '发布分支', '盐城发布分支', 0, '2023-07-09 00:57:22', NULL);
COMMIT;

-- ----------------------------
-- Table structure for cm_job_merge
-- ----------------------------
DROP TABLE IF EXISTS `cm_job_merge`;
CREATE TABLE `cm_job_merge` (
  `id` bigint(50) NOT NULL,
  `schedule_id` bigint(20) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `job_desc` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `project_id` bigint(20) DEFAULT NULL,
  `source_branch` varchar(255) DEFAULT NULL COMMENT '源分支',
  `target_branch` varchar(255) DEFAULT NULL COMMENT '目标分支',
  `status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `result_desc` varchar(255) DEFAULT NULL COMMENT '任务结果',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合并分支任务';

-- ----------------------------
-- Records of cm_job_merge
-- ----------------------------
BEGIN;
INSERT INTO `cm_job_merge` (`id`, `schedule_id`, `job_title`, `job_desc`, `project_id`, `source_branch`, `target_branch`, `status`, `result_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677727935009705985, 1677727934120513538, '合并分支', '合并多租户分支 dev-->test1', 1677723558601580546, 'dev', 'test1', '3', 'merge失败', 1, '2023-07-09 02:57:54', NULL);
INSERT INTO `cm_job_merge` (`id`, `schedule_id`, `job_title`, `job_desc`, `project_id`, `source_branch`, `target_branch`, `status`, `result_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677727935034871810, 1677727934120513538, '合并分支', '合并多租户分支 dev-->test2', 1677723558601580546, 'dev', 'test2', '3', 'merge失败', 1, '2023-07-09 15:19:20', NULL);
INSERT INTO `cm_job_merge` (`id`, `schedule_id`, `job_title`, `job_desc`, `project_id`, `source_branch`, `target_branch`, `status`, `result_desc`, `is_deleted`, `create_time`, `update_time`) VALUES (1677727935051649026, 1677727934120513538, '合并分支', '合并多租户分支 dev-->test4', 1677723558601580546, 'dev', 'test4', '1', '成功', 0, '2023-07-09 17:21:48', NULL);
COMMIT;

-- ----------------------------
-- Table structure for cm_project
-- ----------------------------
DROP TABLE IF EXISTS `cm_project`;
CREATE TABLE `cm_project` (
  `id` bigint(50) NOT NULL,
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `project_desc` varchar(255) DEFAULT NULL COMMENT '项目描述',
  `project_url` varchar(255) DEFAULT NULL COMMENT '项目仓库地址',
  `local_path` varchar(255) DEFAULT NULL COMMENT '仓库本地路径',
  `account` varchar(255) DEFAULT NULL COMMENT '仓库账号',
  `password` varchar(255) DEFAULT NULL COMMENT '仓库密码',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cm_project
-- ----------------------------
BEGIN;
INSERT INTO `cm_project` (`id`, `project_name`, `project_desc`, `project_url`, `local_path`, `account`, `password`, `is_deleted`, `create_time`, `update_time`) VALUES (1677723558601580546, 'testgit', '', 'https://gitcode.net/u013409833/testgit.git', '/Users/wangxuqiang/Documents/gittemp', '13524359120', 'qasnuT-jaqded-hetpy7', 0, '2023-07-09 14:56:44', NULL);
INSERT INTO `cm_project` (`id`, `project_name`, `project_desc`, `project_url`, `local_path`, `account`, `password`, `is_deleted`, `create_time`, `update_time`) VALUES (1677935329169977345, 't1', 'tt', 'https://gitcode.net/u013409833/testgit.git', '/Users/wangxuqiang/Documents/gittemp', '13524359120', 'qasnuT-jaqded-hetpy7', 0, '2023-07-09 14:58:52', NULL);
INSERT INTO `cm_project` (`id`, `project_name`, `project_desc`, `project_url`, `local_path`, `account`, `password`, `is_deleted`, `create_time`, `update_time`) VALUES (1677935330084335617, 't2', 't2t', 'https://gitcode.net/u013409833/testgit.git', '/Users/wangxuqiang/Documents/gittemp', '13524359120', 'qasnuT-jaqded-hetpy7', 0, '2023-07-09 14:58:52', NULL);
COMMIT;

-- ----------------------------
-- Table structure for cm_schedule
-- ----------------------------
DROP TABLE IF EXISTS `cm_schedule`;
CREATE TABLE `cm_schedule` (
  `id` bigint(50) NOT NULL,
  `tenant_name` varchar(255) DEFAULT NULL COMMENT '租户名称',
  `schedule_type` varchar(255) DEFAULT NULL COMMENT '任务类型',
  `schedule_title` varchar(255) DEFAULT NULL,
  `schedule_desc` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `status` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cm_schedule
-- ----------------------------
BEGIN;
INSERT INTO `cm_schedule` (`id`, `tenant_name`, `schedule_type`, `schedule_title`, `schedule_desc`, `status`, `is_deleted`, `create_time`, `update_time`) VALUES (1677727934120513538, '多租户', 'merge', '合并多租户开发', '多租户任务', '1', 0, '2023-07-09 01:14:45', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
