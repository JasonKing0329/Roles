package com.king.app.roles.page.chapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.king.app.jactionbar.OnMenuItemListener;
import com.king.app.roles.R;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.view.dialog.DraggableDialogFragment;
import com.king.app.roles.view.dialog.SimpleDialogs;

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
    private DraggableDialogFragment editorDialog;

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
        holder.getJActionbar().inflateMenu(R.menu.page_chapter);
        holder.getJActionbar().setOnMenuItemListener(new OnMenuItemListener() {
            @Override
            public void onMenuItemSelected(int menuId) {
                switch (menuId) {
                    case R.id.menu_add:
                        addNewItem();
                        break;
                }
            }
        });
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

            @Override
            public void onEditItem(Chapter chapter, int position) {
                editChapter(chapter);
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
        DraggableDialogFragment.Builder builder = new DraggableDialogFragment.Builder()
                .setTitle("Chapter")
                .setContentFragment(editor);
        if (chapter != null && chapter.getId() != null) {
            builder.setShowDelete(true)
                    .setOnDeleteListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteChapter(chapter);
                        }
                    });
        }
        editorDialog = builder.build();
        editorDialog.show(getChildFragmentManager(), "DraggableDialogFragment");
    }

    private void deleteChapter(final Chapter chapter) {
        new SimpleDialogs().showWarningActionDialog(getActivity()
                , "Delete chapter will delete all related data, click ok to continue"
                , getString(R.string.ok), null
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == DialogInterface.BUTTON_POSITIVE) {
                            presenter.delete(chapter);
                            loadData();
                            editorDialog.dismiss();
                        }
                    }
                });
    }

}
