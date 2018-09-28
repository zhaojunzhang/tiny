package com.tiny.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 异步执行的服务
 * 需要参数一个Function参数
 * @author  by zhangzhaojun on 2017/12/28.
 */
public class AsynService<T> {
    private final static Logger logger = LoggerFactory.getLogger(AsynService.class);
    //默认队列大小
    private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();

    private Consumer consumer;
    private String name;
    private ThreadPoolExecutor threadPoolExecutor;

    volatile boolean runing = false;

    public AsynService(Consumer consumer, ThreadPoolExecutor threadPoolExecutor, String name) {
        this.name = name;
        this.consumer = consumer;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void run() {
        addShutdownHook();
        new Thread(() -> {
            Thread.currentThread().setName("AsynService-" + name);
            setRuning(true);
            asynRun();
        }).start();
    }

    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("关闭{}线程", name);
            setRuning(false);
            excutor();
            threadPoolExecutor.shutdown();
            try {
                if (threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPoolExecutor.shutdownNow();
                }
            } catch (Exception ex) {
                logger.error("关闭 {} 线程失败", name, ex);
            }
        }));
    }

    public void put(T object) {
        queue.offer(object);
    }

    public void asynRun() {
        while (isRuning()) {
            excutor();
        }

    }

    public void excutor() {
        do {
            try {
                final T object = queue.poll();
                if (null != object) {
                    threadPoolExecutor.execute(() -> consumer.accept(object));
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(20L);
                    } catch (InterruptedException ignored) {
                    }
                }
            } catch (Exception e) {
                logger.info("执行任务失败,name:{}", name, e);
            }
        } while (!queue.isEmpty());
    }

    public boolean isRuning() {
        return runing;
    }

    public void setRuning(boolean runing) {
        this.runing = runing;
    }

    public int getTaskCount() {
        return this.queue.size();
    }
}