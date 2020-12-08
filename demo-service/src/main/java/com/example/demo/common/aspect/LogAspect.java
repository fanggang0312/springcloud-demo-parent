package com.example.demo.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 接口方法切面
 *
 * @Author: fg
 * @Date: 2020/11/5
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAspect {

    /**
     * Controller 类切面
     */
    @Pointcut("execution(public * com.example.demo.controller.*.*(..))")
    public void log() {
    }

    /**
     * 前置通知：在目标方法执行前调用
     */
    @Before(value = "log()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("/￣\\================================ " + joinPoint.getSignature().getName() + " doBefore ===================================");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String classAndMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info("| 1 【url】：" + url + ",【method】:" + method);
        log.info("| 2 【目标方法】：" + classAndMethod);
        log.info("| 3 【参数】：{}", Arrays.toString(joinPoint.getArgs()));

    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning(returning = "returnValue", pointcut = "log()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnValue) {
        String classAndMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        log.info("| 4 【返回值】:" + String.valueOf(returnValue));
        log.info("\\__/================================ " + joinPoint.getSignature().getName() + " End =====================================");
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing(pointcut = "log()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        log.info("\\__/================================ " + joinPoint.getSignature().getName() + " doAfterThrowing =====================================");
    }


}

