package me.bedpotato.bedadditions.utilities.SQLUtil;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {

    private final HikariConfig hikariConfig = new HikariConfig();
    public int port;
    public Connection connection;
    public String type, host, database, username, password;
    protected HikariDataSource dataSource;

    public MySQL(String type, String host, int port, String database, String username, String password) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        if (!isConnected()) {
            switch (type) {
                case "mysql" -> hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
                case "mariadb" -> hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            }
            String url = "jdbc:" + type + "://" + host + ":" + port + "/" + database;
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setMaximumPoolSize(20); // Adjust as needed
            hikariConfig.setMaxLifetime(180000); // Adjust as needed
            hikariConfig.setConnectionTimeout(3000L); // Adjust as needed
            hikariConfig.addDataSourceProperty("cachePrepStmts", true);
            hikariConfig.addDataSourceProperty("autoReconnect", true);
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
