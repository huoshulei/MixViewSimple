package com.hsl_4.mix;

/**
 * 创建人: 霍述雷
 * 时 间:2017/1/3 8:48.
 */
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
