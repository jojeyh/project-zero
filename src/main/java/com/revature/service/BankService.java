package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
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

    public int createClient(Client client) {
        validateClientInfo(client);
        return bankDao.createClient(client);
    }

    public List<Client> getAllClients() {
        return this.bankDao.getAllClients();
    }

    public Client getClientWithId(String id) throws ClientNotFoundException {
        int client_id = Integer.parseInt(id);

        Client client = this.bankDao.getClientWithId(client_id);

        if (client == null) {
            throw new ClientNotFoundException("Client with id: " + id + " was not found.");
        }

        return client;
    }

    public Client updateClientWithId(Client client, Integer clientId) throws WrongIdException {
        if (client.getId() != clientId) {
            throw new WrongIdException("Cannot change a client's ID.  Either create a new record or update with same ID");
        } else {
            return this.bankDao.updateClientWithId(client);
        }
    }

    public void deleteClientWithId(String client_id) {
        this.bankDao.deleteClientWithId(client_id);
    }

    public boolean addAccountById(Account account) {
        return this.bankDao.addAccountById(account);
    }

    public List<Account> getAllClientAccounts(Integer client_id) {
        return this.bankDao.getAllClientAccounts(client_id);
    }

    public List<Account> getAllClientAccountsInBetween(Integer client_id, Integer amountLessThan, Integer amountGreaterThan) {
        return this.bankDao.getAllClientAccountsInBetween(client_id, amountLessThan, amountGreaterThan);
    }

    public Account getAccountById(Integer accountId) {
        return this.bankDao.getAccountById(accountId);
    }

    // TODO Change this return Object to Account
    public Object updateClientAccount(Account updatedAccount) {
        return this.bankDao.updateClientAccount(updatedAccount);
    }

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
            throw new IllegalArgumentException("Last name must be alphabetical characters only.");
        }
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