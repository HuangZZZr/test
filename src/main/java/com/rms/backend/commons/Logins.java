package com.rms.backend.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logins {

    //操作的模块
    String model() default "";
    //操作的类型
    Operation operation() default Operation.OTHER;

}

