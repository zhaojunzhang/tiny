package com.tiny.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认拒绝执行
 * @author  by zhangzhaojun on 2017/12/28.
 */
public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {

    private final Logger log = LoggerFactory.getLogger(DefaultRejectedExecutionHandler.class);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("rejected execution, max queue size {}, max thread pool size {},task size {}",
                executor.getQueue().size(), executor.getMaximumPoolSize(), executor.getTaskCount());
    }
}