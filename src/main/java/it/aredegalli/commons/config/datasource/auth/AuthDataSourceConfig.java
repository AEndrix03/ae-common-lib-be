package it.aredegalli.commons.config.datasource.auth;

import com.zaxxer.hikari.HikariDataSource;
import it.aredegalli.commons.config.properties.CommonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AuthDataSourceConfig {

    private final CommonProperties.AuthProperties.DataSourceProperties databaseProperties;

    public AuthDataSourceConfig(CommonProperties commonProperties) {
        this.databaseProperties = commonProperties.getAuth().getDatasource();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(this.databaseProperties.getUrl());
        dataSource.setUsername(this.databaseProperties.getUsername());
        dataSource.setPassword(this.databaseProperties.getPassword());
        dataSource.setDriverClassName(this.databaseProperties.getDriverClassName());
        dataSource.setMaximumPoolSize(this.databaseProperties.getMaxPoolSize());
        dataSource.setMinimumIdle(this.databaseProperties.getMinIdle());
        return dataSource;
    }
}

