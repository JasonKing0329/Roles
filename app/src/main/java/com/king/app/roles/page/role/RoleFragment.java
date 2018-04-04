package com.king.app.roles.page.role;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.jactionbar.OnMenuItemListener;
import com.king.app.jactionbar.OnSearchListener;
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

public class RoleFragment extends ModuleFragment<RoleViewModel> {

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
    protected RoleViewModel createViewModel() {
        return ViewModelProviders.of(this).get(RoleViewModel.class);
    }

    @Override
    protected void onCreate(View view) {
        // 选择模式下只允许新增
        if (isSelectMode()) {
            holder.getJActionbar().inflateMenu(R.menu.page_role_select);
        }
        else {
            holder.getJActionbar().inflateMenu(R.menu.page_role);
            holder.getJActionbar().setOnSearchListener(new OnSearchListener() {
                @Override
                public void onSearchWordsChanged(String words) {
                    if (roleAdapter != null) {
                        roleAdapter.filter(words);
                    }
                }
            });
        }

        holder.getJActionbar().setOnMenuItemListener(new OnMenuItemListener() {
            @Override
            public void onMenuItemSelected(int menuId) {
                switch (menuId) {
                    case R.id.menu_add:
                        addNewItem();
                        break;
                    case R.id.menu_sort:
                        showSortFilter();
                        break;
                    case R.id.menu_delete:
                        holder.getJActionbar().showConfirmStatus(menuId);
                        setDeleteMode(true);
                        break;
                    case R.id.menu_drag:
                        holder.getJActionbar().showConfirmStatus(menuId);
                        setDragMode(true);
                        break;
                }
            }
        });
        holder.getJActionbar().setOnConfirmListener(new OnConfirmListener() {
            @Override
            public boolean disableInstantDismissConfirm() {
                return true;
            }

            @Override
            public boolean disableInstantDismissCancel() {
                return false;
            }

            @Override
            public boolean onConfirm(int actionId) {
                switch (actionId) {
                    case R.id.menu_delete:
                        confirmDelete();
                        break;
                    case R.id.menu_drag:
                        confirmDrag();
                        break;
                }
                return true;
            }

            @Override
            public boolean onCancel(int actionId) {
                switch (actionId) {
                    case R.id.menu_delete:
                        setDeleteMode(false);
                        break;
                    case R.id.menu_drag:
                        setDragMode(false);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void loadData() {
        viewModel.rolesObserver.observe(this, new Observer<List<RoleItemBean>>() {
            @Override
            public void onChanged(@Nullable List<RoleItemBean> roleItemBeans) {
                showRole(roleItemBeans);
            }
        });
        viewModel.loadRoles(getStoryId());
    }

    private void showRole(List<RoleItemBean> roles) {
        if (roleAdapter == null) {
            roleAdapter = new RoleAdapter();
            roleAdapter.setList(roles);
            roleAdapter.setSwipeMenuRecyclerView(binding.rvItems);
            roleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<RoleItemBean>() {
                @Override
                public void onClickItem(int position, RoleItemBean data) {
                    if (isSelectMode()) {
                        onSelectId(data.getRole().getId());
                    }
                    else {
                        showRoleEditor(data.getRole());
                    }
                }
            });
            binding.rvItems.setAdapter(roleAdapter);
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

    private void showSortFilter() {
        RoleSortFilterDialog sortFilter = new RoleSortFilterDialog();
        sortFilter.setOnSortFilterListener(new RoleSortFilterDialog.OnSortFilterListener() {
            @Override
            public void onSortAndFilter(int sortType, List<Race> races, Kingdom kingdom) {
                viewModel.sortAndFilter(sortType, races, kingdom);
            }

            @Override
            public long getStoryId() {
                return RoleFragment.this.getStoryId();
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Sort and filter")
                .setContentFragment(sortFilter)
                .setMaxHeight(ScreenUtils.getScreenHeight())
                .build();
        dialogFragment.show(getChildFragmentManager(), "RoleSortFilterDialog");
    }

    private void showRoleEditor(final Role role) {
        roleEditor = new RoleEditor();
        roleEditor.setOnRoleListener(new RoleEditor.OnRoleListener() {
            @Override
            public void onSaveRole(Role role, List<Race> raceList, Kingdom kingdom) {
                viewModel.insertOrUpdate(role, raceList, kingdom);
                loadData();
                showMessageLong("Save successfully");
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

    public void confirmDrag() {
        viewModel.confirmDrag(roleAdapter.getList());
        loadData();
        holder.getJActionbar().cancelConfirmStatus();
        setDragMode(false);
    }

    public void confirmDelete() {
        new SimpleDialogs().showWarningActionDialog(getActivity()
                , "Delete role will delete all related data, click ok to continue"
                , getString(R.string.ok), null
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            viewModel.confirmDelete(roleAdapter.getSelectedData());
                            loadData();
                        }
                        holder.getJActionbar().cancelConfirmStatus();
                        setDeleteMode(false);
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
