-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db;
USE mall_db;

-- 创建用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建商品表
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    brand VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建购物车项表
CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 插入示例数据
INSERT INTO products (name, description, price, stock, image_url, brand, category) VALUES
('iPhone 15', '最新款苹果手机，拥有超凡的性能和出色的摄影能力。配备A17芯片，6.1英寸Super Retina XDR显示屏，支持5G网络。', 9999.00, 50, NULL, 'Apple', '手机'),
('MacBook Pro', '专业级笔记本电脑，搭载M3芯片，提供无与伦比的性能和续航能力。14英寸Liquid Retina XDR显示屏，适合专业创作。', 19999.00, 20, NULL, 'Apple', '电脑'),
('iPad Air', '轻薄平板电脑，配备M2芯片，性能强劲。11英寸Liquid Retina显示屏，支持Apple Pencil和妙控键盘。', 5999.00, 30, NULL, 'Apple', '平板'),
('Apple Watch', '智能手表，具备健康监测、运动追踪等功能。支持心率监测、血氧检测，防水设计，适合全天佩戴。', 3999.00, 100, NULL, 'Apple', '穿戴设备'),
('华为P50 Pro', '华为P50 Pro手机，徕卡影像系统，超感知镜头，支持5G网络。', 6999.00, 40, NULL, '华为', '手机'),
('华为MateBook X Pro', '华为MateBook X Pro笔记本电脑，轻薄设计，高性能处理器。', 12999.00, 25, NULL, '华为', '电脑'),
('华为MatePad Pro', '华为MatePad Pro平板电脑，高性能，适合办公和娱乐。', 4999.00, 30, NULL, '华为', '平板'),
('华为Watch GT', '华为Watch GT智能手表，超长续航，健康监测。', 2199.00, 50, NULL, '华为', '穿戴设备'),
('小米13', '小米13手机，骁龙8 Gen2处理器，徕卡影像，120Hz OLED屏幕。', 3999.00, 60, NULL, '小米', '手机'),
('小米笔记本Pro', '小米笔记本Pro，高性能轻薄本，适合办公和娱乐。', 5999.00, 35, NULL, '小米', '电脑'),
('小米平板5', '小米平板5，2.5K高清屏幕，骁龙860处理器。', 2499.00, 40, NULL, '小米', '平板'),
('小米手环7', '小米手环7，1.62英寸AMOLED屏幕，多种运动模式。', 299.00, 100, NULL, '小米', '穿戴设备'),
('OPPO Find X6', 'OPPO Find X6手机，哈苏影像系统，超光影三主摄。', 4499.00, 45, NULL, 'OPPO', '手机'),
('OPPO Pad', 'OPPO Pad平板电脑，2.8K高清屏幕，支持手写笔。', 2699.00, 30, NULL, 'OPPO', '平板'),
('OPPO Watch', 'OPPO Watch智能手表，支持eSIM，独立通话。', 1999.00, 40, NULL, 'OPPO', '穿戴设备'),
('vivo X100', 'vivo X100手机，蔡司光学镜头，天玑9300芯片。', 4299.00, 50, NULL, 'vivo', '手机'),
('vivo Pad2', 'vivo Pad2平板电脑，12.1英寸大屏，适合娱乐办公。', 2799.00, 35, NULL, 'vivo', '平板'),
('vivo Watch3', 'vivo Watch3智能手表，支持血氧检测，超长续航。', 1799.00, 45, NULL, 'vivo', '穿戴设备'),
('荣耀Magic6', '荣耀Magic6手机，骁龙8 Gen3处理器，AI智能体验。', 4699.00, 40, NULL, '荣耀', '手机'),
('荣耀MagicBook', '荣耀MagicBook笔记本电脑，轻薄设计，高性能。', 5499.00, 30, NULL, '荣耀', '电脑'),
('荣耀平板8', '荣耀平板8，12英寸大屏，适合家庭娱乐。', 1599.00, 50, NULL, '荣耀', '平板'),
('荣耀手表GS3', '荣耀手表GS3，专业运动手表，支持多种运动模式。', 1299.00, 40, NULL, '荣耀', '穿戴设备');