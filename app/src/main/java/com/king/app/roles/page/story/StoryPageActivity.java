package com.king.app.roles.page.story;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.MvpActivity;
import com.king.app.roles.view.dialog.DescriptionEditor;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/24 0024 10:39
 */

public class StoryPageActivity extends MvpActivity<StoryPagePresenter> implements StoryPageView {

    public static final String KEY_STORY_ID = "story_id";

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @Override
    protected int getContentView() {
        return R.layout.activity_story_page;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected StoryPagePresenter createPresenter() {
        return new StoryPagePresenter();
    }

    @Override
    protected void initData() {
        long storyId = getIntent().getLongExtra(KEY_STORY_ID, -1);
        presenter.loadStory(storyId);
    }

    @Override
    public void showStory(String name, String description) {
        tvTitle.setText(name);
        if (TextUtils.isEmpty(description)) {
            tvDescription.setText("No description, press to add one.");
        }
        else {
            tvDescription.setText(description);
        }
    }

    @OnClick({R.id.iv_back, R.id.group_race, R.id.group_kingdom, R.id.group_chapters, R.id.group_characters, R.id.group_modules
        , R.id.tv_description})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.group_race:
                break;
            case R.id.group_kingdom:
                break;
            case R.id.group_chapters:
                break;
            case R.id.group_characters:
                break;
            case R.id.group_modules:
                break;
            case R.id.tv_description:
                editDescription();
                break;
        }
    }

    private void editDescription() {
        DescriptionEditor editor = new DescriptionEditor();
        editor.setOnDescriptionListener(new DescriptionEditor.OnDescriptionListener() {
            @Override
            public String getInitContent() {
                return presenter.getDescription();
            }

            @Override
            public void onSaveDescription(String content) {
                presenter.saveDescription(content);
                tvDescription.setText(content);
                showMessage("Save successfully");
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Story outline")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getSupportFragmentManager(), "DraggableDialogFragment");
    }
}
