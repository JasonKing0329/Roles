package com.king.app.roles.page.kingdom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleViewHolder;

import butterknife.BindView;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/26 9:24
 */
public class KingdomAdapter extends ModuleAdapter<KingdomAdapter.KingdomHolder, Kingdom> {

    @Override
    protected int getItemLayoutRes() {
        return R.layout.adapter_kingdom_item;
    }

    @Override
    protected KingdomHolder newViewHolder(View view) {
        return new KingdomHolder(view);
    }

    @Override
    protected ViewGroup getGroupItem(KingdomHolder holder) {
        return holder.groupItem;
    }

    @Override
    protected CheckBox getCheckBox(KingdomHolder holder) {
        return holder.cbCheck;
    }

    @Override
    protected View getDragView(KingdomHolder holder) {
        return holder.ivDrag;
    }

    @Override
    protected void onBindSubHolder(KingdomHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.tvDescription.setText(list.get(position).getDescription());
    }

    public static class KingdomHolder extends ModuleViewHolder {

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

        public KingdomHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View getTouchHandler() {
            return ivDrag;
        }
    }
}
