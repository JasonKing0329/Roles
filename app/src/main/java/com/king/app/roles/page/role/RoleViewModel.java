package com.king.app.roles.page.role;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleDao;
import com.king.app.roles.model.entity.RoleRaces;
import com.king.app.roles.model.entity.RoleRacesDao;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.model.entity.RoleRelationsDao;
import com.king.app.roles.page.module.ModuleViewModel;
import com.king.app.roles.utils.ListUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
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
 * <p/>创建时间: 2018/4/4 13:41
 */
public class RoleViewModel extends ModuleViewModel {

    private long mStoryId;

    public MutableLiveData<List<RoleItemBean>> rolesObserver;

    public RoleViewModel(@NonNull Application application) {
        super(application);
        rolesObserver = new MutableLiveData<>();
        draggableVisibility.set(View.VISIBLE);
        normalVisibility.set(View.GONE);
    }

    public void loadRoles(long storyId) {
        mStoryId = storyId;
        queryRoles(storyId, AppConstants.ROLE_SORT_BY_SEQUENCE)
                .flatMap(new Function<List<Role>, ObservableSource<List<RoleItemBean>>>() {
                    @Override
                    public ObservableSource<List<RoleItemBean>> apply(List<Role> roles) throws Exception {
                        return parseRoles(roles);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RoleItemBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<RoleItemBean> roles) {
                        rolesObserver.setValue(roles);
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

    private Observable<List<Role>> queryRoles(final long storyId, final int orderType) {
        return Observable.create(new ObservableOnSubscribe<List<Role>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Role>> e) throws Exception {
                RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
                QueryBuilder<Role> builder = dao.queryBuilder();
                builder.where(RoleDao.Properties.StoryId.eq(storyId));
                if (orderType == AppConstants.ROLE_SORT_BY_NAME) {
                    builder.orderAsc(RoleDao.Properties.Name);
                }
                else {
                    builder.orderAsc(RoleDao.Properties.Sequence);
                }
                List<Role> list = builder
                        .build().list();
                e.onNext(list);
            }
        });
    }

    private Observable<List<RoleItemBean>> parseRoles(final List<Role> list) {
        return Observable.create(new ObservableOnSubscribe<List<RoleItemBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RoleItemBean>> e) throws Exception {
                List<RoleItemBean> roleItemBeans = new ArrayList<>();
                RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
                for (Role role:list) {
                    RoleItemBean bean = new RoleItemBean();
                    bean.setRole(role);
                    bean.setDebut(getDebutText(role.getChapter()));

                    QueryBuilder<RoleRelations> builder = dao.queryBuilder();
                    int number = (int) builder
                            .where(builder.or(RoleRelationsDao.Properties.RoleId.eq(role.getId()), RoleRelationsDao.Properties.RelationId.eq(role.getId())))
                            .buildCount().count();
                    bean.setRelations(number);

                    roleItemBeans.add(bean);
                }

                e.onNext(roleItemBeans);
            }
        });
    }

    private String getDebutText(Chapter chapter) {
        if (chapter == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        if (chapter.getParent() != null) {
            buffer.append("第").append(chapter.getParent().getIndex()).append("章 ").append(chapter.getParent().getName()).append(" - ");
        }
        buffer.append("(").append(chapter.getIndex()).append(")").append(chapter.getName());
        return buffer.toString();
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

    public void confirmDrag(List<RoleItemBean> list) {
        List<Role> roles = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            list.get(i).getRole().setSequence(i + 1);
            roles.add(list.get(i).getRole());
        }
        RoleDao dao = RApplication.getInstance().getDaoSession().getRoleDao();
        dao.updateInTx(roles);
        dao.detachAll();
    }

    public void confirmDelete(List<RoleItemBean> roles) {
        List<Role> list = new ArrayList<>();
        for (int i = 0; i < roles.size(); i ++) {
            roles.get(i).getRole().setSequence(i + 1);
            list.add(roles.get(i).getRole());
        }
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

    public void sortAndFilter(int sortType, final List<Race> races, final Kingdom kingdom) {
        queryRoles(mStoryId, sortType)
                .flatMap(new Function<List<Role>, ObservableSource<List<Role>>>() {
                    @Override
                    public ObservableSource<List<Role>> apply(List<Role> roles) throws Exception {
                        return filterRole(roles, races, kingdom);
                    }
                })
                .flatMap(new Function<List<Role>, ObservableSource<List<RoleItemBean>>>() {
                    @Override
                    public ObservableSource<List<RoleItemBean>> apply(List<Role> roles) throws Exception {
                        return parseRoles(roles);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<RoleItemBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<RoleItemBean> roles) {
                        rolesObserver.setValue(roles);
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

    private Observable<List<Role>> filterRole(final List<Role> roles, final List<Race> races, final Kingdom kingdom) {
        return Observable.create(new ObservableOnSubscribe<List<Role>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Role>> e) throws Exception {
                if (ListUtil.isEmpty(races) && kingdom == null) {
                    e.onNext(roles);
                }
                else {
                    List<Role> list = new ArrayList<>();
                    for (Role role:roles) {
                        if (!ListUtil.isEmpty(races)) {
                            if (!isSameRace(role.getRaceList(), races)) {
                                continue;
                            }
                        }
                        if (kingdom != null) {
                            if (role.getKingdomId() != kingdom.getId()) {
                                continue;
                            }
                        }
                        list.add(role);
                    }
                    e.onNext(list);
                }
            }
        });
    }

    /**
     * contain relationship if filter race is single
     * full match relationship if filter race is more than one
     * @param roleRaces
     * @param filterRaces
     * @return
     */
    private boolean isSameRace(List<Race> roleRaces, List<Race> filterRaces) {
        boolean result = false;
        if (filterRaces.size() == 1) {
            for (Race race:roleRaces) {
                if (race.getId() == filterRaces.get(0).getId()) {
                    result = true;
                }
            }
        }
        else {
            List<Long> rolesIds = new ArrayList<>();
            List<Long> filterIds = new ArrayList<>();
            for (Race race:roleRaces) {
                rolesIds.add(race.getId());
            }
            for (Race race:filterRaces) {
                filterIds.add(race.getId());
            }
            Collections.sort(rolesIds);
            Collections.sort(filterIds);
            StringBuffer bufferRoles = new StringBuffer();
            for (long id:rolesIds) {
                bufferRoles.append("_").append(id);
            }
            StringBuffer bufferFilters = new StringBuffer();
            for (long id:filterIds) {
                bufferFilters.append("_").append(id);
            }
            if (bufferRoles.toString().equals(bufferFilters.toString())) {
                result = true;
            }
        }
        return result;
    }
}
