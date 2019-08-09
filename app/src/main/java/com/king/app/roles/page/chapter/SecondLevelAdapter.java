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
 * <p/>创建时间: 2018/3/26 14:25
 */
public class SecondLevelAdapter extends AbstractExpandableAdapterItem implements View.OnClickListener {

    TextView tvName;
    TextView tvDescription;
    LinearLayout groupItem;
    ImageView ivEdit;
    ImageView ivFile;

    private OnChapterItemListener onChapterItemListener;

    public SecondLevelAdapter(OnChapterItemListener onChapterItemListener) {
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
        ivFile = root.findViewById(R.id.iv_file);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        Chapter chapter = (Chapter) model;
        if (TextUtils.isEmpty(chapter.getFile())) {
            ivFile.setVisibility(View.GONE);
        }
        else {
            ivFile.setVisibility(View.VISIBLE);
        }
        tvName.setText("    (" + chapter.getIndex() + ") " + chapter.getName());
        if (TextUtils.isEmpty(chapter.getDescription())) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText("    " + chapter.getDescription());
        }

        ItemInfo info = new ItemInfo();
        info.chapter = chapter;
        info.position = position;
        groupItem.setTag(info);
        groupItem.setOnClickListener(this);

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChapterItemListener.onEditSubItem(chapter, position);
            }
        });
    }

    private class ItemInfo {
        Chapter chapter;
        int position;
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public void onClick(View view) {

        ItemInfo info = (ItemInfo) view.getTag();
        onChapterItemListener.onClickChapter(info.chapter, info.position);
    }

}
