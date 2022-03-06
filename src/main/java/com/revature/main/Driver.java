package com.revature.main;

import com.revature.controller.BankController;
import com.revature.controller.Controller;
import io.javalin.Javalin;

public class Driver {
    public static void main(String[] args) {
        Javalin app = Javalin.create();

        map(app, new BankController());

        app.start(7070);
    }

    public static void map(Javalin app, Controller... controllers) {
        for(Controller c : controllers) {
            c.mapEndpoints(app);
        }
    }
}
