package com.king.app.roles.page.role;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.model.entity.RoleRelationsDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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
public class RelationPresenter extends BasePresenter<RelationView> {

    private Role mRole;

    @Override
    protected void onCreate() {

    }

    public Role getRole() {
        return mRole;
    }

    public void loadRelations(long roleId) {
        view.showLoading();
        queryRole(roleId)
                .flatMap(new Function<Role, ObservableSource<List<RoleRelations>>>() {
                    @Override
                    public ObservableSource<List<RoleRelations>> apply(Role role) throws Exception {
                        mRole = role;
                        return queryRelations(role.getId());
                    }
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
                        view.dismissLoading();
                        view.showRelations(roleRelations);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        view.showMessage("Query error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Role> queryRole(final long roleId) {
        return Observable.create(new ObservableOnSubscribe<Role>() {
            @Override
            public void subscribe(ObservableEmitter<Role> e) throws Exception {
                RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
                Role role = dao.queryBuilder()
                        .where(RoleDao.Properties.Id.eq(roleId))
                        .build().unique();
                e.onNext(role);
            }
        });
    }

    private Observable<List<RoleRelations>> queryRelations(final long roleId) {
        return Observable.create(new ObservableOnSubscribe<List<RoleRelations>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RoleRelations>> e) throws Exception {
                RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
                QueryBuilder<RoleRelations> builder = dao.queryBuilder();
                List<RoleRelations> list = builder
                        .where(builder.or(RoleRelationsDao.Properties.RoleId.eq(roleId), RoleRelationsDao.Properties.RelationId.eq(roleId)))
                        .build().list();
                e.onNext(list);
            }
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
