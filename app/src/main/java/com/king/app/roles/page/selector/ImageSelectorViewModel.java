package com.king.app.roles.page.selector;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.conf.AppConfig;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageSelectorViewModel extends BaseViewModel {

    public MutableLiveData<List<Object>> itemsObserver = new MutableLiveData<>();

    public ObservableInt upperVisibility = new ObservableInt(View.GONE);

    private String mKeyword;
    private File mFolder;

    public ImageSelectorViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadImages() {
        loadFolder(new File(AppConfig.IMG_SELECT_ROOT));
    }

    public void loadFolder(File folder) {
        mFolder = folder;
        setUpperVisibility();
        getItems(folder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(List<Object> list) {
                        itemsObserver.setValue(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        messageObserver.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setUpperVisibility() {
        if (AppConfig.IMG_SELECT_ROOT.equals(mFolder.getPath())) {
            upperVisibility.set(View.GONE);
        }
        else {
            upperVisibility.set(View.VISIBLE);
        }
    }

    private Observable<List<Object>> getItems(File folder) {
        return Observable.create(e -> {
            List<Object> list = new ArrayList<>();
            File[] files = folder.listFiles(fileFilter);
            for (File file:files) {
                if (!TextUtils.isEmpty(mKeyword) && !file.getName().toLowerCase().contains(mKeyword.toLowerCase())) {
                    continue;
                }
                if (file.isDirectory()) {
                    ImageFolder imageFolder = new ImageFolder();
                    imageFolder.setFile(file);
                    list.add(imageFolder);
                }
                else {
                    list.add(file);
                }
            }
            Collections.sort(list, new ItemComparator());
            e.onNext(list);
        });
    }

    private FileFilter fileFilter = file -> !file.getName().endsWith(".nomedia");

    public void onKeywordsChanged(String text) {
        mKeyword = text;
        loadFolder(mFolder);
    }

    public void upper() {
        mFolder = mFolder.getParentFile();
        setUpperVisibility();
        loadFolder(mFolder);
    }

    private class ItemComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            // folder在前
            // 同级按名称升序
            int valueObj1 = (o1 instanceof ImageFolder) ? 0:1;
            int valueObj2 = (o2 instanceof ImageFolder) ? 0:1;
            int result = valueObj1 - valueObj2;
            if (result == 0) {
                String name1 = (o1 instanceof ImageFolder) ? ((ImageFolder) o1).getFile().getName():((File) o1).getName();
                String name2 = (o2 instanceof ImageFolder) ? ((ImageFolder) o2).getFile().getName():((File) o2).getName();
                return name1.compareTo(name2);
            }
            else {
                return result;
            }
        }
    }
}
