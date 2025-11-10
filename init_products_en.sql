-- Create database
CREATE DATABASE IF NOT EXISTS mall_db;
USE mall_db;

-- Set character set
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

-- Delete data from tables with foreign key constraints first
DELETE FROM order_items;
DELETE FROM cart_items;
DELETE FROM orders;

-- Delete product data
DELETE FROM products;

-- Insert product data
INSERT INTO products (name, description, price, stock, image_url, brand, category) VALUES
('iPhone 15', 'Latest Apple smartphone with A17 chip and 5G support.', 9999.00, 50, NULL, 'Apple', 'Phone'),
('MacBook Pro', 'Professional laptop with M3 chip for creative work.', 19999.00, 20, NULL, 'Apple', 'Computer'),
('iPad Air', 'Lightweight tablet with M2 chip and Apple Pencil support.', 5999.00, 30, NULL, 'Apple', 'Tablet'),
('Apple Watch', 'Smartwatch with health monitoring and fitness tracking.', 3999.00, 100, NULL, 'Apple', 'Wearable'),
('Huawei P50 Pro', 'Huawei phone with Leica camera system and 5G support.', 6999.00, 40, NULL, 'Huawei', 'Phone'),
('Huawei MateBook X Pro', 'Huawei laptop with slim design and high-performance processor.', 12999.00, 25, NULL, 'Huawei', 'Computer'),
('Huawei MatePad Pro', 'Huawei tablet with high performance for work and entertainment.', 4999.00, 30, NULL, 'Huawei', 'Tablet'),
('Huawei Watch GT', 'Huawei smartwatch with long battery life and health monitoring.', 2199.00, 50, NULL, 'Huawei', 'Wearable'),
('Xiaomi 13', 'Xiaomi phone with Snapdragon processor and 120Hz OLED screen.', 3999.00, 60, NULL, 'Xiaomi', 'Phone'),
('Xiaomi Notebook Pro', 'Xiaomi laptop with high performance and slim design.', 5999.00, 35, NULL, 'Xiaomi', 'Computer'),
('Xiaomi Pad 5', 'Xiaomi tablet with high-definition screen and strong performance.', 2499.00, 40, NULL, 'Xiaomi', 'Tablet'),
('Xiaomi Band 7', 'Xiaomi band with multiple sports modes and long battery life.', 299.00, 100, NULL, 'Xiaomi', 'Wearable'),
('OPPO Find X6', 'OPPO phone with Hasselblad camera system.', 4499.00, 45, NULL, 'OPPO', 'Phone'),
('OPPO Pad', 'OPPO tablet with high-definition screen and stylus support.', 2699.00, 30, NULL, 'OPPO', 'Tablet'),
('OPPO Watch', 'OPPO smartwatch with eSIM support and standalone calling.', 1999.00, 40, NULL, 'OPPO', 'Wearable'),
('vivo X100', 'vivo phone with Zeiss optics and Dimensity chip.', 4299.00, 50, NULL, 'vivo', 'Phone'),
('vivo Pad2', 'vivo tablet with large screen design for entertainment and work.', 2799.00, 35, NULL, 'vivo', 'Tablet'),
('vivo Watch3', 'vivo smartwatch with blood oxygen detection and long battery life.', 1799.00, 45, NULL, 'vivo', 'Wearable'),
('Honor Magic6', 'Honor phone with Snapdragon processor and AI experience.', 4699.00, 40, NULL, 'Honor', 'Phone'),
('Honor MagicBook', 'Honor laptop with slim design and high performance.', 5499.00, 30, NULL, 'Honor', 'Computer'),
('Honor Pad 8', 'Honor tablet with large screen design for family entertainment.', 1599.00, 50, NULL, 'Honor', 'Tablet'),
('Honor Watch GS3', 'Honor watch with professional sports functions and multiple modes.', 1299.00, 40, NULL, 'Honor', 'Wearable');