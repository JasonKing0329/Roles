package com.king.app.roles.page.role;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.page.module.ModuleActivity;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.utils.ScreenUtils;
import com.king.app.roles.view.dialog.DraggableDialogFragment;
import com.king.app.roles.view.dialog.SimpleDialogs;

import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:41
 */

public class RoleFragment extends ModuleFragment<RolePresenter> implements RoleView {

    private final int REQUEST_CHAPTER = 151;
    private final int REQUEST_ROLE = 152;

    private RoleAdapter roleAdapter;

    private RoleEditor roleEditor;
    private RelationsDialog relationsDialog;
    private RelationEditor relationEditor;

    public static RoleFragment newInstance(long storyId, boolean selectMode) {
        RoleFragment fragment = new RoleFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        bundle.putBoolean(KEY_SELECT_MODE, selectMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreate(View view) {
        // 选择模式下只允许新增
        if (isSelectMode()) {
            holder.hideDeleteAction();
            holder.hideDragAction();
        }

        holder.registerTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (roleAdapter != null) {
                    roleAdapter.filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected RolePresenter createPresenter() {
        return new RolePresenter();
    }

    @Override
    protected void loadData() {
        presenter.loadRoles(getStoryId());
    }

    @Override
    public void showRole(List<Role> roles) {
        if (roleAdapter == null) {
            roleAdapter = new RoleAdapter();
            roleAdapter.setList(roles);
            roleAdapter.setSwipeMenuRecyclerView(rvItems);
            roleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<Role>() {
                @Override
                public void onClickItem(int position, Role data) {
                    if (isSelectMode()) {
                        onSelectId(data.getId());
                    }
                    else {
                        showRoleEditor(data);
                    }
                }
            });
            rvItems.setAdapter(roleAdapter);
        }
        else {
            roleAdapter.setList(roles);
            roleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ModuleAdapter getAdapter() {
        return roleAdapter;
    }

    @Override
    public void addNewItem() {

        showRoleEditor(null);
    }

    private void showRoleEditor(final Role role) {
        roleEditor = new RoleEditor();
        roleEditor.setOnRoleListener(new RoleEditor.OnRoleListener() {
            @Override
            public void onSaveRole(Role role, List<Race> raceList, Kingdom kingdom) {
                presenter.insertOrUpdate(role, raceList, kingdom);
                loadData();
                showMessage("Save successfully");
            }

            @Override
            public long getStoryId() {
                return RoleFragment.this.getStoryId();
            }

            @Override
            public Role getInitKingdom() {
                return role;
            }

            @Override
            public void selectChapter() {
                RoleFragment.this.selectChapter();
            }

            @Override
            public void showRelations(Role role) {
                showRelationDialog(role);
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Role")
                .setContentFragment(roleEditor)
                .setMaxHeight(ScreenUtils.getScreenHeight())
                .build();
        dialogFragment.show(getChildFragmentManager(), "RoleEditor");
    }

    private void showRelationDialog(final Role role) {
        relationsDialog = RelationsDialog.newInstance(role.getId());
        relationsDialog.setOnRelationListener(new RelationsDialog.OnRelationListener() {
            @Override
            public void onNewRelation() {
                showRelationEditor(role, null);
            }

            @Override
            public void onEditRelation(RoleRelations data) {
                showRelationEditor(role, data);
            }

            @Override
            public void onRelationDeleted(RoleRelations relations) {
                roleEditor.refreshRelations();
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle(role.getName())
                .setContentFragment(relationsDialog)
                .build();
        dialogFragment.show(getChildFragmentManager(), "RelationsDialog");
    }

    private void showRelationEditor(final Role role, final RoleRelations relation) {
        relationEditor = new RelationEditor();
        relationEditor.setOnRelationListener(new RelationEditor.OnRelationListener() {
            @Override
            public Role getRole() {
                return role;
            }

            @Override
            public RoleRelations getInitRoleRelation() {
                return relation;
            }

            @Override
            public void saveRelation(RoleRelations relations) {
                relationsDialog.saveRelation(relations);
                relationsDialog.refresh();
                roleEditor.refreshRelations();
            }

            @Override
            public void onSelectRole() {
                selectRole();
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle(role.getName())
                .setContentFragment(relationEditor)
                .build();
        dialogFragment.show(getChildFragmentManager(), "RelationEditor");
    }

    @Override
    public void confirmDrag() {
        presenter.confirmDrag(roleAdapter.getList());
        loadData();
        notifyDragDone();
    }

    @Override
    public void confirmDelete() {
        new SimpleDialogs().showWarningActionDialog(getActivity()
                , "Delete role will delete all related data, click ok to continue"
                , getString(R.string.ok), null
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            presenter.confirmDelete(roleAdapter.getSelectedData());
                            loadData();
                            notifyDeleteDone();
                        }
                        else {
                            notifyDeleteDone();
                        }
                    }
                });
    }

    private void selectChapter() {

        Intent intent = new Intent().setClass(getActivity(), ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_STORY_ID, getStoryId());
        intent.putExtra(ModuleActivity.KEY_PAGE_TYPE, ModuleActivity.PAGE_TYPE_CHAPTER);
        intent.putExtra(ModuleActivity.KEY_SELECT_MODE, true);
        startActivityForResult(intent, REQUEST_CHAPTER);
    }

    private void selectRole() {

        Intent intent = new Intent().setClass(getActivity(), ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_STORY_ID, getStoryId());
        intent.putExtra(ModuleActivity.KEY_PAGE_TYPE, ModuleActivity.PAGE_TYPE_CHARACTER);
        intent.putExtra(ModuleActivity.KEY_SELECT_MODE, true);
        startActivityForResult(intent, REQUEST_ROLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHAPTER) {
            if (resultCode == Activity.RESULT_OK) {
                long chapterId = data.getLongExtra(ModuleActivity.KEY_RESULT_ID, -1);
                roleEditor.onChapterSelected(chapterId);
            }
        }
        else if (requestCode == REQUEST_ROLE) {
            if (resultCode == Activity.RESULT_OK) {
                long roleId = data.getLongExtra(ModuleActivity.KEY_RESULT_ID, -1);
                relationEditor.onRoleSelected(roleId);
            }
        }
    }
}
