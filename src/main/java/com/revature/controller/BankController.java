package com.revature.controller;

import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;
import com.revature.service.BankService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.json.JSONObject;

import java.util.List;

public class BankController implements Controller {
    private BankService bankService;

    public BankController() {
        this.bankService = new BankService();
    }

    public Handler createClient = (ctx) -> {
        JSONObject obj = new JSONObject(ctx.body());
        this.bankService.createClient(obj.getString("lastName"), obj.getString("firstName"));
    };

    public Handler getAllClients = ctx -> {
        List<Client> clients = this.bankService.getAllClients();
        ctx.json(clients);
    };

    public Handler getClientWithId = ctx -> {
        try {
            Client client = this.bankService.getClientWithId(ctx.pathParam("client_id"));
            ctx.json(client);
        } catch(ClientNotFoundException e) {
            String err = e.getMessage();
            ctx.json(e);
            ctx.status(404);
        }
    };
/*
    public Handler updateClientWithId = ctx -> {
        JSONObject obj = newJSONObject(ctx.body());
        Client client = this.bankService.updateClientWithid(ctx.pathParam("client_id"));
        ctx.json(client);
    };

 */

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/clients", createClient);
        app.get("/clients", getAllClients);
        app.get("/clients/{client_id}", getClientWithId);
        //app.put("/clients/{client_id}", updateClientWithId);
    }
}
