package com.king.app.roles.page.selector;

import com.king.app.roles.R;
import com.king.app.roles.base.TwoTypeBindingAdapter;
import com.king.app.roles.databinding.AdapterImageFolderBinding;
import com.king.app.roles.databinding.AdapterImageItemBinding;

import java.io.File;

public class ImageSelectorAdapter extends TwoTypeBindingAdapter<AdapterImageFolderBinding, AdapterImageItemBinding, ImageFolder, File> {

    @Override
    protected Class getType1Class() {
        return ImageFolder.class;
    }

    @Override
    protected Class getType2Class() {
        return File.class;
    }

    @Override
    protected int getType1LayoutRes() {
        return R.layout.adapter_image_folder;
    }

    @Override
    protected int getType2LayoutRes() {
        return R.layout.adapter_image_item;
    }

    @Override
    protected void onBindType1(AdapterImageFolderBinding binding, int position, ImageFolder bean) {
        binding.setBean(bean);
    }

    @Override
    protected void onBindType2(AdapterImageItemBinding binding, int position, File bean) {
        binding.setBean(bean);
    }

    public int getSpanSize(int position) {
        if (isType1(position)) {
            return 4;
        }
        else {
            return 1;
        }
    }
}
