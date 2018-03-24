package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:31
 */
@Entity(nameInDb = "chapter")
public class Chapter {
    @Id(autoincrement = true)
    private Long id;

    private long storyId;

    private int level;

    private int index;

    private String name;

    private String description;

    @Generated(hash = 1763325670)
    public Chapter(Long id, long storyId, int level, int index, String name,
            String description) {
        this.id = id;
        this.storyId = storyId;
        this.level = level;
        this.index = index;
        this.name = name;
        this.description = description;
    }

    @Generated(hash = 393170288)
    public Chapter() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStoryId() {
        return this.storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
