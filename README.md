# MyAuth

#### 介绍
    一个授权管理系统，使用springboot、mybatis-plus、redis、mysql等。
此仓库为后端代码<br>
![logo](https://images.gitee.com/uploads/images/2022/0218/215303_dbbda392_5370510.png)

* 我的QQ：[1330166564](https://wpa.qq.com/msgrd?v=3&uin=1330166564&site=qq&menu=yes)
* 官方Q群：[1016357430](https://jq.qq.com/?_wv=1027&k=eaectWIr)
* 官方网站：[https://www.myauthx.cn/](https://www.myauthx.cn/)

#### 前端
- [@TianYe](https://gitee.com/fieldtianye)
项目地址https://gitee.com/fieldtianye/my-auth-web
- [@施瑞贤](https://gitee.com/shiruixian)
项目地址https://gitee.com/shiruixian/my-auth-vue
- [@杨灿林](https://gitee.com/yang-canlin)
项目地址https://gitee.com/yang-canlin/my-auth

#### 开发环境
    - Windows10 家庭版 21H1
    - Java JDK 11
    - Maven 3.6.3
    - IDEA 2021.1
    - Redis 3.0.504
    - MySQL 8.0.12

##### 2.打包
    修改application.yml里的环境 

#### 使用教程
##### 1.安装
    1.  克隆仓库到本地
    2.  修改配置：application-dev.yml和application-prod.yml
    3.  启动Redis、MySQL
    4.  运行MyAuthApplication.java启动类
    5.  访问测试地址，查看是否正常：http://localhost:8081/myauth/web/connect

##### 2.打包
    1.  修改application.yml里的环境

    2.  设置跳过测试
![IDEA打包教程](https://images.gitee.com/uploads/images/2022/0208/094816_5322dd06_5370510.png)

#### 接口
#### API文档

在线地址：[https://www.apifox.cn/apidoc/project-646981/](https://www.apifox.cn/apidoc/project-646981/)

##### 功能接口

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
##### 网页接口
- [x] 获取web信息
- [x] 登录
- [x] 检查token
- [x] 检查服务状态
- [x] 获取更新日志
- [x] 修改密码
- [x] 软件管理
- [x] 版本管理
- [x] 回复管理
- [x] 卡密管理
- [x] 用户管理
- [x] 函数管理
- [x] 事件管理
- [x] 封禁管理
- [ ] 数据管理
- [ ] 系统设置
- [ ] 数据统计

##### 开放接口
- [ ] 获取在线人数
- [ ] 查询授权
- [ ] 获取用户总数

##### 其他功能

- [ ] 钉钉消息推送
- [ ] 定时任务检查服务器性能消耗状态


#### 感谢
    我的各位朋友