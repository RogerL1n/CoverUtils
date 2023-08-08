package com.lry.distributedSence.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * Desc:  基于Redisson的分布式锁简单实现
 * @param null
 * @return {@link null}
 * @author PCK
 * @date 2023/8/8 16:22
 */
public class RedissonDistributedLock {
    private final RedissonClient redissonClient;

    public RedissonDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean tryLock(String lockKey, long timeout, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
}

 class DistributedLockExample1 {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");

        RedissonClient redissonClient = Redisson.create(config);

        RedissonDistributedLock distributedLock = new RedissonDistributedLock(redissonClient);

        String lockKey = "myLock";
        try {
            if (distributedLock.tryLock(lockKey, 10, TimeUnit.SECONDS)) {
                // 获取锁成功，执行需要同步的操作
                System.out.println("Lock acquired, performing critical section...");
                Thread.sleep(5000); // 模拟执行一些耗时操作
            } else {
                // 获取锁失败
                System.out.println("Failed to acquire lock, another process owns the lock.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            distributedLock.unlock(lockKey);
        }

        // 关闭 Redisson 客户端
        redissonClient.shutdown();
    }
}
