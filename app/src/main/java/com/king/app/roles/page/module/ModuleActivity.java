package com.king.app.roles.page.module;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseActivity;
import com.king.app.roles.page.chapter.ChapterFragment;
import com.king.app.roles.page.kingdom.KingdomFragment;
import com.king.app.roles.page.race.RaceFragment;
import com.king.app.roles.page.role.RoleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:43
 */

public class ModuleActivity extends BaseActivity implements ModuleFragmentHolder {

    public static final String KEY_STORY_ID = "story_id";
    public static final String KEY_SELECT_MODE = "select_mode";
    public static final String KEY_PAGE_TYPE = "key_page_type";

    public static final String KEY_RESULT_ID = "key_result_id";

    public static final int PAGE_TYPE_RACE = 1;
    public static final int PAGE_TYPE_KINGDOM = 2;
    public static final int PAGE_TYPE_CHAPTER = 3;
    public static final int PAGE_TYPE_CHARACTER = 4;

    private boolean isDeleteAction;
    private boolean isDragAction;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_move)
    ImageView ivMove;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.group_icon)
    LinearLayout groupIcon;
    @BindView(R.id.group_confirm)
    LinearLayout groupConfirm;
    @BindView(R.id.group_ft_container)
    FrameLayout groupFtContainer;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.group_search)
    RelativeLayout groupSearch;

    private ModuleFragment mFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_story_module;
    }

    @Override
    protected void initView() {
        groupConfirm.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);
        initFragment();
    }

    private void initFragment() {
        int type = getIntent().getIntExtra(KEY_PAGE_TYPE, PAGE_TYPE_RACE);
        long storyId = getIntent().getLongExtra(KEY_STORY_ID, -1);
        boolean selectMode = getIntent().getBooleanExtra(KEY_SELECT_MODE, false);
        switch (type) {
            case PAGE_TYPE_RACE:
                mFragment = RaceFragment.newInstance(storyId);
                tvTitle.setText("Races");
                break;
            case PAGE_TYPE_KINGDOM:
                mFragment = KingdomFragment.newInstance(storyId);
                tvTitle.setText("Kingdoms");
                break;
            case PAGE_TYPE_CHAPTER:
                mFragment = ChapterFragment.newInstance(storyId, selectMode);
                tvTitle.setText("Chapters");
                break;
            case PAGE_TYPE_CHARACTER:
                mFragment = RoleFragment.newInstance(storyId, selectMode);
                tvTitle.setText("Characters");
                ivSearch.setVisibility(View.VISIBLE);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.group_ft_container, mFragment, "ContentFragment")
                .commit();
    }

    @OnClick({R.id.iv_delete, R.id.iv_move, R.id.iv_add, R.id.tv_cancel, R.id.tv_ok, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_delete:
                isDeleteAction = true;
                mFragment.setDeleteMode(true);
                showActionConfirm(true);
                break;
            case R.id.iv_move:
                isDragAction = true;
                mFragment.setDragMode(true);
                showActionConfirm(true);
                break;
            case R.id.iv_add:
                mFragment.addNewItem();
                break;
            case R.id.tv_cancel:
                if (isDeleteAction) {
                    cancelDelete();
                } else if (isDragAction) {
                    cancelDrag();
                }
                break;
            case R.id.tv_ok:
                if (isDeleteAction) {
                    doDelete();
                } else if (isDragAction) {
                    doDrag();
                }
                break;
        }
    }

    @OnClick({R.id.iv_search, R.id.iv_search_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                showSearchBar();
                break;
            case R.id.iv_search_close:
                hideSearchBar();
                break;
        }
    }

    private void showSearchBar() {
        if (groupSearch.getVisibility() != View.VISIBLE) {
            groupSearch.setVisibility(View.VISIBLE);
            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(500);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            groupSearch.startAnimation(animation);
        }
    }

    private void hideSearchBar() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupSearch.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        groupSearch.startAnimation(animation);
    }

    @Override
    public void registerTextWatcher(TextWatcher textWatcher) {
        etSearch.addTextChangedListener(textWatcher);
    }

    private void showActionConfirm(boolean show) {
        if (show) {
            groupConfirm.setVisibility(View.VISIBLE);
            groupIcon.setVisibility(View.GONE);
        } else {
            groupConfirm.setVisibility(View.GONE);
            groupIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancelDelete() {
        isDeleteAction = false;
        mFragment.setDeleteMode(false);
        showActionConfirm(false);
    }

    @Override
    public void cancelDrag() {
        isDragAction = false;
        mFragment.setDragMode(false);
        showActionConfirm(false);
    }

    private void doDrag() {
        mFragment.confirmDrag();
    }

    private void doDelete() {
        mFragment.confirmDelete();
    }

    @Override
    public void onSelectId(long id) {
        Intent data = new Intent();
        data.putExtra(KEY_RESULT_ID, id);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void hideDeleteAction() {
        ivDelete.setVisibility(View.GONE);
    }

    @Override
    public void hideDragAction() {
        ivMove.setVisibility(View.GONE);
    }

}
