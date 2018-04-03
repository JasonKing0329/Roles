package com.king.app.roles.page.story;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.king.app.jactionbar.OnBackListener;
import com.king.app.roles.R;
import com.king.app.roles.base.MvvmActivity;
import com.king.app.roles.databinding.ActivityStoryPageBinding;
import com.king.app.roles.page.module.ModuleActivity;
import com.king.app.roles.view.dialog.DescriptionEditor;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/24 0024 10:39
 */

public class StoryPageActivity extends MvvmActivity<ActivityStoryPageBinding, StoryPageViewModel> {

    public static final String KEY_STORY_ID = "story_id";

    @Override
    protected int getContentView() {
        return R.layout.activity_story_page;
    }

    @Override
    protected void initView() {
        binding.actionbar.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }
        });
        binding.tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDescription();
            }
        });
    }

    @Override
    protected StoryPageViewModel createViewModel() {
        return ViewModelProviders.of(this).get(StoryPageViewModel.class);
    }

    @Override
    protected void initData() {
        binding.setViewModel(viewModel);
        long storyId = getIntent().getLongExtra(KEY_STORY_ID, -1);
        viewModel.loadStory(storyId);
        viewModel.moduleObserver.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                startModuleActivity(integer);
            }
        });
    }

    private void startModuleActivity(int type) {
        Intent intent = new Intent().setClass(this, ModuleActivity.class);
        intent.putExtra(ModuleActivity.KEY_STORY_ID, getIntent().getLongExtra(KEY_STORY_ID, -1));
        intent.putExtra(ModuleActivity.KEY_PAGE_TYPE, type);
        startActivity(intent);
    }

    private void editDescription() {
        DescriptionEditor editor = new DescriptionEditor();
        editor.setOnDescriptionListener(new DescriptionEditor.OnDescriptionListener() {
            @Override
            public String getInitContent() {
                return viewModel.getDescription();
            }

            @Override
            public void onSaveDescription(String content) {
                viewModel.saveDescription(content);
                binding.tvDescription.setText(content);
                showMessageLong("Save successfully");
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Story outline")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getSupportFragmentManager(), "DraggableDialogFragment");
    }
}
