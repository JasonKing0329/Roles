package com.king.app.roles.page.role;

import com.king.app.roles.base.BaseView;
import com.king.app.roles.model.entity.Role;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 10:03
 */
public interface RoleView extends BaseView {
    void showRole(List<RoleItemBean> roles);
}
