package com.hsl_4.mix;

/**
 * Created by hsl_4 on 2017/1/3.
 */
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
