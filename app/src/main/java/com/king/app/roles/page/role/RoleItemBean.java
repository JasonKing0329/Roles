package com.king.app.roles.page.role;

import com.king.app.roles.model.entity.Role;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/4/2 10:17
 */
public class RoleItemBean {

    private Role role;

    private String debut;

    private String relations;

    private String roleName;

    private String power;

    private String description;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getRelations() {
        return relations;
    }

    public void setRelations(String relations) {
        this.relations = relations;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
