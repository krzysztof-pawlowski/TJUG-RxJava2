package com.example;

import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZipExample {

    private static Logger log = LoggerFactory.getLogger(ZipExample.class);

    public static void main(String[] args) throws InterruptedException {
        Flowable<String> colors = Flowable.just("red", "green", "blue");
        Flowable<Long> timer = Flowable.interval(1, TimeUnit.SECONDS);

        Flowable<String> periodicEmitter = Flowable.zip(colors, timer, (key, val) -> key);

        final CountDownLatch latch = new CountDownLatch(1);

        periodicEmitter.subscribe(
            val -> log.info("Received: " + val),
            err -> log.error("Error: " + err),
            () -> {
                log.info("Completed!");
                latch.countDown();
            }
        );

        latch.await();
    }
}
