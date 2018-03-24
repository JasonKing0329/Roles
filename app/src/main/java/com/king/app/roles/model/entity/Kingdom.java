package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:12
 */
@Entity(nameInDb = "kingdom")
public class Kingdom {
    @Id(autoincrement = true)
    private Long id;

    private long storyId;

    private String name;

    private String description;

    @Generated(hash = 1572364575)
    public Kingdom(Long id, long storyId, String name, String description) {
        this.id = id;
        this.storyId = storyId;
        this.name = name;
        this.description = description;
    }

    @Generated(hash = 1825001849)
    public Kingdom() {
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
