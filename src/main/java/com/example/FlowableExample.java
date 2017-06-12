package com.example;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowableExample {

    private static Logger log = LoggerFactory.getLogger(FlowableExample.class);

    public static void main(String[] args) {

        Flowable<Integer> flowable = Flowable.just(1, 2, 3);

        flowable.subscribe(new Subscriber<Integer>() {
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            public void onNext(Integer integer) {
                log.info("Received: " + integer);
            }

            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
            }

            public void onComplete() {
                log.info("Completed!");
            }
        });

    }

}
