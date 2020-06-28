package com.xinkao.skmvp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.ButterKnife;

public abstract class SkBaseAdapter<T> extends BaseAdapter implements LifecycleObserver {

    protected List<T> mDataList;

    public SkBaseAdapter(@NotNull Context mContext) {
        ((LifecycleOwner) mContext).getLifecycle().addObserver(this);
    }

    public SkBaseAdapter(@NotNull Context mContext, List<T> mDataList) {
        ((LifecycleOwner) mContext).getLifecycle().addObserver(this);
        this.mDataList = mDataList;
    }

    public void setNewData(List<T> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
            convertView.setTag(getViewHolder(convertView));
        }

        ((BaseHolder) convertView.getTag()).setData(convertView, mDataList.get(position), position);

        return convertView;
    }



    protected abstract int getLayout();

    protected abstract BaseHolder getViewHolder(View view);

    protected void onDestroy() {
        mDataList = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(LifecycleOwner owner) {
        onDestroy();
        owner.getLifecycle().removeObserver(this);
    }

    protected abstract class BaseHolder {
        public BaseHolder(View view) {
            ButterKnife.bind(this, view);
        }

        protected abstract void setData(View view, T bean, int position);
    }

}
