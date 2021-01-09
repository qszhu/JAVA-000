package io.github.qszhu.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CountDownLatch;

public class DCounter {
    static int count;

    public static void main(String[] args) throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(16);
        config.setMaxTotal(128);
        JedisPool jedisPool = new JedisPool(config, "localhost");
        DLock lock = new DLock(DCounter.class.getCanonicalName(), jedisPool);

        count = 100000;
        System.out.println("total count " + count);
        int workers = 10;
        int work = count / workers;
        boolean useLock = true; // change to false to disable locking
        CountDownLatch latch = new CountDownLatch(workers);
        for (int i = 0; i < workers; i++) {
            new Thread(() -> {
                for (int j = 0; j < work; j++) {
                    if (useLock) {
                        while (!lock.lock()) {
                        }
                        count--;
                        lock.unlock();
                    } else {
                        count--;
                    }
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println("after " + count);
    }
}
