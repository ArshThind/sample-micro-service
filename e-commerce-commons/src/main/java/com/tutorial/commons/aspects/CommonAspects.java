package com.tutorial.commons.aspects;

import com.tutorial.commons.annotations.DaoProfiler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class CommonAspects {

    @Around("@annotation(com.tutorial.commons.annotations.DaoProfiler)")
    public Object logQueryExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DaoProfiler profiler = signature.getMethod().getAnnotation(DaoProfiler.class);
        log.warn("Query Started: {}", profiler.queryName());
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;
        log.warn("Query Finished: {}, Runtime: {} ms", profiler.queryName(), executionTime);
        return result;
    }
}
