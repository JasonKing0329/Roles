package com.king.app.roles.page.role;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.ButterKnifeFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.KingdomDao;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.RaceDao;
import com.king.app.roles.utils.ListUtil;
import com.king.app.roles.view.adapter.TagAdapter;
import com.king.app.roles.view.dialog.DraggableHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/29 9:28
 */
public class RoleSortFilterDialog extends ButterKnifeFragment {

    @BindView(R.id.cb_race)
    CheckBox cbRace;
    @BindView(R.id.rv_race)
    RecyclerView rvRace;
    @BindView(R.id.cb_kingdom)
    CheckBox cbKingdom;
    @BindView(R.id.rv_kingdom)
    RecyclerView rvKingdom;
    @BindView(R.id.rb_sequence)
    RadioButton rbSequence;
    @BindView(R.id.rb_name)
    RadioButton rbName;

    private TagAdapter<Race> raceAdapter;

    private TagAdapter<Kingdom> kingdomAdapter;

    public OnSortFilterListener onSortFilterListener;

    private DraggableHolder draggableHolder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_role_sort_filter;
    }

    @Override
    protected void onCreate(View view) {
        cbRace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rvRace.setVisibility(isChecked ? View.VISIBLE:View.GONE);
            }
        });
        cbKingdom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rvKingdom.setVisibility(isChecked ? View.VISIBLE:View.GONE);
            }
        });

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rvRace.setLayoutManager(manager);
        manager = new GridLayoutManager(getActivity(), 3);
        rvKingdom.setLayoutManager(manager);

        loadRaces();
        loadKingdoms();
    }

    public void setOnSortFilterListener(OnSortFilterListener onSortFilterListener) {
        this.onSortFilterListener = onSortFilterListener;
    }

    /**
     * 多选
     */
    private void loadRaces() {
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        List<Race> races = dao.queryBuilder()
                .where(RaceDao.Properties.StoryId.eq(onSortFilterListener.getStoryId()))
                .build().list();
        raceAdapter = new TagAdapter<Race>() {
            @Override
            protected void onBindTag(TextView tvTag, int position) {
                tvTag.setText(list.get(position).getName());
            }

            @Override
            protected boolean compareItem(Race targetItem, Race listItem) {
                return targetItem.getId() == listItem.getId();
            }

        };
        raceAdapter.setList(races);
        rvRace.setAdapter(raceAdapter);
    }

    /**
     * 单选
     */
    private void loadKingdoms() {
        KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
        List<Kingdom> kingdoms = dao.queryBuilder()
                .where(KingdomDao.Properties.StoryId.eq(onSortFilterListener.getStoryId()))
                .build().list();
        kingdomAdapter = new TagAdapter<Kingdom>() {
            @Override
            protected void onBindTag(TextView tvTag, int position) {
                tvTag.setText(list.get(position).getName());
            }

            @Override
            protected boolean compareItem(Kingdom targetItem, Kingdom listItem) {
                return targetItem.getId() == listItem.getId();
            }

        };
        kingdomAdapter.setSingleSelect(true);
        kingdomAdapter.setList(kingdoms);
        rvKingdom.setAdapter(kingdomAdapter);
    }

    @OnClick(R.id.tv_ok)
    public void onViewClicked() {
        int sortType = getSortType();
        List<Race> races = null;
        if (cbRace.isChecked()) {
            races = raceAdapter.getSelectedData();
            if (ListUtil.isEmpty(races)) {
                showMessageLong("Please select races");
                return;
            }
        }
        Kingdom kingdom = null;
        if (cbKingdom.isChecked()) {
            List<Kingdom> list = kingdomAdapter.getSelectedData();
            if (ListUtil.isEmpty(list)) {
                showMessageLong("Please select kingdom");
                return;
            }
            kingdom = list.get(0);
        }
        onSortFilterListener.onSortAndFilter(sortType, races, kingdom);

        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    public int getSortType() {
        if (rbName.isChecked()) {
            return AppConstants.ROLE_SORT_BY_NAME;
        }
        return AppConstants.ROLE_SORT_BY_SEQUENCE;
    }

    public interface OnSortFilterListener {
        /**
         *
         * @param sortType see AppConstants.ROLE_SORT_BY_XX
         * @param races
         * @param kingdom
         */
        void onSortAndFilter(int sortType, List<Race> races, Kingdom kingdom);

        long getStoryId();
    }
}
