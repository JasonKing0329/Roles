package com.king.app.roles.page.race;

import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.databinding.DialogRaceEditorBinding;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.view.dialog.DraggableHolder;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class RaceEditor extends MvvmFragment<DialogRaceEditorBinding, BaseViewModel> {

    private Race mRace;

    private OnRaceListener onRaceListener;

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_race_editor;
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
            mRace = onRaceListener.getInitRace();
            if (mRace != null) {
                binding.etName.setText(mRace.getName());
                binding.etDescription.setText(mRace.getDescription());
            }
        }
    }

    public void setOnRaceListener(OnRaceListener onRaceListener) {
        this.onRaceListener = onRaceListener;
    }

    private void onClickOk() {
        if (onRaceListener != null) {
            if (mRace == null) {
                mRace = new Race();
            }
            mRace.setName(binding.etName.getText().toString());
            mRace.setDescription(binding.etDescription.getText().toString());
            onRaceListener.onSaveRace(mRace);
        }
        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    public interface OnRaceListener {
        void onSaveRace(Race race);

        Race getInitRace();
    }
}
