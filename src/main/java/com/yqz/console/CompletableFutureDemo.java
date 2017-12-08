package com.yqz.console;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {

    public static void main(String[] args) {
        CompletableFuture.completedFuture(1).thenAccept((s) ->
                System.out.println(s));

        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).thenComposeAsync((s) -> {
            return CompletableFuture.supplyAsync(() -> {
                return s + ",s2";
            });
        }).join();
        System.out.println(result);
    }
}
