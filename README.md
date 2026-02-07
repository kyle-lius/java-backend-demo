# Spring Boot 后端项目说明

## 项目概述
这是一个基于Spring Boot的后端项目，集成了MySQL数据库和Redis缓存，提供了用户管理、产品管理和订单管理的基本功能。

## 技术栈
- Spring Boot 2.7.18
- MyBatis 2.3.2
- MySQL 8.0
- Redis
- Lombok
- Java 17

## 项目结构
```
src/main/java/com/example/backenddemo/
├── BackendDemoApplication.java    # 启动类
├── controller/
│   ├── UserController.java        # 用户控制器
│   ├── ProductController.java     # 产品控制器
│   └── OrderController.java       # 订单控制器
├── service/
│   ├── UserService.java           # 用户服务层
│   ├── ProductService.java        # 产品服务层
│   └── OrderService.java          # 订单服务层
├── mapper/
│   ├── UserMapper.java            # 用户数据访问层
│   ├── ProductMapper.java         # 产品数据访问层
│   ├── OrderMapper.java           # 订单数据访问层
│   └── OrderItemMapper.java       # 订单项数据访问层
└── entity/
    ├── User.java                  # 用户实体类
    ├── Product.java               # 产品实体类
    ├── Order.java                 # 订单实体类
    └── OrderItem.java             # 订单项实体类

src/main/resources/
├── application.yml                # 配置文件
└── db/
    └── table.sql             # 数据库建表脚本

src/main/resources/mapper/
    ├── UserMapper.xml             # 用户SQL映射文件
    ├── ProductMapper.xml          # 产品SQL映射文件
    ├── OrderMapper.xml            # 订单SQL映射文件
    └── OrderItemMapper.xml        # 订单项SQL映射文件
```

## 配置说明

### application.yml 配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/backenddemo?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 2000ms
```

### 数据库配置
1. 创建数据库：`userdb`
2. 执行 `src/main/resources/db/table.sql` 脚本创建用户表
3. 确保MySQL服务正常运行

### Redis配置
确保Redis服务在本地6379端口正常运行

## API接口说明

### 用户管理接口
- **GET /api/users** - 获取所有用户
- **GET /api/users/{id}** - 根据ID获取用户
- **GET /api/users/username/{username}** - 根据用户名获取用户
- **POST /api/users** - 创建用户
- **PUT /api/users** - 更新用户
- **DELETE /api/users/{id}** - 删除用户

### 产品管理接口
- **GET /api/product/{id}** - 根据ID查询产品
- **GET /api/product/all** - 查询所有产品

### 订单管理接口
- **GET /api/order/all** - 查询所有订单
- **POST /api/order/create** - 创建订单


### 请求示例
### 创建用户
```json
// POST /api/users
{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138000"
}
```

### 响应格式
```json
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

### 产品列表
```json
// GET /api/product/all
{}
```
### 响应格式
```
{
    "data": [
        {
            "id": 1,
            "name": "iPhone 15",
            "description": "最新款苹果手机",
            "price": 5999.00,
            "stock": 100,
            "category": "手机",
            "imageUrl": null,
            "status": 1,
            "createTime": "2026-02-06T16:45:20",
            "updateTime": "2026-02-06T16:45:20"
        }
    ],
    "success": true,
    "count": 1
}
```

### 获取产品信息
```json
// GET /api/product/{id}
```

### 响应格式
```
{
    "data": {
        "id": 1,
        "name": "Gingham shirt 10",
        "description": "Loose-fitting shirt in a cotton blend. Lapel collar and long sleeves with buttoned cuffs. Front button closure.",
        "price": 5999.00,
        "stock": 95,
        "category": "手套",
        "imageUrl": "https://vgjxh8g2f7b2sc78-89325797681.shopifypreview.com/cdn/shop/files/12_bb32d7f3-ba39-45f7-9ba1-6b679b8a95fe.jpg?v=1764044468&width=1800",
        "status": 1,
        "createTime": "2026-02-06T16:45:20",
        "updateTime": "2026-02-07T17:15:07"
    },
    "success": true
}
```

### 创建订单
```json
// POST /api/order/create
{
    "userId": 1,
    "shippingAddress": "海淀区万寿路甲 15 号七区 10 号楼二单元 402 号",
    "receiverName": "尹业金",
    "receiverPhone": "13333333333",
    "items": [
        {
            "productId": 1,
            "quantity": 1
        }
    ]
}
```
### 响应格式
```json
{
    "data": {
        "orderNo": "ORD17704557073932975",
        "orderId": 12,
        "totalPrice": 5999.00
    },
    "success": true,
    "message": "订单创建成功"
}
```

## 启动项目
```bash
# 编译项目
./mvnw clean compile

# 运行项目
./mvnw spring-boot:run

# 打包项目
./mvnw package -DskipTests
```

## 电商订单系统实现总结

### 1. 数据库表结构
- **产品表 (product)**: 存储商品信息，包括名称、价格、库存、分类等
- **订单表 (order)**: 存储订单信息，包括订单号、用户ID、总价、状态等
- **订单项表 (order_item)**: 存储订单中的具体商品信息
- **日志表 (log)**: 存储系统操作日志，包括操作类型、模块、用户、IP地址、请求参数、执行时间等

### 2. 实体类
- **Product**: 产品实体类
- **Order**: 订单实体类
- **OrderItem**: 订单项实体类
- **Log**: 日志实体类

### 3. 数据访问层 (Mapper)
- **ProductMapper**: 产品数据访问接口及XML映射
- **OrderMapper**: 订单数据访问接口及XML映射
- **OrderItemMapper**: 订单项数据访问接口及XML映射
- **LogMapper**: 日志数据访问接口及XML映射

### 4. 业务逻辑层 (Service)
- **ProductService**: 提供产品的增删改查、上下架等功能
- **OrderService**: 提供订单的创建、取消、支付、发货、完成等完整流程
- **LogService**: 提供日志的记录、查询、清理等功能

### 5. 控制器层 (Controller)
- **ProductController**: 提供产品相关API接口
- **OrderController**: 提供订单相关API接口
- **LogController**: 提供日志管理相关API接口

### 6. 切面层 (Aspect)
- **LogAspect**: 通过AOP切面自动记录系统操作日志

### 7. 订单流程功能
- 创建订单时自动扣减库存
- 支持订单状态流转（待付款→已付款→已发货→已完成）
- 订单取消时自动恢复库存（针对已付款未发货的订单）

### 7. 技术特性
- 使用Spring Boot + MyBatis + MySQL技术栈
- 支持事务处理，保证数据一致性
- 包含完整的错误处理和返回格式
- 支持分页查询和条件搜索

## 注意事项
1. 确保Java 17环境
2. 确保MySQL和Redis服务正常运行
3. 根据实际环境修改数据库连接配置
4. 生产环境建议修改默认密码和配置
