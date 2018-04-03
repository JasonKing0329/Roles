package com.king.app.roles.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/4/2 15:02
 */
public class BaseViewModule extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;

    public MutableLiveData<Boolean> loadingObserver = new MutableLiveData<>();
    public MutableLiveData<String> messageObserver = new MutableLiveData<>();

    public BaseViewModule(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}