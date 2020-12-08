package com.example.demo.feign.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * OKHTTP是目前比较火的一个HTTP客户端，具有以下优点：
 * <p>
 * 支持SPDY，可以合并多个到同一主机的请求。
 * 使用连接池技术减少请求延迟。
 * 使用GZIP压缩减少传输数据量。
 * 缓存响应避免重复请求。
 *
 * @Author: fg
 * @Date: 2020/11/19
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {
    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(60, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(60, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(60, TimeUnit.SECONDS)
                //是否自动重连
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool())
                //构建OkHttpClient对象
                .build();
    }
}
