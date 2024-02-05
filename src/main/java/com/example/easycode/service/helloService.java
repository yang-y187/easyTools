package com.example.easycode.service;

import com.example.easycode.Interface.Log;
import org.springframework.stereotype.Service;

/**
 * @author wangyangyang
 * @create 2023-11-26 1:05 AM
 */

@Service
@Log(logKey = "我是key")
public class helloService {
    public String sayHello(Integer a, Integer b) {
        return "hello" + a+b;
    }
}
