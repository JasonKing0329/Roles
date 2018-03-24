package com.king.app.roles.view.dialog;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.utils.DebugLog;
import com.king.app.roles.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 可拖拽移动的base dialog框架
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/20 11:45
 */
public class DraggableDialogFragment extends BaseDialogFragment implements DraggableHolder {

    @BindView(R.id.group_dialog)
    ViewGroup groupDialog;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.group_ft_container)
    ViewGroup groupFtContent;

    private Point startPoint, touchPoint;

    private String title;

    private int backgroundColor;

    private boolean hideClose;

    private Fragment contentFragment;

    private int maxHieight;

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_base;
    }

    @Override
    protected void initView(View view) {

        if (title != null) {
            tvTitle.setText(title);
        }
        if (backgroundColor != 0) {
            GradientDrawable drawable = (GradientDrawable) groupDialog.getBackground();
            drawable.setColor(backgroundColor);
        }
        if (hideClose) {
            ivClose.setVisibility(View.GONE);
        }

        initDragParams();

        if (contentFragment != null) {
            replaceContentFragment(contentFragment, "ContentView");
        }

        groupFtContent.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e("groupFtContent height=" + groupFtContent.getHeight());
                limitMaxHeihgt();
            }
        });
    }

    protected void replaceContentFragment(Fragment target, String tag) {
        if (target != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.group_ft_container, target, tag);
            ft.commit();
        }
    }

    private void limitMaxHeihgt() {
        int maxContentHeight = getMaxHeight();
        if (groupFtContent.getHeight() > maxContentHeight) {
            ViewGroup.LayoutParams params = groupFtContent.getLayoutParams();
            params.height = maxContentHeight;
            groupFtContent.setLayoutParams(params);
        }
    }

    /**
     * 子类可选择覆盖
     * @return
     */
    protected int getMaxHeight() {
        if (maxHieight != 0) {
            return maxHieight;
        }
        else {
            return ScreenUtils.getScreenHeight(getActivity()) * 3 / 5;
        }
    }

    private void initDragParams() {
        touchPoint = new Point();
        startPoint = new Point();
        groupDialog.setOnTouchListener(new DialogTouchListener());
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

    public void setMaxHieight(int maxHieight) {
        this.maxHieight = maxHieight;
    }

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
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

        private int maxHieight;

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

        public Builder setContentFragment(Fragment contentFragment) {
            this.contentFragment = contentFragment;
            return this;
        }

        public Builder setMaxHieight(int maxHieight) {
            this.maxHieight = maxHieight;
            return this;
        }

        public DraggableDialogFragment build() {
            DraggableDialogFragment fragment = new DraggableDialogFragment();
            fragment.setHideClose(hideClose);
            if (title != null) {
                fragment.setTitle(title);
            }
            if (backgroundColor != 0) {
                fragment.setBackgroundColor(backgroundColor);
            }
            if (contentFragment != null) {
                fragment.setContentFragment(contentFragment);
            }
            return fragment;
        }
    }

}
