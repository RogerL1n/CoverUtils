package com.lry.distributedSence.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;




/**
 * @author lin
 * 基于redis的分布式锁的实现
 * 简单处理了一下死锁逻辑
 *
 */
public class DistributedLockDeath {
    private static final String LOCK_KEY = "my_lock";
    private static final String LOCK_VALUE = "locked";
    // 锁的过期时间，单位毫秒
    private static final int LOCK_EXPIRE_TIME = 30000;


    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            boolean acquired = acquireLock(jedis, LOCK_KEY, LOCK_VALUE, LOCK_EXPIRE_TIME,10);
            if (acquired) {
                try {
                    // 执行需要加锁的操作
                    System.out.println("Lock acquired. Performing critical section.");
                    Thread.sleep(2000);
                    // 模拟执行操作
                } finally {
                    releaseLock(jedis, LOCK_KEY, LOCK_VALUE);
                    System.out.println("Lock released.");
                }
            } else {
                System.out.println("Failed to acquire lock.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Desc:acquireLock方法会进行多次尝试获取锁，每次尝试都会等待一段时间后再重试，
     * 总等待时间不超过maxRetries * expireTime。
     * 这样可以避免由于获取锁失败而导致的无限等待，从而减少死锁风险。
     * @param jedis
     * @param lockKey
     * @param lockValue
     * @param expireTime
     * @param maxRetries
     * @return {@link boolean}
     * @author PCK
     * @date 2023/8/8 13:03
     */
    private static boolean acquireLock(Jedis jedis, String lockKey, String lockValue, int expireTime, int maxRetries) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < maxRetries * expireTime) {
            SetParams params = new SetParams().nx().px(expireTime);
            String result = jedis.set(lockKey, lockValue, params);
            if ("OK".equals(result)) {
                return true;
            }
            try {
                Thread.sleep(100); // 等待一段时间后重试
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }


    private static void releaseLock(Jedis jedis, String lockKey, String lockValue) {
        String value = jedis.get(lockKey);
        if (lockValue.equals(value)) {
            jedis.del(lockKey);
        }
    }
}
