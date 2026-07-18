package com.example.easycode.mode;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangyangyang
 * @Description: 流式执行器
 * @date 2026-03-28 15:20
 */
public class FlowExecutor<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowExecutor.class);

    private final FlowConfig config;

    private final T value;

    private FlowExecutor(FlowConfig config, T value) {
        this.config = config;
        this.value = value;
    }

    /**
     * 创建无初始值执行流。
     *
     * @return 执行流
     */
    public static FlowExecutor<Void> start() {
        return start("", true);
    }

    /**
     * 创建无初始值执行流。
     *
     * @param keyword    日志关键字
     * @param logEnabled 是否打印日志
     * @return 执行流
     */
    public static FlowExecutor<Void> start(String keyword, boolean logEnabled) {
        return new FlowExecutor<>(new FlowConfig(keyword, logEnabled), null);
    }

    /**
     * 创建带初始值执行流。
     *
     * @param source     初始值
     * @param keyword    日志关键字
     * @param logEnabled 是否打印日志
     * @param <T>        初始值类型
     * @return 执行流
     */
    public static <T> FlowExecutor<T> start(T source, String keyword, boolean logEnabled) {
        return new FlowExecutor<>(new FlowConfig(keyword, logEnabled), source);
    }

    /**
     * 调整日志关键字。
     *
     * @param keyword 日志关键字
     * @return 新执行流
     */
    public FlowExecutor<T> keyword(String keyword) {
        return new FlowExecutor<>(config.keyword(keyword), value);
    }

    /**
     * 调整日志开关。
     *
     * @param enabled 是否打印日志
     * @return 新执行流
     */
    public FlowExecutor<T> log(boolean enabled) {
        return new FlowExecutor<>(config.log(enabled), value);
    }

    /**
     * 执行无入参、有返回值步骤。
     *
     * @param supplier 执行逻辑
     * @param <R>      返回值类型
     * @return 新执行流
     */
    public <R> FlowExecutor<R> exec(FlowSupplier<R> supplier) {
        return exec(null, supplier);
    }

    /**
     * 执行具名无入参、有返回值步骤。
     *
     * @param stepName 步骤名称
     * @param supplier 执行逻辑
     * @param <R>      返回值类型
     * @return 新执行流
     */
    public <R> FlowExecutor<R> exec(String stepName, FlowSupplier<R> supplier) {
        assertNotNull(supplier, "supplier can not be null");
        Step step = config.nextStep(stepName);
        logBefore(step, value);
        try {
            R result = supplier.get();
            logAfter(step, result);
            return new FlowExecutor<>(step.config, result);
        } catch (Exception e) {
            throw onException(step, value, e);
        }
    }

    /**
     * 执行有入参、有返回值步骤。
     *
     * @param function 执行逻辑
     * @param <R>      返回值类型
     * @return 新执行流
     */
    public <R> FlowExecutor<R> exec(FlowFunction<T, R> function) {
        return exec(null, function);
    }

    /**
     * 执行具名有入参、有返回值步骤。
     *
     * @param stepName 步骤名称
     * @param function 执行逻辑
     * @param <R>      返回值类型
     * @return 新执行流
     */
    public <R> FlowExecutor<R> exec(String stepName, FlowFunction<T, R> function) {
        assertNotNull(function, "function can not be null");
        Step step = config.nextStep(stepName);
        logBefore(step, value);
        try {
            R result = function.apply(value);
            logAfter(step, result);
            return new FlowExecutor<>(step.config, result);
        } catch (Exception e) {
            throw onException(step, value, e);
        }
    }

    /**
     * 消费当前值，不改变执行流结果。
     *
     * @param consumer 消费逻辑
     * @return 新执行流
     */
    public FlowExecutor<T> peek(FlowConsumer<T> consumer) {
        return peek(null, consumer);
    }

    /**
     * 具名消费当前值，不改变执行流结果。
     *
     * @param stepName 步骤名称
     * @param consumer 消费逻辑
     * @return 新执行流
     */
    public FlowExecutor<T> peek(String stepName, FlowConsumer<T> consumer) {
        assertNotNull(consumer, "consumer can not be null");
        Step step = config.nextStep(stepName);
        logBefore(step, value);
        try {
            consumer.accept(value);
            logAfter(step, value);
            return new FlowExecutor<>(step.config, value);
        } catch (Exception e) {
            throw onException(step, value, e);
        }
    }

    /**
     * 执行无入参、无返回值步骤，不改变执行流结果。
     *
     * @param runnable 执行逻辑
     * @return 新执行流
     */
    public FlowExecutor<T> run(FlowRunnable runnable) {
        return run(null, runnable);
    }

    /**
     * 执行具名无入参、无返回值步骤，不改变执行流结果。
     *
     * @param stepName 步骤名称
     * @param runnable 执行逻辑
     * @return 新执行流
     */
    public FlowExecutor<T> run(String stepName, FlowRunnable runnable) {
        assertNotNull(runnable, "runnable can not be null");
        Step step = config.nextStep(stepName);
        logBefore(step, value);
        try {
            runnable.run();
            logAfter(step, value);
            return new FlowExecutor<>(step.config, value);
        } catch (Exception e) {
            throw onException(step, value, e);
        }
    }

    /**
     * 获取最终结果。
     *
     * @return 最终结果
     */
    public T get() {
        return value;
    }

    private void logBefore(Step step, Object input) {
        logValue(step.logKey, input);
    }

    private void logAfter(Step step, Object output) {
        logValue(step.logKey + ".return", output);
    }

    private RuntimeException onException(Step step, Object input, Exception exception) {
        if (config.logEnabled) {
            LOGGER.error(step.logKey + ".throw: " + toJson(input), exception);
        }
        if (exception instanceof RuntimeException) {
            return (RuntimeException) exception;
        }
        return new IllegalStateException(step.errorMessage(), exception);
    }

    private void logValue(String logKey, Object value) {
        if (!config.logEnabled) {
            return;
        }
        LOGGER.info("{}: {}", logKey, toJson(value));
    }

    private static String toJson(Object value) {
        try {
            return JSON.toJSONString(value);
        } catch (RuntimeException e) {
            return JSON.toJSONString(String.valueOf(value));
        }
    }

    private static void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private static boolean hasText(String value) {
        return value != null && value.trim().length() > 0;
    }

    private static class FlowConfig {
        private final String keyword;
        private final boolean logEnabled;
        private final int stepIndex;

        private FlowConfig(String keyword, boolean logEnabled) {
            this(keyword, logEnabled, 0);
        }

        private FlowConfig(String keyword, boolean logEnabled, int stepIndex) {
            this.keyword = keyword;
            this.logEnabled = logEnabled;
            this.stepIndex = stepIndex;
        }

        private FlowConfig keyword(String keyword) {
            return new FlowConfig(keyword, logEnabled, stepIndex);
        }

        private FlowConfig log(boolean enabled) {
            return new FlowConfig(keyword, enabled, stepIndex);
        }

        private Step nextStep(String stepName) {
            int nextStepIndex = stepIndex + 1;
            String actualStepName = hasText(stepName) ? stepName : "step-" + nextStepIndex;
            String actualKeyword = hasText(keyword) ? keyword : "flow";
            FlowConfig nextConfig = new FlowConfig(keyword, logEnabled, nextStepIndex);
            return new Step(actualKeyword + "." + actualStepName, nextConfig);
        }
    }

    private static class Step {
        private final String logKey;
        private final FlowConfig config;

        private Step(String logKey, FlowConfig config) {
            this.logKey = logKey;
            this.config = config;
        }

        private String errorMessage() {
            return logKey + " flow step error";
        }
    }

    @FunctionalInterface
    public interface FlowSupplier<R> {
        R get() throws Exception;
    }

    @FunctionalInterface
    public interface FlowFunction<T, R> {
        R apply(T source) throws Exception;
    }

    @FunctionalInterface
    public interface FlowConsumer<T> {
        void accept(T source) throws Exception;
    }

    @FunctionalInterface
    public interface FlowRunnable {
        void run() throws Exception;
    }
}
