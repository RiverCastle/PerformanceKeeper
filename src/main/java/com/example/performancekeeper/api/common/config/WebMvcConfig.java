package com.example.performancekeeper.api.common.config;

import com.example.performancekeeper.api.common.queryCounter.ApiQueryCounter;
import com.example.performancekeeper.api.common.queryCounter.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods(HttpMethod.GET.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.OPTIONS.name());
    }
}
