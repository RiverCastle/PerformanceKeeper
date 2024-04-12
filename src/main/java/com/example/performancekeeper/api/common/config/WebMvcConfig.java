package com.example.performancekeeper.api.common.config;

import com.example.performancekeeper.api.common.queryCounter.ApiQueryCounter;
import com.example.performancekeeper.api.common.queryCounter.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ApiQueryCounter apiQueryCounter;

    public WebMvcConfig(ApiQueryCounter apiQueryCounter) {
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor(apiQueryCounter))
                .excludePathPatterns("/css/**", "/images/**", "/js/**");
    }
}
