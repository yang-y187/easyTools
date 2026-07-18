package com.example.easycode.mode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HandlerChainTests {

    @Test
    void shouldHandleBySupportedHandlersInOrder() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler(new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return context > 0;
            }

            @Override
            public String handle(String source, Integer context) {
                return source + "-first";
            }
        });
        chain.addHandler(new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return context > 1;
            }

            @Override
            public String handle(String source, Integer context) {
                return source + "-second";
            }
        });

        Assertions.assertEquals("start-first-second", chain.handle("start", 2));
    }

    @Test
    void shouldExecuteSingleHandlerByName() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler("first", append("-first"));
        chain.addHandler("second", append("-second"));

        Assertions.assertEquals("start-second", chain.handleOne("second", "start", 1));
    }

    @Test
    void shouldSupportFunctionalHandler() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler("lambda", (source, context) -> source + context);

        Assertions.assertEquals("start1", chain.handle("start", 1));
    }

    @Test
    void shouldContinueFromSpecifiedHandler() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler("first", append("-first"));
        chain.addHandler("second", append("-second"));
        chain.addHandler("third", append("-third"));

        Assertions.assertEquals("start-second-third", chain.handleFrom("second", "start", 1));
        Assertions.assertEquals("start-third", chain.handleAfter("second", "start", 1));
    }

    @Test
    void shouldAllowHandlerToSkipCurrentStepAndEnterNext() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler("skip", new ChainAroundHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return true;
            }

            @Override
            public String handle(String source, Integer context, ChainInvoker<String, Integer> chain) {
                return chain.next(source);
            }
        });
        chain.addHandler("next", append("-next"));

        Assertions.assertEquals("start-next", chain.handle("start", 1));
    }

    @Test
    void shouldAllowHandlerToStopChain() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addHandler("stop", new ChainAroundHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return true;
            }

            @Override
            public String handle(String source, Integer context, ChainInvoker<String, Integer> chain) {
                return source + "-stopped";
            }
        });
        chain.addHandler("next", append("-next"));

        Assertions.assertEquals("start-stopped", chain.handle("start", 1));
    }

    @Test
    void shouldTriggerCallbacks() {
        List<String> events = new ArrayList<>();
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.addCallback(new ChainCallback<String, Integer>() {
            @Override
            public void beforeChain(String source, Integer context) {
                events.add("beforeChain");
            }

            @Override
            public void beforeHandler(String handlerName, ChainHandler<String, Integer> handler, String source, Integer context) {
                events.add("before:" + handlerName);
            }

            @Override
            public void afterHandler(String handlerName, ChainHandler<String, Integer> handler, String source, Integer context, String result) {
                events.add("after:" + handlerName + ":" + result);
            }

            @Override
            public void onSkip(String handlerName, ChainHandler<String, Integer> handler, String source, Integer context) {
                events.add("skip:" + handlerName);
            }

            @Override
            public void afterChain(String source, Integer context, String result) {
                events.add("afterChain:" + result);
            }
        });
        chain.addHandler("first", append("-first"));
        chain.addHandler("skip", new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return false;
            }

            @Override
            public String handle(String source, Integer context) {
                return source + "-skip";
            }
        });

        Assertions.assertEquals("start-first", chain.handle("start", 1));
        Assertions.assertEquals("beforeChain", events.get(0));
        Assertions.assertTrue(events.contains("before:first"));
        Assertions.assertTrue(events.contains("after:first:start-first"));
        Assertions.assertTrue(events.contains("skip:skip"));
        Assertions.assertTrue(events.contains("afterChain:start-first"));
    }

    @Test
    void shouldRecoverFromExceptionAndContinue() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.exceptionHandler((handlerName, handler, source, context, exception, invoker) -> invoker.next(source + "-recovered"));
        chain.addHandler("error", new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return true;
            }

            @Override
            public String handle(String source, Integer context) {
                throw new IllegalStateException("broken");
            }
        });
        chain.addHandler("next", append("-next"));

        Assertions.assertEquals("start-recovered-next", chain.handle("start", 1));
    }

    @Test
    void shouldRecoverWhenSupportThrowsException() {
        HandlerChain<String, Integer> chain = new HandlerChain<>();
        chain.exceptionHandler((handlerName, handler, source, context, exception, invoker) -> invoker.next(source + "-support-recovered"));
        chain.addHandler("error-support", new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                throw new IllegalStateException("support broken");
            }

            @Override
            public String handle(String source, Integer context) {
                return source + "-unused";
            }
        });
        chain.addHandler("next", append("-next"));

        Assertions.assertEquals("start-support-recovered-next", chain.handle("start", 1));
    }

    private ChainHandler<String, Integer> append(String value) {
        return new ChainHandler<String, Integer>() {
            @Override
            public boolean support(Integer context) {
                return true;
            }

            @Override
            public String handle(String source, Integer context) {
                return source + value;
            }
        };
    }
}
