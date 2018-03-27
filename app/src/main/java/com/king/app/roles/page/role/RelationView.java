package com.king.app.roles.page.role;

import com.king.app.roles.base.BaseView;
import com.king.app.roles.model.entity.RoleRelations;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:36
 */
public interface RelationView extends BaseView {
    void showRelations(List<RoleRelations> roleRelations);
}
