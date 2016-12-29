package com.hsl_4.mixviewsimple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsl_4.mix.ItemViewProvider;

/**
 * Created by hsl_4 on 2016/12/29.
 */

public class Simple implements ItemViewProvider<String, Simple.Holder> {
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, LayoutInflater inflater) {
        TextView itemView = new TextView(parent.getContext());
        itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        itemView.setBackgroundColor(0x555555);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, String item) {
        ((TextView) holder.itemView).setText(item+"simple");
    }

    class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
