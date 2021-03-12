package com.example.demo.common.config;

import com.example.demo.mapper.DBMapper;
import com.example.demo.utils.AppConfigUtil;
import com.example.demo.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationInit implements ApplicationRunner {

    @Value("${swagger.enable:false}")
    private boolean enableSwagger;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String port;

    @Autowired
    DBMapper dbMapper;

    @Override
    public void run(ApplicationArguments args) {
        log.info("==================================================");
        log.info("| 项目启动初始化");
        log.info("| 当前项目环境为：{}", AppConfigUtil.getActiveProfile());

        if (AppConfigUtil.isDevEnv()) {
            if (enableSwagger) {
                log.info("| Swagger API http://"
                        + WebUtil.getServerIP()
                        + ":"
                        + port
                        + contextPath
                        + "/swagger-ui.html#/");
            }
        } else if (AppConfigUtil.isTestEnv()) {
            log.info("| 启动数据库心跳");
            dbHeartbeat();
        } else if (AppConfigUtil.isProdEnv()) {
            log.info("| 启动数据库心跳");
            dbHeartbeat();
        }
        log.info("| 项目启动成功");
        log.info("==================================================");
    }

    @Async
    public void dbHeartbeat() {
        while (true) {
            try {
                dbMapper.select();
                if (log.isDebugEnabled()) {
                    log.debug("数据库心跳连接");
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException interruptedException) {
                    log.error("", interruptedException);
                }
            }
        }
    }
}
