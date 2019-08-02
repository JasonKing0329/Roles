package com.king.app.roles.page.role;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseBindingAdapter;
import com.king.app.roles.databinding.AdapterRelationItemBinding;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleRelations;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:20
 */
public class RelationAdapter extends BaseBindingAdapter<AdapterRelationItemBinding, RoleRelations> {

    private Role mRole;

    private OnRelationItemListener onRelationItemListener;

    public void setRole(Role mRole) {
        this.mRole = mRole;
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_relation_item;
    }

    @Override
    protected void onBindItem(AdapterRelationItemBinding binding, int position, RoleRelations bean) {
        Role role = mRole.getId() == list.get(position).getRoleId() ? list.get(position).getRole2() : list.get(position).getRole1();
        binding.tvRole.setText(role.getName());
        binding.tvRelation.setText(list.get(position).getRelationship());
        binding.ivDelete.setOnClickListener(view -> onRelationItemListener.onDelete(list.get(position)));
        binding.ivEdit.setOnClickListener(view -> onRelationItemListener.onEdit(list.get(position)));
    }

    public void setOnRelationItemListener(OnRelationItemListener onRelationItemListener) {
        this.onRelationItemListener = onRelationItemListener;
    }

    public interface OnRelationItemListener {
        void onDelete(RoleRelations relations);
        void onEdit(RoleRelations relations);
    }
}
