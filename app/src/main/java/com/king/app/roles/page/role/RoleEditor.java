package com.king.app.roles.page.role;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConstants;
import com.king.app.roles.conf.StoryType;
import com.king.app.roles.databinding.DialogRoleEditorBinding;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.ChapterDao;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.model.entity.KingdomDao;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.model.entity.RaceDao;
import com.king.app.roles.model.entity.Role;
import com.king.app.roles.model.entity.RoleRelations;
import com.king.app.roles.model.entity.RoleRelationsDao;
import com.king.app.roles.page.story.StoryInstance;
import com.king.app.roles.utils.ListUtil;
import com.king.app.roles.view.adapter.TagAdapter;
import com.king.app.roles.view.dialog.DraggableHolder;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 22:03
 */

public class RoleEditor extends MvvmFragment<DialogRoleEditorBinding, BaseViewModel> {

    private Role mRole;

    private OnRoleListener onRoleListener;

    private DraggableHolder draggableHolder;

    private TagAdapter<Race> raceAdapter;

    private TagAdapter<Kingdom> kingdomAdapter;

    private long mChapterId;

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
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {

        binding.tvChapter.setOnClickListener(v -> selectChapter());
        binding.tvRelations.setOnClickListener(v -> {
            if (onRoleListener != null) {
                onRoleListener.showRelations(mRole);
            }
        });
        binding.tvOk.setOnClickListener(v -> onClickOk());

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        binding.rvRace.setLayoutManager(manager);
        manager = new GridLayoutManager(getActivity(), 3);
        binding.rvKingdom.setLayoutManager(manager);

        binding.tvRelations.setVisibility(View.GONE);
    }

    @Override
    protected void onCreateData() {

        if (StoryInstance.getInstance().getStory().getType() == StoryType.R_K_C_C) {
            binding.etPower.setVisibility(View.VISIBLE);
        }
        else {
            binding.etPower.setVisibility(View.GONE);
        }

        if (onRoleListener != null) {
            mRole = onRoleListener.getInitKingdom();
            // 编辑模式才显示Relations
            if (mRole != null) {
                binding.tvRelations.setVisibility(View.VISIBLE);
                binding.etName.setText(mRole.getName());
                binding.etNickname.setText(mRole.getNickname());
                binding.etPower.setText(mRole.getPower());
                binding.etDescription.setText(mRole.getDescription());
                if (mRole.getChapter() != null) {
                    if (mRole.getChapter().getLevel() == AppConstants.CHAPTER_LEVEL_SECOND) {
                        binding.tvChapter.setText("Chapter   第" + mRole.getChapter().getParent().getIndex() + "章 - (" + mRole.getChapter().getIndex() + ")");
                    }
                    else {
                        binding.tvChapter.setText("Chapter   第" + mRole.getChapter().getIndex() + "章");
                    }
                }

                // 加载Relation人数
                loadRelationNumber();
            }
        }

        loadRaces();
        loadKingdoms();
    }

    private void loadRelationNumber() {
        RoleRelationsDao dao = RApplication.getInstance().getDaoSession().getRoleRelationsDao();
        QueryBuilder<RoleRelations> builder = dao.queryBuilder();
        int count = (int) builder
                .where(builder.or(RoleRelationsDao.Properties.RoleId.eq(mRole.getId()), RoleRelationsDao.Properties.RelationId.eq(mRole.getId())))
                .buildCount().count();
        binding.tvRelations.setText("Relations (" + count + " persons)");
    }

    public void refreshRelations() {
        loadRelationNumber();
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
        binding.rvRace.setAdapter(raceAdapter);
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
        binding.rvKingdom.setAdapter(kingdomAdapter);
    }

    public void setOnRoleListener(OnRoleListener onRoleListener) {
        this.onRoleListener = onRoleListener;
    }

    private void onClickOk() {
        if (onRoleListener != null) {
            if (mRole == null) {
                mRole = new Role();
            }
            mRole.setName(binding.etName.getText().toString());
            mRole.setNickname(binding.etNickname.getText().toString());
            mRole.setPower(binding.etPower.getText().toString());
            mRole.setDescription(binding.etDescription.getText().toString());
            mRole.setChapterId(mChapterId);

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

    private void selectChapter() {
        onRoleListener.selectChapter();
    }

    public void onChapterSelected(long chapterId) {
        mChapterId = chapterId;
        Chapter chapter = loadChapter();
        if (chapter.getLevel() == AppConstants.CHAPTER_LEVEL_SECOND) {
            binding.tvChapter.setText("Chapter   第" + chapter.getParent().getIndex() + "章 - (" + chapter.getIndex() + ")");
        }
        else {
            binding.tvChapter.setText("Chapter   第" + chapter.getIndex() + "章");
        }
    }

    private Chapter loadChapter() {
        ChapterDao dao = RApplication.getInstance().getDaoSession().getChapterDao();
        Chapter chapter = dao.queryBuilder()
                .where(ChapterDao.Properties.Id.eq(mChapterId))
                .unique();
        return chapter;
    }

    public interface OnRoleListener {
        void onSaveRole(Role role, List<Race> raceList, Kingdom kingdom);

        long getStoryId();

        Role getInitKingdom();

        void selectChapter();

        void showRelations(Role role);
    }
}
