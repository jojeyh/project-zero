package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankDao {

    public Client createClient(Client client) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "INSERT INTO clients (lastname, firstname, accounts) VALUES (?, ?, ARRAY [0])";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, client.getLastName());
            stmt.setString(2, client.getFirstName());

            if (stmt.executeUpdate() == 1) {
                System.out.println("Successfully inserted new client.");
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT * FROM clients WHERE lastname=? AND firstname=?"
                );
                pstmt.setString(1, client.getLastName());
                pstmt.setString(2, client.getFirstName());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Client(
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getInt("id")
                    );
                }
            } else {
                System.out.println("Insertion failed.");
            }
        } catch (SQLException e) {
            System.out.println("Client creation failed: " + e.getMessage());
        }
        return null;
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
                        //new ArrayList<Integer>(Arrays.asList(rs.getArray("accounts")))
                );
                // TODO POssibly clean this casting by using something more concise/direct
                Array arr = rs.getArray("accounts");
                Integer[] accountIds = (Integer[]) arr.getArray();
                ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(accountIds));
                client.setAccounts(ids);
                /*

                This code will be used later for updating accounts

                for (int i=0; i < ids.length; i++) {
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
                    //client.addAccount(account);
                }
                 */
                arr.free();
                clients.add(client);
            } // while
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client getClientWithId(Integer client_id) {
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
                ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(accountIds));
                client.setAccounts(ids);
                /*
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
                    //client.addAccount(account);
                }

                 */
                arr.free();
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } // getClientWithId

    public Client updateClientWithId(Client client) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "UPDATE clients SET firstname=?, lastname=?, accounts=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setInt(4, client.getId());
            stmt.setArray(3, conn.createArrayOf("int", client.getAccounts().toArray()));

            // TODO Change this System.out to a logging function and/or exception
            if (stmt.executeUpdate() == 1) {
                System.out.println("Client successfully updated.");
                return client;
            } else {
                System.out.println("Client not updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    } // updateClientWithId

    public void deleteClientWithId(String clientId) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            Integer id = Integer.parseInt(clientId);
            String query = "DELETE FROM clients WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            // TODO change print to log
            if (stmt.executeUpdate()==1) {
                System.out.println("Client successfully deleted.");
            } else {
                System.out.println("Client not deleted or did not exist.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
