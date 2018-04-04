package com.king.app.roles.page.role;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.ButterKnifeFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.view.dialog.DraggableHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:26
 */
public class RelationEditor extends ButterKnifeFragment {

    @BindView(R.id.tv_role)
    TextView tvRole;
    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.et_relation)
    EditText etRelation;
    public OnRelationListener onRelationListener;

    private Role mRole;

    private Role mRoleRelated;

    private RoleRelations mRelation;

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_relation_editor;
    }

    @Override
    protected void onCreate(View view) {
        mRole = onRelationListener.getRole();
        mRelation = onRelationListener.getInitRoleRelation();
        if (mRelation != null) {
            mRoleRelated = mRole.getId() == mRelation.getRoleId() ? mRelation.getRole2():mRelation.getRole1();
            tvRole.setText(mRoleRelated.getName());
            etRelation.setText(mRelation.getRelationship());
            spType.setSelection(mRelation.getRelationshipType());
        }
    }

    public void setOnRelationListener(OnRelationListener onRelationListener) {
        this.onRelationListener = onRelationListener;
    }

    @OnClick({R.id.tv_ok, R.id.tv_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (mRoleRelated == null) {
                    showMessageLong("No role related");
                    return;
                }
                if (mRelation == null) {
                    mRelation = new RoleRelations();
                }
                mRelation.setRoleId(mRole.getId());
                mRelation.setRelationId(mRoleRelated.getId());
                mRelation.setRelationshipType(spType.getSelectedItemPosition());
                mRelation.setRelationship(etRelation.getText().toString());
                onRelationListener.saveRelation(mRelation);
                if (draggableHolder != null) {
                    draggableHolder.dismiss();
                }
                break;
            case R.id.tv_role:
                if (onRelationListener != null) {
                    onRelationListener.onSelectRole();
                }
                break;
        }
    }

    public void onRoleSelected(long roleId) {
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        mRoleRelated = dao.queryBuilder()
                .where(RoleDao.Properties.Id.eq(roleId))
                .build().unique();
        tvRole.setText(mRoleRelated.getName());
    }

    public interface OnRelationListener {
        Role getRole();
        RoleRelations getInitRoleRelation();
        void saveRelation(RoleRelations relations);
        void onSelectRole();
    }
}
