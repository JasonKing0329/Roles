package com.king.app.roles.page.story;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.model.entity.Story;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 17:20
 */
public class StoryListAdapter extends BaseRecyclerAdapter<StoryListAdapter.StoryHolder, Story> implements View.OnClickListener {

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
    protected StoryHolder newViewHolder(View view) {
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, int position) {
        holder.mMenuRecyclerView = swipeMenuRecyclerView;

        holder.tvName.setText(list.get(position).getName());
        holder.ivDrag.setVisibility(isDrag ? View.VISIBLE:View.GONE);

        if (isSelect) {
            holder.cbCheck.setVisibility(View.VISIBLE);
            holder.cbCheck.setChecked(checkMap.get(position));
        }
        else {
            holder.cbCheck.setVisibility(View.GONE);
        }

        holder.groupItem.setTag(position);
        holder.groupItem.setOnClickListener(this);
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

    public List<Story> getSelectedData() {
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i ++) {
            if (checkMap.get(i)) {
                stories.add(list.get(i));
            }
        }
        return stories;
    }

    public static class StoryHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        @BindView(R.id.group_item)
        ViewGroup groupItem;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_drag)
        ImageView ivDrag;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;

        SwipeMenuRecyclerView mMenuRecyclerView;

        public StoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivDrag.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mMenuRecyclerView.startDrag(this);
                    break;
                }
            }
            return false;
        }
    }
}
