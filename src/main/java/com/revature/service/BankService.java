package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;

import java.util.List;

public class BankService {
    private final BankDao bankDao;

    public BankService() {
        this.bankDao = new BankDao();
    }

    public void createClient(String lastName, String firstName) {
        bankDao.createClient(lastName, firstName);
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

    public Client updateClientWithId(String id) {
        int client_id = Integer.parseInt(id);

        return this.bankDao.updateClientWithId(client_id);
    }

}
