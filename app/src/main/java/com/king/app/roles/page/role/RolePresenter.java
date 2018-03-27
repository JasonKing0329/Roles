package com.king.app.roles.page.role;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRaces;
import com.king.app.roles.model.entity.RoleRacesDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.model.entity.RoleRelationsDao;
import com.king.app.roles.utils.ListUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:43
 */

public class RolePresenter extends BasePresenter<RoleView> {

    private long mStoryId;

    @Override
    protected void onCreate() {

    }

    public void loadRoles(long storyId) {
        mStoryId = storyId;
        queryRoles(storyId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Role>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Role> roles) {
                        view.showRole(roles);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<List<Role>> queryRoles(final long storyId) {
        return Observable.create(new ObservableOnSubscribe<List<Role>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Role>> e) throws Exception {
                RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
                List<Role> list = dao.queryBuilder()
                        .where(RoleDao.Properties.StoryId.eq(storyId))
                        .orderAsc(RoleDao.Properties.Sequence)
                        .build().list();
                e.onNext(list);
            }
        });
    }

    public void insertOrUpdate(Role role, List<Race> raceList, Kingdom kingdom) {
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        RoleRacesDao roleRacesDao = RApplication.getInstance().getDaoSession().getRoleRacesDao();
        if (role.getId() == null) {
            role.setStoryId(mStoryId);
            role.setSequence((int) dao.count());
        }
        else {
            // delete from role_races
            roleRacesDao.queryBuilder()
                    .where(RoleRacesDao.Properties.RoleId.eq(role.getId()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            roleRacesDao.detachAll();
        }
        if (kingdom != null) {
            role.setKingdomId(kingdom.getId());
        }
        // insert or update role
        dao.insertOrReplace(role);
        dao.detachAll();

        // insert to role_races
        if (!ListUtil.isEmpty(raceList)) {
            for (Race race:raceList) {
                RoleRaces rr = new RoleRaces();
                rr.setRoleId(role.getId());
                rr.setRaceId(race.getId());
                roleRacesDao.insert(rr);
            }
        }
    }

    public void confirmDrag(List<Role> list) {
        for (int i = 0; i < list.size(); i ++) {
            list.get(i).setSequence(i + 1);
        }
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        dao.updateInTx(list);
        dao.detachAll();
    }

    public void confirmDelete(List<Role> list) {
        // delete from role
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        dao.deleteInTx(list);
        dao.detachAll();

        RoleRacesDao roleRacesDao = RApplication.getInstance().getDaoSession().getRoleRacesDao();
        RoleRelationsDao relationsDao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
        for (Role role:list) {
            // delete from role_races
            roleRacesDao.queryBuilder()
                    .where(RoleRacesDao.Properties.RoleId.eq(role.getId()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            roleRacesDao.detachAll();

            // delete from role_relations
            QueryBuilder<RoleRelations> builder = relationsDao.queryBuilder();
            builder.where(builder.or(RoleRelationsDao.Properties.RoleId.eq(role.getId()), RoleRelationsDao.Properties.RelationId.eq(role.getId())))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            relationsDao.detachAll();
        }
    }
}
