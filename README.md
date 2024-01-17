

# 介绍

当前包旨在帮助简化使用，提供实用工具

# 使用

## 引入包

```xml
    <groupId>easy.code.tool</groupId>
    <artifactId>easyCode</artifactId>
    <version>1.0-SNAPSHOT</version>
```

开启该easyTools的功能

- 在Spring启动类中添加注解 @EnableEasyTools
-

## @Log注解的使用

可修饰类和方法 并且可以指定key，在方法维度

修饰后，可在动态配置中配置就可打印出入参日志和抛出异常异常日志
log.${ClassName}.${MethodName}          打印方法入参 入参前缀即log.${ClassName}.${MethodName}
log.${ClassName}.${MethodName}.return   打印方法出参，打印前缀即 log.${ClassName}.${MethodName}.return
log.${ClassName}.${MethodName}.throw    打印方法异常信息，打印前缀即 log.${ClassName}.${MethodName}.throw
${keyName} 若指定注解key，则打印时，日志前缀为 ${keyName}.${MethodName} 若是注解修饰的方法，打印日志为${keyName}

注意：只能打印对象的public方法，不能打印static或者非public的方法
@Log注解 若是修饰类，并且指定了key，开关开启的情况下，则该类的所有方法入参都会打印，出参同理

动态配置 value为true和false。默认为false，即不打印出入参日志