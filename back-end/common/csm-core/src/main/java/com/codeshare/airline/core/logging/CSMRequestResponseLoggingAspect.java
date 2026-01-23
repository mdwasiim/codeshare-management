/*
package com.codeshare.airline.common.services.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class RequestResponseLoggingAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("REQUEST {} args={}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));


        Object result = joinPoint.proceed();


        log.info("RESPONSE {} result={} time={}ms", joinPoint.getSignature(), result, System.currentTimeMillis() - start);
        return result;
    }

}
*/
