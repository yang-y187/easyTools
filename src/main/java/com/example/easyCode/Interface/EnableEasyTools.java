package com.example.easyCode.Interface;

import com.example.easyCode.Aop.LogAop;
import com.example.easyCode.config.DynamicConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangyangyang137
 * @Description: easyTools 工具包 配置开关
 * @date 2024-01-05 20:17
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DynamicConfig.class, LogAop.class})
public @interface EnableEasyTools {

}
