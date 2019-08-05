package com.king.app.roles.view.dialog;

import android.databinding.ViewDataBinding;

import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/5 14:53
 */
public abstract class DraggableContentFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends MvvmFragment<T, VM> {

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            draggableHolder = (DraggableHolder) holder;
        }
    }

    public void dismiss() {
        if (draggableHolder != null) {
            draggableHolder.dismissAllowingStateLoss();
        }
    }
}
