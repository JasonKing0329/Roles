package com.king.app.roles.page.module;

import com.king.app.jactionbar.JActionbar;
import com.king.app.roles.base.IFragmentHolder;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:50
 */

public interface ModuleFragmentHolder extends IFragmentHolder {

    JActionbar getJActionbar();

    void onSelectId(long id);

}
