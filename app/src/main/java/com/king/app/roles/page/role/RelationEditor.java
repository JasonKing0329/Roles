package com.king.app.roles.page.role;

import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.databinding.DialogRelationEditorBinding;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.view.dialog.DraggableContentFragment;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:26
 */
public class RelationEditor extends DraggableContentFragment<DialogRelationEditorBinding, BaseViewModel> {

    public OnRelationListener onRelationListener;

    private Role mRole;

    private Role mRoleRelated;

    private RoleRelations mRelation;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_relation_editor;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        binding.tvRole.setOnClickListener(v -> {
            if (onRelationListener != null) {
                onRelationListener.onSelectRole();
            }
        });
        binding.tvOk.setOnClickListener(v -> onClickOk());
    }

    @Override
    protected void onCreateData() {
        if (onRelationListener != null) {
            mRole = onRelationListener.getRole();
            mRelation = onRelationListener.getInitRoleRelation();
        }
        if (mRelation != null) {
            mRoleRelated = mRole.getId() == mRelation.getRoleId() ? mRelation.getRole2():mRelation.getRole1();
            binding.tvRole.setText(mRoleRelated.getName());
            binding.etRelation.setText(mRelation.getRelationship());
            binding.spType.setSelection(mRelation.getRelationshipType());
        }
    }

    public void setOnRelationListener(OnRelationListener onRelationListener) {
        this.onRelationListener = onRelationListener;
    }

    private void onClickOk() {
        if (mRoleRelated == null) {
            showMessageLong("No role related");
            return;
        }
        if (mRelation == null) {
            mRelation = new RoleRelations();
        }
        mRelation.setRoleId(mRole.getId());
        mRelation.setRelationId(mRoleRelated.getId());
        mRelation.setRelationshipType(binding.spType.getSelectedItemPosition());
        mRelation.setRelationship(binding.etRelation.getText().toString());
        onRelationListener.saveRelation(mRelation);

        dismiss();
    }

    public void onRoleSelected(long roleId) {
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        mRoleRelated = dao.queryBuilder()
                .where(RoleDao.Properties.Id.eq(roleId))
                .build().unique();
        binding.tvRole.setText(mRoleRelated.getName());
    }

    public interface OnRelationListener {
        Role getRole();
        RoleRelations getInitRoleRelation();
        void saveRelation(RoleRelations relations);
        void onSelectRole();
    }
}
