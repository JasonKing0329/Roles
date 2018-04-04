package com.king.app.roles.page.race;

import android.view.View;
import android.widget.EditText;

import com.king.app.roles.R;
import com.king.app.roles.base.ButterKnifeFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.view.dialog.DraggableHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class RaceEditor extends ButterKnifeFragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_description)
    EditText etDescription;

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
    protected void onCreate(View view) {
        if (onRaceListener != null) {
            mRace = onRaceListener.getInitRace();
            if (mRace != null) {
                etName.setText(mRace.getName());
                etDescription.setText(mRace.getDescription());
            }
        }
    }

    public void setOnRaceListener(OnRaceListener onRaceListener) {
        this.onRaceListener = onRaceListener;
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if (onRaceListener != null) {
            if (mRace == null) {
                mRace = new Race();
            }
            mRace.setName(etName.getText().toString());
            mRace.setDescription(etDescription.getText().toString());
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
