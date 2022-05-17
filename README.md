# MyAuth

### 介绍
    一个简单的授权管理系统，使用了springboot、mybatis-plus、redis、mysql等。
    此仓库为后端代码

![logo](https://images.gitee.com/uploads/images/2022/0218/215303_dbbda392_5370510.png)

* 我的QQ：[1330166564](https://wpa.qq.com/msgrd?v=3&uin=1330166564&site=qq&menu=yes)
* 官方Q群：[1016357430](https://jq.qq.com/?_wv=1027&k=eaectWIr)
### 其他分支
- [@Lonely](https://gitee.com/Lonely_LY)
  项目地址 https://gitee.com/Lonely_LY/myauth <br>
  在本仓库的基础上增加了邮件通知、易支付等功能
### 前端
- [@TianYe](https://gitee.com/fieldtianye)
项目地址 https://gitee.com/fieldtianye/my-auth-web
- [@施瑞贤](https://gitee.com/shiruixian)
项目地址 https://gitee.com/shiruixian/my-auth-vue

### 预览

MyAuthWeb @TianYe版预览
https://www.cnblogs.com/daen/p/16019664.html


### 开发环境
    - Windows10 家庭版 21H1
    - Java JDK 17
    - Maven 3.6.3
    - IDEA 2021.1
    - Redis 3.0.504
    - MySQL 8.0.12

### 使用教程
#### 1.安装
    1.  克隆仓库到本地
    2.  修改配置：application-dev.yml和application-prod.yml
    3.  启动Redis、MySQL
    4.  导入doc/myauth.sql到数据库
    5.  运行MyAuthApplication.java启动类
    6.  访问测试地址，查看是否正常：http://localhost:8081/myauth/web/connect
    7.  运行后会检查admin表，如果是首次运行，即没有任何用户，那么会自动添加一个，账号admin，密码123456，该账号拥有最高权限，账号必须为admin不能修改
#### 2.打包
    1.  修改application.yml里的环境
![IDEA打包教程](https://images.gitee.com/uploads/images/2022/0311/191225_bdb8cfee_5370510.png)

### 部署教程
[MyAuth 后端 宝塔面板部署教程](https://www.cnblogs.com/daen/p/15997872.html)
<br>
[MyAuth 前端 宝塔面板部署教程](https://www.cnblogs.com/daen/p/16015813.html)

### 各语言DEMO
见doc/demo/目录

### API文档
在线地址：[https://www.apifox.cn/apidoc/project-646981/](https://www.apifox.cn/apidoc/project-646981/)

###  功能清单

##### 公开接口
- [x] 获取更新日志
- [x] 检查服务状态
- [x] 获取web信息
- [x] 获取软件列表_自助用
- [x] 自助注册账号
- [x] 自助修改账号
- [x] 查询账号信息
- [x] 查询管理员信息
- [x] 查询额外存储信息

##### 软件接口
- [x] 初始化
- [x] 检测更新
- [x] 注册
- [x] 登录
- [x] 心跳
- [x] 使用卡密
- [x] 获取回复
- [x] 执行JS
- [x] 上报数据
- [x] 触发事件
- [x] 解绑
- [x] 修改密码
- [x] 修改资料

##### 后台接口
- [x] 个人信息修改
- [x] 软件管理
- [x] 版本管理
- [x] 用户管理
- [x] 回复管理
- [x] 卡密管理
- [x] 函数管理
- [x] 事件管理
- [x] 封禁管理
- [x] 数据管理
- [x] 管理员管理
- [x] 代理管理
- [x] 管理员日志
- [x] 用户日志
- [x] 菜单管理
- [x] 角色管理
- [x] 策略管理
- [x] 代理卡密
- [x] 系统设置
- [x] 数据统计
- [x] 数据图表
- [x] 额外存储类型
- [x] 额外存储
- [x] 代理我的授权
- [x] 代理我的卡密
- [x] 代理我的余额

##### 开放接口
- [x] 获取在线人数
- [x] 获取用户总数
- [x] 添加额外存储信息
- [x] 删除额外存储信息
- [x] 获取额外存储列表信息
  
- 目前就这几个，有需要的话，我再加

##### 其他功能
- [ ] 钉钉消息推送

##### 附带
###### 更新日志
    doc中有两个更新日志页面，效果如下
    其中一个是PHP版本，一个是JS版本（HTML纯静态）
    - PHP版本可以保证源服务器地址不被泄露
    - JS版本可以部署在各种托管上（只上传index.html即可），不依赖nginx等环境，但是会泄露源服务器地址
    各位权衡考虑使用哪个
	![示例](https://images.gitee.com/uploads/images/2022/0301/225502_1788301a_5370510.png)
###### 小贴士
> 更新内容每行前面加上【新增】【修复】【优化】【其他】【删除】等标签，更新日志页面会自动处理成tag

例如：
![示例](https://images.gitee.com/uploads/images/2022/0317/235803_7fe34f73_5370510.png)
效果：
![示例](https://images.gitee.com/uploads/images/2022/0317/235842_d3407750_5370510.png)

#### 感谢
    我的各位朋友们
