package com.king.app.roles.view.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BindingDialogFragment;
import com.king.app.roles.databinding.DialogLoadingBinding;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/29 13:37
 */
public class ProgressDialogFragment extends BindingDialogFragment<DialogLoadingBinding> {

    private String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(android.app.DialogFragment.STYLE_NORMAL, R.style.LoadingDialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView(View view) {
        mBinding.tvMessage.setText(message);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        if (isAdded()) {
            ft.show(this);
        } else {
            ft.add(this, tag);
        }
        ft.commitAllowingStateLoss();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
