package com.king.app.roles.page.module;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.king.app.roles.base.BaseBindingAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:06
 */

public abstract class ModuleAdapter<V extends ViewDataBinding, T> extends BaseBindingAdapter<V, T> {

    protected boolean isDrag;

    protected boolean isSelect;

    private SwipeMenuRecyclerView swipeMenuRecyclerView;

    protected SparseBooleanArray checkMap;

    private String mKeyword;

    private List<T> originList;

    public ModuleAdapter() {
        checkMap = new SparseBooleanArray();
    }

    @Override
    public void setList(@NonNull List<T> data) {
        originList = data;
        list = new ArrayList<>();
        for (T t:originList) {
            list.add(t);
        }
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    public void setSelect(boolean select) {
        this.isSelect = select;
    }

    public void setSwipeMenuRecyclerView(SwipeMenuRecyclerView swipeMenuRecyclerView) {
        this.swipeMenuRecyclerView = swipeMenuRecyclerView;
    }

    public void swapData(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    protected void onClickItem(View v, int position) {
        if (isSelect) {
            checkMap.put(position, !checkMap.get(position));
            notifyItemChanged(position);
        }
        else if (isDrag) {

        }
        else {
            if (onItemClickListener != null) {
                onItemClickListener.onClickItem(v, position, list.get(position));
            }
        }
    }

    public List<T> getSelectedData() {
        List<T> stories = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i ++) {
            if (checkMap.get(i)) {
                stories.add(list.get(i));
            }
        }
        return stories;
    }

    @Override
    protected void onHolderCreated(BindingHolder holder, V binding) {
        getDragView(binding).setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    swipeMenuRecyclerView.startDrag(holder);
                    break;
                }
            }
            return false;
        });
    }

    @Override
    protected void onBindItem(V binding, int position, T bean) {
        getDragView(binding).setVisibility(isDrag ? View.VISIBLE:View.GONE);
        getCheckBox(binding).setVisibility(isSelect ? View.VISIBLE:View.GONE);
        getCheckBox(binding).setChecked(checkMap.get(position));

        onBindSubHolder(binding, position);
    }

    protected abstract CheckBox getCheckBox(V holder);

    protected abstract View getDragView(V holder);

    protected abstract void onBindSubHolder(V holder, int position);

    public T getData(int position) {
        return list.get(position);
    }

    public void filter(String text) {
        if (!text.equals(mKeyword)) {
            list.clear();
            mKeyword = text;
            for (int i = 0; i < originList.size(); i ++) {
                if (TextUtils.isEmpty(text)) {
                    list.add(originList.get(i));
                }
                else {
                    if (isMatchForKeyword(originList.get(i), text)) {
                        list.add(originList.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    protected abstract boolean isMatchForKeyword(T t, String text);

}
