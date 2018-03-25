package com.king.app.roles.page.module;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.king.app.roles.base.BaseRecyclerAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:06
 */

public abstract class ModuleAdapter<VH extends ModuleViewHolder, T> extends BaseRecyclerAdapter<VH, T> implements View.OnClickListener {

    protected boolean isDrag;

    protected boolean isSelect;

    private SwipeMenuRecyclerView swipeMenuRecyclerView;

    protected SparseBooleanArray checkMap;

    public ModuleAdapter() {
        checkMap = new SparseBooleanArray();
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
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (isSelect) {
            checkMap.put(position, !checkMap.get(position));
            notifyItemChanged(position);
        }
        else if (isDrag) {

        }
        else {
            if (onItemClickListener != null) {
                onItemClickListener.onClickItem(position, list.get(position));
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
    public void onBindViewHolder(VH holder, int position) {
        holder.mMenuRecyclerView = swipeMenuRecyclerView;
        getDragView(holder).setVisibility(isDrag ? View.VISIBLE:View.GONE);
        getCheckBox(holder).setVisibility(isSelect ? View.VISIBLE:View.GONE);
        getCheckBox(holder).setChecked(checkMap.get(position));

        getGroupItem(holder).setTag(position);
        getGroupItem(holder).setOnClickListener(this);
        onBindSubHolder(holder, position);
    }

    protected abstract ViewGroup getGroupItem(VH holder);

    protected abstract CheckBox getCheckBox(VH holder);

    protected abstract View getDragView(VH holder);

    protected abstract void onBindSubHolder(VH holder, int position);
}
