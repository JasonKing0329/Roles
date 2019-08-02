package com.king.app.roles.page.chapter;

import android.view.View;
import android.widget.AdapterView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.databinding.DialogChapterEditorBinding;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.ChapterDao;
import com.king.app.roles.view.dialog.DraggableHolder;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class ChapterEditor extends MvvmFragment<DialogChapterEditorBinding, BaseViewModel> {

    private Chapter mChapter;

    private OnChapterListener onChapterListener;

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_chapter_editor;
    }

    @Override
    protected void onCreate(View view) {
        binding.etParentIndex.setVisibility(View.GONE);

        binding.spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 1) {
                    binding.etParentIndex.setVisibility(View.VISIBLE);
                }
                else {
                    binding.etParentIndex.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvOk.setOnClickListener(v -> onClickOk());
    }

    @Override
    protected void onCreateData() {
        if (onChapterListener != null) {
            mChapter = onChapterListener.getInitChapter();
            if (mChapter != null) {
                binding.etName.setText(mChapter.getName());
                binding.etIndex.setText(String.valueOf(mChapter.getIndex()));
                if (mChapter.getParentId() != 0) {
                    binding.etParentIndex.setText(String.valueOf(mChapter.getParent().getIndex()));
                }
                binding.etDescription.setText(mChapter.getDescription());
                if (mChapter.getLevel() == AppConstants.CHAPTER_LEVEL_FIRST) {
                    binding.spLevel.setSelection(0);
                }
                else {
                    binding.spLevel.setSelection(1);
                }
            }
        }
    }

    public void setOnChapterListener(OnChapterListener onChapterListener) {
        this.onChapterListener = onChapterListener;
    }

    private void onClickOk() {
        if (onChapterListener != null) {
            if (mChapter == null) {
                mChapter = new Chapter();
            }
            if (!checkIndex()) {
                return;
            }
            if (binding.spLevel.getSelectedItemPosition() == 0) {
                mChapter.setLevel(AppConstants.CHAPTER_LEVEL_FIRST);
            }
            else {
                mChapter.setLevel(AppConstants.CHAPTER_LEVEL_SECOND);
            }
            mChapter.setName(binding.etName.getText().toString());
            mChapter.setDescription(binding.etDescription.getText().toString());
            onChapterListener.onSaveChapter(mChapter);
        }
        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    private boolean checkIndex() {

        String index = binding.etIndex.getText().toString();
        try {
            mChapter.setIndex(Integer.parseInt(index));
        } catch (Exception e) {
            binding.etIndex.setError("error data");
            return false;
        }

        if (binding.etParentIndex.getVisibility() == View.VISIBLE) {
            String parentIndex = binding.etParentIndex.getText().toString();
            try {
                int pIndex = Integer.parseInt(parentIndex);
                long parentId = queryParentByIndex(pIndex);
                if (parentId == 0) {
                    binding.etParentIndex.setError("parent is not existed");
                    return false;
                }
                mChapter.setParentId(parentId);
            } catch (Exception e) {
                binding.etParentIndex.setError("error data");
                return false;
            }
        }
        return true;
    }

    private long queryParentByIndex(int pIndex) {
        ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
        try {
            Chapter chapter = dao.queryBuilder()
                    .where(ChapterDao.Properties.StoryId.eq(onChapterListener.getStoryId())
                        , ChapterDao.Properties.Level.eq(AppConstants.CHAPTER_LEVEL_FIRST)
                        , ChapterDao.Properties.Index.eq(pIndex))
                    .build().unique();
            return chapter.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public interface OnChapterListener {
        void onSaveChapter(Chapter chapter);
        long getStoryId();
        Chapter getInitChapter();
    }
}
