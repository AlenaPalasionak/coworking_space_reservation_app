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
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${hikari.maximumPoolSize}")
    private int maximumPoolSize;
    @Value("${hikari.minimumIdle}")
    private int minimumIdle;
    @Value("${hikari.idleTimeout}")
    private int idleTimeout;
    @Value("${database.driver}")
    private String driver;


    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);

        return new HikariDataSource(hikariConfig);
    }
}
