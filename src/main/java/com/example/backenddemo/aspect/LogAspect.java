package com.example.backenddemo.aspect;

import com.example.backenddemo.entity.Log;
import com.example.backenddemo.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面类
 * 用于拦截Controller层方法，记录操作日志
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 环绕通知，拦截Controller层的所有公共方法
     * 记录请求信息、执行时间、返回结果等
     */
    @Around("execution(public * com.example.backenddemo.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String ipAddress = getClientIpAddress(request);
        
        // 构建日志对象
        Log log = new Log();
        log.setMethod(method);
        log.setUrl(url);
        log.setIpAddress(ipAddress);
        log.setParams(getParams(joinPoint));
        
        // 设置模块名称
        String className = joinPoint.getTarget().getClass().getSimpleName();
        if (className.contains("UserController")) {
            log.setModule("用户管理");
        } else if (className.contains("ProductController")) {
            log.setModule("产品管理");
        } else if (className.contains("OrderController")) {
            log.setModule("订单管理");
        } else {
            log.setModule("系统管理");
        }
        
        // 设置操作类型
        if ("GET".equalsIgnoreCase(method)) {
            log.setOperationType("查询");
        } else if ("POST".equalsIgnoreCase(method)) {
            log.setOperationType("创建");
        } else if ("PUT".equalsIgnoreCase(method)) {
            log.setOperationType("更新");
        } else if ("DELETE".equalsIgnoreCase(method)) {
            log.setOperationType("删除");
        } else {
            log.setOperationType("其他");
        }
        
        // 设置操作描述
        log.setDescription(className + "." + joinPoint.getSignature().getName());
        
        Object result = null;
        String exception = null;
        
        try {
            // 执行原方法
            result = joinPoint.proceed();
            
            // 记录返回结果
            log.setResult(objectMapper.writeValueAsString(result));
            
            return result;
        } catch (Exception e) {
            // 记录异常信息
            exception = e.getClass().getSimpleName() + ": " + e.getMessage();
            log.setException(exception);
            throw e; // 重新抛出异常
        } finally {
            // 计算执行时间
            long executionTime = System.currentTimeMillis() - startTime;
            log.setExecutionTime(executionTime);
            
            // 保存日志
            try {
                logService.saveLog(log);
            } catch (Exception e) {
                logger.error("保存日志失败", e);
            }
            
            logger.info("模块: {}, 操作类型: {}, 方法: {}, URL: {}, 执行时间: {}ms", 
                       log.getModule(), log.getOperationType(), log.getDescription(), 
                       log.getUrl(), executionTime);
        }
    }
    
    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedHeader == null || xForwardedHeader.isEmpty() || "unknown".equalsIgnoreCase(xForwardedHeader)) {
            xForwardedHeader = request.getHeader("Proxy-Client-IP");
        }
        if (xForwardedHeader == null || xForwardedHeader.isEmpty() || "unknown".equalsIgnoreCase(xForwardedHeader)) {
            xForwardedHeader = request.getHeader("WL-Proxy-Client-IP");
        }
        if (xForwardedHeader == null || xForwardedHeader.isEmpty() || "unknown".equalsIgnoreCase(xForwardedHeader)) {
            xForwardedHeader = request.getRemoteAddr();
        }
        
        // 处理多个IP的情况，取第一个非unknown的有效IP
        if (xForwardedHeader != null && xForwardedHeader.contains(",")) {
            xForwardedHeader = xForwardedHeader.split(",")[0];
        }
        
        return xForwardedHeader;
    }
    
    /**
     * 获取请求参数
     * @param joinPoint 切点
     * @return 参数字符串
     */
    private String getParams(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                // 过滤掉HttpServletRequest、HttpServletResponse等对象
                Object[] filteredArgs = Arrays.stream(args)
                    .filter(arg -> !(arg instanceof HttpServletRequest))
                    .toArray();
                
                if (filteredArgs.length > 0) {
                    return objectMapper.writeValueAsString(filteredArgs);
                }
            }
        } catch (Exception e) {
            logger.error("获取请求参数失败", e);
        }
        return "{}";
    }
}