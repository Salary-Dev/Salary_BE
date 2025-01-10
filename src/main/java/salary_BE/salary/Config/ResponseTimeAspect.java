package salary_BE.salary.Config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTimeAspect.class);

    @Around("execution(* salary_BE.salary.Controller.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        Object result = joinPoint.proceed();         // 실제 메서드 실행
        long endTime = System.currentTimeMillis();   // 종료 시간 기록

        logger.info("Method {} executed in {}ms", joinPoint.getSignature(), (endTime - startTime));

        return result;
    }
}

