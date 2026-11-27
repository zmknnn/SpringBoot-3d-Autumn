
package org.example.individual12.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(org.example.individual12.annotation.LogExecution)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();

        long startTime = System.nanoTime();

        log.info("Виклик методу: {}", methodName);

        try {
            Object result = joinPoint.proceed();

            long duration = (System.nanoTime() - startTime) / 1_000_000;

            log.info("Метод {} завершився успішно", methodName);
            log.info("Час виконання: {} ms", duration);

            return result;

        } catch (Throwable exception) {
            log.error("Помилка під час виконання методу {}: {}",
                    methodName, exception.getMessage());

            throw exception;
        }
    }
}
