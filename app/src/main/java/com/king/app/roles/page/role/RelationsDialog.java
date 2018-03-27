package com.king.app.roles.page.role;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvpFragment;
import com.king.app.roles.model.entity.RoleRelations;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:36
 */
public class RelationsDialog extends MvpFragment<RelationPresenter> implements RelationView {

    private static final String KEY_ROLE_ID = "key_role_id";

    @BindView(R.id.rv_relations)
    RecyclerView rvRelations;

    private RelationAdapter adapter;

    private OnRelationListener onRelationListener;

    public static RelationsDialog newInstance(long roleId) {
        RelationsDialog dialog = new RelationsDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_ROLE_ID, roleId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {

    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_relations;
    }

    @Override
    protected void onCreate(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRelations.setLayoutManager(manager);
    }

    @Override
    protected RelationPresenter createPresenter() {
        return new RelationPresenter();
    }

    @Override
    protected void onCreateData() {
        presenter.loadRelations(getArguments().getLong(KEY_ROLE_ID));
    }

    @Override
    public void showRelations(List<RoleRelations> roleRelations) {
        adapter = new RelationAdapter();
        adapter.setRole(presenter.getRole());
        adapter.setList(roleRelations);
        adapter.setOnRelationItemListener(new RelationAdapter.OnRelationItemListener() {
            @Override
            public void onDelete(RoleRelations relations) {
                presenter.deleteRelation(relations);
                onCreateData();
                if (onRelationListener != null) {
                    onRelationListener.onRelationDeleted(relations);
                }
            }

            @Override
            public void onEdit(RoleRelations relations) {
                if (onRelationListener != null) {
                    onRelationListener.onEditRelation(relations);
                }
            }
        });

        rvRelations.setAdapter(adapter);
    }

    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        if (onRelationListener != null) {
            onRelationListener.onNewRelation();
        }
    }

    public void setOnRelationListener(OnRelationListener onRelationListener) {
        this.onRelationListener = onRelationListener;
    }

    public void saveRelation(RoleRelations relations) {
        presenter.insertOrUpdateRelation(relations);
    }

    public void refresh() {
        onCreateData();
    }

    public interface OnRelationListener {
        void onNewRelation();
        void onEditRelation(RoleRelations data);
        void onRelationDeleted(RoleRelations relations);
    }
}
