package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.WrongIdException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// TODO Add exceptions and error checking in this layer, check each method to handle any edge cases or incorrect info

public class BankService {
    private static Logger logger = LoggerFactory.getLogger(BankService.class);

    private final BankDao bankDao;

    public BankService() {
        this.bankDao = new BankDao();
    }

    public BankService(BankDao mockedDao) {
        this.bankDao = mockedDao;
    }

    public int createClient(Client client) throws IllegalArgumentException {
        validateClientInfo(client);

        return bankDao.createClient(client);
    }

    public List<Client> getAllClients() {
        return this.bankDao.getAllClients();
    }

    public Client getClientWithId(String id) throws ClientNotFoundException {
        try {
            int client_id = Integer.parseInt(id);

            Client client = this.bankDao.getClientWithId(client_id);

            if (client == null) {
                throw new ClientNotFoundException("Client with id: " + id + " was not found.");
            }

            return client;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided must be a valid int");
        }
    }

    public Client updateClientWithId(Client client, Integer clientId) throws WrongIdException {
        try {
            if (client.getId() != clientId) {
                throw new WrongIdException("Cannot change a client's ID.  Either create a new record or update with same ID");
            } else {
                return this.bankDao.updateClientWithId(client);
            }
        } catch (NumberFormatException e) {
            logger.debug("Invalid ID entered");
            return null;
        }
    }

    public boolean deleteClientWithId(String client_id) {
        try {
            Integer id = Integer.parseInt(client_id);

            return this.bankDao.deleteClientWithId(Integer.parseInt(client_id));
        } catch (NumberFormatException e) {
            logger.debug("Invalid ID entered");
            throw new IllegalArgumentException("Invalid ID entered");
        }
    }

    // TODO WRite tests
    public boolean addAccountById(Account account) {
        return this.bankDao.addAccountById(account);
    }

    // TODO write tests
    public List<Account> getAllClientAccounts(Integer client_id) {
        return this.bankDao.getAllClientAccounts(client_id);
    }

    // TODO write tests
    public List<Account> getAllClientAccountsInBetween(Integer client_id, Integer amountLessThan, Integer amountGreaterThan) {
        return this.bankDao.getAllClientAccountsInBetween(client_id, amountLessThan, amountGreaterThan);
    }

    // TODO write tests
    public Account getAccountById(Integer accountId) {
        return this.bankDao.getAccountById(accountId);
    }

    // TODO write test
    public Account updateClientAccount(Account updatedAccount) {
        return this.bankDao.updateClientAccount(updatedAccount);
    }

    // TODO write test
    public void deleteAccount(Integer accountId) {
        this.bankDao.deleteAccount(accountId);
    }

    private void validateClientInfo(Client client) {
        client.setFirstName(client.getFirstName().trim());
        client.setLastName(client.getLastName().trim());

        if (!client.getFirstName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("First name must be alphabetical characters only");
        }

        if (!client.getLastName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Last name must be alphabetical characters only");
        }

        logger.info("Client " + client.getId() + " validated");
    }

    private void validateAccountInfo(Account account) {
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("Account balance must be non-negative integer");
        }

        if (!BankService.enumContains(account.getAccountType())) {
            throw new IllegalArgumentException("Account type is not a valid type");
        }
    }

    private static boolean enumContains(Account.AccountType e) {
        for (Account.AccountType accountType : Account.AccountType.values()) {
            if (accountType.equals(e)) {
                return true;
            }
        }
        return false;
    }
}