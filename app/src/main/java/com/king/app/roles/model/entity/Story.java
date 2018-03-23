package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

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

    @Generated(hash = 1228647423)
    public Story(Long id, String name, int type, int sequence) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sequence = sequence;
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
}
