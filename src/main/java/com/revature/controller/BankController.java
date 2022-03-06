package com.revature.controller;

import com.revature.service.BankService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.json.JSONObject;

public class BankController implements Controller {
    private BankService bankService;

    public BankController() {
        this.bankService = new BankService();
    }

    public Handler createClient = (ctx) -> {
        JSONObject obj = new JSONObject(ctx.body());
        bankService.createClient(obj.getString("lastName"), obj.getString("firstName"));
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/clients", createClient);
    }
}
