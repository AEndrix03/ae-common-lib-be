package it.aredegalli.commons.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class AuthJpaConfig {

    private final HikariDataSource authDataSource;

    public AuthJpaConfig(HikariDataSource authDataSource) {
        this.authDataSource = authDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(authDataSource)
                .packages("it.aredegalli.commons.model")
                .persistenceUnit("authPersistenceUnit")
                .build();
    }

    @Bean
    public JpaTransactionManager authTransactionManager(EntityManagerFactory authEntityManagerFactory) {
        return new JpaTransactionManager(authEntityManagerFactory);
    }
}
