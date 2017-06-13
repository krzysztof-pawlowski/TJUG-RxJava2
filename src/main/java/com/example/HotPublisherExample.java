package com.example;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class HotPublisherExample {

    private static Logger log = LoggerFactory.getLogger(HotPublisherExample.class);

    private static int counter;

    public static void main(String[] args) throws InterruptedException {

        ConnectableFlowable<Integer> connectableObservable = ConnectableFlowable.<Integer>create(subscriber -> {

            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                counter++;
                subscriber.onNext(counter);
            }, 0, 500, MILLISECONDS);

            subscriber.setCancellable(scheduledExecutorService::shutdown);

        }, BackpressureStrategy.BUFFER)
            .publish();

        Flowable<Integer> flowable = connectableObservable.refCount();

        final CountDownLatch latch = new CountDownLatch(2);

        log.info("Subscribing 1st");
        flowable
            .take(5)
            .subscribe(val -> log.info("Subscriber1 received: {}", val),
                val -> log.info("Subscriber1 error: {}", val.getMessage()),
                () -> {
                    log.info("Subscriber1 received completed");
                    latch.countDown();
                });

        Thread.sleep(1000);

        log.info("Subscribing 2nd");
        //we're not seeing the code inside .create() re-executed
        flowable
            .take(2)
            .subscribe((val) -> log.info("Subscriber2 received: {}", val),
                val -> log.info("Subscriber1 error: {}", val.getMessage()),
                () -> {
                    log.info("Subscriber1 received completed");
                    latch.countDown();
                });

        latch.await();
    }

}
