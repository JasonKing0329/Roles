package com.king.app.roles.page.chapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.model.entity.Chapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 14:35
 */
public class FirstLevelAdapter extends AbstractExpandableAdapterItem {

    TextView tvName;
    TextView tvDescription;
    LinearLayout groupItem;
    ImageView ivEdit;

    private OnChapterItemListener onChapterItemListener;

    public FirstLevelAdapter(OnChapterItemListener onChapterItemListener) {
        super();
        this.onChapterItemListener = onChapterItemListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_chapter_item_second;
    }

    @Override
    public void onBindViews(View root) {
        groupItem = root.findViewById(R.id.group_item);
        tvName = root.findViewById(R.id.tv_name);
        tvDescription = root.findViewById(R.id.tv_description);
        ivEdit = root.findViewById(R.id.iv_edit);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, final int position) {
        super.onUpdateViews(model, position);
        FirstLevelItem item = (FirstLevelItem) model;
        final Chapter chapter = item.getChapter();
        tvName.setText("第" + chapter.getIndex() + "章 " + chapter.getName());
        if (TextUtils.isEmpty(chapter.getDescription())) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(chapter.getDescription());
        }

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChapterItemListener.onEditItem(chapter, position);
            }
        });
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
