package com.king.app.roles.page.chapter;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;

import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.ChapterDao;
import com.king.app.roles.page.module.ModuleViewModel;

import java.util.ArrayList;
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
public class ChapterViewModel extends ModuleViewModel {

    public MutableLiveData<List<FirstLevelItem>> chaptersObserver;

    public ChapterViewModel(@NonNull Application application) {
        super(application);
        chaptersObserver = new MutableLiveData<>();
        draggableVisibility.set(View.GONE);
        normalVisibility.set(View.VISIBLE);
    }

    public void loadChapters() {
        queryFirstChapters(getStoryId())
                .flatMap(chapters -> parseFirstChapters(chapters))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<FirstLevelItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<FirstLevelItem> list) {
                        chaptersObserver.setValue(list);
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

    private Observable<List<Chapter>> queryFirstChapters(final long storyId) {
        return Observable.create(new ObservableOnSubscribe<List<Chapter>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Chapter>> e) throws Exception {
                ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
                List<Chapter> list = dao.queryBuilder()
                        .where(ChapterDao.Properties.StoryId.eq(storyId)
                                , ChapterDao.Properties.Level.eq(AppConstants.CHAPTER_LEVEL_FIRST))
                        .orderAsc(ChapterDao.Properties.Index)
                        .build().list();
                e.onNext(list);
            }
        });
    }

    private Observable<List<FirstLevelItem>> parseFirstChapters(final List<Chapter> chapters) {
        return Observable.create(new ObservableOnSubscribe<List<FirstLevelItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FirstLevelItem>> e) throws Exception {
                List<FirstLevelItem> list = new ArrayList<>();
                for (Chapter chapter:chapters) {
                    FirstLevelItem item = new FirstLevelItem();
                    item.setChapter(chapter);
                    list.add(item);
                }
                e.onNext(list);
            }
        });
    }

    public void insertOrUpdate(Chapter chapter) {
        ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
        if (chapter.getId() == null) {
            chapter.setStoryId(getStoryId());
        }
        dao.insertOrReplace(chapter);
        dao.detachAll();
    }

    public void delete(Chapter chapter) {
        ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
        // delete all sub chapters
        dao.deleteInTx(chapter.getChildren());
        // delete from chapter
        dao.delete(chapter);
        dao.detachAll();

        // TODO update chapter of role
    }
}
