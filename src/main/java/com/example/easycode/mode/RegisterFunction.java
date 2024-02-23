package com.example.easycode.mode;


import java.util.HashMap;
import java.util.function.Function;


/**
 * @author wangyangyang137
 * @Description: 注册器模式
 * @date 2024-02-23 15:10
 */
public abstract class RegisterFunction<L, M, R> {

    /**
     * 注册器集合
     */
    private final HashMap<L, Function<M, R>> FUNCTION_MAP = new HashMap<>();

    /**
     * 注册字段对应的处理函数
     *
     * @param key          字段key
     * @param dealFunction 处理函数
     * this.registerFunction("1",(t)-> t.toString());
     */
    public void registerFunction(L key, Function<M, R> dealFunction) {
        FUNCTION_MAP.put(key, dealFunction);
    }

    /**
     * 应用指定key对应的函数来处理输入
     *
     * @param key   用于查找函数的键
     * @param input 输入参数
     * @return 函数处理后的响应
     */
    public R applyFunction(L key, M input) {
        Function<M, R> f = FUNCTION_MAP.get(key);
        if (f != null) {
            return f.apply(input);
        }
        return null;
    }

}
