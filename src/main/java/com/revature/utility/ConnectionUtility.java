package com.revature.utility;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtility {

    private String url;
    private String username;
    private String password;

    public ConnectionUtility(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());

        Connection conn = DriverManager.getConnection(this.url, this.username, this.password);

        return conn;
    }
}
