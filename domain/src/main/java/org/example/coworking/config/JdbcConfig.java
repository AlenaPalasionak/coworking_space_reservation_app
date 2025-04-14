package org.example.coworking.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up a HikariCP data source.
 */
public class JdbcConfig {

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

    /**
     * Returns the configured HikariCP data source.
     *
     * @return the data source instance.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}
