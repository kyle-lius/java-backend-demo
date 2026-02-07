package com.example.backenddemo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private Integer status; // 1-上架，0-下架
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}