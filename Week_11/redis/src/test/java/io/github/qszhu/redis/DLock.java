package io.github.qszhu.redis;

import com.sun.tools.javac.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class DLock {
    private static final long DEFAULT_TIMEOUT_MILLIS = 30000;

    private String name;
    private JedisPool jedisPool;
    private String lockId;
    private String unlockScriptSha;

    public DLock(String name, JedisPool jedisPool) {
        this.name = name;
        this.jedisPool = jedisPool;
        this.lockId = UUID.randomUUID().toString();
        this.unlockScriptSha = loadUnlockScript();
    }

    public boolean lock() {
        return lock(DEFAULT_TIMEOUT_MILLIS);
    }

    public boolean lock(long timeoutMillis) {
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.set(name, lockId, SetParams.setParams().nx().px(timeoutMillis));
            return (res != null && res.equals("OK"));
        }
    }

    public boolean unlock() {
        try (Jedis jedis = jedisPool.getResource()) {
            Object res = jedis.evalsha(unlockScriptSha, List.of(name), List.of(lockId));
            return (Long) res != 0;
        }
    }

    private String loadUnlockScript() {
        try (Jedis jedis = jedisPool.getResource()) {
            String res = jedis.scriptLoad(String.join("\n",
                    "if redis.call(\"get\",KEYS[1]) == ARGV[1] then",
                    "    return redis.call(\"del\",KEYS[1])",
                    "else",
                    "    return 0",
                    "end"));
            return res;
        }
    }

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(config, "localhost");
        DLock lock = new DLock("lock", jedisPool);

        System.out.println(lock.lock());
        System.out.println(lock.lock());

        System.out.println(lock.unlock());
        System.out.println(lock.unlock());
    }
}
