package com.hsl_4.mix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 创建人: 霍述雷
 * 时 间:2016/12/28 10:48.
 */

public interface ItemViewProvider<T,V extends RecyclerView.ViewHolder> {

    V onCreateViewHolder(ViewGroup parent, LayoutInflater inflater);

    void onBindViewHolder(V holder, T item);
}
