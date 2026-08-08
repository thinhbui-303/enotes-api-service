package com.thinhbqt.enotes_api_service.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class LoggingAspect {
    @Around("Execution(* com.thinhbqt.enotes_api_service.controller..*(..))")
    public Object jointPointController(ProceedingJoinPoint joinPoint)throws Throwable{
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {}()",className,methodName);
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long duration = System.currentTimeMillis()-start;
		log.info("Exceution End :: {} :: {}() ::{} ms",className,methodName,duration);
		return result;

    }
    @Around("Execution(* com.thinhbqt.enotes_api_service.service..*(..))")
    public Object jointPointService(ProceedingJoinPoint joinPoint)throws Throwable{
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("Calling :: {} :: {}()",className,methodName);
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long duration = System.currentTimeMillis()-start;
		log.info("Exceution End :: {} :: {}() ::{} ms",className,methodName,duration);
		return result;

    }
}
