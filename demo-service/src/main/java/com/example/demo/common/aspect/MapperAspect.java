package com.example.demo.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * mapper切面
 *
 * @author fg
 * @date 2020/11/5
 */
@Aspect
@Component
@Slf4j
public class MapperAspect {

    private static final long NACO = 1000 * 1000;

    private static final long STAND = 500;

    /**
     * mapper切面
     */
    @Pointcut("execution(* com.example.demo.mapper.*Mapper.*(..))")
    public void mapper() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before(value = "mapper()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("Start execute: " + joinPoint);
    }

    /**
     * 后置通知：在目标方法执行后调用，若目标方法出现异常，则不执行
     */
    @AfterReturning(pointcut = "mapper()")
    public void logServiceAccess(JoinPoint joinPoint) {
        log.info("Completed execute: " + joinPoint);
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("mapper()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime();
        boolean slowFlag = false;
        String searchDesc = "正常";
        if (((end - begin) / NACO) > STAND) {
            slowFlag = true;
            searchDesc = "慢查询";
        }
        String logData =
                ""
                        + " \n ==> 调用Mapper方法：[" + pjp.getSignature().toString() + "]"
                        + " \n ==> 参数：[" + JSON.toJSONString(Arrays.toString(pjp.getArgs())) + "]"
                        + " \n ==> 结果：[" + JSON.toJSONString(obj) + "] "
                        + " \n ==> 耗时：[" + ((end - begin) / NACO) + "]毫秒"
                        + " \n ==> 运行状态：[" + searchDesc + "]" +
                        "";
        log.info(logData);
        // 插入到日志表中(分慢查询和快查询)
        return obj;
    }

}
