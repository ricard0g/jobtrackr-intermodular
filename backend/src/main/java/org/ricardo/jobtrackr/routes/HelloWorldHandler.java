package org.ricardo.jobtrackr.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ricardo.jobtrackr.routes.base.HandlerBase;

import java.io.IOException;

public class HelloWorldHandler extends HandlerBase implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200, "Hello World Putaso\n");
    }
}
