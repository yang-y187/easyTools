package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 责任链异常处理器
 * @date 2026-03-28 12:37
 */
public interface ChainExceptionHandler<T, C> {

    /**
     * 处理节点异常。
     *
     * 可以直接返回值结束链路，也可以调用 chain.next(recoveredValue) 从下一个节点继续。
     *
     * @param handlerName 处理器名称
     * @param handler     当前处理器
     * @param source      当前数据
     * @param context     上下文
     * @param exception   处理器异常
     * @param chain       后续处理器调用入口
     * @return 处理结果
     */
    T handle(String handlerName, ChainHandler<T, C> handler, T source, C context, RuntimeException exception, ChainInvoker<T, C> chain);
}

