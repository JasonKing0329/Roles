package com.king.app.roles.page.chapter;

import com.king.app.roles.base.BasePresenter;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.ChapterDao;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.KingdomDao;

import java.util.ArrayList;
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
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:43
 */

public class ChapterPresenter extends BasePresenter<ChapterView> {

    private long mStoryId;

    @Override
    protected void onCreate() {

    }

    public void loadChapters(long storyId) {
        mStoryId = storyId;
        queryFirstChapters(storyId)
                .flatMap(new Function<List<Chapter>, ObservableSource<List<FirstLevelItem>>>() {
                    @Override
                    public ObservableSource<List<FirstLevelItem>> apply(List<Chapter> chapters) throws Exception {
                        return parseFirstChapters(chapters);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<FirstLevelItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<FirstLevelItem> list) {
                        view.showChapters(list);
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
            chapter.setStoryId(mStoryId);
        }
        dao.insertOrReplace(chapter);
        dao.detachAll();
    }

    public void confirmDelete(List<Chapter> list) {
        ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
        dao.deleteInTx(list);
        dao.detachAll();
    }
}
