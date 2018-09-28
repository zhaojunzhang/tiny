package com.tiny.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 使用使用当前线程继续执行，堵塞
 * @author  by zhangzhaojun on 2017/12/28.
 */
public class CallerRunsExecutionHandler implements RejectedExecutionHandler {
    private final Logger log = LoggerFactory.getLogger(CallerRunsExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("callerRuns, max queue size {}, max thread pool size {}", executor.getQueue().size(), executor.getMaximumPoolSize());
        if (!executor.isShutdown()) {
            r.run();
        }
    }
}
