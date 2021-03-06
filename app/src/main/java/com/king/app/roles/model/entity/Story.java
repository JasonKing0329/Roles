package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:22
 */
@Entity(nameInDb = "stories")
public class Story {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private int type;

    private int sequence;

    private String description;

    @Generated(hash = 1542555605)
    public Story(Long id, String name, int type, int sequence, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sequence = sequence;
        this.description = description;
    }

    @Generated(hash = 922655990)
    public Story() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
