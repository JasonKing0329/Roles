package com.king.app.roles.page.role;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.Role;
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

    private RoleAdapter roleAdapter;

    private RoleEditor roleEditor;

    public static RoleFragment newInstance(long storyId) {
        RoleFragment fragment = new RoleFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreate(View view) {

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
                    showRoleEditor(data);
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
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Role")
                .setContentFragment(roleEditor)
                .setMaxHieight(ScreenUtils.getScreenHeight() * 4 / 5)
                .build();
        dialogFragment.show(getChildFragmentManager(), "DraggableDialogFragment");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHAPTER) {
            if (resultCode == Activity.RESULT_OK) {
                long chapterId = data.getLongExtra(ModuleActivity.KEY_RESULT_ID, -1);
                roleEditor.onChapterSelected(chapterId);
            }
        }
    }
}
