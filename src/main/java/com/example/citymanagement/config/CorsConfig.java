package com.example.citymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 允许特定的跨域来源，而不是使用通配符
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        // 允许跨域的HTTP方法
        config.addAllowedMethod("*");

        // 允许跨域的请求头
        config.addAllowedHeader("*");

        // 是否允许携带凭证（如cookie）
        config.setAllowCredentials(true);

        // 预检请求的有效期，单位为秒
        config.setMaxAge(3600L);

        // 对所有接口应用这个配置
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}