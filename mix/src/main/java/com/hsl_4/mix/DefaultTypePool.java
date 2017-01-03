package com.hsl_4.mix;


import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

/**
 * 创建人: 霍述雷
 * 时 间:2016/12/28 10:32.
 */

class DefaultTypePool implements TypePool {
    private ArrayMap<Class<?>, ItemViewProvider> map;

    DefaultTypePool() {
        map = new ArrayMap<>();
    }

    @Override
    public void inject(@NonNull Class<?> clazz, @NonNull ItemViewProvider provider) {
        map.put(clazz, provider);
    }

    @Override
    public int indexOf(@NonNull Class<?> clazz) {
        int index = map.indexOfKey(clazz);
        if (index >= 0) return index;
        for (int i = 0; i < map.size(); i++) {
            if (map.keyAt(i).isAssignableFrom(clazz)) return i;
        }
        return index;
    }

    @Override
    public ItemViewProvider getProvider(int index) {
        return map.valueAt(index);
    }

    @Override
    public ItemViewProvider getProvider(Class<?> clazz) {
        ItemViewProvider provider = map.get(clazz);
        if (provider != null) return provider;
        for (Class<?> c : map.keySet()) {
            if (c.isAssignableFrom(clazz)) return map.get(c);
        }
        throw new ClassCastException("数据类型注册错误!请检查 数据视图关系是否注册");
    }
}
