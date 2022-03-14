package com.revature.controller;

import com.google.gson.Gson;
import com.revature.exception.WrongIdException;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.BankService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// TODO Add ctx.status to all Handlers
public class BankController implements Controller {
    private BankService bankService;

    public BankController() {
        this.bankService = new BankService();
    }

    public Handler createClient = (ctx) -> {
        JSONObject obj = new JSONObject(ctx.body());
        Integer id = this.bankService.createClient(new Client(
                obj.getString("firstname"),
                obj.getString("lastname")
        ));
        if (id > 0) {
            ctx.result("Client successfully created with an id of " + id);
        } else {
            ctx.result("Client could not be created");
        }
    };

    public Handler getAllClients = ctx -> {
        List<Client> clients = this.bankService.getAllClients();
        ctx.json(clients);
    };

    public Handler getClientWithId = ctx -> {
        Client client = this.bankService.getClientWithId(ctx.pathParam("client_id"));
        ctx.json(client);
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
        Boolean ok = this.bankService.deleteClientWithId(ctx.pathParam("client_id"));
        if (ok) {
            ctx.result("Client deleted");
            ctx.status(200);
        } else {
            ctx.result("Client could not be deleted");
            ctx.status(404);
        }
    };

    public Handler addAccountById = ctx -> {
        Gson gson = new Gson();
        Account account = gson.fromJson(ctx.body(), Account.class);
        account.setClientId(Integer.parseInt(ctx.pathParam("client_id")));
        if (this.bankService.addAccountById(account)) {
            ctx.result("Account added successfully");
            ctx.status(200); // OK
        } else {
            ctx.result("Account could not be added");
            ctx.status(400); // Bad request
        }
    };

    public Handler getAllClientAccounts = ctx -> {
        List<Account> accounts = new ArrayList<>();
        if (!ctx.queryParamMap().isEmpty()) {
            accounts = this.bankService.getAllClientAccountsInBetween(
                    ctx.pathParam("client_id"),
                    ctx.queryParam("amountLessThan"),
                    ctx.queryParam("amountGreaterThan")
            );
        } else {
            accounts = this.bankService.getAllClientAccounts(ctx.pathParam("client_id"));
        }
        ctx.json(accounts);
        ctx.status(200); // OK
    };

    public Handler getAccountById = ctx -> {
        Account account = this.bankService.getAccountById(ctx.pathParam("account_id"));
        ctx.json(account);
    };

    public Handler updateClientAccount = ctx -> {
        Gson gson = new Gson();
        Account updatedAccount = gson.fromJson(ctx.body(), Account.class);
        ctx.json(this.bankService.updateClientAccount(updatedAccount));
    };

    public Handler deleteAccount = ctx -> {
        this.bankService.deleteAccount(ctx.pathParam("account_id"));
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
