package com.revature.main;

import io.javalin.Javalin;

public class Driver {
    public static void main(String[] args) {
        Javalin app = Javalin.create();

        map(app, new BankController());

        app.start();
    }

    public static void map(Javalin app, Controller... controllers) {
        for(Controller c : controllers) {
            c.mapEndpoints(app);
        }
    }
}
