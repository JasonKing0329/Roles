package com.king.app.roles.page.module;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;

import com.king.app.jactionbar.JActionbar;
import com.king.app.jactionbar.OnBackListener;
import com.king.app.roles.R;
import com.king.app.roles.base.MvvmActivity;
import com.king.app.roles.databinding.ActivityStoryModuleBinding;
import com.king.app.roles.page.chapter.ChapterFragment;
import com.king.app.roles.page.kingdom.KingdomFragment;
import com.king.app.roles.page.race.RaceFragment;
import com.king.app.roles.page.role.RoleFragment;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:43
 */

public class ModuleActivity extends MvvmActivity<ActivityStoryModuleBinding, ModuleViewModel> implements ModuleFragmentHolder {

    public static final String KEY_STORY_ID = "story_id";
    public static final String KEY_SELECT_MODE = "select_mode";
    public static final String KEY_PAGE_TYPE = "key_page_type";

    public static final String KEY_RESULT_ID = "key_result_id";

    public static final int PAGE_TYPE_RACE = 1;
    public static final int PAGE_TYPE_KINGDOM = 2;
    public static final int PAGE_TYPE_CHAPTER = 3;
    public static final int PAGE_TYPE_CHARACTER = 4;

    private ModuleFragment mFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_story_module;
    }

    @Override
    protected ModuleViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ModuleViewModel.class);
    }

    @Override
    protected void initView() {
        binding.actionbar.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initData() {
        initFragment();
    }

    private void initFragment() {
        int type = getIntent().getIntExtra(KEY_PAGE_TYPE, PAGE_TYPE_RACE);
        long storyId = getIntent().getLongExtra(KEY_STORY_ID, -1);
        boolean selectMode = getIntent().getBooleanExtra(KEY_SELECT_MODE, false);
        switch (type) {
            case PAGE_TYPE_RACE:
                mFragment = RaceFragment.newInstance(storyId);
                binding.actionbar.setTitle("Races");
                break;
            case PAGE_TYPE_KINGDOM:
                mFragment = KingdomFragment.newInstance(storyId);
                binding.actionbar.setTitle("Kingdoms");
                break;
            case PAGE_TYPE_CHAPTER:
                mFragment = ChapterFragment.newInstance(storyId, selectMode);
                binding.actionbar.setTitle("Chapters");
                break;
            case PAGE_TYPE_CHARACTER:
                mFragment = RoleFragment.newInstance(storyId, selectMode);
                binding.actionbar.setTitle("Characters");
                binding.actionbar.enableSearch();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.group_ft_container, mFragment, "ContentFragment")
                .commit();
    }

    @Override
    public JActionbar getJActionbar() {
        return binding.actionbar;
    }

    @Override
    public void onSelectId(long id) {
        Intent data = new Intent();
        data.putExtra(KEY_RESULT_ID, id);
        setResult(RESULT_OK, data);
        finish();
    }

}
