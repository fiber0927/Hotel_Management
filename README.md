# 酒店管理系统 Hotel Management System

> 前后端分离的酒店内部管理平台。**管理员端**负责房态监控、预订处理、入住退房、客房服务与账单；
> **顾客端**负责浏览房间、提交预订、查看自己的预订记录。
>
> 后端 Spring Boot 3 + 原生 MyBatis，前端 Vue 3 + Element Plus。

## 技术栈

| 层级 | 技术 |
|---|---|
| 后端 | JDK 21 · Spring Boot 3.2.0 · **原生 MyBatis**（8 个 Mapper 接口 + 8 个 XML）· Lombok |
| 数据库 | MySQL 8（InnoDB / utf8mb4）· HikariCP 连接池 |
| 前端 | Vue 3（Options API）· Vue Router 4 · Axios · Element Plus · Vite 5 |
| 构建 | Maven（后端）· npm（前端） |

## 功能模块

### 管理员端 `/main/*`

| 模块 | 说明 |
|---|---|
| 登录 | 管理员账号登录，登录态存 sessionStorage |
| 数据仪表盘 | 房间总数 / 可订 / 已订 / 已入住 / 按楼层统计 |
| 房间管理 | 房间增删改查、房态手工调整、按楼层/房型筛选 |
| 预订管理 | 预订确认 / 拒绝 / 取消 / 完成，按状态查询 |
| 入住退房 | 办理入住、办理退房、确认退房（释放房间） |
| 客房服务 | 清洁 / 维修任务的创建、指派、完成 |
| 账单管理 | 账单生成、按入住记录查询、结算 |
| 客人管理 | 客人信息维护 |

### 顾客端 `/customer/*`

| 模块 | 说明 |
|---|---|
| 注册 / 登录 | 顾客账号注册（用户名 + 密码 + 姓名 + 手机号）与登录 |
| 浏览房间 | 按楼层 / 房型查看可预订房间 |
| 在线预约 | 选择房间与入住日期，提交预订申请（状态 `pending`） |
| 我的预订 | 查看自己的预订记录 |

## 核心业务设计

### 1. 客房状态流转

房间状态存在 `room.status`，实际使用三个值：

| 值 | 含义 |
|---|---|
| `available` | 空闲，可预订 |
| `booked` | 已被预订，尚未入住 |
| `occupied` | 已入住（含已退房待清洁） |

完整链路：

```
顾客提交预订          booking.status = pending          room 不变
        ↓
管理员确认预订        booking.status = confirmed        room → booked
                      同时自动创建一条清洁任务（cleaning，套房优先级 high）
        ↓
顾客办理入住          check_in_record = checked_in      room → occupied
        ↓
顾客退房              check_out_time 写入              room 仍为 occupied
        ↓
管理员确认退房                                         room → available
```

**一个刻意的业务决策：退房后房间不立即释放。**
房间刚退完还没清洁，不能直接再卖出去，所以 `checkOut` 只写退房时间、房态保持 `occupied`，
必须等管理员 `confirmCheckout` 才回到 `available`。这层约束写在 `CheckInService.checkOut` 的注释里。

### 2. 跨表事务

确认预订要同时写**三张表**：预订状态、房间状态、清洁任务；
入住 / 退房要写**两张表**。这些操作用 Spring 事务包住，保证要么全成功要么全回滚：

| Service 方法 | 涉及表 |
|---|---|
| `BookingService.confirmBooking` | booking + room + housekeeping_task |
| `CheckInService.checkIn` / `checkOut` | check_in_record + room |
| `CheckInService.confirmCheckout` | room |

### 3. 数据层约定

- **原生 MyBatis + XML**，8 个 Mapper 全部使用 `#{}` 预编译占位符，**没有一处 `${}` 字符串拼接**
- 全项目统一**构造器注入**，依赖字段声明为 `final`
- 唯一索引（`admin.username` / `customer.username` / `room.room_number`）作为并发查重的最后一道防线
  —— 应用层的"先查再插"在并发下会失效，数据库约束才是真防线

## 数据库设计

8 张表，`utf8mb4` + InnoDB：

| 表 | 说明 | 关键索引 |
|---|---|---|
| `admin` | 管理员 | `username` UNIQUE |
| `customer` | 顾客 | `username` UNIQUE |
| `guest` | 入住客人 | — |
| `room` | 房间 | `room_number` UNIQUE |
| `booking` | 预订 | `guest_id` |
| `check_in_record` | 入住/退房记录 | `booking_id`、`room_id` |
| `housekeeping_task` | 清洁 / 维修任务 | `room_id` |
| `bill` | 账单 | `check_in_id` |

初始化脚本：`database/schema.sql`（含 77 间房的数据）。

## 项目结构

```
Hotel_Management/
├── backend/                       # 后端 Spring Boot 项目
│   └── src/main/
│       ├── java/com/hotel/
│       │   ├── config/            # WebConfig（CORS）
│       │   ├── controller/        # 8 个 REST 控制器
│       │   ├── entity/            # 8 个实体类（Lombok @Data）
│       │   ├── mapper/            # 8 个 Mapper 接口
│       │   └── service/           # 8 个 Service
│       └── resources/mapper/      # 8 个 MyBatis XML
├── frontend/                      # 前端 Vue 项目
│   └── src/
│       ├── api/index.js           # Axios 封装 + 401 拦截
│       ├── router/index.js        # 路由 + 角色守卫
│       └── views/                 # 页面（admin / customer）
├── database/schema.sql            # 建库建表脚本
└── docs/                          # 项目文档（开发记录、问题记录）
```

