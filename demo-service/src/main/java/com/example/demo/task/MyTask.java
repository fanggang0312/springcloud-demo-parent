package com.example.demo.task;

import com.example.demo.utils.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 任务类
 *
 * @Author: fg
 * @Date: 2020/11/09
 */
@Slf4j
@Data
public class MyTask implements Runnable {

    @Override
    public void run() {
        log.info("========> Task start." + StrUtil.date2string(new Date()));
        Thread.currentThread().setName("-myThreadPool-thread-");
        //todo

    }
}
