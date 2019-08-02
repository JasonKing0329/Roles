package com.king.app.roles.page.role;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.model.entity.RoleRelationsDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/27 9:37
 */
public class RelationViewModel extends BaseViewModel {

    private Role mRole;

    public MutableLiveData<List<RoleRelations>> relationsObserver = new MutableLiveData<>();

    public RelationViewModel(@NonNull Application application) {
        super(application);
    }

    public Role getRole() {
        return mRole;
    }

    public void loadRelations(long roleId) {
        loadingObserver.setValue(true);
        queryRole(roleId)
                .flatMap((Function<Role, ObservableSource<List<RoleRelations>>>) role -> {
                    mRole = role;
                    return queryRelations(role.getId());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RoleRelations>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<RoleRelations> roleRelations) {
                        loadingObserver.setValue(false);
                        relationsObserver.setValue(roleRelations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadingObserver.setValue(false);
                        messageObserver.setValue("Query error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Role> queryRole(final long roleId) {
        return Observable.create(e -> {
            RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
            Role role = dao.queryBuilder()
                    .where(RoleDao.Properties.Id.eq(roleId))
                    .build().unique();
            e.onNext(role);
        });
    }

    private Observable<List<RoleRelations>> queryRelations(final long roleId) {
        return Observable.create(e -> {
            RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
            QueryBuilder<RoleRelations> builder = dao.queryBuilder();
            List<RoleRelations> list = builder
                    .where(builder.or(RoleRelationsDao.Properties.RoleId.eq(roleId), RoleRelationsDao.Properties.RelationId.eq(roleId)))
                    .build().list();
            e.onNext(list);
        });
    }

    public void insertOrUpdateRelation(RoleRelations relations) {
        RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
        dao.insertOrReplace(relations);
    }

    public void deleteRelation(RoleRelations relations) {
        RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
        dao.delete(relations);
    }
}
