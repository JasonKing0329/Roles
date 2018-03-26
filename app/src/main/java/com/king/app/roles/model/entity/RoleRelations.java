package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

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

    @ToOne(joinProperty = "roleId")
    private Role role1;

    @ToOne(joinProperty = "relationId")
    private Role role2;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 687624107)
    private transient RoleRelationsDao myDao;

    @Generated(hash = 1296319170)
    private transient Long role1__resolvedKey;

    @Generated(hash = 1717878753)
    private transient Long role2__resolvedKey;

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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 224659700)
    public Role getRole1() {
        long __key = this.roleId;
        if (role1__resolvedKey == null || !role1__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoleDao targetDao = daoSession.getRoleDao();
            Role role1New = targetDao.load(__key);
            synchronized (this) {
                role1 = role1New;
                role1__resolvedKey = __key;
            }
        }
        return role1;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1403411324)
    public void setRole1(@NotNull Role role1) {
        if (role1 == null) {
            throw new DaoException(
                    "To-one property 'roleId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.role1 = role1;
            roleId = role1.getId();
            role1__resolvedKey = roleId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1929882460)
    public Role getRole2() {
        long __key = this.relationId;
        if (role2__resolvedKey == null || !role2__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoleDao targetDao = daoSession.getRoleDao();
            Role role2New = targetDao.load(__key);
            synchronized (this) {
                role2 = role2New;
                role2__resolvedKey = __key;
            }
        }
        return role2;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 464542934)
    public void setRole2(@NotNull Role role2) {
        if (role2 == null) {
            throw new DaoException(
                    "To-one property 'relationId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.role2 = role2;
            relationId = role2.getId();
            role2__resolvedKey = relationId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1507424982)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoleRelationsDao() : null;
    }
}
