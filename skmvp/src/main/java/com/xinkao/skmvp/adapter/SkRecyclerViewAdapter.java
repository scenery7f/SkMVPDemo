package com.xinkao.skmvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;

import java.util.List;

public abstract class SkRecyclerViewAdapter<T> extends RecyclerView.Adapter<SkRecyclerViewHolder<T>> implements LifecycleObserver {

    protected List<T> mBeans;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    boolean isFirst = true; // 用于绑定生命周期

    public SkRecyclerViewAdapter(List<T> mBeans) {
        this.mBeans = mBeans;
    }

    /**
     * 遍历所有 {@link SkRecyclerViewHolder}, 释放他们需要释放的资源
     *
     * @param recyclerView {@link RecyclerView}
     */
    public void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof SkRecyclerViewHolder) {
                ((SkRecyclerViewHolder) viewHolder).onDestroy();
            }
        }
    }

    @NonNull
    @Override
    public SkRecyclerViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isFirst) { // 用于注册生命周期。
            isFirst = false;
            ((LifecycleOwner) parent.getContext()).getLifecycle().addObserver(this);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(getLayout(viewType), parent, false);

        SkRecyclerViewHolder<T> holder = getHolder(view, viewType);
        holder.setOnItemClickListener(new SkRecyclerViewHolder.OnViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, viewType, mBeans.get(position), position);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SkRecyclerViewHolder<T> holder, int position) {
        holder.setData(mBeans.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mBeans != null ? mBeans.size() : 0;
    }

    protected abstract int getLayout(int viewType);

    protected abstract SkRecyclerViewHolder<T> getHolder(@NonNull View view, int viewType);

    /**
     * 返回数据集合
     *
     * @return 数据集合
     */
    public List<T> getBeans() {
        return mBeans;
    }

    /**
     * 获得 RecyclerView 中某个 position 上的 item 数据
     *
     * @param position 在 RecyclerView 中的位置
     * @return 数据
     */
    public T getItem(int position) {
        return mBeans == null ? null : mBeans.get(position);
    }


    /**
     * 设置 item 点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * item 点击事件
     *
     * @param <T>
     */
    public interface OnRecyclerViewItemClickListener<T> {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param viewType 布局类型
         * @param data     数据
         * @param position 在 RecyclerView 中的位置
         */
        void onItemClick(@NonNull View view, int viewType, @NonNull T data, int position);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

    protected void onDestroy() { }
}
