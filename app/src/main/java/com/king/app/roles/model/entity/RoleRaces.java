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
 * @time 2018/3/23 0023 22:26
 */
@Entity(nameInDb = "role_races")
public class RoleRaces {

    @Id(autoincrement = true)
    private Long id;

    private long roleId;

    private long raceId;

    @Generated(hash = 376078509)
    public RoleRaces(Long id, long roleId, long raceId) {
        this.id = id;
        this.roleId = roleId;
        this.raceId = raceId;
    }

    @Generated(hash = 669297054)
    public RoleRaces() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getRaceId() {
        return this.raceId;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

}
