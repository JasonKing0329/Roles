package com.king.app.roles.page.story;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.StoryType;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.model.entity.StoryDao;
import com.king.app.roles.page.module.ModuleActivity;

import io.reactivex.Observable;
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

    public ObservableInt raceVisibility = new ObservableInt(View.GONE);
    public ObservableInt kingdomVisibility = new ObservableInt(View.GONE);
    public ObservableField<String> titleText = new ObservableField<>();
    public ObservableField<String> descriptionText = new ObservableField<>();
    public MutableLiveData<Integer> moduleObserver = new MutableLiveData<>();

    private Story mStory;

    public StoryPageViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadStory(final long storyId) {
        Observable.create((ObservableOnSubscribe<Story>) e -> {
            StoryDao dao = RApplication.getInstance().getDaoSession().getStoryDao();
            Story story = dao.queryBuilder()
                    .where(StoryDao.Properties.Id.eq(storyId))
                    .build().unique();
            e.onNext(story);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Story>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Story story) {
                        if (story.getType() == StoryType.R_K_C_C) {
                            raceVisibility.set(View.VISIBLE);
                            kingdomVisibility.set(View.VISIBLE);
                        }
                        else {
                            raceVisibility.set(View.GONE);
                            kingdomVisibility.set(View.GONE);
                        }

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
