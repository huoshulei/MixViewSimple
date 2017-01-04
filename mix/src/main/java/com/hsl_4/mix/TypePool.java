package com.hsl_4.mix;

import android.support.annotation.NonNull;

/**
 * 创建人: 霍述雷
 * 时 间:2017/1/3 8:49.
 */
public interface TypePool {
    void inject(@NonNull Class<?> clazz, @NonNull ItemViewProvider provider);

    int indexOf(@NonNull Class<?> clazz);

    ItemViewProvider getProvider(int index);

    ItemViewProvider getProvider(Class<?> clazz);
}
