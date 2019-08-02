package com.king.app.roles.view.dialog;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.king.app.roles.R;
import com.king.app.roles.base.BindingDialogFragment;
import com.king.app.roles.databinding.DialogBaseBinding;
import com.king.app.roles.utils.DebugLog;
import com.king.app.roles.utils.ScreenUtils;

/**
 * 描述: 可拖拽移动的base dialog框架
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/20 11:45
 */
public class DraggableDialogFragment extends BindingDialogFragment<DialogBaseBinding> implements DraggableHolder {

    private Point startPoint, touchPoint;

    private String title;

    private int backgroundColor;

    private boolean hideClose;

    private boolean showDelete;

    private Fragment contentFragment;

    private int maxHeight;

    private View.OnClickListener onDeleteListener;

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_base;
    }

    @Override
    protected void initView(View view) {

        if (title != null) {
            mBinding.tvTitle.setText(title);
        }
        if (backgroundColor != 0) {
            GradientDrawable drawable = (GradientDrawable) mBinding.groupDialog.getBackground();
            drawable.setColor(backgroundColor);
        }
        if (hideClose) {
            mBinding.ivClose.setVisibility(View.GONE);
        }
        if (showDelete) {
            mBinding.ivDelete.setVisibility(View.VISIBLE);
            mBinding.ivDelete.setOnClickListener(onDeleteListener);
        }

        mBinding.ivClose.setOnClickListener(v -> dismissAllowingStateLoss());

        initDragParams();

        if (contentFragment != null) {
            replaceContentFragment(contentFragment, "ContentView");
        }

        mBinding.groupFtContainer.post(() -> {
            DebugLog.e("mBinding.groupFtContainer height=" + mBinding.groupFtContainer.getHeight());
            limitMaxHeight();
        });
    }

    protected void replaceContentFragment(Fragment target, String tag) {
        if (target != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.group_ft_container, target, tag);
            ft.commit();
        }
    }

    private void limitMaxHeight() {
        int maxContentHeight = getMaxHeight();
        if (mBinding.groupFtContainer.getHeight() > maxContentHeight) {
            ViewGroup.LayoutParams params = mBinding.groupFtContainer.getLayoutParams();
            params.height = maxContentHeight;
            mBinding.groupFtContainer.setLayoutParams(params);
        }
    }

    /**
     * 子类可选择覆盖
     * @return
     */
    protected int getMaxHeight() {
        if (maxHeight != 0) {
            return maxHeight;
        }
        else {
            return ScreenUtils.getScreenHeight() * 3 / 5;
        }
    }

    private void initDragParams() {
        touchPoint = new Point();
        startPoint = new Point();
        mBinding.groupDialog.setOnTouchListener(new DialogTouchListener());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setHideClose(boolean hideClose) {
        this.hideClose = hideClose;
    }

    public void setContentFragment(Fragment contentFragment) {
        this.contentFragment = contentFragment;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setOnDeleteListener(View.OnClickListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    private class Point {
        float x;
        float y;
    }

    /**
     * move dialog
     */
    private class DialogTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getRawX();//
                    float y = event.getRawY();
                    startPoint.x = x;
                    startPoint.y = y;
                    DebugLog.d("ACTION_DOWN x=" + x + ", y=" + y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getRawX();
                    y = event.getRawY();
                    touchPoint.x = x;
                    touchPoint.y = y;
                    float dx = touchPoint.x - startPoint.x;
                    float dy = touchPoint.y - startPoint.y;

                    move((int) dx, (int) dy);

                    startPoint.x = x;
                    startPoint.y = y;
                    break;
                case MotionEvent.ACTION_UP:
                    break;

                default:
                    break;
            }
            return true;
        }
    }

    public static class Builder {

        private String title;

        private int backgroundColor;

        private boolean hideClose;

        private int maxHeight;

        private boolean showDelete;

        private View.OnClickListener onDeleteListener;

        private Fragment contentFragment;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setHideClose(boolean hideClose) {
            this.hideClose = hideClose;
            return this;
        }

        public Builder setShowDelete(boolean showDelete) {
            this.showDelete = showDelete;
            return this;
        }

        public Builder setContentFragment(Fragment contentFragment) {
            this.contentFragment = contentFragment;
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
            return this;
        }

        public Builder setOnDeleteListener(View.OnClickListener onDeleteListener) {
            this.onDeleteListener = onDeleteListener;
            return this;
        }

        public DraggableDialogFragment build() {
            DraggableDialogFragment fragment = new DraggableDialogFragment();
            fragment.setHideClose(hideClose);
            fragment.setShowDelete(showDelete);
            fragment.setOnDeleteListener(onDeleteListener);
            if (title != null) {
                fragment.setTitle(title);
            }
            if (backgroundColor != 0) {
                fragment.setBackgroundColor(backgroundColor);
            }
            if (contentFragment != null) {
                fragment.setContentFragment(contentFragment);
            }
            if (maxHeight != 0) {
                fragment.setMaxHeight(maxHeight);
            }
            return fragment;
        }
    }
}
