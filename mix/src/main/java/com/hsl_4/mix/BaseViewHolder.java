package com.hsl_4.mix;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.OnClickListener;
import static android.view.View.OnLongClickListener;
import static android.view.View.VISIBLE;

/**
 *
 * 创建人:    霍述雷
 * 创建时间:  2016/11/1 9:29
 *
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> views;

   public BaseViewHolder(View loadView) {
        super(loadView);
        views = new SparseArray<>();
    }

    public BaseViewHolder setText(@IdRes int viewId, CharSequence text) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(text);
            return this;
        }
        throw new ClassCastException("目标不是TextView");
    }

    public BaseViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setText(resId);
            return this;
        }
        throw new ClassCastException("目标不是TextView");
    }

    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
            return this;
        }
        throw new ClassCastException("目标不是ImageView");
    }

    /**
     * @param url A file path, or a uri or url handled by {@link com.bumptech.glide.load.model.UriLoader}.
     */
    public BaseViewHolder setImageUrl(@IdRes int viewId, String url) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            Glide.with(view.getContext())
                    .load(url).into((ImageView) view);
            return this;
        }
        throw new ClassCastException("目标不是ImageView");
    }

    public BaseViewHolder setBackgrounColor(@IdRes int viewId, @ColorInt int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int viewId, @DrawableRes int backgroundRes) {
        getView(viewId).setBackgroundResource(backgroundRes);
        return this;
    }

    public BaseViewHolder setTextColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
            return this;
        }
        throw new ClassCastException("目标不是TextView");
    }

    public BaseViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
            return this;
        }
        throw new ClassCastException("目标不是ImageView");
    }

    public BaseViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
            return this;
        }
        throw new ClassCastException("目标不是ImageView");
    }

    public BaseViewHolder setAlpha(@IdRes int viewId, float alpha) {
        getView(viewId).setAlpha(alpha);
        return this;
    }

    public BaseViewHolder setVisible(@IdRes int viewId, @Visibility int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public BaseViewHolder linkify(@IdRes int viewId) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            Linkify.addLinks((TextView) view, Linkify.ALL);
            return this;
        }
        throw new ClassCastException("目标不是ImageView");
    }

    public BaseViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
            ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | 128);
            return this;
        }
        throw new ClassCastException("目标不是TextView");
    }

    public BaseViewHolder setTypeface(Typeface typeface, @IdRes int... viewId) {
        for (int id : viewId) {
            setTypeface(id, typeface);
        }
        return this;
    }


    public BaseViewHolder setOnClickListener(@IdRes int viewId, OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnClickListener(OnClickListener listener, @IdRes int... viewIds) {
        for (int id : viewIds) {
            getView(id).setOnClickListener(listener);
        }
        return this;
    }

    public BaseViewHolder setOnLongClickListener(@IdRes int viewId, OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    public View getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return view;
    }

    /**
     * @param view 根据view创建item布局
     * @return
     */
    public  static BaseViewHolder onCreate(View view) {
        return new BaseViewHolder(view);
    }

    /**
     * @return 创建默认布局
     */
    public static BaseViewHolder onCreate(ViewGroup parent, @LayoutRes int layoutId) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false));
    }

    /**
     * @return 创建加载布局
     */
    public static BaseViewHolder onCreateLoadView(ViewGroup parent) {
        return new BaseViewHolder((LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loading_view, parent, false)));
    }

    /**
     * @return 获取item对象
     */
    public View getItemView() {
        return itemView;
    }

    /**
     * @hide
     */
    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

}
