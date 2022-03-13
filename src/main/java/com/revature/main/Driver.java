package com.revature.main;

import com.revature.controller.BankController;
import com.revature.controller.ExceptionController;
import com.revature.controller.Controller;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Driver {
    private static Logger logger = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        app.before(ctx -> {
            logger.info(ctx.method() + " request received for " + ctx.path());
        });

        map(app, new BankController(), new ExceptionController());

        app.start(7070);
    }

    public static void map(Javalin app, Controller... controllers) {
        for(Controller c : controllers) {
            c.mapEndpoints(app);
        }
    }
}
