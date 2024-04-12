package com.example.performancekeeper.api.common.queryCounter;



import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfig {
    private final ApiQueryInspector apiQueryInspector;

    public HibernateConfig(ApiQueryInspector apiQueryInspector) {
        this.apiQueryInspector = apiQueryInspector;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties ->
                hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR, apiQueryInspector);
    }
}
