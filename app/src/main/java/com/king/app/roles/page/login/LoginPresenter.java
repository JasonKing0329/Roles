package com.king.app.roles.page.login;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.model.SettingProperty;
import com.king.app.roles.utils.FileUtil;
import com.king.app.roles.utils.MD5Util;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:04
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    @Override
    protected void onCreate() {

    }

    public void prepare() {
        view.showLoading();
        prepareDatas()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Object o) {
                        view.dismissLoading();

                        RApplication.getInstance().createGreenDao();
                        if (SettingProperty.isEnableFingerPrint()) {
                            view.showFingerPrint();
                        }
                        else {
                            view.showLoginFrame();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.dismissLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Object> prepareDatas() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                // 创建base目录
                for (String path: AppConfig.DIRS) {
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                }

                // 检查数据库是否存在
                FileUtil.copyDbFromAssets(AppConfig.DB_NAME);

                // init server url
//                BaseUrl.getInstance().setBaseUrl(SettingProperty.getServerBaseUrl());

                e.onNext(new Object());
                e.onComplete();
            }
        });
    }

    public void checkPassword(String pwd) {
        if ("38D08341D686315F".equals(MD5Util.get16MD5Capital(pwd))) {
            view.permitLogin();
        }
        else {
            view.showMessage("密码错误");
        }
    }
}
