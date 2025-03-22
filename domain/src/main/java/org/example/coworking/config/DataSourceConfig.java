package org.example.coworking.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConfig {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static final HikariDataSource dataSource;

    private static final int MAX_POOL_SIZE = 10;
    private static final int MINIMUM_IDLE_SIZE = 2;
    private static final int IDLE_TIMEOUT = 30_000;

    static {
        hikariConfig.setJdbcUrl(PropertyConfig.getProperties().getProperty("database.url"));
        hikariConfig.setUsername(PropertyConfig.getProperties().getProperty("database.username"));
        hikariConfig.setPassword(PropertyConfig.getProperties().getProperty("database.password"));
        hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
        hikariConfig.setMinimumIdle(MINIMUM_IDLE_SIZE);
        hikariConfig.setIdleTimeout(IDLE_TIMEOUT);

        dataSource = new HikariDataSource(hikariConfig);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
