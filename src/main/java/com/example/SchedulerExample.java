package com.example;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SchedulerExample {

    private static Logger log = LoggerFactory.getLogger(SchedulerExample.class);

    public static void main(String[] args) throws InterruptedException {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);

        Flowable<Integer> flowable = Flowable.range(5, 10);

        flowable
            .subscribeOn(Schedulers.from(fixedThreadPool))
            .map(integer -> {
                log.info("Value: " + integer);
                return integer;
            })

            .observeOn(Schedulers.io())
            .subscribe(
                val -> log.info("Received: " + val),
                err -> log.error("Error: " + err),
                () -> log.info("Completed!")
            );

    }
}
