package com.king.app.roles.page.kingdom;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.KingdomDao;
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
public class KingdomViewModel extends ModuleViewModel {

    private long mStoryId;

    public MutableLiveData<List<Kingdom>> kingdomsObserver;

    public KingdomViewModel(@NonNull Application application) {
        super(application);
        kingdomsObserver = new MutableLiveData<>();
        draggableVisibility.set(View.VISIBLE);
        normalVisibility.set(View.GONE);
    }

    public void loadKingdoms(long storyId) {
        mStoryId = storyId;
        queryKingdoms(storyId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Kingdom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Kingdom> kingdoms) {
                        kingdomsObserver.setValue(kingdoms);
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

    private Observable<List<Kingdom>> queryKingdoms(final long storyId) {
        return Observable.create(new ObservableOnSubscribe<List<Kingdom>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Kingdom>> e) throws Exception {
                KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
                List<Kingdom> list = dao.queryBuilder()
                        .where(KingdomDao.Properties.StoryId.eq(storyId))
                        .orderAsc(KingdomDao.Properties.Sequence)
                        .build().list();
                e.onNext(list);
            }
        });
    }

    public void insertOrUpdate(Kingdom kingdom) {
        KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
        if (kingdom.getId() == null) {
            kingdom.setStoryId(mStoryId);
            kingdom.setSequence((int) dao.count());
        }
        dao.insertOrReplace(kingdom);
        dao.detachAll();
    }

    public void confirmDrag(List<Kingdom> list) {
        for (int i = 0; i < list.size(); i ++) {
            list.get(i).setSequence(i + 1);
        }
        KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
        dao.updateInTx(list);
        dao.detachAll();
    }

    public void confirmDelete(List<Kingdom> list) {
        KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
        dao.deleteInTx(list);
        dao.detachAll();
    }
}
