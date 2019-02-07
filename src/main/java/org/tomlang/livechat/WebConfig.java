package org.tomlang.livechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tomlang.livechat.interceptors.AppOwnerInterceptor;
import org.tomlang.livechat.interceptors.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    @Autowired
    AppOwnerInterceptor appOwnerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/api/secured/**");
        registry.addInterceptor(appOwnerInterceptor)
            .addPathPatterns("/api/secured/app/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedMethods("PUT", "POST", "GET", "DELETE", "PATCH", "HEAD", "OPTIONS");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

}
