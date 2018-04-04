package com.king.app.roles.view.dialog;

import android.view.View;
import android.widget.EditText;

import com.king.app.roles.R;
import com.king.app.roles.base.ButterKnifeFragment;
import com.king.app.roles.base.IFragmentHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/24 0024 11:02
 */

public class DescriptionEditor extends ButterKnifeFragment {

    @BindView(R.id.et_text)
    EditText etText;

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
    protected void onCreate(View view) {
        if (onDescriptionListener != null) {
            etText.setText(onDescriptionListener.getInitContent());
        }
    }

    public void setOnDescriptionListener(OnDescriptionListener onDescriptionListener) {
        this.onDescriptionListener = onDescriptionListener;
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if (onDescriptionListener != null) {
            onDescriptionListener.onSaveDescription(etText.getText().toString());
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
