package com.king.app.roles.page.story;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.model.entity.StoryDao;

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
 * @time 2018/3/24 0024 10:39
 */

public class StoryPagePresenter extends BasePresenter<StoryPageView> {

    private Story mStory;

    @Override
    protected void onCreate() {

    }

    public void loadStory(final long storyId) {
        Observable.create(new ObservableOnSubscribe<Story>() {
            @Override
            public void subscribe(ObservableEmitter<Story> e) throws Exception {
                StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
                Story story = dao.queryBuilder()
                        .where(StoryDao.Properties.Id.eq(storyId))
                        .build().unique();
                e.onNext(story);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Story>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Story story) {
                        mStory = story;
                        view.showStory(story.getName(), story.getDescription());
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

    public String getDescription() {
        return mStory.getDescription();
    }

    public void saveDescription(String content) {
        StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
        mStory.setDescription(content);
        dao.update(mStory);
        dao.detachAll();
    }
}
