package io.github.qszhu.redis;

import com.sun.tools.javac.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class DLock {
    private static final long DEFAULT_TIMEOUT_MILLIS = 30000;

    private String name;
    private String lockId;
    private Jedis jedis;
    private String unlockScriptSha;

    public DLock(String name, Jedis jedis) {
        this.name = name;
        this.jedis = jedis;
        this.unlockScriptSha = loadUnlockScript();
    }

    public boolean lock() {
        return lock(DEFAULT_TIMEOUT_MILLIS);
    }

    public boolean lock(long timeoutMillis) {
        if (lockId == null) {
            lockId = UUID.randomUUID().toString();
        }
        String res = jedis.set(name, lockId, SetParams.setParams().nx().px(timeoutMillis));
        return res != null && res.equals("OK");
    }

    public boolean unlock() {
        Object res = jedis.evalsha(unlockScriptSha, List.of(name), List.of(lockId));
        return (Long) res != 0;
    }

    private String loadUnlockScript() {
        String res = jedis.scriptLoad(String.join("\n",
                "if redis.call(\"get\",KEYS[1]) == ARGV[1] then",
                "    return redis.call(\"del\",KEYS[1])",
                "else",
                "    return 0",
                "end"));
        return res;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        DLock lock = new DLock("lock", jedis);

        System.out.println(lock.lock());
        System.out.println(lock.lock());

        System.out.println(lock.unlock());
        System.out.println(lock.unlock());
    }
}
