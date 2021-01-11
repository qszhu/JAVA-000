package io.github.qszhu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class DCounter {
    private String name;
    private JedisPool jedisPool;
    private String decrByScriptSha;

    public DCounter(String name, JedisPool jedisPool) {
        this.name = String.format(name + ":" + UUID.randomUUID().toString());
        this.jedisPool = jedisPool;
        this.decrByScriptSha = loadDecrByScript();
    }

    public boolean setCount(long value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.set(name, String.valueOf(value));
            return (res != null && res.equals("OK"));
        }
    }

    public long getCount() {
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.get(name);
            return Long.parseLong(res);
        }
    }

    public boolean decreaseBy(long delta) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.watch(name);
            long value = Long.parseLong(jedis.get(name));
            long newValue = value - delta;
            if (newValue < 0) return false;

            Transaction trans = jedis.multi();
            trans.set(name, String.valueOf(newValue));
            List<Object> res = trans.exec();
            return res != null && res.stream().allMatch(o -> o != null && o.equals("OK"));
        }
    }

    public boolean decreaseBy1(long delta) {
        try (Jedis jedis = jedisPool.getResource()) {
            Object res = jedis.evalsha(decrByScriptSha, com.sun.tools.javac.util.List.of(name), com.sun.tools.javac.util.List.of(String.valueOf(delta)));
            return (Long) res != 0;
        }
    }

    private String loadDecrByScript() {
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.scriptLoad(String.join("\n",
                    "if redis.call(\"get\",KEYS[1]) >= ARGV[1] then",
                    "    return redis.call(\"decrby\",KEYS[1],ARGV[1])",
                    "else",
                    "    return 0",
                    "end"));
            return res;
        }
    }

    public static void main(String[] args) throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(16);
        config.setMaxTotal(128);
        JedisPool jedisPool = new JedisPool(config, "localhost");

        final int count = 100000;
        final int workers = 10;
        DCounter counter = new DCounter(DCounter.class.getCanonicalName(), jedisPool);
        counter.setCount(count);
        System.out.println("before: " + counter.getCount());

        CountDownLatch latch = new CountDownLatch(workers);
        long t = System.currentTimeMillis();
        for (int i = 0; i < workers; i++) {
            new Thread(() -> {
                int orders = 0;
                int failed = 0;
                while (counter.getCount() > 0) {
                    if (counter.decreaseBy1(1)) {
                        orders++;
                    } else {
                        failed++;
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Thread " + Thread.currentThread().getId() + " completed " + orders + " failed " + failed);
                latch.countDown();
            }).start();
        }
        latch.await();

        t = System.currentTimeMillis() - t;
        System.out.println("elasped " + t + " ms");

        System.out.println("after: " + counter.getCount());
    }
}
