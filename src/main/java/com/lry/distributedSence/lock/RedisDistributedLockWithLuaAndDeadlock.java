package com.lry.distributedSence.lock;

/**
 * @program: CoverUtils
 * @description:
 * Lua脚本确保了SETNX和EXPIRE操作的原子性，
 * 死锁处理的部分则通过循环和超时机制来避免获取锁时的死锁问题
 * @author: Pck
 * @create: 2023-08-08 22:20
 **/
import redis.clients.jedis.Jedis;

public class RedisDistributedLockWithLuaAndDeadlock {

    private static final String LOCK_KEY = "my_lock";

    private static final String LOCK_VALUE = "locked";

    private static final int LOCK_EXPIRY = 30000; // 锁的过期时间，单位毫秒

    private static final int LOCK_TIMEOUT = 5000; // 获取锁的超时时间，单位毫秒

    private static final int LOCK_RETRY_INTERVAL = 100; // 重试获取锁的间隔时间，单位毫秒


    private Jedis jedis;
    private String lockValue; // 用于标识持有锁的进程
    private boolean isLocked = false; // 是否持有锁
    private String script =
            "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then" +
                    "   redis.call('expire', KEYS[1], tonumber(ARGV[2]))" +
                    "   return 'OK'" +
                    "else" +
                    "   return 'NOT_OK'" +
                    "end";

    public RedisDistributedLockWithLuaAndDeadlock(String host, int port) {
        jedis = new Jedis(host, port);
        // 生成持有锁的标识
        lockValue = Thread.currentThread().getId() + "-" + System.nanoTime();
    }

    public boolean acquireLock() throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTimestamp < LOCK_TIMEOUT) {
            String result = (String) jedis.eval(script, 1, LOCK_KEY,
                    LOCK_VALUE, String.valueOf(LOCK_EXPIRY));
            if ("OK".equals(result)) {
                isLocked = true;
                return true; // 成功获取锁
            }
            Thread.sleep(LOCK_RETRY_INTERVAL);
        }
        return false; // 获取锁超时
    }

    public void releaseLock() {
        if (isLocked) {
            String currentValue = jedis.get(LOCK_KEY);
            if (lockValue.equals(currentValue)) {
                jedis.del(LOCK_KEY); // 只有持有锁的进程才能释放锁
                isLocked = false;
            }
        }
    }

    public void close() {
        jedis.close();
    }

    public static void main(String[] args) {
        String redisHost = "localhost";
        int redisPort = 6379;

        RedisDistributedLockWithLuaAndDeadlock lock = new RedisDistributedLockWithLuaAndDeadlock(redisHost, redisPort);

        try {
            if (lock.acquireLock()) {
                System.out.println("Lock acquired. Performing critical section.");
                Thread.sleep(5000); // 模拟执行业务逻辑的时间
                System.out.println("Critical section completed.");
            } else {
                System.out.println("Failed to acquire lock.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.releaseLock();
            lock.close();
        }
    }
}
