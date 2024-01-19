package com.example.easyCode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LogAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogAopApplication.class, args);
    }

}
