package com.lzy.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ServerVerticle extends AbstractVerticle {

    private static final int HTTP_PORT = 9090;

    static int getHttpPort() {
        return HTTP_PORT;
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.get("/hello").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            HttpServerRequest request = routingContext.request();
            response.putHeader("content-type", "text/html; charset=UTF-8").end("hello world, 李子园", "UTF8");
        });

        router.post("/hello")
                .handler(BodyHandler.create())
                .handler(routingContext -> {
                    JsonObject json = routingContext.getBodyAsJson();
                    routingContext.response().putHeader("content-type", "text/html; charset=UTF-8").end(json.getString("name"), "UTF8");


                });

        httpServer.requestHandler(router).listen(HTTP_PORT, event -> {
            if (event.succeeded()) {
                System.out.println("server start, 服务启动在" + HTTP_PORT + "端口");
            } else {
                System.out.println("server start failed");
            }
        });



        //vertx.createHttpServer().requestHandler(req -> req.response().end("hello world")).listen(8080);
    }
}
