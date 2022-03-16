package com.revature.utility;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtility {

    private ConnectionUtility() {}

    public static Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        Connection conn = DriverManager.getConnection(url, username, password);

        return conn;
    }
}
