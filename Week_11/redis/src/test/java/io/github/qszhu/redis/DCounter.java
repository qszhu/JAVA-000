package io.github.qszhu.redis;

import redis.clients.jedis.Jedis;

public class DCounter {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        DLock lock = new DLock("lock", jedis);

        // TODO
    }
}
