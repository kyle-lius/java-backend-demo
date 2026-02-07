package com.example.backenddemo.service;

import com.example.backenddemo.entity.Log;
import com.example.backenddemo.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志服务层
 * 提供日志相关的业务逻辑处理
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 保存日志信息
     * @param log 日志对象
     * @return 是否保存成功
     */
    public boolean saveLog(Log log) {
        log.setCreateTime(LocalDateTime.now());
        return logMapper.insert(log) > 0;
    }

    /**
     * 批量保存日志信息
     * @param logs 日志列表
     * @return 是否批量保存成功
     */
    public boolean batchSaveLogs(List<Log> logs) {
        return logMapper.batchInsert(logs) > 0;
    }

    /**
     * 根据ID查询日志
     * @param id 日志ID
     * @return 日志对象
     */
    public Log getLogById(Long id) {
        return logMapper.selectById(id);
    }

    /**
     * 获取所有日志
     * @return 日志列表
     */
    public List<Log> getAllLogs() {
        return logMapper.selectAll();
    }

    /**
     * 分页查询日志
     * @param page 页码
     * @param size 页面大小
     * @return 日志列表
     */
    public List<Log> getLogsByPage(int page, int size) {
        int offset = (page - 1) * size;
        return logMapper.selectByPage(offset, size);
    }

    /**
     * 根据模块查询日志
     * @param module 模块名称
     * @return 日志列表
     */
    public List<Log> getLogsByModule(String module) {
        return logMapper.selectByModule(module);
    }

    /**
     * 根据操作类型查询日志
     * @param operationType 操作类型
     * @return 日志列表
     */
    public List<Log> getLogsByOperationType(String operationType) {
        return logMapper.selectByOperationType(operationType);
    }

    /**
     * 清空所有日志
     * @return 是否清空成功
     */
    public boolean clearAllLogs() {
        return logMapper.deleteAll() > 0;
    }

    /**
     * 根据时间范围删除日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否删除成功
     */
    public boolean deleteLogsByTimeRange(String startTime, String endTime) {
        return logMapper.deleteByTimeRange(startTime, endTime) > 0;
    }
}