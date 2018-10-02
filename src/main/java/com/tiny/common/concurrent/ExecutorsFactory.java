package com.tiny.common.concurrent;

import java.util.concurrent.*;

/**
 * @author  by zhangzhaojun on 2017/12/28.
 */
public class ExecutorsFactory {

    //队列默认长度
    private final static int DEFAULT_CAPACITY = 20;
    //CPU个数
    private final static int CPU_SIZE = Runtime.getRuntime().availableProcessors();
    //默认最大任务处理线程数
    private final static int DEFAULT_MAX_POOL_SIZE = CPU_SIZE * 2 + 1;
    //默认最小任务处理线程数
    private final static int DEFAULT_CORE_POOL_SIZE = DEFAULT_MAX_POOL_SIZE / 2 + 1;
    //当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，毫秒。
    private final static long DEFAULT_KEEP_ALIVE_TIME = 500L;

    private final static RejectedExecutionHandler rejectedExecutionHandler = new BlockProviderExecutionHandler();

    public static ThreadPoolExecutor newNameThreadPool(String prefix) {
        return newNameThreadPool(DEFAULT_CORE_POOL_SIZE, DEFAULT_CAPACITY, rejectedExecutionHandler, prefix);
    }

    public static ThreadPoolExecutor newNameThreadPool(int maxSize, String prefix) {
        return newNameThreadPool(maxSize, DEFAULT_CAPACITY, rejectedExecutionHandler, prefix);
    }

    public static ThreadPoolExecutor newNameThreadPool(int maxSize, int queueSize, String prefix) {
        return newNameThreadPool(maxSize, queueSize, rejectedExecutionHandler, prefix);
    }

    public static ThreadPoolExecutor newNameThreadPool(int maxSize, RejectedExecutionHandler rejectedExecutionHandler, String prefix) {
        return newNameThreadPool(maxSize, DEFAULT_CAPACITY, rejectedExecutionHandler, prefix);
    }

    public static ThreadPoolExecutor newNameThreadPool(int maxSize, int queueSize, RejectedExecutionHandler rejectedExecutionHandler, String prefix) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(queueSize);
        ThreadFactory threadFactory = new NamedThreadFactory(prefix);
        int maxPoolSize = maxSize > DEFAULT_MAX_POOL_SIZE ? maxSize : DEFAULT_MAX_POOL_SIZE;
        return new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, maxPoolSize,
                DEFAULT_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, workQueue, threadFactory, rejectedExecutionHandler);
    }


}
