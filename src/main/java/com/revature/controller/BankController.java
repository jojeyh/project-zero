package com.revature.controller;

import com.google.gson.Gson;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.BankService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

// TODO Add ctx.status to all Handlers
public class BankController implements Controller {
    private BankService bankService;

    public BankController() {
        this.bankService = new BankService();
    }

    public static Logger logger = new LoggerFactory.getLogger(BankController.class);

    public Handler createClient = (ctx) -> {
        JSONObject obj = new JSONObject(ctx.body());
        logger.info("Calling BankService.createClient...");
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
        logger.info("Calling BankService.getAllClients");
        List<Client> clients = this.bankService.getAllClients();
        ctx.json(clients);
    };

    public Handler getClientWithId = ctx -> {
        logger.info("Calling BankService.getClientWithId");
        Client client = this.bankService.getClientWithId(ctx.pathParam("client_id"));
        ctx.json(client);
    };

    public Handler updateClientWithId = ctx -> {
        JSONObject body = new JSONObject(ctx.body());
        logger.info("Calling BankService.updateClientId...");
        Client updatedClient = this.bankService.updateClientWithId(
                body.getString("firstname"),
                body.getString("lastname"),
                ctx.pathParam("client_id")
        );
        ctx.json(updatedClient);
        ctx.status(200);
    };

    public Handler deleteClientWithId = ctx -> {
        logger.info("Calling BankService.deleteClientWithId...");
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
        logger.info("Calling BankService.addAccountById");
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
            logger.info("Calling BankService.getAllClientAccountsInBetween...");
            accounts = this.bankService.getAllClientAccountsInBetween(
                    ctx.pathParam("client_id"),
                    ctx.queryParam("amountLessThan"),
                    ctx.queryParam("amountGreaterThan")
            );
        } else {
            logger.info("Calling BankService.getAllClientAccounts");
            accounts = this.bankService.getAllClientAccounts(ctx.pathParam("client_id"));
        }
        ctx.json(accounts);
        ctx.status(200); // OK
    };

    public Handler getAccountById = ctx -> {
        logger.info("Calling BankService.getAccountById");
        Account account = this.bankService.getAccountById(ctx.pathParam("account_id"));
        ctx.json(account);
    };

    public Handler updateClientAccount = ctx -> {
        Gson gson = new Gson();
        Account updatedAccount = gson.fromJson(ctx.body(), Account.class);
        updatedAccount.setId(Integer.parseInt(ctx.pathParam("account_id")));
        updatedAccount.setClientId(Integer.parseInt(ctx.pathParam("client_id")));
        logger.info("Calling BankService.updateClientAccount");
        ctx.json(this.bankService.updateClientAccount(updatedAccount));
    };

    public Handler deleteAccount = ctx -> {
        logger.log("Calling BankService.deleteAccount");
        Boolean ok = this.bankService.deleteAccount(ctx.pathParam("account_id"));
        if (ok) {
            ctx.result("Account " + ctx.pathParam("account_id") + " deleted");
        } else {
            ctx.result("Account " + ctx.pathParam("account_id") + " could not be deleted");
        }
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
