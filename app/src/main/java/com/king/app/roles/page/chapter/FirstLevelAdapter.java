package com.king.app.roles.page.chapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.model.entity.Chapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 14:35
 */
public class FirstLevelAdapter extends AbstractExpandableAdapterItem {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.group_item)
    LinearLayout groupItem;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_chapter_item_second;
    }

    @Override
    public void onBindViews(View root) {
        ButterKnife.bind(this, root);
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
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        FirstLevelItem item = (FirstLevelItem) model;
        Chapter chapter = item.getChapter();
        tvName.setText("第" + chapter.getIndex() + "章 " + chapter.getName());
        if (TextUtils.isEmpty(chapter.getDescription())) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(chapter.getDescription());
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
