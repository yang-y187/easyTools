package com.example.easycode.mode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StrategyExecutorTests {

    @Test
    void shouldExecuteRegisteredStrategy() {
        StrategyExecutor<String, Integer, Integer> executor = new StrategyExecutor<>();
        executor.registerFunction("double", value -> value * 2);

        Assertions.assertEquals(Integer.valueOf(6), executor.execute("double", 3));
    }

    @Test
    void shouldUseDefaultStrategyWhenMissing() {
        StrategyExecutor<String, Integer, Integer> executor = new StrategyExecutor<>();

        Assertions.assertEquals(Integer.valueOf(4), executor.executeOrDefault("unknown", 3, value -> value + 1));
    }
}

