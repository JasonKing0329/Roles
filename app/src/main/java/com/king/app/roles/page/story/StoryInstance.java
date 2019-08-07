package com.king.app.roles.page.story;

import com.king.app.roles.model.entity.Story;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/7 16:08
 */
public class StoryInstance {

    private static StoryInstance instance;

    private Story story;

    public static StoryInstance getInstance() {
        if (instance == null) {
            instance = new StoryInstance();
        }
        return instance;
    }

    private StoryInstance() {

    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public void destroy() {
        story = null;
        instance = null;
    }
}
