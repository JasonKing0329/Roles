package com.king.app.roles.page.race;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.RaceDao;

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

public class RacePresenter extends BasePresenter<RaceView> {

    private long mStoryId;

    @Override
    protected void onCreate() {

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
                        view.showRaces(races);
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

    public void addNewRace(String name, String description) {
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        Race race = new Race();
        race.setStoryId(mStoryId);
        race.setName(name);
        race.setDescription(description);
        race.setSequence((int) dao.count());
        dao.insert(race);
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
