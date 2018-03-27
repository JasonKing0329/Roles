package com.king.app.roles.page.role;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleRelations;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:20
 */
public class RelationAdapter extends BaseRecyclerAdapter<RelationAdapter.RelationHolder, RoleRelations> {

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
    protected RelationHolder newViewHolder(View view) {
        return new RelationHolder(view);
    }

    @Override
    public void onBindViewHolder(RelationHolder holder, final int position) {
        Role role = mRole.getId() == list.get(position).getRoleId() ? list.get(position).getRole2() : list.get(position).getRole1();
        holder.tvRole.setText(role.getName());
        holder.tvRelation.setText(list.get(position).getRelationship());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRelationItemListener.onDelete(list.get(position));
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRelationItemListener.onEdit(list.get(position));
            }
        });
    }

    public void setOnRelationItemListener(OnRelationItemListener onRelationItemListener) {
        this.onRelationItemListener = onRelationItemListener;
    }

    public static class RelationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_role)
        TextView tvRole;
        @BindView(R.id.tv_relation)
        TextView tvRelation;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;

        public RelationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRelationItemListener {
        void onDelete(RoleRelations relations);
        void onEdit(RoleRelations relations);
    }
}
