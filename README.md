# easyTools

`easyTools` 是一个面向 Java / Spring 业务项目的轻量级快速介入工具包。

它不追求覆盖所有“常用工具类”，而是聚焦三类高频场景：

- 运行期观测：用注解和 AOP 快速补充方法入参、返回值、异常日志。
- 业务扩展点：用注册器、策略执行器、转换器减少分支堆叠。
- 边界治理：统一接口结果、业务异常、参数校验、脱敏和 TraceId 辅助能力。

## 引入方式

```xml
<groupId>easy.code.tool</groupId>
<artifactId>easyCode</artifactId>
<version>1.0-SNAPSHOT</version>
```

## 启用方式

在 Spring Boot 启动类上添加：

```java
@EnableEasyTools
```

`@EnableEasyTools` 当前会导入动态配置入口和日志 AOP。

## 核心模块

### `com.example.easycode.Interface`

- `EnableEasyTools`：工具包开关。
- `Log`：日志注解，支持类级别和方法级别标记。

### `com.example.easycode.aop`

- `LogAop`：基于 `@Log` 的方法日志切面。

当前日志 key 规则：

- `log.${ClassName}.${MethodName}`：打印方法入参。
- `log.${ClassName}.${MethodName}.return`：打印方法返回值。
- `log.${ClassName}.${MethodName}.throw`：打印方法异常。
- `log.${ClassName}.*`：打印类下全部公共方法入参。
- `log.${ClassName}.*.return`：打印类下全部公共方法返回值。

### `com.example.easycode.convert`

- `Converter<S,T>`：转换器接口。
- `ConverterKey`：转换器注册 key。
- `FunctionConverter<S,T>`：函数式转换器实现。
- `DefaultConverterRegistry`：默认转换器注册器。

示例：

```java
DefaultConverterRegistry registry = new DefaultConverterRegistry();
registry.register(String.class, Integer.class, Integer::valueOf);

Integer value = registry.convert("100", Integer.class);
```

### `com.example.easycode.mode`

这一包只保留快速介入业务扩展点和边界模型，不再放纯设计模式示例。

- `AbstractRegistry<K,V>`：通用注册器底座。
- `RegisterFunction<L,M,R>`：函数注册器。
- `StrategyExecutor<K,T,R>`：策略执行器。
- `ChainHandler<T,C>`：处理链节点。
- `ChainAroundHandler<T,C>`：可控制是否进入后续节点的处理链节点。
- `ChainInvoker<T,C>`：后续节点调用入口。
- `ChainCallback<T,C>`：链路回调。
- `ChainExceptionHandler<T,C>`：链路异常处理器。
- `HandlerChain<T,C>`：支持顺序执行、单节点执行、跳转执行、回调和异常恢复的处理链。
- `FlowExecutor<T>`：支持关键字、日志开关、步骤入参/出参 JSON 日志的流式执行器，日志 key 规则和 `@Log` 保持一致。
- `ResultCode`：返回码抽象。
- `DefaultResultCode`：默认返回码。
- `Result<T>`：通用结果对象。
- `PageResult<T>`：分页结果对象。
- `BizException`：业务异常。

策略示例：

```java
StrategyExecutor<String, Integer, Integer> executor = new StrategyExecutor<>();
executor.registerFunction("double", value -> value * 2);

Integer result = executor.execute("double", 3);
```

处理链示例：

```java
HandlerChain<String, Integer> chain = new HandlerChain<>();
chain.addHandler(new ChainHandler<String, Integer>() {
    @Override
    public boolean support(Integer context) {
        return context > 0;
    }

    @Override
    public String handle(String source, Integer context) {
        return source + "-done";
    }
});

String value = chain.handle("start", 1);
```

高级处理链示例：

```java
HandlerChain<String, Integer> chain = new HandlerChain<>();

chain.addCallback(new ChainCallback<String, Integer>() {
    @Override
    public void beforeHandler(String handlerName, ChainHandler<String, Integer> handler, String source, Integer context) {
        // callback
    }
});

chain.exceptionHandler((handlerName, handler, source, context, exception, invoker) -> {
    return invoker.next(source + "-recovered");
});

chain.addHandler("skip-current", new ChainAroundHandler<String, Integer>() {
    @Override
    public boolean support(Integer context) {
        return true;
    }

    @Override
    public String handle(String source, Integer context, ChainInvoker<String, Integer> invoker) {
        return invoker.next(source);
    }
});

String onlyOne = chain.handleOne("skip-current", "start", 1);
String fromStep = chain.handleFrom("skip-current", "start", 1);
```

流式执行器示例：

```java
String result = FlowExecutor.start("create-order", true)
        .exec("load", () -> "order")
        .peek("validate", value -> ValidatorUtil.notBlank(value, "order can not be blank"))
        .run("audit", () -> System.out.println("audit"))
        .exec("build-result", value -> value + "-done")
        .get();
```

日志 key 规则：

- `${keyword}.${stepName}`：步骤入参。
- `${keyword}.${stepName}.return`：步骤出参。
- `${keyword}.${stepName}.throw`：步骤异常。

结果示例：

```java
Result<String> success = Result.success("ok");
PageResult<String> page = PageResult.success(1, 10, 2L, java.util.Arrays.asList("a", "b"));
```

### `com.example.easycode.util`

- `AssertUtil`：内部参数断言，失败时抛标准运行时异常。
- `DateUtil`：日期格式化、解析、开始/结束时间、日期差值。
- `MaskUtil`：字符串、手机号、邮箱、身份证脱敏。
- `TraceIdUtil`：线程内 TraceId 创建、获取、清理。
- `ValidatorUtil`：业务参数校验，失败时抛 `BizException`。
- `ClassCastUtil`：基础类型 cast。

示例：

```java
ValidatorUtil.notBlank(name, "name can not be blank");
String traceId = TraceIdUtil.getOrCreateTraceId();
String mobile = MaskUtil.maskMobile("13812345678");
```

## 当前边界

保留：

- AOP 日志和动态配置入口。
- 注册器、策略、转换器、处理链。
- 结果对象、业务异常、业务校验。
- TraceId、脱敏、日期等少量边界辅助工具。

不放入核心：

- Builder / Observer / Template Method 等纯设计模式示例。
- Spring `BeanUtils` 这类薄包装。
- `SpringContextUtil` 这类 service locator 式逃生口。
- IDE 配置、AI 本地配置、构建产物。

## 构建和测试

项目使用 Maven：

```bash
mvn test
```

当前仓库没有 Maven wrapper。如果本机没有安装 `mvn`，需要先准备 Maven 环境。
