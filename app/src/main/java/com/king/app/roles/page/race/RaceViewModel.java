package com.king.app.roles.page.race;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.RaceDao;
import com.king.app.roles.page.module.ModuleViewModel;

import java.util.List;

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
 * <p/>创建时间: 2018/4/4 13:41
 */
public class RaceViewModel extends ModuleViewModel {

    private long mStoryId;

    public MutableLiveData<List<Race>> racesObserver;

    public RaceViewModel(@NonNull Application application) {
        super(application);
        racesObserver = new MutableLiveData<>();
        draggableVisibility.set(View.VISIBLE);
        normalVisibility.set(View.GONE);
    }

    public void loadRaces(long storyId) {
        mStoryId = storyId;
        queryRaces(storyId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Race>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Race> races) {
                        racesObserver.setValue(races);
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

    private Observable<List<Race>> queryRaces(final long storyId) {
        return Observable.create(new ObservableOnSubscribe<List<Race>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Race>> e) throws Exception {
                RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
                List<Race> list = dao.queryBuilder()
                        .where(RaceDao.Properties.StoryId.eq(storyId))
                        .orderAsc(RaceDao.Properties.Sequence)
                        .build().list();
                e.onNext(list);
            }
        });
    }

    public void insertOrUpdate(Race race) {
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        if (race.getId() == null) {
            race.setStoryId(mStoryId);
            race.setSequence((int) dao.count());
        }
        dao.insertOrReplace(race);
        dao.detachAll();
    }

    public void confirmDrag(List<Race> list) {
        for (int i = 0; i < list.size(); i ++) {
            list.get(i).setSequence(i + 1);
        }
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        dao.updateInTx(list);
        dao.detachAll();
    }

    public void confirmDelete(List<Race> list) {
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        dao.deleteInTx(list);
        dao.detachAll();
    }
}
