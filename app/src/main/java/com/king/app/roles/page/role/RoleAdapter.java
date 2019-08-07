package com.king.app.roles.page.role;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.king.app.roles.R;
import com.king.app.roles.databinding.AdapterRoleItemBinding;
import com.king.app.roles.page.module.ModuleAdapter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:24
 */
public class RoleAdapter extends ModuleAdapter<AdapterRoleItemBinding, RoleItemBean> {

    public OnRoleListener onRoleListener;

    public void setOnRoleListener(OnRoleListener onRoleListener) {
        this.onRoleListener = onRoleListener;
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_role_item;
    }

    @Override
    protected CheckBox getCheckBox(AdapterRoleItemBinding holder) {
        return holder.cbCheck;
    }

    @Override
    protected View getDragView(AdapterRoleItemBinding holder) {
        return holder.ivDrag;
    }

    @Override
    protected void onBindSubHolder(AdapterRoleItemBinding holder, int position) {
        holder.setBean(getData(position));
        holder.setIndex(String.valueOf(position + 1));
        holder.tvRelations.setOnClickListener(v -> onRoleListener.onClickRelationships(getData(position)));
    }

    @Override
    protected boolean isMatchForKeyword(RoleItemBean role, String text) {
        if (TextUtils.isEmpty(role.getRole().getName())) {
            if (text.equals(role.getRole().getName())) {
                return true;
            }
            else {
                return false;
            }
        }
        return role.getRole().getName().toLowerCase().contains(text.toLowerCase());
    }

    public interface OnRoleListener {
        void onClickRelationships(RoleItemBean role);
    }
}
