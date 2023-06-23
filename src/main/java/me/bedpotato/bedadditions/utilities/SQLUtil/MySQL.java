package me.bedpotato.bedadditions.utilities.SQLUtil;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.bedpotato.bedadditions.BedAdditions;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {

    private final HikariConfig hikariConfig = new HikariConfig();
    public int port;
    public Connection connection;
    public String host, database, username, password;
    protected HikariDataSource dataSource;

    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        if (!isConnected()) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException var12) {
                BedAdditions.getPlugin().getLogger().severe("Driver org.mariadb.jdbc.Driver not found");

            }

            hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setMaximumPoolSize(10);
            hikariConfig.setMinimumIdle(10);
            hikariConfig.setMaxLifetime(180000);
            hikariConfig.setConnectionTimeout(3000L);
            hikariConfig.setIdleTimeout(hikariConfig.getMinimumIdle() < hikariConfig.getMaximumPoolSize() ? 60000L : 0L);
            hikariConfig.addDataSourceProperty("cachePrepStmts", true);
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            dataSource = new HikariDataSource(hikariConfig);
            connection = dataSource.getConnection();
        }
    }


    public void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public Connection getConnection() {
        return connection;
    }
}
