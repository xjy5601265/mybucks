package com.spring.mybucks.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Component
@Slf4j
@Aspect
public class PerformanceAspect {
    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable{
        long startTime = System.currentTimeMillis();
        String name = "-";
        String result = "Y";
        try{
            name = pjp.getSignature().toShortString();
            log.info("{};开始执行;开始时间:{};参数:{}",name,startTime,pjp.getArgs().toString());
            return pjp.proceed();
        }catch (Throwable e){
            result = "N";
            throw e;
        }finally{
            long endTime = System.currentTimeMillis();
            log.info("{};{};{}ms",name,result,endTime - startTime);
        }
    }

    @Pointcut("execution(* com.spring.mybucks.service..*(..))")
    private void repositoryOps(){
    }
}
