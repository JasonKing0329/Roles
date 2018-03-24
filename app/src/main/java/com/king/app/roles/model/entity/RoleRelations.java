package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:26
 */
@Entity(nameInDb = "role_relations")
public class RoleRelations {

    @Id(autoincrement = true)
    private Long id;

    private long storyId;

    private long roleId;

    private long relationId;

    private int relationshipType;

    private String relationship;

    @Generated(hash = 1214852620)
    public RoleRelations(Long id, long storyId, long roleId, long relationId,
            int relationshipType, String relationship) {
        this.id = id;
        this.storyId = storyId;
        this.roleId = roleId;
        this.relationId = relationId;
        this.relationshipType = relationshipType;
        this.relationship = relationship;
    }

    @Generated(hash = 973540838)
    public RoleRelations() {
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

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getRelationId() {
        return this.relationId;
    }

    public void setRelationId(long relationId) {
        this.relationId = relationId;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getRelationshipType() {
        return this.relationshipType;
    }

    public void setRelationshipType(int relationshipType) {
        this.relationshipType = relationshipType;
    }
}
