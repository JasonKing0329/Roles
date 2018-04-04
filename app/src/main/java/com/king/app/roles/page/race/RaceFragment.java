package com.king.app.roles.page.race;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.jactionbar.OnMenuItemListener;
import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.model.entity.Race;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.page.module.ModuleViewModel;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:41
 */

public class RaceFragment extends ModuleFragment<RaceViewModel> {

    private RaceAdapter raceAdapter;

    public static RaceFragment newInstance(long storyId) {
        RaceFragment fragment = new RaceFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected RaceViewModel createViewModel() {
        return ViewModelProviders.of(this).get(RaceViewModel.class);
    }

    @Override
    protected void onCreate(View view) {
        holder.getJActionbar().inflateMenu(R.menu.page_race);
        holder.getJActionbar().setOnMenuItemListener(new OnMenuItemListener() {
            @Override
            public void onMenuItemSelected(int menuId) {
                switch (menuId) {
                    case R.id.menu_add:
                        addNewItem();
                        break;
                    case R.id.menu_delete:
                        holder.getJActionbar().showConfirmStatus(menuId);
                        setDeleteMode(true);
                        break;
                    case R.id.menu_drag:
                        holder.getJActionbar().showConfirmStatus(menuId);
                        setDragMode(true);
                        break;
                }
            }
        });
        holder.getJActionbar().setOnConfirmListener(new OnConfirmListener() {
            @Override
            public boolean disableInstantDismissConfirm() {
                return false;
            }

            @Override
            public boolean disableInstantDismissCancel() {
                return false;
            }

            @Override
            public boolean onConfirm(int actionId) {
                switch (actionId) {
                    case R.id.menu_delete:
                        confirmDelete();
                        setDeleteMode(false);
                        break;
                    case R.id.menu_drag:
                        confirmDrag();
                        setDragMode(false);
                        break;
                }
                return true;
            }

            @Override
            public boolean onCancel(int actionId) {
                switch (actionId) {
                    case R.id.menu_delete:
                        setDeleteMode(false);
                        break;
                    case R.id.menu_drag:
                        setDragMode(false);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void loadData() {
        viewModel.racesObserver.observe(this, new Observer<List<Race>>() {
            @Override
            public void onChanged(@Nullable List<Race> races) {
                showRaces(races);
            }
        });
        viewModel.loadRaces(getStoryId());
    }

    private void showRaces(List<Race> races) {
        if (raceAdapter == null) {
            raceAdapter = new RaceAdapter();
            raceAdapter.setList(races);
            raceAdapter.setSwipeMenuRecyclerView(binding.rvItems);
            raceAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<Race>() {
                @Override
                public void onClickItem(int position, Race data) {
                    showRaceEditor(data);
                }
            });
            binding.rvItems.setAdapter(raceAdapter);
        }
        else {
            raceAdapter.setList(races);
            raceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ModuleAdapter getAdapter() {
        return raceAdapter;
    }

    @Override
    public void addNewItem() {

        showRaceEditor(null);
    }

    private void showRaceEditor(final Race race) {
        RaceEditor editor = new RaceEditor();
        editor.setOnRaceListener(new RaceEditor.OnRaceListener() {
            @Override
            public void onSaveRace(Race race) {
                viewModel.insertOrUpdate(race);
                loadData();
                showMessageLong("Save successfully");
            }

            @Override
            public Race getInitRace() {
                return race;
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Race")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getChildFragmentManager(), "DraggableDialogFragment");
    }

    public void confirmDrag() {
        viewModel.confirmDrag(raceAdapter.getList());
        loadData();
    }

    public void confirmDelete() {
        viewModel.confirmDelete(raceAdapter.getSelectedData());
        loadData();
    }

}
