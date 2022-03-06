package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankDao {

    public void createClient(Client client) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            Stringbuilder sb = new StringBuilder();

            sb.append("INSERT INTO clients (firstName, lastName) VALUES ('");
            sb.append(client.getLastName());
            sb.append("', '");
            sb.append(client.getFirstName());
            sb.append("')");

            PreparedStatement stmt = conn.prepareStatement(sb.toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Client creation failed: " + e.getMessage());
        }
    }
}
