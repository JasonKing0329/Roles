package com.king.app.roles.model.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/23 0023 22:13
 */
@Entity(nameInDb = "role")
public class Role {
    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String nickname;

    private long storyId;

    private long kingdomId;

    private long raceId;

    private long chapterId;

    /**
     * 核心层级
     */
    private int coreLevel;

    private String power;

    private String description;

    private int sequence;

    @ToOne(joinProperty = "chapterId")
    private Chapter chapter;

    @ToOne(joinProperty = "kingdomId")
    private Kingdom kingdom;

    @ToOne(joinProperty = "raceId")
    private Race race;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1785861362)
    private transient RoleDao myDao;

    @Generated(hash = 1243681789)
    public Role(Long id, String name, String nickname, long storyId, long kingdomId, long raceId,
            long chapterId, int coreLevel, String power, String description, int sequence) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.storyId = storyId;
        this.kingdomId = kingdomId;
        this.raceId = raceId;
        this.chapterId = chapterId;
        this.coreLevel = coreLevel;
        this.power = power;
        this.description = description;
        this.sequence = sequence;
    }

    @Generated(hash = 844947497)
    public Role() {
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

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public long getRaceId() {
        return this.raceId;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public long getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public int getCoreLevel() {
        return this.coreLevel;
    }

    public void setCoreLevel(int coreLevel) {
        this.coreLevel = coreLevel;
    }

    public String getPower() {
        return this.power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Generated(hash = 647692238)
    private transient Long chapter__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 453768668)
    public Chapter getChapter() {
        long __key = this.chapterId;
        if (chapter__resolvedKey == null || !chapter__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChapterDao targetDao = daoSession.getChapterDao();
            Chapter chapterNew = targetDao.load(__key);
            synchronized (this) {
                chapter = chapterNew;
                chapter__resolvedKey = __key;
            }
        }
        return chapter;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 776640969)
    public void setChapter(@NotNull Chapter chapter) {
        if (chapter == null) {
            throw new DaoException(
                    "To-one property 'chapterId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.chapter = chapter;
            chapterId = chapter.getId();
            chapter__resolvedKey = chapterId;
        }
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

    @Generated(hash = 661079782)
    private transient Long race__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 487172066)
    public Race getRace() {
        long __key = this.raceId;
        if (race__resolvedKey == null || !race__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RaceDao targetDao = daoSession.getRaceDao();
            Race raceNew = targetDao.load(__key);
            synchronized (this) {
                race = raceNew;
                race__resolvedKey = __key;
            }
        }
        return race;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1946042495)
    public void setRace(@NotNull Race race) {
        if (race == null) {
            throw new DaoException(
                    "To-one property 'raceId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.race = race;
            raceId = race.getId();
            race__resolvedKey = raceId;
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

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 957214601)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoleDao() : null;
    }
}