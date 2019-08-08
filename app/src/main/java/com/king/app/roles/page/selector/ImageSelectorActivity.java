package com.king.app.roles.page.selector;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.MvvmActivity;
import com.king.app.roles.base.TwoTypeBindingAdapter;
import com.king.app.roles.databinding.ActivityImageSelectorBinding;

import java.io.File;

public class ImageSelectorActivity extends MvvmActivity<ActivityImageSelectorBinding, ImageSelectorViewModel> {

    public static final String RESP_FILE_PATH = "file_path";

    private ImageSelectorAdapter adapter;

    @Override
    protected ImageSelectorViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ImageSelectorViewModel.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_image_selector;
    }

    @Override
    protected void initView() {
        binding.setModel(viewModel);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanSize(position);
            }
        });
        binding.rvImages.setLayoutManager(manager);
        binding.actionbar.setOnSearchListener(text -> viewModel.onKeywordsChanged(text));
    }

    @Override
    protected void initData() {
        viewModel.itemsObserver.observe(this, items -> {
            if (adapter == null) {
                adapter = new ImageSelectorAdapter();
                adapter.setOnItemClickListener(new TwoTypeBindingAdapter.OnItemClickListener<ImageFolder, File>() {
                    @Override
                    public void onClickType1(View view, int position, ImageFolder data) {
                        viewModel.loadFolder(data.getFile());
                    }

                    @Override
                    public void onClickType2(View view, int position, File data) {
                        Intent intent = new Intent();
                        intent.putExtra(RESP_FILE_PATH, data.getPath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                adapter.setList(items);
                binding.rvImages.setAdapter(adapter);
            }
            else {
                adapter.setList(items);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.loadImages();
    }
}
