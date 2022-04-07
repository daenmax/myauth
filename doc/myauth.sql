-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2022-03-17 20:57:53
-- 服务器版本： 5.6.50-log
-- PHP 版本： 7.4.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `myauth`
--

-- --------------------------------------------------------

--
-- 表的结构 `ma_acard`
--

CREATE TABLE `ma_acard` (
  `id` int(11) NOT NULL,
  `ckey` varchar(255) DEFAULT NULL,
  `money` varchar(255) DEFAULT '0' COMMENT '余额',
  `add_time` int(10) DEFAULT NULL COMMENT '生成时间',
  `let_time` int(10) DEFAULT NULL COMMENT '使用时间',
  `let_user` varchar(255) DEFAULT NULL COMMENT '使用人账号',
  `status` int(2) DEFAULT '0' COMMENT '卡密状态，0=未使用，1=已使用，2=被禁用'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_admin`
--

CREATE TABLE `ma_admin` (
  `id` int(11) NOT NULL,
  `user` varchar(255) DEFAULT NULL,
  `pass` varchar(255) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `reg_time` int(10) DEFAULT NULL,
  `last_time` int(10) DEFAULT NULL,
  `last_ip` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '0=禁用，1=正常',
  `role` int(11) DEFAULT NULL COMMENT '角色ID',
  `money` varchar(255) DEFAULT NULL COMMENT '账户余额'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_alog`
--

CREATE TABLE `ma_alog` (
  `id` int(11) NOT NULL,
  `money` varchar(255) DEFAULT NULL COMMENT '变动余额，负数为扣除',
  `after_money` varchar(255) DEFAULT NULL COMMENT '变动后的余额',
  `admin_id` int(11) DEFAULT NULL COMMENT '管理员ID',
  `data` varchar(255) DEFAULT NULL COMMENT '可以为管理员ID、生成卡密的信息、添加授权的信息、使用的卡密',
  `type` varchar(255) DEFAULT NULL COMMENT '管理员奖惩、生成卡密、添加授权、使用卡密',
  `add_time` int(10) DEFAULT NULL COMMENT '变动时间'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_ban`
--

CREATE TABLE `ma_ban` (
  `id` int(11) NOT NULL,
  `value` varchar(255) DEFAULT NULL COMMENT '要封禁的对象',
  `add_time` int(10) DEFAULT NULL,
  `to_time` int(10) DEFAULT NULL COMMENT '封禁到期时间，-1=永久',
  `why` varchar(255) DEFAULT NULL COMMENT '封禁原因',
  `from_soft_id` int(11) DEFAULT NULL,
  `type` int(2) DEFAULT NULL COMMENT '封禁类型，1=机器码，2=IP，3=账号'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_card`
--

CREATE TABLE `ma_card` (
  `id` int(11) NOT NULL,
  `ckey` varchar(255) DEFAULT NULL,
  `point` int(10) DEFAULT '0' COMMENT '点数',
  `seconds` int(10) DEFAULT '0' COMMENT '秒数',
  `add_time` int(10) DEFAULT NULL COMMENT '生成时间',
  `let_time` int(10) DEFAULT NULL COMMENT '使用时间',
  `let_user` varchar(255) DEFAULT NULL COMMENT '使用人账号',
  `status` int(2) DEFAULT '0' COMMENT '卡密状态，0=未使用，1=已使用，2=被禁用',
  `from_soft_id` int(11) DEFAULT NULL COMMENT '所属软件id',
  `from_admin_id` int(11) DEFAULT NULL COMMENT '生成人ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_config`
--

CREATE TABLE `ma_config` (
  `id` int(11) NOT NULL,
  `seo_title` varchar(255) DEFAULT NULL,
  `seo_keywords` varchar(255) DEFAULT NULL,
  `seo_description` varchar(255) DEFAULT NULL,
  `dingbot_access_token` varchar(255) DEFAULT NULL COMMENT '钉钉机器人',
  `dingbot_msg` int(2) DEFAULT '0' COMMENT '0=关闭通知，1=开启通知',
  `open_api_key` varchar(255) DEFAULT NULL COMMENT '开放接口key'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_data`
--

CREATE TABLE `ma_data` (
  `id` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL COMMENT '上报类型',
  `content` text COMMENT '上报内容',
  `ip` varchar(255) DEFAULT NULL COMMENT 'IP',
  `add_time` int(10) DEFAULT NULL COMMENT '上报时间',
  `from_soft_id` int(11) DEFAULT NULL,
  `from_ver_id` int(11) DEFAULT NULL,
  `device_info` varchar(255) DEFAULT NULL COMMENT '设备信息',
  `device_code` varchar(255) DEFAULT NULL COMMENT '机器码'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_event`
--

CREATE TABLE `ma_event` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '事件名称，同一软件下禁止重复',
  `point` int(10) DEFAULT '0' COMMENT '点数变动值，增加为正数，扣除为负数',
  `seconds` int(10) DEFAULT '0' COMMENT '秒数变动值，增加为正数，扣除为负数',
  `add_time` int(10) DEFAULT NULL,
  `status` int(2) DEFAULT '1' COMMENT '0=禁用，1=正常',
  `from_soft_id` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_js`
--

CREATE TABLE `ma_js` (
  `id` int(11) NOT NULL,
  `js_fun` varchar(255) DEFAULT NULL COMMENT '执行JS函数名，同一软件下不要重复',
  `js_content` text COMMENT 'JS代码',
  `add_time` int(10) DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '0=禁用，1=正常',
  `from_soft_id` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '备注'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_menu`
--

CREATE TABLE `ma_menu` (
  `id` varchar(56) NOT NULL COMMENT 'UUID',
  `parent_id` varchar(56) DEFAULT NULL COMMENT '父ID，根则空',
  `level` int(2) NOT NULL COMMENT '层级，从1开始',
  `sort` int(2) DEFAULT '1' COMMENT '排序，越小越大，从1开始',
  `type` int(2) NOT NULL COMMENT '1=目录，2=菜单',
  `path` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `icon` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `ma_menu`
--

INSERT INTO `ma_menu` (`id`, `parent_id`, `level`, `sort`, `type`, `path`, `title`, `icon`) VALUES
('59664367-11eb-489e-867d-af6c66129d03', '0', 1, 1, 2, '/Home/Introduce', '首页', 'home'),
('30633357-dd12-415e-8631-660e2aa6b9ad', '0', 1, 3, 1, NULL, '信息管理', 'info-circle'),
('e70b3176-a2fa-4fc5-90f3-5abd9e2d13b8', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 2, 2, '/DataMaintenance/PassCardManage/List', '卡密管理', NULL),
('e50cadbc-8b2b-4c8f-a262-9da95d3371bb', '30633357-dd12-415e-8631-660e2aa6b9ad', 2, 1, 2, '/InfoManage/SoftManage/List', '软件管理', ''),
('94beb8ae-a606-4dea-bfa8-57bc734818bf', '30633357-dd12-415e-8631-660e2aa6b9ad', 2, 2, 2, '/InfoManage/VersionsManage/List', '版本管理', ''),
('bcb68a35-eb0f-4696-acd8-3c76897e0f0f', '30633357-dd12-415e-8631-660e2aa6b9ad', 2, 3, 2, '/InfoManage/UserManage/List', '用户管理', ''),
('d6321208-4980-46e3-b3d4-ec057009472c', '0', 1, 4, 1, NULL, '数据维护', 'database'),
('b89eb2d8-40e4-4698-ab5a-ddaed7846ee1', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 1, 2, '/DataMaintenance/MsgManage/List', '回复管理', NULL),
('1d3838df-bc61-42e1-a149-dcc2705e2894', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 3, 2, '/DataMaintenance/FuncManage/List', '函数管理', NULL),
('5b35fe6a-da5f-46d5-b1ed-076308a38a13', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 4, 2, '/DataMaintenance/EventManage/List', '事件管理', NULL),
('922666ba-1ed1-4b6b-9043-30aeead1eebe', 'da3af6df-cd00-4746-b788-bd2dfeab716f', 2, 1, 2, '/MyManage/MyAuthManage/List', '我的授权', NULL),
('dbe9effe-0dc8-4378-a64c-5bf904ac6fae', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 6, 2, '/DataMaintenance/DataManage/List', '数据管理', NULL),
('a59b674e-8d45-46c2-be6e-ff44d2ee7b86', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 5, 2, '/DataMaintenance/BannedManage/List', '封禁管理', NULL),
('da3af6df-cd00-4746-b788-bd2dfeab716f', '0', 1, 6, 1, NULL, '我的管理', 'user'),
('43767678-01c5-4b54-bfac-e108b6ceb32a', '0', 1, 5, 1, NULL, '系统管理', 'setting'),
('d7720078-3b65-4f83-b404-c117e5c46b68', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 1, 2, '/SystemManage/AdminManage/List', '管理员管理', NULL),
('546ea73a-1d46-41f5-bfa6-e646b3e741ae', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 4, 2, '/SystemManage/MenuManage/List', '菜单管理', NULL),
('d27462d2-b316-4983-9066-f283fcf17e46', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 7, 2, '/SystemManage/SettingManage/index', '系统管理', NULL),
('d58b4547-e20f-4eb9-b7d5-b3195ed5cc18', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 5, 2, '/SystemManage/RoleManage/List', '角色管理', NULL),
('014bf6d7-cff3-4b61-aa3b-f59cc0dd3235', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 6, 2, '/SystemManage/StrategyManage/List', '策略管理', NULL),
('71eedb8d-ae9e-4958-a351-c8dfb4711f21', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 3, 2, '/SystemManage/LogManage/List', '用户日志', NULL),
('9d953050-f354-4e9f-9f15-b91ea6a666f0', '43767678-01c5-4b54-bfac-e108b6ceb32a', 2, 2, 2, '/SystemManage/AdminLogManage/List', '管理员日志', NULL),
('23a8778f-9424-449a-a68e-54b12e4c30f3', 'da3af6df-cd00-4746-b788-bd2dfeab716f', 2, 2, 2, '/MyManage/MyPassCardManage/List', '我的卡密', NULL),
('cd256048-3f8e-4fb7-ada3-55d538ce7611', 'da3af6df-cd00-4746-b788-bd2dfeab716f', 2, 3, 2, '/MyManage/MyBalanceManage/List', '我的余额', NULL),
('d499258d-ea25-47e1-b580-ed98830df37f', 'd6321208-4980-46e3-b3d4-ec057009472c', 2, 7, 2, '/DataMaintenance/AgencyPassCardManage/List', '代理卡密', NULL),
('e4fa47b7-aa79-4708-9ce1-c7b686db6a50', NULL, 1, 2, 2, '/DataStatistics/List', '数据看板', 'radar-chart');

-- --------------------------------------------------------

--
-- 表的结构 `ma_msg`
--

CREATE TABLE `ma_msg` (
  `id` int(11) NOT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `msg` text,
  `status` int(2) DEFAULT '1' COMMENT '0=禁用，1=正常',
  `from_soft_id` int(11) DEFAULT NULL COMMENT '所属软件id',
  `from_ver_id` int(11) DEFAULT NULL COMMENT '所属软件版本，留空则全版本可用'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_openapi`
--

CREATE TABLE `ma_openapi` (
  `id` int(11) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `add_time` int(11) DEFAULT NULL,
  `status` int(2) DEFAULT '0' COMMENT '0=禁用，1=正常',
  `remark` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_plog`
--

CREATE TABLE `ma_plog` (
  `id` int(11) NOT NULL,
  `point` int(10) DEFAULT '0' COMMENT '变动点数，扣除为负数',
  `after_point` int(10) DEFAULT '0' COMMENT '变动后的点数',
  `seconds` int(10) DEFAULT '0' COMMENT '变动秒数，扣除为负数',
  `after_seconds` int(10) DEFAULT '0' COMMENT '变动后的到期时间',
  `from_user` varchar(255) DEFAULT NULL COMMENT '变动账号',
  `add_time` int(10) DEFAULT NULL COMMENT '变动时间',
  `from_event_name` varchar(255) DEFAULT NULL COMMENT '所属事件名称',
  `from_event_id` int(11) DEFAULT NULL COMMENT '所属事件ID',
  `from_soft_id` int(11) DEFAULT NULL,
  `from_ver_id` int(11) DEFAULT NULL,
  `remark` text COMMENT '日志的说明内容，使用卡密时包含使用的卡密'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_role`
--

CREATE TABLE `ma_role` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '角色名',
  `from_soft_id` int(11) DEFAULT NULL COMMENT '0=超级管理员',
  `meun_ids` text COMMENT '只存menu的id，json数组',
  `discount` int(11) NOT NULL DEFAULT '100' COMMENT '折扣，单位百分%'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `ma_role`
--

INSERT INTO `ma_role` (`id`, `name`, `from_soft_id`, `meun_ids`, `discount`) VALUES
(1, '超级管理员', 0, '[\"59664367-11eb-489e-867d-af6c66129d03\",\"e4fa47b7-aa79-4708-9ce1-c7b686db6a50\",\"e50cadbc-8b2b-4c8f-a262-9da95d3371bb\",\"94beb8ae-a606-4dea-bfa8-57bc734818bf\",\"bcb68a35-eb0f-4696-acd8-3c76897e0f0f\",\"b89eb2d8-40e4-4698-ab5a-ddaed7846ee1\",\"e70b3176-a2fa-4fc5-90f3-5abd9e2d13b8\",\"1d3838df-bc61-42e1-a149-dcc2705e2894\",\"5b35fe6a-da5f-46d5-b1ed-076308a38a13\",\"a59b674e-8d45-46c2-be6e-ff44d2ee7b86\",\"dbe9effe-0dc8-4378-a64c-5bf904ac6fae\",\"d499258d-ea25-47e1-b580-ed98830df37f\",\"d7720078-3b65-4f83-b404-c117e5c46b68\",\"9d953050-f354-4e9f-9f15-b91ea6a666f0\",\"71eedb8d-ae9e-4958-a351-c8dfb4711f21\",\"546ea73a-1d46-41f5-bfa6-e646b3e741ae\",\"d58b4547-e20f-4eb9-b7d5-b3195ed5cc18\",\"014bf6d7-cff3-4b61-aa3b-f59cc0dd3235\",\"d27462d2-b316-4983-9066-f283fcf17e46\",\"922666ba-1ed1-4b6b-9043-30aeead1eebe\",\"23a8778f-9424-449a-a68e-54b12e4c30f3\",\"cd256048-3f8e-4fb7-ada3-55d538ce7611\",\"30633357-dd12-415e-8631-660e2aa6b9ad\",\"d6321208-4980-46e3-b3d4-ec057009472c\",\"43767678-01c5-4b54-bfac-e108b6ceb32a\",\"da3af6df-cd00-4746-b788-bd2dfeab716f\"]', 100),
(2, '授权商', 1, '[\"59664367-11eb-489e-867d-af6c66129d03\",\"922666ba-1ed1-4b6b-9043-30aeead1eebe\",\"23a8778f-9424-449a-a68e-54b12e4c30f3\",\"cd256048-3f8e-4fb7-ada3-55d538ce7611\",\"da3af6df-cd00-4746-b788-bd2dfeab716f\"]', 80),
(3, '代理商', 1, '[\"59664367-11eb-489e-867d-af6c66129d03\",\"922666ba-1ed1-4b6b-9043-30aeead1eebe\",\"23a8778f-9424-449a-a68e-54b12e4c30f3\",\"cd256048-3f8e-4fb7-ada3-55d538ce7611\",\"da3af6df-cd00-4746-b788-bd2dfeab716f\"]', 70);

-- --------------------------------------------------------

--
-- 表的结构 `ma_soft`
--

CREATE TABLE `ma_soft` (
  `id` int(11) NOT NULL,
  `skey` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(2) DEFAULT '1' COMMENT '0=停用，1=正常，2=维护',
  `type` int(2) DEFAULT '0' COMMENT '0=需要授权，1=不需要授权',
  `add_time` int(10) DEFAULT NULL,
  `gen_key` varchar(255) DEFAULT NULL COMMENT '数据加密秘钥',
  `gen_status` int(2) DEFAULT '0' COMMENT '0=数据不加密，1=数据加密',
  `bind_device_code` int(2) DEFAULT '0' COMMENT '0=不绑定机器码，1=绑定机器码',
  `heart_time` int(10) DEFAULT '0' COMMENT '心跳有效时间',
  `register` int(2) DEFAULT '0' COMMENT '0=关闭注册，1=开启注册'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_strategy`
--

CREATE TABLE `ma_strategy` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '策略名称，例如：月卡，年卡',
  `type` int(2) DEFAULT '1' COMMENT '1=期限卡，2=余额卡',
  `value` int(10) DEFAULT NULL COMMENT '卡面额',
  `sort` int(2) DEFAULT '1' COMMENT '排序，越小越前，从1开始',
  `price` varchar(11) DEFAULT '0.00' COMMENT '价格',
  `from_soft_id` int(11) DEFAULT NULL,
  `status` int(2) DEFAULT '1' COMMENT '状态。1=正常，0=禁用'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_user`
--

CREATE TABLE `ma_user` (
  `id` int(11) NOT NULL,
  `user` varchar(255) DEFAULT NULL COMMENT '账号',
  `pass` varchar(255) DEFAULT NULL COMMENT '密码',
  `name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `point` int(30) DEFAULT '0' COMMENT '点数',
  `qq` varchar(255) DEFAULT NULL,
  `last_ip` varchar(255) DEFAULT NULL COMMENT '最后登录或注册时的IP',
  `last_time` int(10) DEFAULT NULL COMMENT '最后登录的时间戳',
  `reg_time` int(10) DEFAULT NULL COMMENT '用户注册的时间戳',
  `auth_time` int(10) DEFAULT '-1' COMMENT '授权到期的时间戳，-1=永久',
  `from_soft_id` int(10) DEFAULT NULL COMMENT '所属软件id',
  `from_soft_key` varchar(255) DEFAULT NULL COMMENT '所属软件key',
  `from_ver_id` int(10) DEFAULT NULL COMMENT '最后登录的软件的版本id',
  `from_ver_key` varchar(255) DEFAULT NULL COMMENT '最后登录的软件的版本key',
  `token` text COMMENT '登录成功的token',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `device_info` varchar(255) DEFAULT NULL COMMENT '最后登录或注册时的设备信息',
  `device_code` varchar(255) DEFAULT NULL COMMENT '最后登录或注册时的机器码',
  `ckey` varchar(255) DEFAULT NULL COMMENT '卡密',
  `from_admin_id` int(11) DEFAULT NULL COMMENT '生成人ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_version`
--

CREATE TABLE `ma_version` (
  `id` int(11) NOT NULL,
  `ver` varchar(255) DEFAULT NULL,
  `vkey` varchar(255) DEFAULT NULL,
  `from_soft_id` int(11) DEFAULT NULL COMMENT '所属软件id',
  `upd_log` text COMMENT '更新日志',
  `upd_time` int(10) DEFAULT NULL,
  `upd_type` int(2) DEFAULT NULL COMMENT '更新模式，0=可选，1=强制',
  `status` int(2) DEFAULT NULL COMMENT '0=停用，1=正常'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_storage`
--

CREATE TABLE `ma_storage` (
  `id` int(11) NOT NULL,
  `from_soft_id` int(11) DEFAULT NULL,
  `from_storage_type_id` int(11) DEFAULT NULL COMMENT '所属存储类型ID',
  `content` text,
  `number` int(11) DEFAULT '1' COMMENT '数量',
  `remark` text,
  `status` int(2) DEFAULT '1' COMMENT '状态，0=禁用，1=正常'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `ma_storage_type`
--

CREATE TABLE `ma_storage_type` (
   `id` int(11) NOT NULL,
   `type` varchar(255) DEFAULT NULL COMMENT '类型',
   `status` int(2) DEFAULT '1' COMMENT '状态，0=禁用，1=正常',
   `from_soft_id` int(12) DEFAULT NULL COMMENT '所属软件id'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

--
-- 转储表的索引
--

--
-- 表的索引 `ma_storage`
--
ALTER TABLE `ma_storage`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_storage_type`
--
ALTER TABLE `ma_storage_type`
  ADD PRIMARY KEY (`id`) USING BTREE;


--
-- 表的索引 `ma_acard`
--
ALTER TABLE `ma_acard`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_admin`
--
ALTER TABLE `ma_admin`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_alog`
--
ALTER TABLE `ma_alog`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_ban`
--
ALTER TABLE `ma_ban`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_card`
--
ALTER TABLE `ma_card`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_config`
--
ALTER TABLE `ma_config`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_data`
--
ALTER TABLE `ma_data`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_event`
--
ALTER TABLE `ma_event`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_js`
--
ALTER TABLE `ma_js`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_menu`
--
ALTER TABLE `ma_menu`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_msg`
--
ALTER TABLE `ma_msg`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_openapi`
--
ALTER TABLE `ma_openapi`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_plog`
--
ALTER TABLE `ma_plog`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_role`
--
ALTER TABLE `ma_role`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_soft`
--
ALTER TABLE `ma_soft`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_strategy`
--
ALTER TABLE `ma_strategy`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_user`
--
ALTER TABLE `ma_user`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 表的索引 `ma_version`
--
ALTER TABLE `ma_version`
  ADD PRIMARY KEY (`id`) USING BTREE;

--
-- 在导出的表使用AUTO_INCREMENT
--
--
-- 使用表AUTO_INCREMENT `ma_storage`
--
ALTER TABLE `ma_storage`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_storage_type`
--
ALTER TABLE `ma_storage_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_acard`
--
ALTER TABLE `ma_acard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_admin`
--
ALTER TABLE `ma_admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_alog`
--
ALTER TABLE `ma_alog`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_ban`
--
ALTER TABLE `ma_ban`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_card`
--
ALTER TABLE `ma_card`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_data`
--
ALTER TABLE `ma_data`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_event`
--
ALTER TABLE `ma_event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_js`
--
ALTER TABLE `ma_js`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_msg`
--
ALTER TABLE `ma_msg`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_openapi`
--
ALTER TABLE `ma_openapi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_plog`
--
ALTER TABLE `ma_plog`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_role`
--
ALTER TABLE `ma_role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用表AUTO_INCREMENT `ma_soft`
--
ALTER TABLE `ma_soft`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_strategy`
--
ALTER TABLE `ma_strategy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_user`
--
ALTER TABLE `ma_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ma_version`
--
ALTER TABLE `ma_version`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
