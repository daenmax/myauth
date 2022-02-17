# MyAuth

#### 介绍
一个授权管理系统，此仓库为后端代码

#### 配套前端

暂无

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
修改application.yml里的环境 
![IDEA打包教程](https://images.gitee.com/uploads/images/2022/0208/094816_5322dd06_5370510.png)

#### 接口
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
##### 网页接口
- [x] 获取web信息
- [x] 登录
- [x] 检查token
- [x] 检查服务状态
- [x] 修改密码
- [x] 软件管理
- [x] 版本管理
- [x] 回复管理
- [ ] 卡密管理
- [ ] 用户管理
- [ ] 函数管理
- [ ] 事件管理
- [ ] 封禁管理
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

#### API文档

1.  在线地址：https://docs.apipost.cn/preview/869c49ca2ae5a81f/9dd4b2f48d34bc08
2.  离线文档：

#### 感谢

