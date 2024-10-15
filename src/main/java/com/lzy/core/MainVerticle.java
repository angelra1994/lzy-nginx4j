package com.lzy.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * @author lzy
 */
public class MainVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
        Vertx.vertx().deployVerticle(new ServerVerticle());
        Vertx.vertx().deployVerticle(new ProxyVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("hello, 冒险者");
    }

}
