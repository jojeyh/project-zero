package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDao {

    // TODO change param to be a Client object
    public void createClient(String lastName, String firstName) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            // TODO Use PreparedStatement methods instead of StringBuilder
            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO clients (lastname, firstname) VALUES ('");
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

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();

        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "SELECT * FROM clients";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("id")
                );
                Array arr = rs.getArray("accounts");
                Integer[] accountIds = (Integer[]) arr.getArray();
                for (int i=0; i < accountIds.length; i++) {
                    String accountQuery = "SELECT * FROM accounts WHERE id=" + accountIds[i];
                    PreparedStatement accountStmt = conn.prepareStatement(accountQuery);
                    ResultSet accountRs = accountStmt.executeQuery();
                    accountRs.next();
                    Account account = new Account(
                            accountRs.getInt("balance"),
                            accountRs.getInt("id")
                    );
                    switch (accountRs.getString("type")) {
                        case "C":
                            account.setAccountType(Account.AccountType.CHECKING);
                            break;
                        case "S":
                            account.setAccountType(Account.AccountType.SAVINGS);
                            break;
                        default:
                            break;
                    }
                    client.addAccount(account);
                }
                arr.free();
                clients.add(client);
            } // while
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getClientWithId(int client_id) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, client_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getInt("id")
                );
                Array arr = rs.getArray("accounts");
                Integer[] accountIds = (Integer[]) arr.getArray();
                for (int i = 0; i < accountIds.length; i++) {
                    String accountQuery = "SELECT * FROM accounts WHERE id=" + accountIds[i];
                    PreparedStatement accountStmt = conn.prepareStatement(accountQuery);
                    ResultSet accountRs = accountStmt.executeQuery();
                    accountRs.next();
                    Account account = new Account(
                            accountRs.getInt("balance"),
                            accountRs.getInt("id")
                    );
                    switch (accountRs.getString("type")) {
                        case "C":
                            account.setAccountType(Account.AccountType.CHECKING);
                            break;
                        case "S":
                            account.setAccountType(Account.AccountType.SAVINGS);
                            break;
                        default:
                            break;
                    }
                    client.addAccount(account);
                }
                arr.free();
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } // getClientWithId

    public Client updateClientWithId(int client_id) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = UPDATE clients SET firstName
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
