package com.king.app.roles.page.role;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseFragment;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.KingdomDao;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.RaceDao;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.utils.ListUtil;
import com.king.app.roles.view.adapter.TagAdapter;
import com.king.app.roles.view.dialog.DraggableHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class RoleEditor extends BaseFragment {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.rv_race)
    RecyclerView rvRace;
    @BindView(R.id.rv_kingdom)
    RecyclerView rvKingdom;
    @BindView(R.id.et_power)
    EditText etPower;

    private Role mRole;

    private OnRoleListener onRoleListener;

    private DraggableHolder draggableHolder;

    private TagAdapter<Race> raceAdapter;

    private TagAdapter<Kingdom> kingdomAdapter;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.dialog_role_editor;
    }

    @Override
    protected void onCreate(View view) {

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        rvRace.setLayoutManager(manager);
        manager = new GridLayoutManager(getActivity(), 3);
        rvKingdom.setLayoutManager(manager);

        if (onRoleListener != null) {
            mRole = onRoleListener.getInitKingdom();
            if (mRole != null) {
                etName.setText(mRole.getName());
                etNickname.setText(mRole.getNickname());
                etPower.setText(mRole.getPower());
                etDescription.setText(mRole.getDescription());
            }
        }

        loadRaces();
        loadKingdoms();
    }

    /**
     * 多选
     */
    private void loadRaces() {
        RaceDao dao = RApplication.getInstance().getDaoSession().getRaceDao();
        List<Race> races = dao.queryBuilder()
                .where(RaceDao.Properties.StoryId.eq(onRoleListener.getStoryId()))
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
        if (mRole != null && mRole.getRaceList() != null) {
            raceAdapter.setSelectedData(mRole.getRaceList());
        }
        rvRace.setAdapter(raceAdapter);
    }

    /**
     * 单选
     */
    private void loadKingdoms() {
        KingdomDao dao = RApplication.getInstance().getDaoSession().getKingdomDao();
        List<Kingdom> kingdoms = dao.queryBuilder()
                .where(KingdomDao.Properties.StoryId.eq(onRoleListener.getStoryId()))
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
        if (mRole != null && mRole.getKingdom() != null) {
            List<Kingdom> list = new ArrayList<>();
            list.add(mRole.getKingdom());
            kingdomAdapter.setSelectedData(list);
        }
        rvKingdom.setAdapter(kingdomAdapter);
    }

    public void setOnRoleListener(OnRoleListener onRoleListener) {
        this.onRoleListener = onRoleListener;
    }

    @OnClick(R.id.tv_ok)
    public void onClick() {
        if (onRoleListener != null) {
            if (mRole == null) {
                mRole = new Role();
            }
            mRole.setName(etName.getText().toString());
            mRole.setNickname(etNickname.getText().toString());
            mRole.setPower(etPower.getText().toString());
            mRole.setDescription(etDescription.getText().toString());

            Kingdom kingdom = null;
            List<Kingdom> kingdoms = kingdomAdapter.getSelectedData();
            if (!ListUtil.isEmpty(kingdoms)) {
                kingdom = kingdoms.get(0);
            }
            onRoleListener.onSaveRole(mRole, raceAdapter.getSelectedData(), kingdom);
        }
        if (draggableHolder != null) {
            draggableHolder.dismiss();
        }
    }

    @OnClick({R.id.tv_chapter, R.id.tv_relations})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_chapter:
                break;
            case R.id.tv_relations:
                break;
        }
    }

    public interface OnRoleListener {
        void onSaveRole(Role role, List<Race> raceList, Kingdom kingdom);

        long getStoryId();

        Role getInitKingdom();
    }
}
