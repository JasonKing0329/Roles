package com.king.app.roles.page.chapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.ChapterDao;
import com.king.app.roles.view.dialog.DraggableHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class ChapterEditor extends BaseFragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_index)
    EditText etIndex;
    @BindView(R.id.et_parent_index)
    EditText etParentIndex;
    @BindView(R.id.sp_level)
    Spinner spLevel;

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
    protected int getContentLayoutRes() {
        return R.layout.dialog_chapter_editor;
    }

    @Override
    protected void onCreate(View view) {
        etParentIndex.setVisibility(View.GONE);

        spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 1) {
                    etParentIndex.setVisibility(View.VISIBLE);
                }
                else {
                    etParentIndex.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (onChapterListener != null) {
            mChapter = onChapterListener.getInitChapter();
            if (mChapter != null) {
                etName.setText(mChapter.getName());
                etDescription.setText(mChapter.getDescription());
                if (mChapter.getLevel() == AppConstants.CHAPTER_LEVEL_FIRST) {
                    spLevel.setSelection(0);
                }
                else {
                    spLevel.setSelection(1);
                }
            }
        }
    }

    public void setOnChapterListener(OnChapterListener onChapterListener) {
        this.onChapterListener = onChapterListener;
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if (onChapterListener != null) {
            if (mChapter == null) {
                mChapter = new Chapter();
            }
            if (!checkIndex()) {
                return;
            }
            if (spLevel.getSelectedItemPosition() == 0) {
                mChapter.setLevel(AppConstants.CHAPTER_LEVEL_FIRST);
            }
            else {
                mChapter.setLevel(AppConstants.CHAPTER_LEVEL_SECOND);
            }
            mChapter.setName(etName.getText().toString());
            mChapter.setDescription(etDescription.getText().toString());
            onChapterListener.onSaveChapter(mChapter);
        }
        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    private boolean checkIndex() {

        String index = etIndex.getText().toString();
        try {
            mChapter.setIndex(Integer.parseInt(index));
        } catch (Exception e) {
            etIndex.setError("error data");
            return false;
        }

        if (etParentIndex.getVisibility() == View.VISIBLE) {
            String parentIndex = etParentIndex.getText().toString();
            try {
                int pIndex = Integer.parseInt(parentIndex);
                long parentId = queryParentByIndex(pIndex);
                if (parentId == 0) {
                    etParentIndex.setError("parent is not existed");
                    return false;
                }
                mChapter.setParentId(parentId);
            } catch (Exception e) {
                etParentIndex.setError("error data");
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
