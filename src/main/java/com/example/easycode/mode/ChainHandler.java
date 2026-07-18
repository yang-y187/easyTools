package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 责任链处理器
 * @date 2026-03-28 12:33
 */
@FunctionalInterface
public interface ChainHandler<T, C> {

    /**
     * 处理器名称，用于单独执行或从指定节点继续执行。
     *
     * @return 处理器名称
     */
    default String name() {
        String simpleName = getClass().getSimpleName();
        return simpleName == null || simpleName.length() == 0 ? getClass().getName() : simpleName;
    }

    /**
     * 当前处理器是否支持当前上下文
     *
     * @param context 上下文
     * @return true 支持 false 不支持
     */
    default boolean support(C context) {
        return true;
    }

    /**
     * 处理数据
     *
     * @param source  原始数据
     * @param context 上下文
     * @return 处理结果
     */
    T handle(T source, C context);

    /**
     * 高级处理入口。默认行为是执行当前处理器后进入下一个处理器。
     * 如果需要跳过当前处理、提前结束链路或包裹后续步骤，可以重写该方法。
     *
     * @param source  原始数据
     * @param context 上下文
     * @param chain   后续处理器调用入口
     * @return 处理结果
     */
    default T handle(T source, C context, ChainInvoker<T, C> chain) {
        return chain.next(handle(source, context));
    }
}
