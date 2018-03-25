package com.king.app.roles.page.module;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import butterknife.ButterKnife;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:31
 */

public abstract class ModuleViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    public SwipeMenuRecyclerView mMenuRecyclerView;

    public ModuleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        getTouchHandler().setOnTouchListener(this);
    }


    public abstract View getTouchHandler();

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mMenuRecyclerView.startDrag(this);
                break;
            }
        }
        return false;
    }
}
