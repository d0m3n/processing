package be.better.at.processing.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"be.better.at.processing.model.jpa"})
@EnableJpaRepositories(basePackages = {"be.better.at.processing.repository"})
@EnableJpaAuditing
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class PersistenceConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

}
