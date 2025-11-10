-- 创建数据库
CREATE DATABASE IF NOT EXISTS mall_db;
USE mall_db;

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

-- 删除外键约束相关的表数据
DELETE FROM order_items;
DELETE FROM cart_items;
DELETE FROM orders;

-- 删除产品数据
DELETE FROM products;

-- 插入产品数据
INSERT INTO products (name, description, price, stock, image_url, brand, category) VALUES
('iPhone 15', 'Apple iPhone 15 with A17 chip and 5G support.', 9999.00, 50, NULL, 'Apple', '手机'),
('MacBook Pro', 'Professional laptop with M3 chip for creative work.', 19999.00, 20, NULL, 'Apple', '电脑'),
('iPad Air', 'Lightweight tablet with M2 chip and Apple Pencil support.', 5999.00, 30, NULL, 'Apple', '平板'),
('Apple Watch', 'Smartwatch with health monitoring and fitness tracking.', 3999.00, 100, NULL, 'Apple', '穿戴设备'),
('华为P50 Pro', 'Huawei phone with Leica camera system and 5G support.', 6999.00, 40, NULL, '华为', '手机'),
('华为MateBook X Pro', 'Huawei laptop with slim design and high-performance processor.', 12999.00, 25, NULL, '华为', '电脑'),
('华为MatePad Pro', 'Huawei tablet with high performance for work and entertainment.', 4999.00, 30, NULL, '华为', '平板'),
('华为Watch GT', 'Huawei smartwatch with long battery life and health monitoring.', 2199.00, 50, NULL, '华为', '穿戴设备'),
('小米13', 'Xiaomi phone with Snapdragon processor and 120Hz OLED screen.', 3999.00, 60, NULL, '小米', '手机'),
('小米笔记本Pro', 'Xiaomi laptop with high performance and slim design.', 5999.00, 35, NULL, '小米', '电脑'),
('小米平板5', 'Xiaomi tablet with high-definition screen and strong performance.', 2499.00, 40, NULL, '小米', '平板'),
('小米手环7', 'Xiaomi band with multiple sports modes and long battery life.', 299.00, 100, NULL, '小米', '穿戴设备'),
('OPPO Find X6', 'OPPO phone with Hasselblad camera system.', 4499.00, 45, NULL, 'OPPO', '手机'),
('OPPO Pad', 'OPPO tablet with high-definition screen and stylus support.', 2699.00, 30, NULL, 'OPPO', '平板'),
('OPPO Watch', 'OPPO smartwatch with eSIM support and standalone calling.', 1999.00, 40, NULL, 'OPPO', '穿戴设备'),
('vivo X100', 'vivo phone with Zeiss optics and Dimensity chip.', 4299.00, 50, NULL, 'vivo', '手机'),
('vivo Pad2', 'vivo tablet with large screen design for entertainment and work.', 2799.00, 35, NULL, 'vivo', '平板'),
('vivo Watch3', 'vivo smartwatch with blood oxygen detection and long battery life.', 1799.00, 45, NULL, 'vivo', '穿戴设备'),
('荣耀Magic6', 'Honor phone with Snapdragon processor and AI experience.', 4699.00, 40, NULL, '荣耀', '手机'),
('荣耀MagicBook', 'Honor laptop with slim design and high performance.', 5499.00, 30, NULL, '荣耀', '电脑'),
('荣耀平板8', 'Honor tablet with large screen design for family entertainment.', 1599.00, 50, NULL, '荣耀', '平板'),
('荣耀手表GS3', 'Honor watch with professional sports functions and multiple modes.', 1299.00, 40, NULL, '荣耀', '穿戴设备');