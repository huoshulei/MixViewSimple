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
    private List                       items;
    private List                       footers;
    private List                       headers;
    private TypePool                   typePool;
    private Context                    mContext;
    private LayoutInflater             inflater;
    //    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView               recyclerView;
    private OnScrollLoad               onScrollListener;
    private Object                     loadView;
    private Map<Integer, Integer>      spanMap;
    private LayoutManager              manager;
    private boolean                    loading;
    private Function<Object, Class<?>> itemTransformation;
//    private Function<Class<?>, Integer> itemViewTypeDistribute;

    public MixRecyclerViewAdapter() {
        this(new ArrayList());
    }

    public MixRecyclerViewAdapter(@NonNull List items) {
        this(items, new DefaultTypePool());
    }

    public MixRecyclerViewAdapter(@NonNull List items, @NonNull TypePool typePool) {
        this.items = items;
        this.typePool = typePool;
        headers = new ArrayList();
        footers = new ArrayList();
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
                if (position < getHeaderCount() || position >= getHeaderCount() + getDataCount()) {
                    ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                    return;
                }
                for (Integer index : spanMap.keySet()) {
                    if (index == position) {
                        ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return typePool.getProvider(viewType).onCreateViewHolder(parent, inflater);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object item = items.get(position);
        typePool.getProvider(holder.getItemViewType()).onBindViewHolder(holder, item);
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getDataCount() + getFooterCount() + getLoadCount();
    }

    public int getDataCount() {
        return items.size();
    }

    public int getHeaderCount() {
        return items.size();
    }

    public int getFooterCount() {
        return items.size();
    }

    public int getLoadCount() {
        return loading ? 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        int index;
        if (position < getHeaderCount()) {
            index = position;
        } else if (position < getHeaderCount() + getDataCount()) {
            index = position - getHeaderCount();
        } else if (position < getHeaderCount() + getDataCount() + getFooterCount()) {
            index = position - getHeaderCount() - getDataCount();
        }
        if (position < getDataCount() + getHeaderCount() + getFooterCount() + getLoadCount()) {
            index = position - getDataCount() - getHeaderCount() - getFooterCount();
        } else throw new IndexOutOfBoundsException("在这儿呢" + this.getClass().getName());
        return getItemType(getItemClass(items.get(index)));
    }

    /**
     * @param itemTransformation 替换默认类型转换器
     */
    public void replaceItemDistribute(Function<Object, Class<?>> itemTransformation) {
        this.itemTransformation = itemTransformation;
    }

    /**
     * 数据类型转换
     */
    private Class<?> getItemClass(Object item) {
        if (itemTransformation == null) itemTransformation = new Function<Object, Class<?>>() {
            @Override
            public Class<?> apply(Object o) {
                return o.getClass();
            }
        };
        return itemTransformation.apply(item);
    }

//    public void replaceItemViewTypeDistribute(Function<Class<?>, Integer> itemViewTypeDistribute) {
//        this.itemViewTypeDistribute = itemViewTypeDistribute;
//    }

    /**
     * 视图类型分发
     */
    private int getItemType(@NonNull Class<?> clazz) {
        int index = typePool.indexOf(clazz);
        if (index >= 0) return index;
        throw new RuntimeException("未注入 {className}.class!"
                .replace("{className}", clazz.getSimpleName()));
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
        addData(items);
    }

    public void setData(Object items) {
        clear();
        addData(items);
    }


    public void addData(List<?> items) {
        addData(items, getDataCount());
    }

    public void addData(Object item) {
        addData(item, getDataCount());
    }

    public void addData(List items, int position) {
        this.items.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void addData(Object item, int position) {
        this.items.add(position, item);
        notifyItemInserted(position);
    }

    public void addHeader(Object header) {
        this.headers.add(header);
        notifyItemInserted(getHeaderCount());
    }

    public void addFooter(Object footer) {
        this.footers.add(footer);
        notifyItemInserted(getHeaderCount() + getDataCount() + getFooterCount());
    }

    public void remove(Object item) {
        int index = headers.indexOf(item);
        int position;
        if (index >= 0) position = index;
        else if ((index = items.indexOf(item)) >= 0)
            position = index + getHeaderCount();
        else if ((index = footers.indexOf(item)) >= 0)
            position = index + getHeaderCount() + getDataCount();
        else return;
        remove(position);
    }

    public void remove(int position) {
        if (position < getHeaderCount()) headers.remove(position);
        else if (position < getHeaderCount() + getDataCount())
            spanMap.remove(items.remove(position - getHeaderCount()));
        else if (position < getHeaderCount() + getDataCount() + getFooterCount())
            footers.remove(position - getHeaderCount() - getDataCount());
        notifyItemRemoved(position);
    }

    public void clear() {
        clearFooter();
        clearItems();
        clearHeader();
    }

    public void clearHeader() {
        headers.clear();
        notifyItemRangeRemoved(0, getHeaderCount());
    }

    public void clearFooter() {
        footers.clear();
        notifyItemRangeRemoved(getHeaderCount() + getDataCount(), getFooterCount());
    }

    public void clearItems() {
        items.clear();
        notifyItemRangeRemoved(getHeaderCount(), getDataCount());
    }


    public void setLoadView(Object loadView) {
        this.loadView = loadView;
        loading = true;
        notifyItemChanged(getItemCount());
    }


    public void addSpanItem(Object item, int span) {
        addSpanItem(item, span, getItemCount() - getFooterCount() - getLoadCount());
    }

    public void addSpanItem(Object item, int span, int position) {
        if (manager instanceof StaggeredGridLayoutManager
                && span != ((StaggeredGridLayoutManager) manager).getSpanCount())
            throw new RuntimeException("瀑布流暂不支持此种布局格式");
        addData(item, position);
        spanMap.put(position, span);
    }

    public void setSpanItem(int span, int position) {
        if (manager instanceof StaggeredGridLayoutManager
                && span != ((StaggeredGridLayoutManager) manager).getSpanCount())
            throw new RuntimeException("瀑布流暂不支持此种布局格式");

        spanMap.put(position, span);
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
            if (position < getHeaderCount() || position >= getHeaderCount() + getDataCount())
                return ((GridLayoutManager) manager).getSpanCount();
            for (Integer index : spanMap.keySet()) {
                if (index == position) {
                    return spanMap.get(index);
                }
            }
            return 1;
        }
    }
}
