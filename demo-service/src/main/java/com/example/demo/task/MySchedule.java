package com.example.demo.task;

import com.example.demo.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * 定时任务
 *
 * @Author: fg
 * @Date: 2020/11/09
 */
@Slf4j
@Configuration
@EnableScheduling
public class MySchedule {

    @Scheduled(cron = "0 0 2 * * *")
    private void mySchedule() {
        log.info("======> schedule start..." + StrUtil.date2string(new Date()) + "==================");
        MyThreadPool.getInstance().executeTask(new MyTask());
    }
}
