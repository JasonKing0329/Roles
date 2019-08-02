package com.king.app.roles.page.race;

import android.view.View;
import android.widget.CheckBox;

import com.king.app.roles.R;
import com.king.app.roles.databinding.AdapterRaceItemBinding;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.page.module.ModuleAdapter;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:14
 */

public class RaceAdapter extends ModuleAdapter<AdapterRaceItemBinding, Race> {

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_race_item;
    }

    @Override
    protected View getDragView(AdapterRaceItemBinding holder) {
        return holder.ivDrag;
    }

    @Override
    protected CheckBox getCheckBox(AdapterRaceItemBinding holder) {
        return holder.cbCheck;
    }

    @Override
    protected void onBindSubHolder(AdapterRaceItemBinding holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvDescription.setText(list.get(position).getDescription());
    }

    @Override
    protected boolean isMatchForKeyword(Race race, String text) {
        return false;
    }

}
