package com.king.app.roles.page.module;

import android.app.Application;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.king.app.roles.base.BaseViewModel;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/4/4 10:44
 */
public class ModuleViewModel extends BaseViewModel {

    public ObservableInt draggableVisibility;
    public ObservableInt normalVisibility;

    public ModuleViewModel(@NonNull Application application) {
        super(application);
        draggableVisibility = new ObservableInt();
        normalVisibility = new ObservableInt();
    }
}
