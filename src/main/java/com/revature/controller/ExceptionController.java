package com.revature.controller;

import com.revature.exception.ClientNotFoundException;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController implements Controller {
    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private ExceptionHandler clientNotFound = (e, ctx) ->  {
        logger.warn("Client cannot be found in records: " + e.getMessage());
        ctx.status(404);
        ctx.json(e.getMessage());
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(ClientNotFoundException.class, clientNotFound);
    }
}
