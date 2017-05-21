-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 2017-02-10 09:09:57
-- 服务器版本： 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `applysystem`
--

-- --------------------------------------------------------

--
-- 表的结构 `t_academy`
--

CREATE TABLE `t_academy` (
  `aID` int(11) NOT NULL,
  `a_name` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_academy`
--

INSERT INTO `t_academy` (`aID`, `a_name`) VALUES
(1, '物理与电子信息工程学院'),
(8, '音乐学院'),
(6, '化学与材料学院'),
(9, '生物学院'),
(10, '体育学院');

-- --------------------------------------------------------

--
-- 表的结构 `t_apply`
--

CREATE TABLE `t_apply` (
  `appID` int(11) NOT NULL,
  `stu_id` int(11) NOT NULL,
  `competition_id` int(11) NOT NULL,
  `app_time` int(11) NOT NULL,
  `check_state` int(11) NOT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `proof` varchar(200) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_apply`
--

INSERT INTO `t_apply` (`appID`, `stu_id`, `competition_id`, `app_time`, `check_state`, `reason`, `proof`) VALUES
(1, 2, 10, 1482447600, 2, ' ', 'allfile/applyproof/2+10.jpg'),
(2, 4, 10, 1482447600, 2, '证明不合格', NULL),
(3, 3, 10, 1482447600, 2, NULL, NULL),
(4, 5, 10, 1482447600, 2, NULL, NULL),
(5, 6, 10, 1482447600, 2, NULL, NULL),
(6, 7, 10, 1482447600, 2, NULL, NULL),
(7, 8, 10, 1482447600, 2, NULL, NULL),
(8, 26, 10, 1482447600, 2, NULL, NULL),
(9, 27, 10, 1482447600, 2, NULL, NULL),
(10, 11, 10, 1482447600, 3, '没有证明文件', NULL),
(11, 12, 10, 1482447600, 2, NULL, NULL),
(12, 13, 10, 1482447600, 2, NULL, NULL),
(13, 20, 10, 1482447600, 2, NULL, NULL),
(14, 21, 10, 1482447600, 2, NULL, NULL),
(15, 24, 10, 1482447600, 2, '没有证明文件', NULL),
(16, 2, 14, 1482447600, 2, '1111', NULL),
(17, 20, 14, 1482447600, 2, '1111', NULL),
(18, 21, 14, 1482447600, 2, '1111', NULL),
(19, 3, 14, 1482447600, 2, '条件不符合', NULL),
(20, 4, 14, 1482447600, 2, NULL, NULL),
(21, 5, 14, 1482447600, 2, NULL, NULL),
(22, 6, 14, 1482447600, 2, NULL, NULL),
(23, 7, 14, 1482447600, 2, '证明不明确', NULL),
(24, 8, 14, 1482447600, 2, NULL, NULL),
(25, 26, 14, 1482447600, 2, NULL, NULL),
(26, 27, 14, 1482447600, 2, NULL, NULL),
(27, 11, 14, 1482447600, 2, NULL, NULL),
(28, 12, 14, 1482447600, 2, NULL, NULL),
(29, 13, 14, 1482447600, 2, NULL, NULL),
(30, 2, 13, 1482447600, 2, NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `t_bulletin`
--

CREATE TABLE `t_bulletin` (
  `bulleID` int(11) NOT NULL,
  `bulle_title` varchar(50) NOT NULL,
  `source` varchar(50) DEFAULT NULL,
  `content` text,
  `adjunct` varchar(200) DEFAULT NULL,
  `release_time` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_bulletin`
--

INSERT INTO `t_bulletin` (`bulleID`, `bulle_title`, `source`, `content`, `adjunct`, `release_time`) VALUES
(3, '公告一2', '教务处2', '1234562', 'allfile/rewardfile/3.doc', 1485869765),
(4, '公告二', '物电学院', '123456', NULL, 1485869784),
(5, '公告一1', '教务处1', '1234561', NULL, 1485881169),
(6, '测试公告', '教务处', '<blockquote>\r\n<p><strong>w问问：</strong>刷刷刷</p>\r\n</blockquote>\r\n', NULL, 1486307485),
(7, '测试公告2', '', NULL, NULL, 1486308749);

-- --------------------------------------------------------

--
-- 表的结构 `t_class`
--

CREATE TABLE `t_class` (
  `cID` int(11) NOT NULL,
  `c_name` varchar(50) NOT NULL,
  `grade` int(11) NOT NULL COMMENT '大一：1  大二：2  大三：3  大四：4 ',
  `major_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_class`
--

INSERT INTO `t_class` (`cID`, `c_name`, `grade`, `major_id`) VALUES
(13, '14信管', 3, 5),
(2, '13网工本', 4, 1),
(4, '13计算机本', 4, 4),
(5, '14计算机本', 3, 4),
(6, '15计算机本', 2, 4),
(7, '14计算机本', 3, 4),
(11, '13西洋曲1班', 4, 8),
(12, '13西洋曲2班', 4, 8);

-- --------------------------------------------------------

--
-- 表的结构 `t_competition`
--

CREATE TABLE `t_competition` (
  `compID` int(11) NOT NULL,
  `comp_name` varchar(40) NOT NULL,
  `explained` text,
  `max_num` int(11) NOT NULL COMMENT '报名的单位的限制',
  `group_num` int(11) NOT NULL COMMENT '小组内人数的限制',
  `comp_time` varchar(50) DEFAULT NULL,
  `s_time` int(11) NOT NULL,
  `f_time` int(11) NOT NULL,
  `major_limited` varchar(50) DEFAULT NULL COMMENT '允许的专业id，中间用|隔开',
  `grade_limited` varchar(20) DEFAULT NULL COMMENT '允许的年级',
  `isproof` int(11) NOT NULL COMMENT '需要凭证：1  不需要：0',
  `isadviser` int(11) NOT NULL COMMENT '需要指导老师：1  不需要：0',
  `isIDcard` int(11) NOT NULL COMMENT '需要身份证：1  不需要：0',
  `paymoney` int(11) NOT NULL,
  `level_id` int(11) NOT NULL,
  `state` int(11) NOT NULL COMMENT '未开始报名：1 报名进行中：2  报名已结束：3',
  `years_y` varchar(10) NOT NULL,
  `years_t` varchar(10) NOT NULL,
  `comp_type` int(11) NOT NULL COMMENT '个人赛：1  小组赛：2',
  `str_limitmajor` varchar(100) DEFAULT NULL COMMENT '限制专业的字符串',
  `isfinish` int(11) NOT NULL COMMENT '竞赛结果编辑结束：1   未结束：0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_competition`
--

INSERT INTO `t_competition` (`compID`, `comp_name`, `explained`, `max_num`, `group_num`, `comp_time`, `s_time`, `f_time`, `major_limited`, `grade_limited`, `isproof`, `isadviser`, `isIDcard`, `paymoney`, `level_id`, `state`, `years_y`, `years_t`, `comp_type`, `str_limitmajor`, `isfinish`) VALUES
(10, '竞赛报名1', '111111111', 100, 1, '11111', 1485392460, 1487984460, '1|4|5', '1|2', 1, 1, 0, 10, 7, 2, '2016-2017', '1', 1, '物理与电子信息工程学院所有专业|', 0),
(14, '程序设计竞赛11', '111', 100, 3, '2017年中旬', 1483664460, 1483923660, '', '1|2|3|4', 1, 1, 1, 0, 7, 3, '2016-2017', '1', 2, '', 0),
(13, '2015年“思科网院杯”大学生网络技术大赛', '1111112啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 10, 2, '2017年中旬', 1482886860, 1486602060, '8', '1|2|3|4', 1, 1, 1, 0, 13, 3, '2015-2016', '2', 2, '钢琴系所有专业|', 0),
(15, '2017年第一届大学生学科基础竞赛', '111', 100, 1, '2017年下半年', 1487120460, 1487898060, '8', '1|2|3|4', 1, 1, 1, 0, 7, 1, '2016-2017', '1', 1, '音乐学院所有专业|', 0),
(16, '竞赛测试1', '111', 200, 1, '', 1486083660, 1487984460, '', '', 0, 0, 0, 0, 7, 2, '2016-2017', '1', 1, '', 0),
(17, '竞赛测试2', '1111', 200, 1, '', 1486083660, 1486087320, '', '', 0, 0, 0, 0, 7, 3, '2016-2017', '1', 1, '', 0),
(18, '竞赛测试3', '', 100, 3, '', 1486083660, 1486087320, '', '', 0, 0, 0, 0, 7, 3, '2016-2017', '1', 2, '', 0),
(19, '竞赛测试4', '我我我', 200, 6, '', 1486083660, 1486087320, '', '', 0, 0, 0, 0, 7, 3, '2016-2017', '1', 2, '', 0),
(20, '测试数据', '啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊', 200, 1, '', 1486083660, 1489539660, '', '', 0, 0, 0, 0, 7, 2, '2016-2017', '1', 1, '', 0);

-- --------------------------------------------------------

--
-- 表的结构 `t_contestants`
--

CREATE TABLE `t_contestants` (
  `contID` int(11) NOT NULL,
  `apply_id` int(11) NOT NULL,
  `iscome` int(11) NOT NULL COMMENT '参加：1  未参加：0',
  `level_id` int(11) NOT NULL,
  `reward_name` text NOT NULL,
  `certificate` text COMMENT '获奖证书',
  `reward_file` text,
  `s_perpoint` varchar(10) NOT NULL,
  `reward_priority` int(11) NOT NULL COMMENT '1到10，1为最高优先级'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_contestants`
--

INSERT INTO `t_contestants` (`contID`, `apply_id`, `iscome`, `level_id`, `reward_name`, `certificate`, `reward_file`, `s_perpoint`, `reward_priority`) VALUES
(1, 1, 1, 7, '二等奖', 'allfile/cercificate/10+1.jpg', 'allfile/rewardfile/10+1.doc', '0.5', 2),
(3, 3, 1, 7, '2015年“思科网院杯”大学生网络技术大赛全国 二等奖', 'allfile/cercificate/10+3.jpg', NULL, '2', 1),
(4, 4, 1, 7, '三等奖', 'allfile/cercificate/10+4.png', NULL, '0', 1),
(5, 6, 1, 7, 'kongde', NULL, NULL, '0', 10),
(6, 5, 1, 7, 'kongde', NULL, NULL, '0', 10),
(7, 7, 1, 7, 'kongde', NULL, NULL, '0', 10),
(8, 8, 1, 7, 'kongde', NULL, NULL, '0', 10),
(9, 9, 1, 7, 'kongde', NULL, NULL, '0', 10),
(17, 14, 1, 7, 'kongde', NULL, NULL, '0', 10),
(12, 2, 1, 7, 'kongde', NULL, NULL, '0', 10),
(13, 11, 1, 7, 'kongde', NULL, NULL, '0', 10),
(18, 13, 1, 7, 'kongde', NULL, NULL, '0', 10),
(19, 12, 1, 7, 'kongde', NULL, NULL, '0', 10),
(20, 15, 1, 7, 'kongde', NULL, NULL, '0', 10),
(27, 19, 1, 11, '二等奖', 'allfile/cercificate/14+27.jpeg', 'allfile/rewardfile/14+27.doc', '1', 2),
(29, 23, 1, 7, 'kongde', NULL, NULL, '0', 10),
(30, 20, 1, 7, '三等奖', NULL, 'allfile/rewardfile/14+30.doc', '0', 3),
(31, 21, 1, 7, 'kongde', NULL, NULL, '0', 10),
(32, 22, 1, 7, 'kongde', NULL, NULL, '0', 10),
(33, 24, 1, 7, 'kongde', NULL, NULL, '0', 10),
(34, 25, 1, 7, 'kongde', NULL, NULL, '0', 10),
(35, 26, 1, 7, 'kongde', NULL, NULL, '0', 10),
(36, 27, 1, 7, 'kongde', NULL, NULL, '0', 10),
(37, 28, 1, 7, 'kongde', NULL, NULL, '0', 10),
(38, 29, 1, 7, 'kongde', NULL, NULL, '0', 10),
(42, 30, 1, 13, 'kongde', NULL, NULL, '0', 10),
(41, 18, 1, 7, '程序大赛B组一等奖', 'allfile/cercificate/14+39.jpeg', 'allfile/rewardfile/14+39.xls', '1', 1),
(39, 16, 1, 7, '程序大赛B组一等奖', 'allfile/cercificate/14+39.jpeg', 'allfile/rewardfile/14+39.xls', '1', 1),
(40, 17, 1, 7, '程序大赛B组一等奖', 'allfile/cercificate/14+39.jpeg', 'allfile/rewardfile/14+39.xls', '1', 1);

-- --------------------------------------------------------

--
-- 表的结构 `t_department`
--

CREATE TABLE `t_department` (
  `dID` int(11) NOT NULL,
  `d_name` varchar(50) NOT NULL,
  `academy_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_department`
--

INSERT INTO `t_department` (`dID`, `d_name`, `academy_id`) VALUES
(1, '网络工程系', 1),
(8, '钢琴系', 8),
(6, '计算机系', 1);

-- --------------------------------------------------------

--
-- 表的结构 `t_group`
--

CREATE TABLE `t_group` (
  `groupID` int(11) NOT NULL,
  `competition_id` int(11) NOT NULL,
  `header_id` int(11) NOT NULL,
  `g_state` int(11) NOT NULL,
  `g_reason` varchar(100) DEFAULT NULL COMMENT '待审核：1 审核通过：2 审核未通过：3 '
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_group`
--

INSERT INTO `t_group` (`groupID`, `competition_id`, `header_id`, `g_state`, `g_reason`) VALUES
(1, 14, 2, 2, '1111'),
(2, 14, 3, 2, '条件不符合'),
(3, 14, 4, 2, NULL),
(4, 14, 5, 2, NULL),
(5, 14, 6, 2, NULL),
(6, 14, 7, 2, '证明不明确'),
(7, 14, 8, 2, NULL),
(8, 14, 26, 2, NULL),
(9, 14, 27, 2, NULL),
(10, 14, 11, 2, NULL),
(11, 14, 12, 2, NULL),
(12, 14, 13, 2, NULL),
(13, 13, 2, 2, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `t_level`
--

CREATE TABLE `t_level` (
  `rID` int(11) NOT NULL,
  `r_name` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_level`
--

INSERT INTO `t_level` (`rID`, `r_name`) VALUES
(7, '国家级'),
(11, '国家协会级'),
(12, '省级'),
(13, '省协会级'),
(14, '市级'),
(15, '市协会级'),
(16, '校级'),
(17, '院级');

-- --------------------------------------------------------

--
-- 表的结构 `t_major`
--

CREATE TABLE `t_major` (
  `mID` int(11) NOT NULL,
  `m_name` varchar(50) NOT NULL,
  `department_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_major`
--

INSERT INTO `t_major` (`mID`, `m_name`, `department_id`) VALUES
(1, '网络工程', 1),
(4, '计算机科学', 6),
(5, '信息管理', 6),
(8, '西洋区专业', 8);

-- --------------------------------------------------------

--
-- 表的结构 `t_manager`
--

CREATE TABLE `t_manager` (
  `ID` int(11) NOT NULL,
  `account` varchar(11) NOT NULL,
  `pwd` varchar(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `t_member`
--

CREATE TABLE `t_member` (
  `stu_id` int(11) NOT NULL,
  `group_id` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_member`
--

INSERT INTO `t_member` (`stu_id`, `group_id`) VALUES
(2, 1),
(20, 1),
(21, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 5),
(7, 6),
(8, 7),
(26, 8),
(27, 9),
(11, 10),
(12, 11),
(13, 12),
(2, 13);

-- --------------------------------------------------------

--
-- 表的结构 `t_mentor`
--

CREATE TABLE `t_mentor` (
  `menID` int(11) NOT NULL,
  `apply_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  `perpoint` varchar(10) NOT NULL,
  `isfirst` int(11) NOT NULL COMMENT '是：1  否：0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_mentor`
--

INSERT INTO `t_mentor` (`menID`, `apply_id`, `teacher_id`, `perpoint`, `isfirst`) VALUES
(1, 1, 1, '3', 1),
(2, 1, 2, '2', 0),
(3, 1, 3, '1', 0),
(4, 16, 2, '2', 1),
(5, 16, 3, '3', 0),
(6, 19, 4, '1', 1);

-- --------------------------------------------------------

--
-- 表的结构 `t_student`
--

CREATE TABLE `t_student` (
  `ID` int(11) NOT NULL,
  `s_number` varchar(11) NOT NULL,
  `pwd` varchar(20) NOT NULL,
  `s_name` varchar(10) NOT NULL,
  `sex` varchar(10) NOT NULL,
  `class_id` int(11) NOT NULL,
  `study_status` int(11) NOT NULL COMMENT '在校：1  已毕业：2  休学/退学：3',
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `IDcard` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_student`
--

INSERT INTO `t_student` (`ID`, `s_number`, `pwd`, `s_name`, `sex`, `class_id`, `study_status`, `phone`, `email`, `IDcard`) VALUES
(2, '13110033101', '123456', '陈安平', '男', 2, 1, '13758777925', '345686441@qq.com', '330483199608166037'),
(3, '13110033102', '123456', '王浩', '男', 2, 1, '13756376456', '345686441@qq.com', '330483199508166032'),
(4, '13110033103', '123456', '邵靖凯', '男', 2, 1, '13745675432', '345686441@qq.com', '330483199508166033'),
(5, '13110033104', '123456', '杨吉', '男', 2, 1, '13758754564', '345686441@qq.com', NULL),
(6, '13110033105', '123456', '张俊炳', '男', 2, 1, '13568333653', '345686441@qq.com', '330456783453412345'),
(7, '13110033106', '123456', '沈立明', '男', 2, 1, '13568444563', '', ''),
(8, '13110033107', '123456', '孔喻', '男', 2, 1, NULL, NULL, NULL),
(26, '10110033101', '10110033101', '角色', '男', 11, 1, '13758777925', '', ''),
(27, '10110033102', '10110033102', '环境', '男', 13, 1, '627925', '', ''),
(11, '13110033110', '123456', '王佳伟', '男', 2, 1, '13017789888', '345686441@qq.com', '330483199508166032'),
(12, '13110033111', '123456', '金欢', '女', 2, 1, '13017789888', '345686441@qq.com', '330483199508166032'),
(13, '13110033112', '123456', '张觅', '女', 2, 1, '13017789888', '345686441@qq.com', '330483199508166032'),
(20, '13110033152', '123456', '林满武', '女', 11, 1, '627925', '345686442@qq.com', '330483199508166034'),
(21, '13110033117', '123456', '陈悦', '男', 2, 3, '13758777925', '', ''),
(24, '13110033176', '13110033176', '王杰希', '男', 4, 1, '13758777925', '', ''),
(25, '14110033101', '14110033101', '郑宇楠', '男', 12, 1, '627925', '', '');

-- --------------------------------------------------------

--
-- 表的结构 `t_teacher`
--

CREATE TABLE `t_teacher` (
  `ID` int(11) NOT NULL,
  `t_number` varchar(11) NOT NULL,
  `pwd` varchar(20) NOT NULL,
  `t_name` varchar(10) NOT NULL,
  `sex` varchar(10) NOT NULL,
  `academy_id` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `major_id` int(11) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `onjob` int(11) NOT NULL COMMENT '在职：1  离职：2  '
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_teacher`
--

INSERT INTO `t_teacher` (`ID`, `t_number`, `pwd`, `t_name`, `sex`, `academy_id`, `department_id`, `major_id`, `phone`, `email`, `onjob`) VALUES
(1, '10110033101', '123456', '教师1', '男', 1, 1, 1, '137758777925', '345686441@qq.com', 1),
(2, '10110033102', '123456', '教师2', '男', 1, 1, 1, '13017789888', '345686441@qq.com', 1),
(3, '10110033103', '123456', '教师3', '男', 1, 1, 1, '13758777925', '345686441@qq.com', 1),
(4, '10110033104', '123456', '教师4', '男', 1, 1, 2, '13017789888', '345686441@qq.com', 1),
(5, '10110033105', '123456', '教师5', '男', 1, 1, 1, '13017789555', '345686441@qq.com', 1),
(6, '10110033106', '123456', '教师6', '男', 1, 1, 1, '13017789888', NULL, 1),
(7, '10110033107', '123456', '教师7', '男', 1, 1, 1, '13758777925', '345686441@qq.com', 2),
(8, '13110033108', '123456', '教师8', '男', 1, 1, 1, '13017789888', '345686441@qq.com', 1),
(9, '10110033109', '123456', '教师9', '男', 1, 1, 1, '13017789888', '345686441@qq.com', 1),
(10, '10110033110', '123456', '教师10', '男', 1, 1, 1, '13017789888', '345686441@qq.com', 1),
(11, '10110033111', '123456', '教师11', '男', 1, 1, 1, '13017789888', '345686441@qq.com', 1),
(13, '10110033121', '123456', '王立军', '男', 1, 6, 4, '13758777925', '345686441@qq.com', 1);

-- --------------------------------------------------------

--
-- 表的结构 `t_years`
--

CREATE TABLE `t_years` (
  `ID` int(11) NOT NULL,
  `y_year` varchar(10) NOT NULL COMMENT '学年'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `t_years`
--

INSERT INTO `t_years` (`ID`, `y_year`) VALUES
(8, '2013-2014'),
(9, '2014-2015'),
(10, '2015-2016'),
(11, '2016-2017');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `t_academy`
--
ALTER TABLE `t_academy`
  ADD PRIMARY KEY (`aID`);

--
-- Indexes for table `t_apply`
--
ALTER TABLE `t_apply`
  ADD PRIMARY KEY (`appID`);

--
-- Indexes for table `t_bulletin`
--
ALTER TABLE `t_bulletin`
  ADD PRIMARY KEY (`bulleID`);

--
-- Indexes for table `t_class`
--
ALTER TABLE `t_class`
  ADD PRIMARY KEY (`cID`);

--
-- Indexes for table `t_competition`
--
ALTER TABLE `t_competition`
  ADD PRIMARY KEY (`compID`);

--
-- Indexes for table `t_contestants`
--
ALTER TABLE `t_contestants`
  ADD PRIMARY KEY (`contID`);

--
-- Indexes for table `t_department`
--
ALTER TABLE `t_department`
  ADD PRIMARY KEY (`dID`);

--
-- Indexes for table `t_group`
--
ALTER TABLE `t_group`
  ADD PRIMARY KEY (`groupID`);

--
-- Indexes for table `t_level`
--
ALTER TABLE `t_level`
  ADD PRIMARY KEY (`rID`);

--
-- Indexes for table `t_major`
--
ALTER TABLE `t_major`
  ADD PRIMARY KEY (`mID`);

--
-- Indexes for table `t_manager`
--
ALTER TABLE `t_manager`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `t_mentor`
--
ALTER TABLE `t_mentor`
  ADD PRIMARY KEY (`menID`);

--
-- Indexes for table `t_student`
--
ALTER TABLE `t_student`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `t_teacher`
--
ALTER TABLE `t_teacher`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `t_years`
--
ALTER TABLE `t_years`
  ADD PRIMARY KEY (`ID`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `t_academy`
--
ALTER TABLE `t_academy`
  MODIFY `aID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- 使用表AUTO_INCREMENT `t_apply`
--
ALTER TABLE `t_apply`
  MODIFY `appID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;
--
-- 使用表AUTO_INCREMENT `t_bulletin`
--
ALTER TABLE `t_bulletin`
  MODIFY `bulleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- 使用表AUTO_INCREMENT `t_class`
--
ALTER TABLE `t_class`
  MODIFY `cID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- 使用表AUTO_INCREMENT `t_competition`
--
ALTER TABLE `t_competition`
  MODIFY `compID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- 使用表AUTO_INCREMENT `t_contestants`
--
ALTER TABLE `t_contestants`
  MODIFY `contID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;
--
-- 使用表AUTO_INCREMENT `t_department`
--
ALTER TABLE `t_department`
  MODIFY `dID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- 使用表AUTO_INCREMENT `t_group`
--
ALTER TABLE `t_group`
  MODIFY `groupID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- 使用表AUTO_INCREMENT `t_level`
--
ALTER TABLE `t_level`
  MODIFY `rID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- 使用表AUTO_INCREMENT `t_major`
--
ALTER TABLE `t_major`
  MODIFY `mID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- 使用表AUTO_INCREMENT `t_manager`
--
ALTER TABLE `t_manager`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `t_mentor`
--
ALTER TABLE `t_mentor`
  MODIFY `menID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- 使用表AUTO_INCREMENT `t_student`
--
ALTER TABLE `t_student`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- 使用表AUTO_INCREMENT `t_teacher`
--
ALTER TABLE `t_teacher`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;
--
-- 使用表AUTO_INCREMENT `t_years`
--
ALTER TABLE `t_years`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
