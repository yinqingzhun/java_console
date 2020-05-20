package com.yqz.console.tech.rx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxSamples {

    static final Logger LOG = LoggerFactory.getLogger(FluxSamples.class);

    public static void main(String... args) throws InterruptedException {

        //  simpleFlux();

//       transformValues();
//
        filterValues();

    }

    private static void simpleFlux() throws InterruptedException {
        // A Flux is a data publisher
        EmitterProcessor<String> stream = EmitterProcessor.<String>create();

        // Log values passing through the Flux and capture the first coming signal
        Mono<String> promise = stream.doOnNext(s -> LOG.info("Consumed String {}", s))
                .next();

        // Publish a value
        stream.onNext("Hello World!");

        promise.block();
    }

    private static void transformValues() throws InterruptedException {
        // A Flux is a data publisher
        EmitterProcessor<String> stream = EmitterProcessor.<String>create();

        // Transform values passing through the Flux, observe and capture the result once.
        Mono<String> promise = stream.map(String::toUpperCase)
                .doOnNext(s -> LOG.info("UC String {}", s))
                .next();

        // Publish a value
        stream.onNext("Hello World!");

        promise.block();
    }

    private static void filterValues() throws InterruptedException {
        // A Flux is a data publisher
        EmitterProcessor<String> stream = EmitterProcessor.<String>create();

        // Filter values passing through the Flux, observe and capture the result once.
        Mono<List<String>> promise = stream.filter(s -> s.startsWith("H"))
                .doOnNext(s -> LOG.info("Filtered String {}", s))
                .collectList();

        // Publish a value
        stream.onNext("Hello World!");
        stream.onNext("Hi,Goodbye World!");
        stream.onComplete();

        promise.block();
    }

}
