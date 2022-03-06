package com.revature.dao;

import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankDao {

    public void createClient(String lastName, String firstName) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO clients (lastName, firstName) VALUES ('");
            sb.append(lastName);
            sb.append("', '");
            sb.append(firstName);
            sb.append("')");
            System.out.println(sb.toString());
            PreparedStatement stmt = conn.prepareStatement(sb.toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Client creation failed: " + e.getMessage());
        }
    }
}
