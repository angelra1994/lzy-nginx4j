package com.lzy.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class ProxyVerticle extends AbstractVerticle {

    private static final int HTTP_PORT = 6060;

    static int getHttpPort() {
        return HTTP_PORT;
    }
    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        HttpClientOptions httpClientOptions = new HttpClientOptions();

        httpClientOptions.setDefaultHost("127.0.0.1");
        httpClientOptions.setDefaultPort(ServerVerticle.getHttpPort());
        httpClientOptions.setKeepAlive(true);

        HttpClient httpClient = vertx.createHttpClient(httpClientOptions);

        // req1: 6060的请求，转发给req2 9090
        httpServer.requestHandler(req1 -> {
            HttpServerResponse resp1 = req1.response();
            resp1.setChunked(true);
            httpClient.request(req1.method(), req1.uri(), ar1 -> {
                if(ar1.succeeded()) {
                    HttpClientRequest req2 = ar1.result();
                    req2.response(ar2 -> {
                        if(ar2.succeeded()) {
                            HttpClientResponse resp2 = ar2.result();
                            resp1.setStatusCode(resp2.statusCode());

                            resp2.handler(x -> {
                                System.out.println("resp2, x = " + x);
                                resp1.putHeader("content-type", "text/html; charset=UTF-8");
                                resp1.write(x);
                            });
                            resp2.endHandler(x -> {
                                resp1.end();
                            });

//                            resp2.handler(x -> {
//                                System.out.println("x = " + x);
//                                resp1.putHeader("content-type", "text/html; charset=UTF-8");
//                                resp1.write(x);
//                                resp1.end();
//                            });
                        }
                    });

//                    req1.handler(x -> {
//                            req2.write(x);
//                            req2.end();
//                    });

                    req1.handler(req2::write);
                    req1.endHandler(x -> {
                        System.out.println("req1, x = " + x);
                        req2.end();
                    });
                }
            });

        }).listen(HTTP_PORT, event -> {
            if (event.succeeded()) {
                System.out.println("proxy start, 服务启动在" + HTTP_PORT + "端口");
            } else {
                System.out.println("proxy start failed");
            }
        });



        //vertx.createHttpServer().requestHandler(req -> req.response().end("hello world")).listen(8080);
    }
}
