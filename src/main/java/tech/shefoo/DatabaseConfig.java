package tech.shefoo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/club");
        config.setUsername("donatello");
        config.setPassword("d0n4t3ll0");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Explicitly set the driver class name (optional)
        config.setMaximumPoolSize(10); // Set the max pool size
        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}