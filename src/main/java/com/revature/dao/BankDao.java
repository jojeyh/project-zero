package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDao {

    private static Logger logger = LoggerFactory.getLogger(BankDao.class);

    public int createClient(Client client) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "INSERT INTO clients (lastname, firstname) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, client.getLastName());
            stmt.setString(2, client.getFirstName());


            if (stmt.executeUpdate() == 1) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    logger.info("New client successfully created with id of " + rs.getInt(1));
                    return rs.getInt(1);
                }
            } else {
                logger.debug("Insertion of new client failed");
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
            logger.debug(e.getMessage());
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
                return client;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
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

            if (stmt.executeUpdate() == 1) {
                logger.info("Client with id " + client.getId() + " info changed.");
                return client;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return null;
    } // updateClientWithId

    public int deleteClientWithId(Integer clientId) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            Integer id = clientId;
            String query = "DELETE FROM clients WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            if (stmt.executeUpdate()==1) {
                logger.info("Deleted client with ID " + clientId);
                return 1;
            }

        } catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        return 0;
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
                logger.info("Added account " + rs.getInt(1) + "to client " + account.getClientId());
                return true;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
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
            logger.info("All accounts for Client " + client_id + " retrieved");
            return accounts;
        } catch (SQLException e) {
            logger.debug(e.getMessage());
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
        logger.info("All accounts between " + amountGreaterThan + " & " + amountLessThan +
                " for Client " + client_id + " retrieved");
        return rangedAccounts;
    }

    public Account getAccountById(Integer accountId) {
        try (Connection conn = ConnectionUtility.getConnection()) {
            String query = "SELECT * FROM accounts WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Account " + rs.getInt("id") + " info retrieved.");
                return new Account(
                    rs.getInt("balance"),
                    rs.getInt("id"),
                    rs.getInt("clientId"),
                    Account.AccountType.valueOf(rs.getString("type"))
                );
            }

        } catch (SQLException e) {
            logger.debug(e.getMessage());
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
                logger.info("Account " + updatedAccount.getId() +
                        " with Client " + updatedAccount.getClientId());
                return updatedAccount;
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
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
                logger.info("Deleted Account " + accountId);
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
    } // deleteAccount
} // class BankDao
