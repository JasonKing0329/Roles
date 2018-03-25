package com.king.app.roles.page.race;

import android.os.Bundle;
import android.view.View;

import com.king.app.roles.model.entity.Race;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:41
 */

public class RaceFragment extends ModuleFragment<RacePresenter> implements RaceView {

    private static final String KEY_STORY_ID = "story_id";

    private RaceAdapter raceAdapter;

    public static RaceFragment newInstance(long storyId) {
        RaceFragment fragment = new RaceFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onCreate(View view) {

    }

    @Override
    protected RacePresenter createPresenter() {
        return new RacePresenter();
    }

    @Override
    protected void loadData() {
        presenter.loadRaces(getArguments().getLong(KEY_STORY_ID));
    }

    @Override
    public void showRaces(List<Race> races) {
        if (raceAdapter == null) {
            raceAdapter = new RaceAdapter();
            raceAdapter.setList(races);
            raceAdapter.setSwipeMenuRecyclerView(rvItems);
            rvItems.setAdapter(raceAdapter);
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

        RaceEditor editor = new RaceEditor();
        editor.setOnRaceListener(new RaceEditor.OnRaceListener() {
            @Override
            public void onSaveRace(String name, String description) {
                presenter.addNewRace(name, description);
                loadData();
                showMessage("Save successfully");
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Race")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getChildFragmentManager(), "DraggableDialogFragment");
    }

    @Override
    public void confirmDrag() {
        presenter.confirmDrag(raceAdapter.getList());
        loadData();
        notifyDragDone();
    }

    @Override
    public void confirmDelete() {
        presenter.confirmDelete(raceAdapter.getSelectedData());
        loadData();
        notifyDeleteDone();
    }

}
