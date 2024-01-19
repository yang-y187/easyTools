package com.example.easyCode.Service;

import com.example.easyCode.Interface.Log;
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
