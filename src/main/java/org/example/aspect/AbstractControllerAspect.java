package org.example.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class AbstractControllerAspect {

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controller() {
    }

    @Pointcut("execution(public * *.*(..))")
    protected void allPublicMethods() {
    }

    @Pointcut("execution(* get*(..))")
    protected void getters() {
    }

    @Pointcut("execution(* set*(..))")
    protected void setters() {
    }
}
