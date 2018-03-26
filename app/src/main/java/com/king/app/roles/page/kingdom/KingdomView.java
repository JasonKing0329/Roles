package com.king.app.roles.page.kingdom;

import com.king.app.roles.base.BaseView;
import com.king.app.roles.model.entity.Kingdom;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:22
 */
public interface KingdomView extends BaseView {
    void showKingdoms(List<Kingdom> kingdoms);
}
