package com.king.app.roles.page.race;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleViewHolder;

import butterknife.BindView;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:14
 */

public class RaceAdapter extends ModuleAdapter<RaceAdapter.RaceHolder, Race> {

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_race_item;
    }

    @Override
    protected RaceHolder newViewHolder(View view) {
        return new RaceHolder(view);
    }

    @Override
    protected View getDragView(RaceHolder holder) {
        return holder.ivDrag;
    }

    @Override
    protected CheckBox getCheckBox(RaceHolder holder) {
        return holder.cbCheck;
    }

    @Override
    protected ViewGroup getGroupItem(RaceHolder holder) {
        return holder.groupItem;
    }

    @Override
    protected void onBindSubHolder(RaceHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvDescription.setText(list.get(position).getDescription());
    }

    public static class RaceHolder extends ModuleViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.iv_drag)
        ImageView ivDrag;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;
        @BindView(R.id.group_item)
        LinearLayout groupItem;

        public RaceHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View getTouchHandler() {
            return ivDrag;
        }
    }
}
