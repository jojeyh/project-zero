package com.revature.service;

import com.revature.dao.BankDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.WrongIdException;
import com.revature.model.Client;

import java.util.List;

public class BankService {
    private final BankDao bankDao;

    public BankService() {
        this.bankDao = new BankDao();
    }

    public Client createClient(Client client) {
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

    // TODO Update this to include exception where ID not found, or create a new client possibly
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
}
