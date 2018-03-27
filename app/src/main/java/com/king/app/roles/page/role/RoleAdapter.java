package com.king.app.roles.page.role;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleViewHolder;
import com.king.app.roles.utils.ListUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:24
 */
public class RoleAdapter extends ModuleAdapter<RoleAdapter.RoleHolder, Role> {

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_role_item;
    }

    @Override
    protected RoleHolder newViewHolder(View view) {
        return new RoleHolder(view);
    }

    @Override
    protected ViewGroup getGroupItem(RoleHolder holder) {
        return holder.groupItem;
    }

    @Override
    protected CheckBox getCheckBox(RoleHolder holder) {
        return holder.cbCheck;
    }

    @Override
    protected View getDragView(RoleHolder holder) {
        return holder.ivDrag;
    }

    @Override
    protected void onBindSubHolder(RoleHolder holder, int position) {

        StringBuffer name = new StringBuffer(list.get(position).getName());
        if (!TextUtils.isEmpty(list.get(position).getNickname())) {
            name.append(" (").append(list.get(position).getNickname()).append(")");
        }
        holder.tvName.setText(name.toString());

        if (TextUtils.isEmpty(list.get(position).getPower())) {
            holder.tvPower.setVisibility(View.GONE);
        }
        else {
            holder.tvPower.setVisibility(View.VISIBLE);
            holder.tvPower.setText(list.get(position).getPower());
        }

        if (TextUtils.isEmpty(list.get(position).getDescription())) {
            holder.tvDescription.setVisibility(View.GONE);
        }
        else {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(getRaceText(list.get(position)) + ", " + list.get(position).getDescription());
        }
    }

    private String getRaceText(Role role) {
        List<Race> races = role.getRaceList();
        if (!ListUtil.isEmpty(races)) {
            if (races.size() > 1) {
                return "Mix of " + races.get(0).getName() + " and " + races.get(1).getName();
            }
            else {
                return races.get(0).getName();
            }
        }
        return "";
    }

    @Override
    protected boolean isMatchForKeyword(Role role, String text) {
        if (TextUtils.isEmpty(role.getName())) {
            if (text.equals(role.getName())) {
                return true;
            }
            else {
                return false;
            }
        }
        return role.getName().toLowerCase().contains(text.toLowerCase());
    }

    public static class RoleHolder extends ModuleViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_power)
        TextView tvPower;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.iv_drag)
        ImageView ivDrag;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;
        @BindView(R.id.group_item)
        LinearLayout groupItem;

        public RoleHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View getTouchHandler() {
            return ivDrag;
        }
    }
}
