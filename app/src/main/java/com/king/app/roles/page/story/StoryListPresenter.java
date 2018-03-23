package com.king.app.roles.page.story;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.model.entity.StoryDao;

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
 * <p/>创建时间: 2018/3/23 16:38
 */
public class StoryListPresenter extends BasePresenter<StoryListView> {

    @Override
    protected void onCreate() {

    }

    public void loadStories() {
        queryStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Story>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Story> stories) {
                        view.showStories(stories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dispatchCommonError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<List<Story>> queryStories() {
        return Observable.create(new ObservableOnSubscribe<List<Story>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Story>> e) throws Exception {
                StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
                e.onNext(dao.loadAll());
            }
        });
    }
}
