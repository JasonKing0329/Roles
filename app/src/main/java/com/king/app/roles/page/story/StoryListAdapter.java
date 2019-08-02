package com.king.app.roles.page.story;

import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseBindingAdapter;
import com.king.app.roles.databinding.AdapterStoryListBinding;
import com.king.app.roles.model.entity.Story;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 17:20
 */
public class StoryListAdapter extends BaseBindingAdapter<AdapterStoryListBinding, Story> {

    private boolean isDrag;

    private boolean isSelect;

    private SwipeMenuRecyclerView swipeMenuRecyclerView;

    private SparseBooleanArray checkMap;

    public StoryListAdapter() {
        checkMap = new SparseBooleanArray();
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_story_list;
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

    @Override
    protected void onHolderCreated(BindingHolder holder, AdapterStoryListBinding binding) {
        binding.ivDrag.setOnTouchListener((v, event) -> {
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
    protected void onBindItem(AdapterStoryListBinding binding, int position, Story bean) {
        binding.tvName.setText(list.get(position).getName());
        binding.ivDrag.setVisibility(isDrag ? View.VISIBLE:View.GONE);

        if (isSelect) {
            binding.cbCheck.setVisibility(View.VISIBLE);
            binding.cbCheck.setChecked(checkMap.get(position));
        }
        else {
            binding.cbCheck.setVisibility(View.GONE);
        }
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

    public List<Story> getSelectedData() {
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i ++) {
            if (checkMap.get(i)) {
                stories.add(list.get(i));
            }
        }
        return stories;
    }
}
