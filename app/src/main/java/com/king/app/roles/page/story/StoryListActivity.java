package com.king.app.roles.page.story;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.app.jactionbar.JActionbar;
import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.roles.R;
import com.king.app.roles.base.MvvmActivity;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.databinding.ActivityStoryListBinding;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.utils.DebugLog;
import com.king.app.roles.utils.ScreenUtils;
import com.king.app.roles.view.dialog.DraggableDialogFragment;
import com.king.app.roles.view.dialog.SimpleDialogs;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:39
 */
public class StoryListActivity extends MvvmActivity<ActivityStoryListBinding, StoryListViewModel> {

    private StoryListAdapter storyListAdapter;

    private boolean isEdit;

    @Override
    protected int getContentView() {
        return R.layout.activity_story_list;
    }

    @Override
    protected void initView() {
        binding.setViewModel(viewModel);
        setupRecyclerView(binding.rvStories);
        setupActionbar(binding.actionbar);
    }

    private void setupActionbar(JActionbar actionbar) {
        actionbar.setOnBackListener(() -> onBackPressed());
        actionbar.setOnMenuItemListener(menuId -> {
            switch (menuId) {
                case R.id.menu_add:
                    addNewStory();
                    break;
                case R.id.menu_delete:
                    binding.actionbar.showConfirmStatus(menuId);
                    storyListAdapter.setSelect(true);
                    storyListAdapter.notifyDataSetChanged();
                    break;
                case R.id.menu_drag:
                    binding.actionbar.showConfirmStatus(menuId);
                    storyListAdapter.setDrag(true);
                    storyListAdapter.notifyDataSetChanged();
                    break;
                case R.id.menu_edit:
                    binding.actionbar.showConfirmStatus(menuId);
                    isEdit = true;
                    break;
                case R.id.menu_load_from:
                    showLoadFrom();
                    break;
                case R.id.menu_save:
                    viewModel.saveDatabase();
                    break;
            }
        });
        actionbar.setOnConfirmListener(new OnConfirmListener() {
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
                        doDelete();
                        break;
                    case R.id.menu_drag:
                        doDrag();
                        break;
                    case R.id.menu_edit:
                        isEdit = false;
                        binding.actionbar.cancelConfirmStatus();
                        break;
                }
                return false;
            }

            @Override
            public boolean onCancel(int actionId) {
                switch (actionId) {
                    case R.id.menu_delete:
                        cancelDelete();
                        break;
                    case R.id.menu_drag:
                        cancelDrag();
                        break;
                    case R.id.menu_edit:
                        isEdit = false;
                        break;
                }
                return true;
            }
        });
    }

    private void setupRecyclerView(SwipeMenuRecyclerView rvStories) {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvStories.setLayoutManager(manager);
        rvStories.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = ScreenUtils.dp2px(2);
            }
        });
        rvStories.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 不同的ViewType不能拖拽换位置。
                if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
                    return false;
                }

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - binding.rvStories.getHeaderItemCount();
                int toPosition = targetHolder.getAdapterPosition() - binding.rvStories.getHeaderItemCount();

                storyListAdapter.swapData(fromPosition, toPosition);
                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。

            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

            }
        });
        rvStories.setOnItemStateChangedListener((viewHolder, actionState) -> {
            if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                DebugLog.e("状态：拖拽");
                // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
//                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(StoryListActivity.this, R.color.grayef));
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_SWIPE) {
                DebugLog.e("状态：滑动删除");
            } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
                DebugLog.e("状态：手指松开");
                // 在手松开的时候还原背景。
//                ViewCompat.setBackground(viewHolder.itemView, ContextCompat.getDrawable(RecordOrderPadActivity.this, R.drawable.white));
            }
        });
    }

    @Override
    protected StoryListViewModel createViewModel() {
        return ViewModelProviders.of(this).get(StoryListViewModel.class);
    }

    @Override
    protected void initData() {
        viewModel.storiesObserver.observe(this, stories -> showStories(stories));
        viewModel.deleteObserver.observe(this, aBoolean -> {
            cancelDelete();
            binding.actionbar.cancelConfirmStatus();
        });
        viewModel.updateObserver.observe(this, aBoolean -> {
            cancelDrag();
            binding.actionbar.cancelConfirmStatus();
        });
        viewModel.loadStories();
    }

    public void showStories(List<Story> stories) {
        if (storyListAdapter == null) {
            storyListAdapter = new StoryListAdapter();
            storyListAdapter.setList(stories);
            storyListAdapter.setOnItemClickListener((view, position, data) -> {
                if (isEdit) {
                    editStory(position, data);
                }
                else {
                    startStoryPage(data);
                }
            });
            storyListAdapter.setSwipeMenuRecyclerView(binding.rvStories);
            binding.rvStories.setAdapter(storyListAdapter);
        } else {
            storyListAdapter.setList(stories);
            storyListAdapter.notifyDataSetChanged();
        }
    }

    private void startStoryPage(Story story) {
        StoryInstance.getInstance().setStory(story);
        Intent intent = new Intent().setClass(this, StoryPageActivity.class);
        startActivity(intent);
    }

    private void cancelDelete() {
        storyListAdapter.setSelect(false);
        storyListAdapter.notifyDataSetChanged();
    }

    private void cancelDrag() {
        storyListAdapter.setDrag(false);
        storyListAdapter.notifyDataSetChanged();
    }

    private void doDrag() {
        viewModel.updateSequences(storyListAdapter.getList());
    }

    private void doDelete() {
        new SimpleDialogs().showWarningActionDialog(this, "Delete story will delete all information related, click ok to continue"
                , getString(R.string.ok), null
                , (dialogInterface, i) -> {
                    if (i == DialogInterface.BUTTON_POSITIVE) {
                        viewModel.deleteStory(storyListAdapter.getSelectedData());
                    }
                    else {
                        cancelDelete();
                        binding.actionbar.cancelConfirmStatus();
                    }
                });
    }

    private void addNewStory() {
        editStory(-1, null);
    }

    private void editStory(int position, Story data) {
        boolean isNew = data == null;
        StoryEditor editor = new StoryEditor();
        editor.setStory(data);
        editor.setOnStoryListener(story -> {
            if (isNew) {
                viewModel.loadStories();
            }
            else {
                storyListAdapter.notifyItemChanged(position);
            }
        });

        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Story")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getSupportFragmentManager(), "StoryEditor");
    }

    private void showLoadFrom() {
        LoadFromFragment content = new LoadFromFragment();
        content.setOnDatabaseChangedListener(() -> {
            RApplication.getInstance().reCreateGreenDao();
            viewModel.loadStories();
        });
        DraggableDialogFragment editDialog = new DraggableDialogFragment.Builder()
                .setTitle("Load from")
                .setShowDelete(false)
                .setContentFragment(content)
                .build();
        editDialog.show(getSupportFragmentManager(), "LoadFromFragment");
    }

}
