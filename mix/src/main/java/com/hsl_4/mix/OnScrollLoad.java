package com.hsl_4.mix;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.*;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * 创建人: 霍述雷
 * 时 间:2016/12/28 13:50.
 */

public class OnScrollLoad extends OnScrollListener {
    private OnLoadMoreListener onLoadMoreListener;

    private int     lastPosition;
    private int     itemCount;
    private boolean isLoading;
    private boolean isEnabled;

    OnScrollLoad(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        isLoading = true;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if (!isLoading) return;
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            itemCount = layoutManager.getItemCount();
            lastPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        } else {
            StaggeredGridLayoutManager manager       = (StaggeredGridLayoutManager) layoutManager;
            int[]                      lastPositions = manager.findLastCompletelyVisibleItemPositions(new int[manager.getSpanCount()]);
            lastPosition = findMax(lastPositions);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!isLoading) return;
        LayoutManager layoutManager = recyclerView.getLayoutManager();
        int           visibleCount  = layoutManager.getChildCount();
        if (visibleCount > 0 && lastPosition >= itemCount - 1 && newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isEnabled) {
                isEnabled = false;
                onLoadMoreListener.onLoadMore();
            }
        } else {
            isEnabled = true;
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 无数据可加载时 关闭加载
     */
    void closeLoading() {
        isLoading = false;
    }
}
