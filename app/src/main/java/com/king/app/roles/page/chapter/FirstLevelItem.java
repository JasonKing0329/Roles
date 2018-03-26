package com.king.app.roles.page.chapter;

import com.king.app.roles.model.entity.Chapter;
import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 14:14
 */
public class FirstLevelItem implements ExpandableListItem {

    private Chapter chapter;
    private List<Chapter> list;
    public boolean mExpanded = false;

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public List<?> getChildItemList() {
        return chapter.getChildren();
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpanded = isExpanded;
    }

    @Override
    public void loadChildItems() {
        chapter.getChildren();
    }
}
