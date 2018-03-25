package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:12
 */
@Entity(nameInDb = "race")
public class Race {

    @Id(autoincrement = true)
    private Long id;

    private long storyId;

    private String name;

    private int type;

    private int sequence;

    private String description;

    @Generated(hash = 658109193)
    public Race(Long id, long storyId, String name, int type, int sequence,
            String description) {
        this.id = id;
        this.storyId = storyId;
        this.name = name;
        this.type = type;
        this.sequence = sequence;
        this.description = description;
    }

    @Generated(hash = 1909252487)
    public Race() {
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

}
