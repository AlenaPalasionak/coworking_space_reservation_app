package org.example.coworking.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up a HikariCP data source.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class JdbcConfig {
    @Value("${datasource.url}")
    String url;
    @Value("${datasource.username}")
    String username;
    @Value("${datasource.password}")
    String password;
    @Value("${hikari.maximumPoolSize}")
    int maximumPoolSize;
    @Value("${hikari.minimumIdle}")
    int minimumIdle;
    @Value("${hikari.idleTimeout}")
    int idleTimeout;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);

        return new HikariDataSource(hikariConfig);
    }
}
