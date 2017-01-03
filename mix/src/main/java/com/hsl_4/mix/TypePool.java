package com.hsl_4.mix;

import android.support.annotation.NonNull;

/**
 * Created by hsl_4 on 2017/1/3.
 */
public interface TypePool {
    void inject(@NonNull Class<?> clazz, @NonNull ItemViewProvider provider);

    int indexOf(@NonNull Class<?> clazz);

    ItemViewProvider getProvider(int index);

    ItemViewProvider getProvider(Class<?> clazz);
}
