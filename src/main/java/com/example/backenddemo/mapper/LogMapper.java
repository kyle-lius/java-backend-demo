package com.example.backenddemo.mapper;

import com.example.backenddemo.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 日志数据访问层接口
 * 定义了对日志表的各种数据库操作方法
 */
@Mapper
public interface LogMapper {
    
    /**
     * 根据ID查询日志
     * @param id 日志ID
     * @return 日志对象，如果不存在则返回null
     */
    Log selectById(Long id);
    
    /**
     * 查询所有日志
     * @return 日志列表
     */
    List<Log> selectAll();
    
    /**
     * 分页查询日志
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 日志列表
     */
    List<Log> selectByPage(int offset, int limit);
    
    /**
     * 根据模块查询日志
     * @param module 模块名称
     * @return 日志列表
     */
    List<Log> selectByModule(String module);
    
    /**
     * 根据操作类型查询日志
     * @param operationType 操作类型
     * @return 日志列表
     */
    List<Log> selectByOperationType(String operationType);
    
    /**
     * 插入日志
     * @param log 日志对象
     * @return 影响的行数
     */
    int insert(Log log);
    
    /**
     * 批量插入日志
     * @param logs 日志列表
     * @return 影响的行数
     */
    int batchInsert(List<Log> logs);
    
    /**
     * 清空日志表
     * @return 影响的行数
     */
    int deleteAll();
    
    /**
     * 根据时间范围删除日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 影响的行数
     */
    int deleteByTimeRange(String startTime, String endTime);
}