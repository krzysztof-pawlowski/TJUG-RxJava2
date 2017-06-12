package com.example;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeExample {


    private static Logger log = LoggerFactory.getLogger(ZipExample.class);

    public static void main(String[] args) throws InterruptedException {
        Flowable<String> colors = Flowable.just("red", "green");
        Flowable<String> timer = Flowable.just("blue", "yellow");

        Flowable<String> periodicEmitter = Flowable.merge(colors, timer);

        periodicEmitter.subscribe(
            val -> log.info("Received: " + val),
            err -> log.error("Error: " + err),
            () -> log.info("Completed!")
        );

    }
}
