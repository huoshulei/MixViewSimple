package com.hsl_4.mix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hsl_4 on 2016/12/28.
 */

class LoadView implements ItemViewProvider<Loading, LoadView.LoadViewHolder> {
    @Override
    public LoadViewHolder onCreateViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new LoadViewHolder(inflater.inflate(R.layout.loading_view, parent, false));
    }

    @Override
    public void onBindViewHolder(LoadViewHolder holder, Loading item) {

    }

    class LoadViewHolder extends RecyclerView.ViewHolder {

        LoadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
