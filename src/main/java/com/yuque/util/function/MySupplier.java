package com.yuque.util.function;

import com.yuque.exception.YuqueException;

/**
 * @author 11029
 * @description
 * @since 2023/8/2 14:01:40
 */
@FunctionalInterface
public interface MySupplier<T> {
    T get() throws YuqueException;
}
