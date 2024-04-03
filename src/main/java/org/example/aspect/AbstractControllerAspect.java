package org.example.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public abstract class AbstractControllerAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Pointcut("execution(public * *.*(..))")
    protected void allPublicMethods() {
    }

    @Pointcut("execution(* get*(..)) && !@annotation(org.springframework.web.bind.annotation.GetMapping)")
    protected void getters() {
    }

    @Pointcut("execution(* set*(..))")
    protected void setters() {
    }

    @Pointcut("execution(* org.springdoc.webmvc.ui.SwaggerConfigResource.openapiJson(..)) || " +
            "execution(* org.springdoc.webmvc.api.OpenApiWebMvcResource.openapiJson(..))")
    protected void swaggerEndPoints(){}
}
