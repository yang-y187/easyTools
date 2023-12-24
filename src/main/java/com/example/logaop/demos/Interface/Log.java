package com.example.logaop.demos.Interface;


import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.*;

/**
 * @author wangyangyang
 * @create 2023-11-26 12:49 AM
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Log {
    String logKey() default "";
}
