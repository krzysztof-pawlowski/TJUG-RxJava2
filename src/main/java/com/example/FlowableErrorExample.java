package com.example;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowableErrorExample {

    private static Logger log = LoggerFactory.getLogger(FlowableErrorExample.class);

    public static void main(String[] args) {

        Flowable<Integer> flowable = Flowable.create(subscriber -> {
            log.info("Started emitting");

            log.info("Emitting 1st");
            subscriber.onNext(1);

            subscriber.onError(new RuntimeException("Test exception"));

            log.info("Emitting 2nd");
            subscriber.onNext(2);
        }, BackpressureStrategy.BUFFER);

        log.info("=======================");
        log.info("EXAMPLE 1 - NO HANDLING");
        log.info("=======================");

        flowable.subscribe(
            val -> log.info("Received: " + val),
            err -> log.error("Error: " + err),
            () -> log.info("Completed!")
        );

        log.info("===========================");
        log.info("EXAMPLE 2 - RETURN ON ERROR");
        log.info("===========================");

        flowable
            .onErrorReturn(throwable -> {
                log.info("Error handled: " + throwable);
                return -1;
            })
            .subscribe(
                val -> log.info("Received: " + val),
                err -> log.error("Error: " + err),
                () -> log.info("Completed!")
            );

        log.info("==================================");
        log.info("EXAMPLE 3 - SWITCH STREAM ON ERROR");
        log.info("==================================");

        Flowable<Integer> flowableOnError = Flowable.range(5, 3);

        flowable
            .onErrorResumeNext(throwable -> flowableOnError)
            .subscribe(
                val -> log.info("Received: " + val),
                err -> log.error("Error: " + err),
                () -> log.info("Completed!")
            );

    }
}
