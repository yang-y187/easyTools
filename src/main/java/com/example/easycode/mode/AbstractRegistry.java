package com.example.easycode.mode;

import com.example.easycode.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyangyang
 * @Description: 通用注册器抽象
 * @date 2026-03-28 12:30
 */
public abstract class AbstractRegistry<K, V> {

    /**
     * 注册器集合
     */
    private final Map<K, V> registryMap = new ConcurrentHashMap<>();

    /**
     * 注册对象
     *
     * @param key   注册key
     * @param value 注册值
     */
    public void register(K key, V value) {
        AssertUtil.notNull(key, "registry key can not be null");
        AssertUtil.notNull(value, "registry value can not be null");
        registryMap.put(key, value);
    }

    /**
     * 批量注册
     *
     * @param valueMap 注册map
     */
    public void registerAll(Map<K, V> valueMap) {
        if (valueMap == null || valueMap.isEmpty()) {
            return;
        }
        for (Map.Entry<K, V> entry : valueMap.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取注册值
     *
     * @param key 注册key
     * @return 注册值
     */
    public V get(K key) {
        return registryMap.get(key);
    }

    /**
     * 获取注册值，不存在则抛出异常
     *
     * @param key 注册key
     * @return 注册值
     */
    public V getRequired(K key) {
        V value = get(key);
        if (value == null) {
            throw new IllegalArgumentException("registry value not found, key=" + key);
        }
        return value;
    }

    /**
     * 是否存在
     *
     * @param key 注册key
     * @return true/false
     */
    public boolean contains(K key) {
        return registryMap.containsKey(key);
    }

    /**
     * 删除注册值
     *
     * @param key 注册key
     * @return 删除的值
     */
    public V remove(K key) {
        return registryMap.remove(key);
    }

    /**
     * 返回注册key集合
     *
     * @return key集合
     */
    public Set<K> keys() {
        return new LinkedHashSet<>(registryMap.keySet());
    }

    /**
     * 返回注册值集合
     *
     * @return value集合
     */
    public Collection<V> values() {
        return new ArrayList<>(registryMap.values());
    }

    /**
     * 当前注册数量
     *
     * @return size
     */
    public int size() {
        return registryMap.size();
    }
}
