package com.king.app.roles.page.kingdom;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.king.app.roles.R;
import com.king.app.roles.databinding.AdapterKingdomItemBinding;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.page.module.ModuleAdapter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:24
 */
public class KingdomAdapter extends ModuleAdapter<AdapterKingdomItemBinding, Kingdom> {

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_kingdom_item;
    }

    @Override
    protected CheckBox getCheckBox(AdapterKingdomItemBinding holder) {
        return holder.cbCheck;
    }

    @Override
    protected View getDragView(AdapterKingdomItemBinding holder) {
        return holder.ivDrag;
    }

    @Override
    protected void onBindSubHolder(AdapterKingdomItemBinding holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        if (TextUtils.isEmpty(list.get(position).getDescription())) {
            holder.tvDescription.setVisibility(View.GONE);
        }
        else {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(list.get(position).getDescription());
        }
    }

    @Override
    protected boolean isMatchForKeyword(Kingdom kingdom, String text) {
        return false;
    }

}
