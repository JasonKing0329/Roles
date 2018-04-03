package com.king.app.roles.page.story;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import com.king.app.roles.base.BaseViewModule;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.SettingProperty;
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
 * <p/>创建时间: 2018/4/3 10:59
 */
public class StoryListViewModel extends BaseViewModule {

    public ObservableBoolean fingerprintCheckStatus;

    public MutableLiveData<Boolean> deleteObserver;

    public MutableLiveData<Boolean> updateObserver;

    public MutableLiveData<List<Story>> storiesObserver;

    public StoryListViewModel(@NonNull Application application) {
        super(application);
        boolean checked = SettingProperty.isEnableFingerPrint();
        fingerprintCheckStatus = new ObservableBoolean(checked);
        storiesObserver = new MutableLiveData<>();
        deleteObserver = new MutableLiveData<>();
        updateObserver = new MutableLiveData<>();
    }

    public CompoundButton.OnCheckedChangeListener getFingerprintCheckedListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingProperty.setEnableFingerPrint(isChecked);
            }
        };
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
                        storiesObserver.setValue(stories);
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
                        storiesObserver.setValue(stories);
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
        loadingObserver.setValue(true);
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
                        loadingObserver.setValue(false);
                        deleteObserver.setValue(true);
                        storiesObserver.setValue(stories);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadingObserver.setValue(false);
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
        loadingObserver.setValue(true);
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
                        loadingObserver.setValue(false);
                        updateObserver.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadingObserver.setValue(false);
                        dispatchCommonError("Insert error", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
