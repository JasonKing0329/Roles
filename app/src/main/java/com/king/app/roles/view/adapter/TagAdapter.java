package com.king.app.roles.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 10:30
 */
public abstract class TagAdapter<T> extends BaseRecyclerAdapter<TagAdapter.TagHolder, T> implements View.OnClickListener {

    private boolean isSingleSelect;

    private SparseBooleanArray checkMap;

    public TagAdapter() {
        checkMap = new SparseBooleanArray();
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_tag;
    }

    @Override
    protected TagHolder newViewHolder(View view) {
        return new TagHolder(view);
    }

    public void setSingleSelect(boolean singleSelect) {
        isSingleSelect = singleSelect;
    }

    @Override
    public void onBindViewHolder(TagHolder holder, int position) {
        holder.tvTag.setTag(position);
        holder.tvTag.setOnClickListener(this);
        holder.tvTag.setSelected(checkMap.get(position));

        onBindTag(holder.tvTag, position);
    }

    protected abstract void onBindTag(TextView tvTag, int position);

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (checkMap.get(position)) {
            checkMap.put(position, false);
            notifyItemChanged(position);
        }
        else {
            if (isSingleSelect) {
                for (int i = 0; i < getItemCount(); i ++) {
                    if (checkMap.get(i)) {
                        checkMap.put(i, false);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }
            checkMap.put(position, true);
            notifyItemChanged(position);
        }
    }

    public void setSelectedData(List<T> data) {
        if (data == null) {
            checkMap.clear();
            return;
        }
        for (int i = 0; i < getItemCount(); i ++) {
            boolean checked = false;
            for (T item:data) {
                if (compareItem(item, list.get(i))) {
                    checked = true;
                    break;
                }
            }
            checkMap.put(i, checked);
        }
    }

    protected abstract boolean compareItem(T targetItem, T listItem);

    public List<T> getSelectedData() {
        List<T> stories = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i ++) {
            if (checkMap.get(i)) {
                stories.add(list.get(i));
            }
        }
        return stories;
    }

    public static class TagHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tag)
        TextView tvTag;

        public TagHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
