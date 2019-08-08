package com.king.app.roles.view.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.king.app.roles.R;
import com.king.app.roles.base.GlideApp;

public class ImageBindingAdapter {

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView view, String url) {
        GlideApp.with(view.getContext())
                .load(url)
                .error(R.drawable.def_small)
                .centerCrop()
                .into(view);
    }
}
