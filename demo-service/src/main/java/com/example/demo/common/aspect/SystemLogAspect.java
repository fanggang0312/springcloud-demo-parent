package com.example.demo.common.aspect;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.annotation.SystemLog;
import com.example.demo.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 系统日志注解切面
 *
 * @author fg
 * @date 2020/11/5
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

    private static final String IP_LOCAL = "0:0:0:0:0:0:0:1";
    private static final String LOGIN_SYSTEM = "登录系统";

    @Resource
    private ISysLogService sysLogService;

    /**
     * 注解切面
     */
    @Pointcut("@annotation(com.example.demo.common.annotation.SystemLog)")
    public void sysLog() {
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Around("sysLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        SystemLog systemLog = getDeclaredAnnotation(joinPoint);
        log.info("/￣\\============@AnnoSysLogAspect --》 method name " + methodName + " args " + Arrays.toString(joinPoint.getArgs()));
        // 执行源方法
        Object proceed = joinPoint.proceed();
        // 模拟进行验证
        log.info("\\__/============@AnnoSysLogAspect==  logger --》 " + systemLog.value() + " auth success");
        //操作
        String optDesc = systemLog.value();
        //操作
        com.example.demo.model.SysLog sysLogAdd = new com.example.demo.model.SysLog();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getLocalAddr();
        //设置ip地址
        if (ip.contains(IP_LOCAL)) {
            ip = "127.0.0.1";
        } else {
            ip = request.getRemoteAddr();
        }
        sysLogAdd.setIp(ip);
        sysLogAdd.setContent(optDesc);
        sysLogAdd.setCreateTime(new Date());
        //记录系统日志
        if (LOGIN_SYSTEM.equals(optDesc)) {
            //登录日志
            String account = (String) params[0];
            sysLogAdd.setAccount(account);
            sysLogAdd.setType("登录日志");
        } else {
            //操作日志
            //todo 获取账号
            String accountOpt = "";
            sysLogAdd.setAccount(accountOpt);
            sysLogAdd.setParam(JSON.toJSONString(params));
            sysLogAdd.setType("操作日志");
        }
        this.sysLogService.insertSysLog(sysLogAdd);
        return proceed;
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public SystemLog getDeclaredAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        SystemLog annotation = objMethod.getDeclaredAnnotation(SystemLog.class);
        // 返回
        return annotation;
    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing(pointcut = "sysLog()")
    public void doAfterThrowing(JoinPoint joinPoint) {
        log.info("\\__/================================ " + joinPoint.getSignature().getName() + " doAfterThrowing =====================================");
    }

}

