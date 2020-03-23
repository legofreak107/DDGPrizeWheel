package me.legofreak107.ddgprizewheel.objects;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class Database {

    private HikariDataSource dataSource;

    /**
     * Instantiate the database object using HikariCP
     * I found these recommended settings online
     */
    public Database() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/ddg");
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setPoolName("DDG-Hikari");
        config.setMaximumPoolSize(3);
        config.setMinimumIdle(10);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(5000);
        config.setInitializationFailTimeout(-1);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Return the HikariDataSource
     * @return HikariDataSource
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    /**
     * Return a connection from the Hikari Pool
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
