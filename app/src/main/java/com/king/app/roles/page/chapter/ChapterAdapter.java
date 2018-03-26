package com.king.app.roles.page.chapter;

import android.support.annotation.NonNull;

import com.king.app.roles.model.entity.Chapter;
import com.zaihuishou.expandablerecycleradapter.adapter.BaseExpandableAdapter;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:07
 */
public class ChapterAdapter extends BaseExpandableAdapter {

    private final int ITEM_TYPE_HEAD = 1;
    private final int ITEM_TYPE_ITEM = 2;

    private OnChapterItemListener onChapterItemListener;

    /**
     *
     * @param data
     */
    protected ChapterAdapter(List<FirstLevelItem> data) {
        super(data);
    }

    public void setOnChapterItemListener(OnChapterItemListener onChapterItemListener) {
        this.onChapterItemListener = onChapterItemListener;
    }

    @NonNull
    @Override
    public AbstractAdapterItem<Object> getItemView(Object type) {
        int itemType = (int) type;
        switch (itemType) {
            case ITEM_TYPE_HEAD:
                return new FirstLevelAdapter();
            case ITEM_TYPE_ITEM:
                return new SecondLevelAdapter(onChapterItemListener);
        }
        return null;
    }

    @Override
    public Object getItemViewType(Object t) {
        if (t instanceof FirstLevelItem) {
            return ITEM_TYPE_HEAD;
        }
        else if (t instanceof Chapter) {
            return ITEM_TYPE_ITEM;
        }
        return -1;
    }

}
