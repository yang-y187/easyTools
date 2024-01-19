package com.example.easyCode.Controller;

import com.example.easyCode.Interface.Log;
import com.example.easyCode.Service.helloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyangyang
 * @create 2023-11-26 12:47 AM
 */

@Controller
@RestController
@Slf4j
public class helloController {
    @Autowired
    private helloService service;


    @GetMapping("/hello")
    @Log
    public String sayHello(Integer a, Integer b) {
        return service.sayHello(a, b);
    }
}
