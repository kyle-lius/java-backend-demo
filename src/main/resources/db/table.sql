-- 创建用户数据库
CREATE DATABASE IF NOT EXISTS userdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE userdb;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建产品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '产品ID',
    name VARCHAR(100) NOT NULL COMMENT '产品名称',
    description TEXT COMMENT '产品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    category VARCHAR(50) COMMENT '产品分类',
    image_url VARCHAR(255) COMMENT '产品图片URL',
    status INT DEFAULT 1 COMMENT '状态：1-上架，0-下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_price DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    order_status INT DEFAULT 1 COMMENT '订单状态：1-待付款，2-已付款，3-已发货，4-已完成，5-已取消',
    shipping_address VARCHAR(255) COMMENT '收货地址',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_order_status (order_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '产品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- 创建日志表
CREATE TABLE IF NOT EXISTS log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    operation_type VARCHAR(50) COMMENT '操作类型',
    module VARCHAR(50) COMMENT '操作模块',
    description TEXT COMMENT '操作描述',
    user_id BIGINT COMMENT '操作用户ID',
    user_name VARCHAR(50) COMMENT '操作用户名称',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    method VARCHAR(10) COMMENT '请求方法',
    url VARCHAR(500) COMMENT '请求URL',
    params TEXT COMMENT '请求参数',
    result TEXT COMMENT '返回结果',
    execution_time BIGINT COMMENT '执行时间（毫秒）',
    exception TEXT COMMENT '异常信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_module (module),
    INDEX idx_operation_type (operation_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- 插入测试数据
INSERT INTO user (username, password, email, phone, status) VALUES
('admin', '123456', 'admin@example.com', '13800138000', 1),
('testuser', '123456', 'test@example.com', '13800138001', 1);

-- 插入测试产品数据
INSERT INTO `backenddemo`.`product` (`name`, `description`, `price`, `stock`, `category`, `image_url`, `status`, `create_time`, `update_time`) VALUES ('Gingham shirt 10', 'Loose-fitting shirt in a cotton blend. Lapel collar and long sleeves with buttoned cuffs. Front button closure.', 5999.00, 96, '手套', 'https://vgjxh8g2f7b2sc78-89325797681.shopifypreview.com/cdn/shop/files/12_bb32d7f3-ba39-45f7-9ba1-6b679b8a95fe.jpg?v=1764044468&width=1800', 1, '2026-02-06 16:45:20', '2026-02-07 15:40:05');
INSERT INTO `backenddemo`.`product` (`name`, `description`, `price`, `stock`, `category`, `image_url`, `status`, `create_time`, `update_time`) VALUES ('Irregular Ceramic Vase 2', 'Rough textured irregular shaped ceramic vase.\n\nCan be filled with water.', 12999.00, 44, '衣服', 'https://vgjxh8g2f7b2sc78-89325797681.shopifypreview.com/cdn/shop/files/16_46410f03-c8ae-4c03-a313-d3d973d7ac96.jpg?v=1764044495&width=1800', 1, '2026-02-06 16:45:20', '2026-02-07 15:39:38');
INSERT INTO `backenddemo`.`product` (`name`, `description`, `price`, `stock`, `category`, `image_url`, `status`, `create_time`, `update_time`) VALUES ('Kids Denim Shorts ¨C Durable Comfort ¨C Play-Ready 9', 'Tired of kids clothes that cant keep up? These denim shorts offer a durable solution, blending classic style with the freedom to move and play without restriction.\n\nFeatures:\n\nClassic fit\nAdjustable waist\nFive-pocket styling\nBreathable denim\nStain-resistant fabric\n\nSpecifications:\n\nMaterial: Cotton denim or cotton blend [4, 5]\nClosure: Button and zip fly or snap closure [1]\nFit: Classic fit, above-knee length [1]\nWaist: Adjustable elastic waistbands [4, 6]\n\nMaterials: Cotton, Denim, Recycled Cotton, Recycled Polyester\n\nColors: Blue, Black, White, Pink, Yellow, Brown, Green, Grey, Tan, Aloe\n\nsda 1111', 1999.00, 197, '裤子', 'https://vgjxh8g2f7b2sc78-89325797681.shopifypreview.com/cdn/shop/files/Kids_27_20Denim_20Shorts_7e1bb1ca-9898-4bf6-b175-7f717797ec72.jpg?v=1764044533&width=1800', 1, '2026-02-06 16:45:20', '2026-02-07 15:39:35');
INSERT INTO `backenddemo`.`product` (`name`, `description`, `price`, `stock`, `category`, `image_url`, `status`, `create_time`, `update_time`) VALUES ('Loose fit washed trousers 10', 'Loose fit trousers in cotton fabric. Front pockets with patch pockets designed at the back. Trouser legs with zipped side pockets. Washed effect. Front closure with zip and button.', 4399.00, 78, '裤子', 'https://vgjxh8g2f7b2sc78-89325797681.shopifypreview.com/cdn/shop/files/8_99ab7de6-dd93-4b2d-af2a-5d806865b70b.jpg?v=1764044424&width=1800', 1, '2026-02-06 16:45:20', '2026-02-07 15:40:32');
