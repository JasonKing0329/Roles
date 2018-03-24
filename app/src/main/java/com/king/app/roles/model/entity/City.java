package com.king.app.roles.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:12
 */
@Entity(nameInDb = "city")
public class City {
    @Id(autoincrement = true)
    private Long id;

    private String name;

    private long storyId;

    private long kingdomId;

    private String description;

    @ToOne(joinProperty = "kingdomId")
    private Kingdom kingdom;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 448079911)
    private transient CityDao myDao;

    @Generated(hash = 723665968)
    public City(Long id, String name, long storyId, long kingdomId, String description) {
        this.id = id;
        this.name = name;
        this.storyId = storyId;
        this.kingdomId = kingdomId;
        this.description = description;
    }

    @Generated(hash = 750791287)
    public City() {
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

    public long getStoryId() {
        return this.storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public long getKingdomId() {
        return this.kingdomId;
    }

    public void setKingdomId(long kingdomId) {
        this.kingdomId = kingdomId;
    }

    @Generated(hash = 888058385)
    private transient Long kingdom__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 763892181)
    public Kingdom getKingdom() {
        long __key = this.kingdomId;
        if (kingdom__resolvedKey == null || !kingdom__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            KingdomDao targetDao = daoSession.getKingdomDao();
            Kingdom kingdomNew = targetDao.load(__key);
            synchronized (this) {
                kingdom = kingdomNew;
                kingdom__resolvedKey = __key;
            }
        }
        return kingdom;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1196182469)
    public void setKingdom(@NotNull Kingdom kingdom) {
        if (kingdom == null) {
            throw new DaoException(
                    "To-one property 'kingdomId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.kingdom = kingdom;
            kingdomId = kingdom.getId();
            kingdom__resolvedKey = kingdomId;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 293508440)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCityDao() : null;
    }
}
