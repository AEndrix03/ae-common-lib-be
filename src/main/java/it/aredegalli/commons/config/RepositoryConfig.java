package it.aredegalli.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "it.aredegalli.commons.repository")
public class RepositoryConfig {
}