## 快速启动

### 环境要求

JDK 21+ · Maven 3.9+ · MySQL 8.0+ · Node.js 18+

### 1. 建库

```bash
mysql -u root -p < database/schema.sql
```

脚本会自动创建 `hotel_management` 库、8 张表，并写入 77 间房和默认管理员。

### 2. 配置数据库连接

仓库里**不包含数据库密码**。克隆后需要自己新建
`backend/src/main/resources/application-local.yml`：

```yaml
spring:
  datasource:
    password: 你的数据库密码
```

这个文件已在 `.gitignore` 里，不会被提交。
`application.yml` 中通过 `spring.profiles.active: local` 自动加载它，
所以**不需要改任何启动命令**。

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

服务地址 `http://localhost:8080`，接口前缀 `/api`。

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5175`（Vite 已把 `/api` 代理到 8080）。

### 演示账号

| 角色 | 用户名 | 密码 | 来源 |
|---|---|---|---|
| 管理员 | `admin` | `admin123` | `database/schema.sql` 初始化写入 |
| 顾客 | — | — | 需在顾客端 `/customer/register` 自行注册 |

## API 一览

共 52 个接口。完整路径以 `/api` 为前缀。

| 模块 | 方法与路径 | 说明 |
|---|---|---|
| 管理员 | `POST /admin/login` | 管理员登录 |
| 顾客 | `POST /customer/login`、`POST /customer/register` | 顾客登录 / 注册 |
| 房间 | `GET /rooms`、`GET /rooms/{id}` | 房间列表 / 详情 |
| | `GET /rooms/available`、`/rooms/floor/{floor}`、`/rooms/type/{roomType}`、`/rooms/floor/{floor}/type/{roomType}` | 按状态 / 楼层 / 房型筛选 |
| | `GET /rooms/stats` | 房态统计（含楼层维度） |
| | `POST /rooms`、`PUT /rooms/{id}`、`PATCH /rooms/{id}/status`、`DELETE /rooms/{id}` | 房间增删改 / 改房态 |
| 预订 | `GET /bookings`、`GET /bookings/{id}`、`GET /bookings/guest/{customerId}`、`GET /bookings/status/{status}` | 预订查询 |
| | `POST /bookings`、`PUT /bookings/{id}` | 新建 / 修改预订 |
| | `PATCH /bookings/{id}/confirm`、`/reject`、`/cancel`、`/complete` | 预订状态流转 |
| | `DELETE /bookings/{id}` | 删除预订 |
| 入住 | `GET /checkin`、`GET /checkin/{id}`、`GET /checkin/booking/{bookingId}`、`GET /checkin/status/{status}` | 入住记录查询 |
| | `POST /checkin` | 办理入住 |
| | `POST /checkin/{id}/checkout`、`POST /checkin/{id}/confirm-checkout` | 退房 / 确认退房 |
| 客房服务 | `GET /housekeeping`、`GET /housekeeping/{id}`、`GET /housekeeping/room/{roomId}`、`GET /housekeeping/status/{status}` | 任务查询 |
| | `POST /housekeeping`、`PUT /housekeeping/{id}`、`PATCH /housekeeping/{id}/complete`、`DELETE /housekeeping/{id}` | 任务维护 |
| 账单 | `GET /bills`、`GET /bills/{id}`、`GET /bills/checkin/{checkInId}`、`GET /bills/paid/{isPaid}` | 账单查询 |
| | `POST /bills`、`PUT /bills/{id}`、`PATCH /bills/{id}/pay` | 账单维护 / 结算 |
| 客人 | `GET /guests`、`GET /guests/{id}`、`POST /guests`、`PUT /guests/{id}`、`DELETE /guests/{id}` | 客人信息维护 |

## 已知限制

这个项目是**练手规模**（8 张表、77 间房），下面这些是明确知道还没做、也写在这里避免误解的部分：

- **后端没有鉴权**。管理员与顾客的区分目前只在前端（路由守卫 + 菜单隔离），
  所有 `/api/**` 接口都可匿名调用。正确做法是加 JWT + 拦截器，并从 token 里取角色而不是信任前端。
- **密码明文存储**，`admin` 表里存的就是明文 `admin123`，登录用 `equals` 直接比对。
  应改为 BCrypt 等加盐哈希。
- **取消 / 拒绝预订不会释放房间**。`cancelBooking`、`rejectBooking`、`deleteBooking` 只改预订状态，
  没有把房间从 `booked` 改回 `available`，房间会卡住无法再订。
  根因是房态变更没有集中入口，每个 Service 各改各的字段。
- **状态值是散落的字符串字面量**（`available` / `booked` / `occupied` 分布在 4 个 Service 里），
  拼错没有编译期检查，应该抽成枚举。
- **列表接口全量返回**，没有分页，数据量上来会卡。
- **没有全局异常处理**，Controller 里逐个 try-catch，`e.printStackTrace()` 直接打到控制台。
- **没有单元测试**，也没有接口文档（Swagger / OpenAPI）。

后续计划：按上面的顺序依次补鉴权 → 统一返回体与全局异常 → 房态集中管理 → 分页 → 单测。

## License

MIT
