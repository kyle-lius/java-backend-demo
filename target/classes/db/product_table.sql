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

-- 插入测试产品数据
INSERT INTO product (name, description, price, stock, category, status) VALUES
('iPhone 15', '最新款苹果手机', 5999.00, 100, '手机', 1),
('MacBook Pro', '苹果笔记本电脑', 12999.00, 50, '电脑', 1),
('AirPods Pro', '苹果无线耳机', 1999.00, 200, '耳机', 1),
('iPad Air', '苹果平板电脑', 4399.00, 80, '平板', 1);