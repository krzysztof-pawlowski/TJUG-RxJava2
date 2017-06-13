package com.example;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeExample {


    private static Logger log = LoggerFactory.getLogger(ZipExample.class);

    public static void main(String[] args) throws InterruptedException {
        Flowable<String> colors1 = Flowable.just("red", "green");
        Flowable<String> colors2 = Flowable.just("blue", "yellow");

        Flowable<String> periodicEmitter = Flowable.merge(colors1, colors2);

        periodicEmitter.subscribe(
            val -> log.info("Received: " + val),
            err -> log.error("Error: " + err),
            () -> log.info("Completed!")
        );

    }
}
