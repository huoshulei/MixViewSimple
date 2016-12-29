package com.hsl_4.mix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.*;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 创建人: 霍述雷
 * 时 间:2016/12/28 10:02.
 */

public class MixRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List           items;
    private TypePool       typePool;
    private Context        mContext;
    private LayoutInflater inflater;
    //    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView   recyclerView;
    private OnScrollLoad   onScrollListener;
    private int headerCount = 0;
    private List header;
    private int footerCount = 0;
    private List                 footer;
    private Map<Object, Integer> spanMap;
    private LayoutManager        manager;
    private boolean              loading;

    public MixRecyclerViewAdapter() {
        this(new ArrayList());
    }

    public MixRecyclerViewAdapter(@NonNull List items) {
        this(items, new TypePool());
    }

    private MixRecyclerViewAdapter(@NonNull List items, @NonNull TypePool typePool) {
        this.items = items;
        this.typePool = typePool;
        header = new ArrayList();
        footer = new ArrayList();
        spanMap = new ArrayMap<>();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.recyclerView == null) this.recyclerView = recyclerView;
        if (mContext == null) mContext = recyclerView.getContext();
        if (inflater == null) this.inflater = LayoutInflater.from(mContext);
        if (manager == null) manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridSpanSize());
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        if (manager instanceof StaggeredGridLayoutManager) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params != null && params instanceof StaggeredGridLayoutManager.LayoutParams) {
                int position = holder.getLayoutPosition();
                if (position < headerCount || position >= getItemCount() - footerCount - 1) {
                    ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                    return;
                }
                for (Object index : spanMap.keySet()) {
                    if (index == items.get(position)) {
//                    span.remove(index);
                        ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return typePool.valueAt(viewType).onCreateViewHolder(parent, inflater);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object item = items.get(position);
//        typePool.get(item.getClass()).onBindViewHolder(holder, item);
        typePool.valueAt(holder.getItemViewType()).onBindViewHolder(holder, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return indexOf(items.get(position).getClass());
    }

    /**
     * 注入视图关系数据绑定
     *
     * @param clazz    item
     * @param provider view holder
     */

    public MixRecyclerViewAdapter inject(@NonNull Class<?> clazz, @NonNull ItemViewProvider provider) {
        typePool.inject(clazz, provider);
        return this;
    }

    private int indexOf(@NonNull Class<?> clazz) {
        int index = typePool.indexOf(clazz);
        if (index >= 0) return index;
        throw new RuntimeException("未注入 {className}.class!"
                .replace("{className}", clazz.getSimpleName()));
    }


    /**
     * @param listener 启用加载更多
     */
    public void openLoadMore(OnLoadMoreListener listener) {
        if (recyclerView == null) throw new RuntimeException("先调用setAdapter()方法 然后添加滚动监听");
        inject(Loading.class, new LoadView());
        setLoadView(new Loading());
        onScrollListener = new OnScrollLoad(listener);
        recyclerView.setOnScrollListener(onScrollListener);
    }

    public void setData(List items) {
        clear();
        addData(items, headerCount);
    }

    public void setData(Object items) {
        clear();
        addData(items, headerCount);
    }


    public void addData(List items) {
        int position = getItemCount() - footerCount;
        addData(items, position);
    }

    public void addData(Object item) {
        int position = getItemCount() - footerCount;
        addData(item, position);
    }

    public void addData(List items, int position) {
        this.items.addAll(position, items);
        int itemCount = getItemCount();
        notifyItemRangeChanged(position, itemCount);
    }

    public void addData(Object item, int position) {
        this.items.add(position, item);
        int itemCount = getItemCount();
        notifyItemRangeChanged(position, itemCount);
    }

    public void remove(Object item) {
        int position = items.indexOf(item);
        this.items.remove(item);
        notifyItemRemoved(position);
    }

    public void remove(int position) {
        spanMap.remove(items.remove(position));
        notifyItemRemoved(position);
    }

    public void clear() {
        this.items.clear();
        this.spanMap.clear();
        items.add(header);
        items.add(footer);
        notifyDataSetChanged();
    }


    public void addHeader(Object header) {
        this.header.add(header);
        addData(header, headerCount);
        headerCount++;
    }

    public void addFooter(Object footer) {
        this.footer.add(footer);
        addData(footer, getItemCount() - isLoading());
        footerCount++;
    }

    public void setLoadView(Object loadView) {
        addFooter(loadView);
        if (loading) remove(getItemCount());
        loading = true;
    }

    private int isLoading() {
        return loading ? 1 : 0;
    }

    public void addSpanItem(Object item, int span) {
        addSpanItem(item, span, getItemCount());
    }

    public void addSpanItem(Object item, int span, int position) {
        if (manager instanceof StaggeredGridLayoutManager
                && span != ((StaggeredGridLayoutManager) manager).getSpanCount())
            throw new RuntimeException("瀑布流暂不支持此种布局格式");
        addData(item, position);
        spanMap.put(item, span);
    }

    public void setSpanItem(int span, int position) {
        if (manager instanceof StaggeredGridLayoutManager
                && span != ((StaggeredGridLayoutManager) manager).getSpanCount())
            throw new RuntimeException("瀑布流暂不支持此种布局格式");
        spanMap.put(items.get(position), span);
    }

    /**
     * 无数据可加载时 关闭加载
     */
    public void closeLoading() {
        onScrollListener.closeLoading();
    }

    private class GridSpanSize extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            if (position < headerCount || position >= getItemCount() - footerCount - 1)
                return ((GridLayoutManager) manager).getSpanCount();
            for (Object index : spanMap.keySet()) {
                if (index == items.get(position)) {
//                    span.remove(index);
                    return spanMap.get(index);
                }
//                if (position > index) break;
            }
            return 1;
        }
    }
}
