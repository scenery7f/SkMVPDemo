package com.xinkao.skmvp.adapter;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SkRecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected OnViewClickListener mOnViewClickListener = null;

    public SkRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
    }

    /**
     * 设置数据
     *
     * @param bean     数据
     * @param position 在 RecyclerView 中的位置
     */
    public abstract void setData(@NonNull T bean, int position);

    /**
     * 用于释放需要释放的资源
     */
    @CallSuper
    protected void onDestroy() {
        mOnViewClickListener = null;
    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            int position = getLayoutPosition();
            if (position != RecyclerView.NO_POSITION)
                mOnViewClickListener.onViewClick(view, getLayoutPosition());
        }
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    /**
     * item 点击事件
     */
    public interface OnViewClickListener {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param position 在 RecyclerView 中的位置
         */
        void onViewClick(View view, int position);
    }
}
