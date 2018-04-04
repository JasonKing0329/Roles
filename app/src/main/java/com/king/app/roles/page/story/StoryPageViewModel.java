package com.king.app.roles.page.story;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.model.entity.StoryDao;
import com.king.app.roles.page.module.ModuleActivity;

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
 * <p/>创建时间: 2018/4/3 16:23
 */
public class StoryPageViewModel extends BaseViewModel {

    public ObservableField<String> titleText;
    public ObservableField<String> descriptionText;
    public MutableLiveData<Integer> moduleObserver;

    private Story mStory;

    public StoryPageViewModel(@NonNull Application application) {
        super(application);
        titleText = new ObservableField<>();
        descriptionText = new ObservableField<>();
        moduleObserver = new MutableLiveData<>();
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
                        titleText.set(story.getName());
                        if (TextUtils.isEmpty(story.getDescription())) {
                            descriptionText.set("No description, press to add one.");
                        }
                        else {
                            descriptionText.set(story.getDescription());
                        }
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

    public void saveDescription(String content) {
        StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
        mStory.setDescription(content);
        dao.update(mStory);
        dao.detachAll();
    }

    public String getDescription() {
        return mStory.getDescription();
    }

    public void onClickRace(View view) {
        moduleObserver.setValue(ModuleActivity.PAGE_TYPE_RACE);
    }

    public void onClickKingdom(View view) {
        moduleObserver.setValue(ModuleActivity.PAGE_TYPE_KINGDOM);
    }

    public void onClickChapter(View view) {
        moduleObserver.setValue(ModuleActivity.PAGE_TYPE_CHAPTER);
    }

    public void onClickCharacter(View view) {
        moduleObserver.setValue(ModuleActivity.PAGE_TYPE_CHARACTER);
    }
}
