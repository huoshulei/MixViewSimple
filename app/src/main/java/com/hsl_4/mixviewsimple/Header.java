package com.hsl_4.mixviewsimple;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsl_4.mix.BaseViewHolder;
import com.hsl_4.mix.ItemViewProvider;

/**
 * Created by hsl_4 on 2016/12/29.
 */

public class Header implements ItemViewProvider<HeaderBean, BaseViewHolder> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, LayoutInflater inflater) {
        TextView itemView = new TextView(parent.getContext());
        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        itemView.setBackgroundColor(0x550055);
        itemView.setPadding(20, 20, 20, 20);
        itemView.setGravity(Gravity.CENTER);
        return BaseViewHolder.onCreate(itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, HeaderBean item) {
        ((TextView) holder.itemView).setText(item.string + "Header");
    }

}
