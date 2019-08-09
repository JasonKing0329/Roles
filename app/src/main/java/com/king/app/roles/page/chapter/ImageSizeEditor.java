package com.king.app.roles.page.chapter;

import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.databinding.FragmentImageSizeBinding;
import com.king.app.roles.model.SettingProperty;
import com.king.app.roles.view.dialog.DraggableContentFragment;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/9 11:11
 */
public class ImageSizeEditor extends DraggableContentFragment<FragmentImageSizeBinding, BaseViewModel> {

    private OnSizeListener onSizeListener;

    public void setOnSizeListener(OnSizeListener onSizeListener) {
        this.onSizeListener = onSizeListener;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_image_size;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        binding.tvOk.setOnClickListener(v -> {
            String size = binding.etWidth.getText().toString();
            try {
                int width = Integer.parseInt(size);
                onSizeListener.onSetSize(width, width);
                // 记住本次设置，下次初始化该值
                SettingProperty.setInsertImageSize(width);
                dismiss();
            } catch (Exception e) {
                binding.etWidth.setError("Error number");
            }
        });
    }

    @Override
    protected void onCreateData() {
        binding.etWidth.setText(String.valueOf(SettingProperty.getInsertImageSize()));
    }

    public interface OnSizeListener {
        void onSetSize(int width, int height);
    }
}
