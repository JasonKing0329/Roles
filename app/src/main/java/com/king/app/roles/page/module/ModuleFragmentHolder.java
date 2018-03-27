package com.king.app.roles.page.module;

import android.text.TextWatcher;

import com.king.app.roles.base.IFragmentHolder;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:50
 */

public interface ModuleFragmentHolder extends IFragmentHolder {
    void cancelDrag();

    void cancelDelete();

    void onSelectId(long id);

    void hideDeleteAction();

    void hideDragAction();

    void registerTextWatcher(TextWatcher textWatcher);
}
