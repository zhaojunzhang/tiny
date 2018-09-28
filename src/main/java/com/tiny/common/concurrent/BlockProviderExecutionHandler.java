package com.tiny.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务丢进队列里等待执行呢
 * @author  by zhangzhaojun on 2017/12/28.
 */
public class BlockProviderExecutionHandler implements RejectedExecutionHandler {
    private final Logger log = LoggerFactory.getLogger(BlockProviderExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                log.error("BlockProvider error, queue size:{}",executor.getQueue().size(), e);
            }
        }
    }

}
