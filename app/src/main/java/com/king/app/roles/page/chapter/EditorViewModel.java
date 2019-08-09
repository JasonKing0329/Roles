package com.king.app.roles.page.chapter;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.model.ChapterModel;
import com.king.app.roles.model.entity.Chapter;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/8 13:40
 */
public class EditorViewModel extends BaseViewModel {

    public ObservableField<String> chapterName = new ObservableField<>();

    public MutableLiveData<String> contentObserver = new MutableLiveData<>();

    public MutableLiveData<Boolean> selectFileType = new MutableLiveData<>();

    public MutableLiveData<ImageBean> insertImage = new MutableLiveData<>();

    private ChapterModel chapterModel;

    private Chapter mChapter;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        chapterModel = new ChapterModel();
    }

    public void loadData(long chapterId) {
        loadingObserver.setValue(true);
        getChapter(chapterId)
                .flatMap(chapter -> {
                    mChapter = chapter;
                    chapterName.set(chapter.getName());
                    return getContent(chapter);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        loadingObserver.setValue(false);
                        contentObserver.setValue(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        messageObserver.setValue(e.getMessage());
                        loadingObserver.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Chapter getChapter() {
        return mChapter;
    }

    private Observable<Chapter> getChapter(long chapterId) {
        return Observable.create(e -> {
            Chapter chapter = getDaoSession().getChapterDao().load(chapterId);
            e.onNext(chapter);
        });
    }

    private ObservableSource<String> getContent(Chapter chapter) {
        return observer -> {
            String content = chapterModel.parseChapterFile(chapter.getFile());
            if (content == null) {
                content = "";
            }
            observer.onNext(content);
        };
    }

    public void saveContent(String html) {
        if (TextUtils.isEmpty(mChapter.getFile())) {
            selectFileType.setValue(true);
            return;
        }
        saveContent(html, mChapter.getFile());
    }

    public void saveContent(String html, String file) {
        loadingObserver.setValue(true);
        saveData(html, file)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        loadingObserver.setValue(false);
                        messageObserver.setValue("Save successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loadingObserver.setValue(false);
                        messageObserver.setValue("Failed to save! Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Boolean> saveData(String html, String file) {
        return Observable.create(e -> {
            boolean result = chapterModel.saveContent(html, file);
            if (result) {
                mChapter.setFile(file);
                getDaoSession().getChapterDao().update(mChapter);
                getDaoSession().getChapterDao().detach(mChapter);
            }
            e.onNext(result);
        });
    }

    /**
     * 控制<img标签的宽高
     * @param imagePath
     * @param maxWidth html里直接是dp
     * @param maxHeight html里直接是dp
     */
    public void insertImageToHtml(String imagePath, int maxWidth, int maxHeight) {
        //获取图片的宽高
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        ImageBean bean = new ImageBean();
        bean.setFilePath(imagePath);
        bean.setAlt(new File(imagePath).getName());
        if (width > height) {
            if (width > maxWidth) {
                bean.setWidth(maxWidth);
                double ratio = (double) width / (double) maxWidth;
                bean.setHeight((int) (height / ratio));
            }
            else {
                bean.setWidth(width);
                bean.setHeight(height);
            }
        }
        else {
            if (height > maxHeight) {
                bean.setHeight(maxHeight);
                double ratio = (double) height / (double) maxHeight;
                bean.setWidth((int) (width / ratio));
            }
            else {
                bean.setWidth(width);
                bean.setHeight(height);
            }
        }
        insertImage.setValue(bean);
    }
}
