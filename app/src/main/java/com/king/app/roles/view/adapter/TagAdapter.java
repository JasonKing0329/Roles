package com.king.app.roles.view.adapter;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseBindingAdapter;
import com.king.app.roles.databinding.AdapterTagBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 10:30
 */
public abstract class TagAdapter<T> extends BaseBindingAdapter<AdapterTagBinding, T> {

    private boolean isSingleSelect;

    private SparseBooleanArray checkMap;

    public TagAdapter() {
        checkMap = new SparseBooleanArray();
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_tag;
    }

    public void setSingleSelect(boolean singleSelect) {
        isSingleSelect = singleSelect;
    }

    @Override
    protected void onBindItem(AdapterTagBinding binding, int position, T bean) {
        binding.tvTag.setSelected(checkMap.get(position));
        onBindTag(binding.tvTag, position);
    }

    protected abstract void onBindTag(TextView tvTag, int position);

    @Override
    protected void onClickItem(View v, int position) {
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
}
