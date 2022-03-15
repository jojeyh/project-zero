package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.NegativeAccountBalance;
import com.revature.exception.WrongIdException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public Client updateClientWithId(String firstname, String lastname, String id) {
        try {
            Client client = new Client(
                    firstname,
                    lastname,
                    Integer.parseInt(id)
            );
            return this.bankDao.updateClientWithId(client);
        } catch (NumberFormatException e) {
            logger.debug("Invalid ID entered");
            throw new IllegalArgumentException("Id must be a positive integer");
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

    public boolean addAccountById(Account account) {
        try {
            validateAccountInfo(account);
            return this.bankDao.addAccountById(account);
        } catch (NegativeAccountBalance e) {
            throw new IllegalArgumentException("Cannot have a negative account balance");
        } catch (WrongIdException e) {
            throw new IllegalArgumentException("Id must be a non-negative integer");
        }
    }

    public List<Account> getAllClientAccounts(String client_id) {
        try {
            Integer id = Integer.parseInt(client_id);
            return this.bankDao.getAllClientAccounts(id);
        } catch (NumberFormatException e) {
            logger.debug("Invalid ID entered");
            throw new IllegalArgumentException("Invalid ID entered");
        }
    }

    public List<Account> getAllClientAccountsInBetween(String client_id, String amountLessThan, String amountGreaterThan) {
        Integer id = Integer.parseInt(client_id);
        Integer lessThan = Integer.parseInt(amountLessThan);
        Integer greaterThan = Integer.parseInt(amountGreaterThan);
        return this.bankDao.getAllClientAccountsInBetween(id, lessThan, greaterThan);
    }

    public Account getAccountById(String accountId) {
        try {
            Integer id = Integer.parseInt(accountId);
            return this.bankDao.getAccountById(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID entered");
        }
    }

    public Account updateClientAccount(Account updatedAccount) {
        try {
            validateAccountInfo(updatedAccount);
            return this.bankDao.updateClientAccount(updatedAccount);
        } catch (NegativeAccountBalance e) {
            throw new IllegalArgumentException("Account cannot have a negative balance");
        } catch (WrongIdException e) {
            throw new IllegalArgumentException("Id must be a non-negative integer");
        }
    }

    public Boolean deleteAccount(String accountId) {
        try {
            Integer id = Integer.parseInt(accountId);
            return this.bankDao.deleteAccount(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id must be a non-negative integer");
        }
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

    private void validateAccountInfo(Account account) throws IllegalArgumentException, NegativeAccountBalance,
            WrongIdException {
        if (account.getBalance() < 0) {
            throw new NegativeAccountBalance("Account balance must be non-negative integer");
        }

        if (account.getClientId() < 0) {
            throw new WrongIdException("Client ID must be a positive integer");
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