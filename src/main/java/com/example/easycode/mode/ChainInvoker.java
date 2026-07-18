package com.example.easycode.mode;

/**
 * @author wangyangyang
 * @Description: 责任链后续步骤调用入口
 * @date 2026-03-28 12:35
 */
public interface ChainInvoker<T, C> {

    /**
     * 进入下一个处理器。
     *
     * @param source 当前数据
     * @return 后续处理结果
     */
    T next(T source);
}

