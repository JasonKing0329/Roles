package com.king.app.roles.page.kingdom;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.jactionbar.OnMenuItemListener;
import com.king.app.roles.R;
import com.king.app.roles.databinding.AdapterKingdomItemBinding;
import com.king.app.roles.model.entity.Kingdom;
import com.king.app.roles.page.module.ModuleAdapter;
import com.king.app.roles.page.module.ModuleFragment;
import com.king.app.roles.view.dialog.DraggableDialogFragment;

import java.util.List;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 21:41
 */

public class KingdomFragment extends ModuleFragment<KingdomViewModel> {

    private KingdomAdapter kingdomAdapter;

    public static KingdomFragment newInstance(long storyId) {
        KingdomFragment fragment = new KingdomFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_STORY_ID, storyId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected KingdomViewModel createViewModel() {
        return ViewModelProviders.of(this).get(KingdomViewModel.class);
    }

    @Override
    protected void onCreate(View view) {
        holder.getJActionbar().inflateMenu(R.menu.page_kingdom);
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
        viewModel.kingdomsObserver.observe(this, new Observer<List<Kingdom>>() {
            @Override
            public void onChanged(@Nullable List<Kingdom> kingdoms) {
                showKingdoms(kingdoms);
            }
        });
        viewModel.loadKingdoms(getStoryId());
    }

    private void showKingdoms(List<Kingdom> kingdoms) {
        if (kingdomAdapter == null) {
            kingdomAdapter = new KingdomAdapter();
            kingdomAdapter.setList(kingdoms);
            kingdomAdapter.setSwipeMenuRecyclerView(binding.rvItems);
            kingdomAdapter.setOnItemClickListener((view, position, data) -> showKingdomEditor(data));
            binding.rvItems.setAdapter(kingdomAdapter);
        }
        else {
            kingdomAdapter.setList(kingdoms);
            kingdomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected ModuleAdapter<AdapterKingdomItemBinding, Kingdom> getAdapter() {
        return kingdomAdapter;
    }

    @Override
    public void addNewItem() {

        showKingdomEditor(null);
    }

    private void showKingdomEditor(final Kingdom kingdom) {
        KingdomEditor editor = new KingdomEditor();
        editor.setOnRaceListener(new KingdomEditor.OnKingdomListener() {

            @Override
            public void onSaveKingdom(Kingdom kingdom) {
                viewModel.insertOrUpdate(kingdom);
                loadData();
                showMessageLong("Save successfully");
            }

            @Override
            public Kingdom getInitKingdom() {
                return kingdom;
            }
        });
        DraggableDialogFragment dialogFragment = new DraggableDialogFragment.Builder()
                .setTitle("Kingdom")
                .setContentFragment(editor)
                .build();
        dialogFragment.show(getChildFragmentManager(), "DraggableDialogFragment");
    }

    public void confirmDrag() {
        viewModel.confirmDrag(kingdomAdapter.getList());
        loadData();
    }

    public void confirmDelete() {
        viewModel.confirmDelete(kingdomAdapter.getSelectedData());
        loadData();
    }

}
