package com.revature.controller;

import com.google.gson.Gson;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.WrongIdException;
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
            // TODO Use ctx.bodyAsClass() instead Gson
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

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/clients", createClient);
        app.get("/clients", getAllClients);
        app.get("/clients/{client_id}", getClientWithId);
        app.put("/clients/{client_id}", updateClientWithId);
        app.delete("/clients/{client_id}", deleteClientWithId);
    }
}
