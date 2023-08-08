package com.lry.distributedSence.id;
/**
 * @author lin
 *
 * 获取全局唯一的id
 *添加了时间回拨的简单处理:当检测到时钟回拨时，首先判断回拨的偏移量，如果偏移量不大，
 * 就使用Thread.sleep等待时钟追上来。如果偏移量较大，可能是潜在的数据损坏问题，会抛出异常。
 */
public class SnowflakeIdUtils {
    //workerIdBits（工作机器ID位数) 2^5 =32 机器数

    private static final long workerIdBits = 5;

    //datacenterIdBits（数据中心ID位数）

    private static final long datacenterIdBits = 5;

    //sequenceBits（序列号位数）

    private static final long sequenceBits = 12;

    //机器id的最大分配值

    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    //中心id的最大分配值

    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private static final long workerIdShift = sequenceBits;
    private static final long datacenterIdShift = sequenceBits + workerIdBits;

    //时间戳需要左移的位数

    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 设置起始时间戳，例如：2021-07-01 00:00:00的时间戳

    private static final long epoch = 1625097600000L;

    public static long generateSnowflakeId(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + maxWorkerId);
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID must be between 0 and " + maxDatacenterId);
        }

        long timestamp = System.currentTimeMillis();
        long lastTimestamp = -1L;
        long sequence = 0L;

        synchronized (SnowflakeIdUtils.class) {
            if (timestamp < lastTimestamp) {
                // 发生时钟回拨，等待时钟追上来
                long offset = lastTimestamp - timestamp;
                if (offset <= 5) {
                    try {
                        Thread.sleep(offset);
                    } catch (InterruptedException e) {
                        // 处理中断异常
                    }
                    timestamp = System.currentTimeMillis();
                } else {
                    throw new RuntimeException("Clock moved backwards by a large offset. Potential data corruption.");
                }
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;
        }

        return ((timestamp - epoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        long workerId = 1;
        long datacenterId = 1;

        for (int i = 0; i < 10; i++) {
            long id = generateSnowflakeId(workerId, datacenterId);
            System.out.println("Generated ID: " + id);
        }
    }
}
