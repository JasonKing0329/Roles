package com.king.app.roles.page.chapter;

import com.king.app.roles.base.BaseView;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:22
 */
public interface ChapterView extends BaseView {

    void showChapters(List<FirstLevelItem> list);
}
