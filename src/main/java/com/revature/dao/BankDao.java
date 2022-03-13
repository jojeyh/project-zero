package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDao {
    public int createClient(Client client) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            // TODO Change this to return GENERATED ID and get rid of SELECT query
            String query = "INSERT INTO clients (lastname, firstname) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, client.getLastName());
            stmt.setString(2, client.getFirstName());


            if (stmt.executeUpdate() == 1) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("New client successfully created with id of " + rs.getLong(1));
                    return rs.getInt(1);
                }
            } else {
                System.out.println("Insertion failed.");
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Client creation failed: " + e.getMessage());
        }
        return 0;
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
                clients.add(client);
            } // while
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO Experiment with passing in a String client_id and see if behavior changes
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

    public boolean addAccountById(Account account) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "INSERT INTO accounts (balance, clientid, type) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, account.getBalance());
            stmt.setInt(2, account.getClientId());
            stmt.setString(3, account.getAccountType().name());
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Successfully inserted account with generated ID " + rs.getInt(1));
                return true;
            } else {
                System.out.println("Could not update client with new account");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    } // addAccountById

    public List<Account> getAllClientAccounts(Integer client_id) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            ArrayList<Account> accounts = new ArrayList<>();
            String query = "SELECT * FROM accounts WHERE clientid=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, client_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getInt("balance"),
                        rs.getInt("id"),
                        rs.getInt("clientid"),
                        Account.AccountType.valueOf(rs.getString("type"))
                ));
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllClientAccountsInBetween(Integer client_id, Integer amountLessThan, Integer amountGreaterThan) {
        ArrayList<Account> accounts = (ArrayList<Account>) getAllClientAccounts(client_id);
        ArrayList<Account> rangedAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            if (account.getBalance() < amountLessThan && account.getBalance() > amountGreaterThan) {
                rangedAccounts.add(account);
            }
        });
        return rangedAccounts;
    }

    public Account getAccountById(Integer accountId) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "SELECT * FROM accounts WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("balance"),
                    rs.getInt("id"),
                    rs.getInt("clientId"),
                    Account.AccountType.valueOf(rs.getString("type"))
                );
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account updateClientAccount(Account updatedAccount) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "UPDATE accounts SET type=?, balance=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(3, updatedAccount.getId());
            stmt.setInt(2, updatedAccount.getBalance());
            stmt.setString(1, updatedAccount.getAccountType().name());
            if (stmt.executeUpdate() == 1) {
                System.out.println("Client account successfully updated.");
                return updatedAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAccount(Integer accountId) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "DELETE FROM accounts WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, accountId);
            if (stmt.executeUpdate() == 1) {
                System.out.println("Successfully deleted account");
            } else {
                System.out.println("Could not delete account or does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // deleteAccount
} // class BankDao
