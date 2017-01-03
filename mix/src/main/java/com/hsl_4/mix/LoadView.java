package com.hsl_4.mix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hsl_4 on 2016/12/28.
 */

class LoadView implements ItemViewProvider<Loading, BaseViewHolder> {
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return BaseViewHolder.onCreateLoadView(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, Loading item) {

    }


}
