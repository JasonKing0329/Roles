package com.king.app.roles.page.login;

import com.king.app.roles.base.BaseView;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:03
 */
public interface LoginView extends BaseView {
    void showFingerPrint();

    void showLoginFrame();

    void permitLogin();
}
