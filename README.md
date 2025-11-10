# EZShop 电商平台

EZShop 是一个基于 Spring Boot 和 Vue.js 构建的完整电商平台，包含用户认证、商品管理、购物车、订单处理等核心功能。

## 项目结构

```
mall-project/
├── mall-backend/     # Spring Boot 后端
│   ├── src/main/java/com/example/mall/
│   │   ├── controller/    # 控制器层
│   │   ├── model/         # 实体模型
│   │   ├── repository/    # 数据访问层
│   │   ├── service/       # 业务逻辑层
│   │   ├── security/      # 安全配置
│   │   └── payload/       # 数据传输对象
│   └── src/main/resources/
│       └── application.yml # 配置文件
└── mall-frontend/    # Vue.js 前端
    ├── src/
    │   ├── api/           # API 接口封装
    │   ├── components/    # 公共组件
    │   ├── views/         # 页面视图
    │   ├── router/        # 路由配置
    │   └── stores/        # 状态管理
    └── package.json
```

## 技术栈

### 后端技术栈
- Spring Boot 3.x
- Spring Security (JWT 认证)
- Spring Data JPA
- MySQL 数据库
- Maven 构建工具

### 前端技术栈
- Vue 3 (Composition API)
- TypeScript
- Vue Router
- Pinia 状态管理
- Axios HTTP 客户端

## 功能特性

1. **用户管理**
   - 用户注册与登录
   - JWT Token 认证
   - 用户信息管理

2. **商品管理**
   - 商品浏览与搜索
   - 商品详情展示
   - 分类展示

3. **购物车功能**
   - 添加/删除商品
   - 修改商品数量
   - 实时计算总价

4. **订单管理**
   - 创建订单
   - 查看订单历史
   - 订单状态跟踪

## 环境要求

### 后端环境
- Java 17+
- MySQL 8.0+
- Maven 3.9+

### 前端环境
- Node.js 16+
- npm 或 yarn

## 安装与运行

### 后端运行步骤

1. 创建数据库：
```sql
CREATE DATABASE IF NOT EXISTS mall_db;
```

2. 修改数据库配置：
在 `mall-backend/src/main/resources/application.yml` 中配置数据库连接信息

3. 构建并运行：
```bash
cd mall-backend
mvn clean install
mvn spring-boot:run
```

### 前端运行步骤

1. 安装依赖：
```bash
cd mall-frontend
npm install
```

2. 运行开发服务器：
```bash
npm run dev
```

## API 文档

后端 API 通过 RESTful 风格设计，主要接口包括：

- 用户认证：`/api/auth/**`
- 商品管理：`/api/products/**`
- 购物车管理：`/api/cart/**`
- 订单管理：`/api/orders/**`

## 部署

### 后端部署
```bash
cd mall-backend
mvn clean package
java -jar target/mall-backend-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
cd mall-frontend
npm run build
```

## 项目特点

- 前后端分离架构
- JWT Token 无状态认证
- 响应式前端设计
- 完整的电商功能流程
- 良好的代码结构和注释

## 开发者信息

本项目为学习用途，展示了完整的电商系统开发流程，包括前后端技术栈的整合应用。