package io.grisu.pojo.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PropertyMethodAspect {
    @Pointcut("call(* *.*(..))")
    public void callAt(Property property) {
    }

    @Around("callAt(Property)")
    public Object around(ProceedingJoinPoint pjp,
                         Property property) throws Throwable {
        System.out.println("property = " + property.name());
        return pjp.proceed();
    }
}