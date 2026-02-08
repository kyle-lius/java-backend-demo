package com.example.backenddemo.util;

import cn.hutool.core.lang.Snowflake;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Component
public class OrderNoGenerator {

    // 雪花算法实例
    private final Snowflake snowflake;

    private RedisTemplate<String, Object> redisTemplate;

    public OrderNoGenerator() {
        // 数据中心ID（0-31），机器ID（0-31）
        // 可以通过配置文件或IP计算得到
        long datacenterId = 1L;
        long machineId = 1L;
        this.snowflake = new Snowflake(datacenterId, machineId);
    }

    /**
     * 生成订单号
     * 格式：业务类型(2) + 日期(8) + 雪花ID(19) + 随机后缀(3)
     * 示例：OR2025011512345678901234567
     */
    public String generateOrderNo(String bizType) {
        // 1. 业务类型（2位）
        String bizCode = getBizCode(bizType);

        // 2. 日期（8位，YYYYMMDD）
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        // 3. 雪花ID（19位）
        long snowflakeId = snowflake.nextId();
        String snowflakeStr = String.format("%019d", snowflakeId);

        // 4. 随机后缀（3位，防止末尾连续相同）
        String randomSuffix = String.format("%03d", ThreadLocalRandom.current().nextInt(1000));

        return bizCode + dateStr + snowflakeStr.substring(0, 16) + randomSuffix;
    }

    /**
     * 生成短格式订单号（适用于展示）
     * 格式：业务类型(2) + 日期(6) + 序列号(6)
     * 示例：OR250115000001
     */
    public String generateShortOrderNo(String bizType) {
        String bizCode = getBizCode(bizType);
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 使用Redis生成每日序列号
        String key = "order:seq:" + bizType + ":" + dateStr;
        Long seq = redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, 48, TimeUnit.HOURS); // 保留48小时

        return bizCode + dateStr + String.format("%06d", seq);
    }

    // 业务类型映射
    private String getBizCode(String bizType) {
        switch (bizType) {
            case "NORMAL": return "OR";  // 普通订单
            case "SECKILL": return "SK"; // 秒杀订单
            case "GROUP": return "GP";   // 拼团订单
            case "FLASH": return "FS";   // 闪购订单
            case "PREORDER": return "PO"; // 预售订单
            default: return "OR";
        }
    }
}