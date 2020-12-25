package com.picturecontrolapi.picturecontrolapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 跨域
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")           // 允许跨域访问的路径
                .allowedOrigins("*")                    // 允许跨域访问的源 *：代表所有域名都可以跨域访问
                .allowedMethods("*")                    // 允许请求的方法  *：表示"GET","POST","PUT","DELETE"
                .maxAge(168000)                         // 预检间隔时间
                .allowedHeaders("*")                    // 允许头部携带任何信息
                .allowCredentials(false);               // 是否发送cookie true：允许携带cookie false：不允许
    }
}
