package com.king.app.roles.page.kingdom;

import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.databinding.DialogKingdomEditorBinding;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.view.dialog.DraggableHolder;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class KingdomEditor extends MvvmFragment<DialogKingdomEditorBinding, BaseViewModel> {

    private Kingdom mKingdom;

    private OnKingdomListener onRaceListener;

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_kingdom_editor;
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
        if (onRaceListener != null) {
            mKingdom = onRaceListener.getInitKingdom();
            if (mKingdom != null) {
                binding.etName.setText(mKingdom.getName());
                binding.etDescription.setText(mKingdom.getDescription());
            }
        }
    }

    public void setOnRaceListener(OnKingdomListener onRaceListener) {
        this.onRaceListener = onRaceListener;
    }

    private void onClickOk() {
        if (onRaceListener != null) {
            if (mKingdom == null) {
                mKingdom = new Kingdom();
            }
            mKingdom.setName(binding.etName.getText().toString());
            mKingdom.setDescription(binding.etDescription.getText().toString());
            onRaceListener.onSaveKingdom(mKingdom);
        }
        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    public interface OnKingdomListener {
        void onSaveKingdom(Kingdom kingdom);

        Kingdom getInitKingdom();
    }
}
