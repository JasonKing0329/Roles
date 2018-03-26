package com.king.app.roles.page.chapter;

import android.os.Bundle;
import android.view.View;

import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 14:52
 */
public class ChapterFragment extends ModuleFragment<ChapterPresenter> implements ChapterView {

    public static ChapterFragment newInstance(long storyId, boolean selectMode) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        bundle.putBoolean(KEY_SELECT_MODE, selectMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ChapterAdapter chapterAdapter;

    @Override
    protected boolean isSupportDragList() {
        return false;
    }

    @Override
    protected ChapterPresenter createPresenter() {
        return new ChapterPresenter();
    }

    @Override
    protected void onCreate(View view) {

    }

    @Override
    protected void loadData() {
        presenter.loadChapters(getStoryId());
    }

    @Override
    public void showChapters(List<FirstLevelItem> list) {
        chapterAdapter = new ChapterAdapter(list);
        chapterAdapter.setOnChapterItemListener(new OnChapterItemListener() {
            @Override
            public void onClickChapter(Chapter chapter, int positionInList) {
                if (isSelectMode()) {
                    onSelectId(chapter.getId());
                }
                else {
                    editChapter(chapter);
                }
            }
        });
        rvItemsNormal.setAdapter(chapterAdapter);
    }

    @Override
    protected ModuleAdapter getAdapter() {
        return null;
    }

    @Override
    public void addNewItem() {
        editChapter(null);
    }

    private void editChapter(final Chapter chapter) {
        ChapterEditor editor = new ChapterEditor();
        editor.setOnChapterListener(new ChapterEditor.OnChapterListener() {
            @Override
            public void onSaveChapter(Chapter chapter) {
                presenter.insertOrUpdate(chapter);
                loadData();
                showMessage("Save successfully");
            }

            @Override
            public long getStoryId() {
                return getArguments().getLong(KEY_STORY_ID);
            }

            @Override
            public Chapter getInitChapter() {
                return chapter;
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Chapter")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getChildFragmentManager(), "DraggableDialogFragment");
    }

    @Override
    public void confirmDrag() {

    }

    @Override
    public void confirmDelete() {

    }
}
