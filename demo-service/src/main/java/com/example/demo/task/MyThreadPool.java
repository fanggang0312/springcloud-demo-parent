package com.example.demo.task;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 *
 * @Author: fg
 * @Date: 2020/11/09
 */
@Slf4j
public class MyThreadPool {

    /**
     * 单例模式
     */
    private static final MyThreadPool instance = new MyThreadPool();

    public static MyThreadPool getInstance() {
        return instance;
    }

    private final ExecutorService executorService;

    /**
     * 自定义线程池参数设置，目前支持并发处理1000多个监控任务
     * <p>
     * io密集型任务（2次数据库操作和一次远程接口调用）
     * 设置核心线程数 = cpu核数 * 2 （参考核心线程数 = CPU核数 / （1-阻塞系数）  ）
     */
    private MyThreadPool() {
        int corePoreSize = Runtime.getRuntime().availableProcessors() * 3;
        this.executorService = new ThreadPoolExecutor(
                corePoreSize, 256, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1024),
                new ThreadPoolExecutor.AbortPolicy());

        log.info("=====> MonitorThreadPool created..");
    }


    /**
     * 执行任务，将任务提交给线程池去执行
     *
     * @param task 任务
     */
    public void

    executeTask(Runnable task) {
        if (executorService != null) {
            // Future future = executorService.submit(task);
            executorService.execute(task);
        }
    }

    /**
     * 关闭，在系统关闭时调用
     */
    public void shutdown() {
        log.info("MonitorThreadPool are shutting down.");
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    /**
     * 获取线程池状态
     *
     * @return 线程池描述字符串
     */
    public String getStatus() {
        if (executorService != null) {
            ThreadPoolExecutor tpe = ((ThreadPoolExecutor) executorService);

            return "Queue size:" + tpe.getQueue().size() +
                    ", active thread:" + tpe.getActiveCount() +
                    ", completed count:" + tpe.getCompletedTaskCount() +
                    ", total:" + tpe.getTaskCount();
        }
        return "Thread pool is shutdown.";
    }

}
