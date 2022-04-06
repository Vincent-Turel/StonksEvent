package fr.stonksdev.backend.controllers;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
class LoggerController {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerController.class);
    private static final String PREFIX = "PolyEvent:Rest-Controller:";
    private static final String PREFIX_COMP = "PolyEvent:Components:";

    @Pointcut("execution(public * fr.stonksdev.backend.controllers..*(..))")
    private void allControllerMethods() {
    } // This enables to attach the pointcut to a method name we can reuse below

    @Before("allControllerMethods()")
    public void logMethodNameAndParametersAtEntry(JoinPoint joinPoint) {
        LOG.info(PREFIX + joinPoint.getThis() + ":Called {}", joinPoint.getSignature().getName() + " " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "allControllerMethods()", returning = "resultVal")
    public void logMethodReturningProperlyController(JoinPoint joinPoint, Object resultVal) {
        LOG.info(PREFIX + joinPoint.getThis() + ":Returned {}", joinPoint.getSignature().getName() + " with value " + resultVal);
    }

    @AfterThrowing(pointcut = "allControllerMethods()", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Exception exception) {
        LOG.warn(PREFIX + joinPoint.getThis() + ":Exception from {}", joinPoint.getSignature().getName() + " with exception " + exception);
    }

//    @AfterReturning(pointcut = "allCompponentMethods()", returning = "resultVal")
//    public void logMethodReturningProperlyComponent(JoinPoint joinPoint, Object resultVal) {
//        LOG.debug(PREFIX_COMP + joinPoint.getThis() + ":Returned {}", joinPoint.getSignature().getName() + " with value " + resultVal);
//    }

}