package com.king.app.roles.page.role;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.databinding.DialogRelationsBinding;
import com.king.app.roles.model.entity.RoleRelations;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:36
 */
public class RelationsDialog extends MvvmFragment<DialogRelationsBinding, RelationViewModel> {

    private static final String KEY_ROLE_ID = "key_role_id";

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
        binding.rvRelations.setLayoutManager(manager);

        binding.tvAdd.setOnClickListener(v -> {
            if (onRelationListener != null) {
                onRelationListener.onNewRelation();
            }
        });
    }

    @Override
    protected RelationViewModel createViewModel() {
        return ViewModelProviders.of(this).get(RelationViewModel.class);
    }

    @Override
    protected void onCreateData() {
        viewModel.relationsObserver.observe(this, list -> showRelations(list));
        viewModel.loadRelations(getArguments().getLong(KEY_ROLE_ID));
    }

    private void showRelations(List<RoleRelations> roleRelations) {
        adapter = new RelationAdapter();
        adapter.setRole(viewModel.getRole());
        adapter.setList(roleRelations);
        adapter.setOnRelationItemListener(new RelationAdapter.OnRelationItemListener() {
            @Override
            public void onDelete(RoleRelations relations) {
                viewModel.deleteRelation(relations);
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

        binding.rvRelations.setAdapter(adapter);
    }

    public void setOnRelationListener(OnRelationListener onRelationListener) {
        this.onRelationListener = onRelationListener;
    }

    public void saveRelation(RoleRelations relations) {
        viewModel.insertOrUpdateRelation(relations);
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
