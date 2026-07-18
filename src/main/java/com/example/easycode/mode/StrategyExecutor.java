package com.example.easycode.mode;

import com.example.easycode.util.AssertUtil;

import java.util.function.Function;

/**
 * @author wangyangyang
 * @Description: 策略执行器
 * @date 2026-03-28 12:31
 */
public class StrategyExecutor<K, T, R> extends RegisterFunction<K, T, R> {

    /**
     * 执行指定策略
     *
     * @param key   策略key
     * @param input 输入参数
     * @return 执行结果
     */
    public R execute(K key, T input) {
        return applyRequired(key, input);
    }

    /**
     * 若策略不存在，则执行默认策略
     *
     * @param key             策略key
     * @param input           输入参数
     * @param defaultStrategy 默认策略
     * @return 执行结果
     */
    public R executeOrDefault(K key, T input, Function<T, R> defaultStrategy) {
        AssertUtil.notNull(defaultStrategy, "defaultStrategy can not be null");
        if (containsFunction(key)) {
            return applyRequired(key, input);
        }
        return defaultStrategy.apply(input);
    }
}
