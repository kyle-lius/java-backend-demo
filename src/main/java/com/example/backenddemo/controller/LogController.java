package com.example.backenddemo.controller;

import com.example.backenddemo.entity.Log;
import com.example.backenddemo.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志控制器
 * 提供日志相关的REST API接口
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 获取所有日志
     * @return 包含日志列表的响应结果
     */
    @GetMapping
    public Map<String, Object> getAllLogs() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Log> logs = logService.getAllLogs();
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", logs);
            result.put("count", logs.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询日志
     * @param page 页码
     * @param size 每页大小
     * @return 包含日志列表的响应结果
     */
    @GetMapping("/page")
    public Map<String, Object> getLogsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Log> logs = logService.getLogsByPage(page, size);
            int totalCount = logService.getAllLogs().size(); // 这里为了演示简单，实际应该在service中提供专门的方法
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", logs);
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("currentPage", page);
            pagination.put("pageSize", size);
            pagination.put("total", totalCount);
            pagination.put("totalPages", (int) Math.ceil((double) totalCount / size));
            result.put("pagination", pagination);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据模块查询日志
     * @param module 模块名称
     * @return 包含日志列表的响应结果
     */
    @GetMapping("/module/{module}")
    public Map<String, Object> getLogsByModule(@PathVariable String module) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Log> logs = logService.getLogsByModule(module);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", logs);
            result.put("count", logs.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据操作类型查询日志
     * @param operationType 操作类型
     * @return 包含日志列表的响应结果
     */
    @GetMapping("/type/{operationType}")
    public Map<String, Object> getLogsByOperationType(@PathVariable String operationType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Log> logs = logService.getLogsByOperationType(operationType);
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", logs);
            result.put("count", logs.size());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 清空所有日志
     * @return 包含操作结果的响应结果
     */
    @DeleteMapping("/clear")
    public Map<String, Object> clearAllLogs() {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = logService.clearAllLogs();
            if (success) {
                result.put("code", 200);
                result.put("message", "日志清空成功");
            } else {
                result.put("code", 500);
                result.put("message", "日志清空失败");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "清空失败: " + e.getMessage());
        }
        return result;
    }
}