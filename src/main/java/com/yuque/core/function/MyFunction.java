package com.yuque.core.function;

import com.yuque.exception.YuqueException;

import java.net.URISyntaxException;

/**
 * @author 11029
 * @description
 * @since 2023/8/1 23:14:18
 */
@FunctionalInterface
public interface MyFunction<T, R> {
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws YuqueException, URISyntaxException;
}
