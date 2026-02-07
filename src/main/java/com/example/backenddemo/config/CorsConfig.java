package com.example.backenddemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS跨域配置类
 * 配置允许跨域访问的规则
 */
@Configuration
public class CorsConfig {

    /**
     * 配置CORS过滤器
     * @return CorsFilter实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建CorsConfiguration对象并配置允许的跨域规则
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的域名（*表示允许所有域名）
        // 在生产环境中应该指定具体的域名，如："http://localhost:3000"
        config.addAllowedOriginPattern("*");
        
        // 允许的请求头
        config.addAllowedHeader("*");
        
        // 允许的请求方法
        config.addAllowedMethod("*");
        
        // 是否允许携带凭证（如cookies）
        config.setAllowCredentials(true);
        
        // 预检请求的有效期（秒）
        config.setMaxAge(3600L);
        
        // 2. 创建UrlBasedCorsConfigurationSource对象
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        
        // 3. 对所有接口都有效
        configSource.registerCorsConfiguration("/**", config);
        
        // 4. 返回CorsFilter对象
        return new CorsFilter(configSource);
    }
}