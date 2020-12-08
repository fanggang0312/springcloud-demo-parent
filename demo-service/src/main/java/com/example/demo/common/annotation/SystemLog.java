package com.example.demo.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @Author: fg
 * @Date: 2020/11/5
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    /**
     * 描述
     *
     * @return
     */
    String value();

}
