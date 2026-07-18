package com.example.easycode.mode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class FlowExecutorTests {

    @Test
    void shouldExecuteSupplierAndFunctionsInOrder() {
        String result = FlowExecutor.start("case", true)
                .exec( () -> "easy")
                .exec(value -> value + "Tools")
                .get();

        Assertions.assertEquals("easyTools", result);
    }

    @Test
    void shouldPeekAndRunWithoutChangingResult() {
        AtomicReference<String> captured = new AtomicReference<>();
        AtomicInteger runCount = new AtomicInteger();

        String result = FlowExecutor.start("easy", "case", true)
                .peek("capture", captured::set)
                .run("count", runCount::incrementAndGet)
                .exec("append", value -> value + "Tools")
                .get();

        Assertions.assertEquals("easy", captured.get());
        Assertions.assertEquals(1, runCount.get());
        Assertions.assertEquals("easyTools", result);
    }

    @Test
    void shouldSupportRuntimeLogSwitch() {
        String result = FlowExecutor.start("case", true)
                .log(false)
                .exec("load", () -> "value")
                .get();

        Assertions.assertEquals("value", result);
    }

    @Test
    void shouldWrapCheckedException() {
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            FlowExecutor.start("case", false)
                    .exec("fail", () -> {
                        throw new Exception("checked");
                    })
                    .get();
        });

        Assertions.assertTrue(exception.getMessage().contains("case.fail"));
    }

    @Test
    void shouldRethrowRuntimeException() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            FlowExecutor.start("case", false)
                    .exec("fail", () -> {
                        throw new IllegalArgumentException("runtime");
                    })
                    .get();
        });

        Assertions.assertEquals("runtime", exception.getMessage());
    }

    @Test
    void shouldKeepDefaultStepIndexWhenReusingBranch() {
        FlowExecutor<String> base = FlowExecutor.start("case", false)
                .exec(() -> "easy");

        IllegalStateException left = Assertions.assertThrows(IllegalStateException.class, () -> {
            base.exec(value -> {
                throw new Exception("left");
            }).get();
        });
        IllegalStateException right = Assertions.assertThrows(IllegalStateException.class, () -> {
            base.exec(value -> {
                throw new Exception("right");
            }).get();
        });

        Assertions.assertTrue(left.getMessage().contains("case.step-2"));
        Assertions.assertTrue(right.getMessage().contains("case.step-2"));
    }

    @Test
    void shouldNotLeakKeywordChangeToOriginalBranch() {
        FlowExecutor<String> base = FlowExecutor.start("case", false)
                .exec(() -> "easy");

        IllegalStateException changed = Assertions.assertThrows(IllegalStateException.class, () -> {
            base.keyword("changed").exec(value -> {
                throw new Exception("changed");
            }).get();
        });
        IllegalStateException original = Assertions.assertThrows(IllegalStateException.class, () -> {
            base.exec(value -> {
                throw new Exception("original");
            }).get();
        });

        Assertions.assertTrue(changed.getMessage().contains("changed.step-2"));
        Assertions.assertTrue(original.getMessage().contains("case.step-2"));
    }

    @Test
    void shouldAdvanceStepIndexThroughPeekAndRun() {
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            FlowExecutor.start("case", false)
                    .exec(() -> "easy")
                    .peek(value -> {
                    })
                    .run(() -> {
                    })
                    .exec(value -> {
                        throw new Exception("checked");
                    })
                    .get();
        });

        Assertions.assertTrue(exception.getMessage().contains("case.step-4"));
    }
}

