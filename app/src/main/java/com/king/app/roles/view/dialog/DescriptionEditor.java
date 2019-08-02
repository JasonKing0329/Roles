package com.king.app.roles.view.dialog;

import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.databinding.DialogDescriptionEditorBinding;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/24 0024 11:02
 */

public class DescriptionEditor extends MvvmFragment<DialogDescriptionEditorBinding, BaseViewModel> {

    private DraggableHolder draggableHolder;

    private OnDescriptionListener onDescriptionListener;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_description_editor;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        binding.tvOk.setOnClickListener(v -> onClickOk());
    }

    @Override
    protected void onCreateData() {
        if (onDescriptionListener != null) {
            binding.etText.setText(onDescriptionListener.getInitContent());
        }
    }

    public void setOnDescriptionListener(OnDescriptionListener onDescriptionListener) {
        this.onDescriptionListener = onDescriptionListener;
    }

    private void onClickOk() {
        if (onDescriptionListener != null) {
            onDescriptionListener.onSaveDescription(binding.etText.getText().toString());
            if (draggableHolder != null) {
                draggableHolder.dismiss();
            }
        }
    }

    public interface OnDescriptionListener {
        String getInitContent();
        void onSaveDescription(String content);
    }
}
