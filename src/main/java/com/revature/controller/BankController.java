package com.revature.controller;

import com.google.gson.Gson;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.WrongIdException;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.BankService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BankController implements Controller {
    private BankService bankService;

    public BankController() {
        this.bankService = new BankService();
    }

    public Handler createClient = (ctx) -> {
        JSONObject obj = new JSONObject(ctx.body());
        Client client = this.bankService.createClient(new Client(
                obj.getString("firstname"),
                obj.getString("lastname")
                )
        );
        ctx.json(client);
    };

    public Handler getAllClients = ctx -> {
        List<Client> clients = this.bankService.getAllClients();
        ctx.json(clients);
    };

    public Handler getClientWithId = ctx -> {
        try {
            Client client = this.bankService.getClientWithId(ctx.pathParam("client_id"));
            ctx.json(client);
            ctx.status(200);
        } catch(ClientNotFoundException e) {
            String err = e.getMessage();
            ctx.json(e);
            ctx.status(404);
        }
    };

    public Handler updateClientWithId = ctx -> {
        try {
            Gson gson = new Gson();
            Client client = gson.fromJson(ctx.body(), Client.class);
            client = this.bankService.updateClientWithId(client, Integer.parseInt(ctx.pathParam("client_id")));
            ctx.json(client);
            ctx.status(200);
        } catch (WrongIdException e) {
            String err = e.getMessage();
            ctx.json(e);
            ctx.status(409);
        }
    };

    public Handler deleteClientWithId = ctx -> {
        this.bankService.deleteClientWithId(ctx.pathParam("client_id"));
    };

    public Handler addAccountById = ctx -> {
        Gson gson = new Gson();
        Account account = gson.fromJson(ctx.body(), Account.class);
        account.setClientId(Integer.parseInt(ctx.pathParam("client_id")));
        account = this.bankService.addAccountById(account);
        ctx.json(account);
        ctx.status(200); // TODO is this correct?
    };

    public Handler getAllClientAccounts = ctx -> {
        List<Account> accounts = new ArrayList<>();
        if (!ctx.queryParamMap().isEmpty()) {
            Integer amountLessThan = Integer.parseInt(ctx.queryParam("amountLessThan"));
            Integer amountGreaterThan = Integer.parseInt(ctx.queryParam("amountGreaterThan"));

            accounts = this.bankService.getAllClientAccountsInBetween(
                    Integer.parseInt(ctx.pathParam("client_id")),
                    amountLessThan,
                    amountGreaterThan
            );
        } else {
            accounts = this.bankService.getAllClientAccounts(Integer.parseInt(ctx.pathParam("client_id")));
        }
        ctx.json(accounts);
        ctx.status(200); // TODO is this correct?
    };

    public Handler getAccountById = ctx -> {
        Integer accountId = Integer.parseInt(ctx.pathParam("account_id"));
        Account account = this.bankService.getAccountById(accountId);
        ctx.json(account);
    };

    public Handler updateClientAccount = ctx -> {
        Gson gson = new Gson();
        Account updatedAccount = gson.fromJson(ctx.body(), Account.class);
        ctx.json(this.bankService.updateClientAccount(updatedAccount));
    };

    public Handler deleteAccount = ctx -> {
        Integer accountId = Integer.parseInt(ctx.pathParam("account_id"));
        this.bankService.deleteAccount(accountId);
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/clients", createClient);
        app.get("/clients", getAllClients);
        app.get("/clients/{client_id}", getClientWithId);
        app.put("/clients/{client_id}", updateClientWithId);
        app.delete("/clients/{client_id}", deleteClientWithId);
        app.post("/clients/{client_id}/accounts", addAccountById);
        app.get("/clients/{client_id}/accounts", getAllClientAccounts);
        app.get("/clients/{client_id}/accounts/{account_id}", getAccountById);
        app.post("/clients/{client_id}/accounts/{account_id}", updateClientAccount);
        app.delete("/clients/{client_id}/accounts/{account_id}", deleteAccount);
    }
}
