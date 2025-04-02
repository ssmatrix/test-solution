package com.example.time.logger.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Value("${query-time.max-execution-time}")
    private long executionTime;

    @Around("execution(* org.springframework.data.jpa.repository.JpaRepository+.*(..))")
    public Object executionTimeMeasurement(ProceedingJoinPoint joinPoint) throws Throwable {
        var startTime = System.currentTimeMillis();
        var result = joinPoint.proceed();

        var totalTime = System.currentTimeMillis() - startTime;
        if (totalTime > executionTime) {
            log.warn("Request from method: " + joinPoint.getSignature() + " took a little longer than usual: " + totalTime + " ms.");
        }

        return result;
    }
}