package com.king.app.roles.page.story;

import com.king.app.roles.base.BaseView;
import com.king.app.roles.model.entity.Story;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:32
 */
public interface StoryListView extends BaseView {
    void showStories(List<Story> stories);

    void deleteDone();

    void updateDone();
}
