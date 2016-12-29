package com.hsl_4.mix;


import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

/**
 * 创建人: 霍述雷
 * 时 间:2016/12/28 10:32.
 */

class TypePool {
    private ArrayMap<Class<?>, ItemViewProvider> typePool;

    TypePool() {
        typePool = new ArrayMap<>();
    }

    void inject(@NonNull Class<?> clazz, @NonNull ItemViewProvider provider) {
        typePool.put(clazz, provider);
    }

    int indexOf(@NonNull Class<?> clazz) {
        int index = typePool.indexOfKey(clazz);
        if (index >= 0) return index;
        for (int i = 0; i < typePool.size(); i++) {
            if (typePool.keyAt(i).isAssignableFrom(clazz)) return i;
        }
        return index;
    }

    ItemViewProvider valueAt(int index) {
        return typePool.valueAt(index);
    }

    ItemViewProvider get(Class<?> key) {
        ItemViewProvider provider = typePool.get(key);
        if (provider != null) return provider;
        for (Class<?> clazz : typePool.keySet()) {
            if (clazz.isAssignableFrom(key)) return typePool.get(clazz);
        }
        throw new ClassCastException("数据类型注册错误!请检查 数据视图关系是否注册");
    }
}
