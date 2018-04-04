package com.king.app.roles.page.kingdom;

import android.view.View;
import android.widget.EditText;

import com.king.app.roles.R;
import com.king.app.roles.base.ButterKnifeFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.view.dialog.DraggableHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class KingdomEditor extends ButterKnifeFragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_description)
    EditText etDescription;

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
    protected void onCreate(View view) {
        if (onRaceListener != null) {
            mKingdom = onRaceListener.getInitKingdom();
            if (mKingdom != null) {
                etName.setText(mKingdom.getName());
                etDescription.setText(mKingdom.getDescription());
            }
        }
    }

    public void setOnRaceListener(OnKingdomListener onRaceListener) {
        this.onRaceListener = onRaceListener;
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if (onRaceListener != null) {
            if (mKingdom == null) {
                mKingdom = new Kingdom();
            }
            mKingdom.setName(etName.getText().toString());
            mKingdom.setDescription(etDescription.getText().toString());
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
