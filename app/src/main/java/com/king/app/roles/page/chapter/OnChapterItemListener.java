package com.king.app.roles.page.chapter;

import com.king.app.roles.model.entity.Chapter;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 16:44
 */
public interface OnChapterItemListener {
    void onClickChapter(Chapter chapter, int positionInList);

    void onEditItem(Chapter chapter, int position);

    void onEditSubItem(Chapter chapter, int position);
}
