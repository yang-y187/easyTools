package com.example.easycode.aop;

import com.alibaba.fastjson.JSON;
import com.example.easycode.Interface.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangyangyang
 * @create 2023-12-03 9:24 PM
 */
@Aspect
@Component
@Slf4j
public class LogAop {
    /**
     * 模拟动态配置
     */
    // 此时可以判断是否进行 动态配置中，是否有指定的key 例如 log.{className}.{methodName}
    private static final HashMap<String, Boolean> map = new HashMap<>();

    public LogAop() {
        map.put("log.helloController.sayHello", true);
        map.put("log.helloService.sayHello", true);
        map.put("log.helloService.sayHello.return", true);
        map.put("我是key", true);
        map.put("我是key.return", true);
    }

    @Pointcut("@annotation(com.example.logaop.demos.Interface.Log)||@within(com.example.logaop.demos.Interface.Log)")
    private void logPointCut() {
    }

    /**
     * 打印方法入参
     *
     * @param joinPoint 切入点
     */
    @Before("logPointCut()")
    public void logParams(JoinPoint joinPoint) throws ClassNotFoundException {
        if (joinPoint.getArgs() == null) {
            return;
        }

        String key = this.getKey(joinPoint);
        String vagueKey = this.getVagueKey(joinPoint);
        if (map.getOrDefault(key, false) || map.getOrDefault(vagueKey, false)) {
            log.info(key + ": " + JSON.toJSONString(joinPoint.getArgs()));
        }
    }

    /**
     * 打印返回值参数
     *
     * @param returnVal 结果
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "returnVal")
    public void logReturn(JoinPoint joinPoint, Object returnVal) throws ClassNotFoundException {
        if (returnVal == null) {
            return;
        }
        String key = this.getKey(joinPoint);
        key += ".return";

        String vagueKey = this.getVagueKey(joinPoint);
        vagueKey += ".return";
        if (map.getOrDefault(key, false) ||map.getOrDefault(vagueKey, false) ) {
            log.info(key + ": " + JSON.toJSONString(returnVal));
        }
    }


    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logThrow(JoinPoint joinPoint, Throwable e) throws ClassNotFoundException {
        if (e == null) {
            return;
        }
        String key = this.getKey(joinPoint);

        String beforeKey = key;
        String throwKey = key + ".throw";
        String returnKey = key + ".return";
        String printLog = null;
        if (map.getOrDefault(key, false)) {
            printLog = Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("");
            log.error(beforeKey + ": {}", printLog, e);
        } else if ( map.getOrDefault(key, false)) {
            printLog = Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("");
            log.error(throwKey + ": {}", printLog, e);
        } else if (map.getOrDefault(key, false)) {
            printLog = Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("");
            log.error(returnKey + ": {}", printLog, e);
        } else {
            printLog = Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("");
            log.error(key + ": {}", printLog, e);
        }
    }

    /**
     * 获取日志：key 方便日志搜索
     */
    private String getKey(JoinPoint joinPoint) throws ClassNotFoundException {
        // 获取类名
        String classSimpleName = joinPoint.getTarget().getClass().getSimpleName();
        // 获取全类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();

        // 获取目标类的注解
        Log annotation = Class.forName(className).getAnnotation(Log.class);
        boolean isClassAnnotation = Objects.nonNull(annotation);
        // 若注解修饰的是方法，则获取目标方法的注解
        if (!isClassAnnotation) {
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            annotation = method.getAnnotation(Log.class);
        }
        if (Objects.isNull(annotation)) {
            return null;
        }
        // 获取Log接口里的LogKey参数
        String logKey = annotation.logKey();
        if (StringUtils.hasText(logKey)) {
            return isClassAnnotation ? logKey + "." + methodName : logKey;
        }
        return "log." + classSimpleName + "." + methodName;
    }

    /**
     * 获取模糊日志：key 方便日志搜索
     */
    private String getVagueKey(JoinPoint joinPoint) {
        // 获取类名
        String classSimpleName = joinPoint.getTarget().getClass().getSimpleName();
        return "log." + classSimpleName + ".*";
    }
}
