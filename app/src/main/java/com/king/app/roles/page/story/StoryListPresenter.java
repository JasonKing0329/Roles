package com.king.app.roles.page.story;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.model.entity.StoryDao;

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
                List<Story> list = dao.queryBuilder()
                        .orderAsc(StoryDao.Properties.Sequence)
                        .build().list();
                e.onNext(list);
            }
        });
    }

    public void addStory(String name) {
        insertStory(name)
                .flatMap(new Function<Story, ObservableSource<List<Story>>>() {
                    @Override
                    public ObservableSource<List<Story>> apply(Story story) throws Exception {
                        return queryStories();
                    }
                })
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
                        dispatchCommonError("Insert error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteStory(List<Story> list) {
        view.showLoading();
        deleteStoryRx(list)
                .flatMap(new Function<Object, ObservableSource<List<Story>>>() {
                    @Override
                    public ObservableSource<List<Story>> apply(Object object) throws Exception {
                        return queryStories();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Story>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Story> stories) {
                        view.dismissLoading();
                        view.deleteDone();
                        view.showStories(stories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        dispatchCommonError("Insert error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Story> insertStory(final String name) {
        return Observable.create(new ObservableOnSubscribe<Story>() {
            @Override
            public void subscribe(ObservableEmitter<Story> e) throws Exception {
                StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
                int count = (int) dao.count();
                Story story = new Story();
                story.setName(name);
                story.setSequence(count);
                dao.insert(story);
                e.onNext(story);
            }
        });
    }

    private Observable<Object> deleteStoryRx(final List<Story> story) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
                dao.deleteInTx(story);
                dao.detachAll();
                e.onNext(new Object());
            }
        });
    }

    private Observable<Object> updateStoryRx(final List<Story> list) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
                for (int i = 0; i < list.size(); i ++) {
                    Story story = list.get(i);
                    story.setSequence(i + 1);
                }
                dao.updateInTx(list);
                e.onNext(new Object());
            }
        });
    }

    public void updateSequences(List<Story> list) {
        view.showLoading();
        updateStoryRx(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Object object) {
                        view.dismissLoading();
                        view.updateDone();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.dismissLoading();
                        dispatchCommonError("Insert error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
