package com.king.app.roles.page.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.databinding.FragmentModuleBinding;
import com.king.app.roles.utils.DebugLog;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;

/**
 * @desc
 * @auth 景阳
 * @time 2018/3/25 0025 20:42
 */

public abstract class ModuleFragment<VM extends ModuleViewModel> extends MvvmFragment<FragmentModuleBinding, VM> {

    protected static final String KEY_SELECT_MODE = "select_mode";

    protected ModuleFragmentHolder holder;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (ModuleFragmentHolder) holder;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_module;
    }

    @Override
    protected void onCreateData() {
        binding.setViewModel(viewModel);
        if (viewModel.normalVisibility.get() == View.VISIBLE) {
            initNormalList();
        }
        else if (viewModel.draggableVisibility.get() == View.VISIBLE) {
            initDraggableList();
        }

        loadData();
    }

    private void initNormalList() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvItemsNormal.setLayoutManager(manager);
    }

    private void initDraggableList() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvItems.setLayoutManager(manager);
        binding.rvItems.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 不同的ViewType不能拖拽换位置。
                if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
                    return false;
                }

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - binding.rvItems.getHeaderItemCount();
                int toPosition = targetHolder.getAdapterPosition() - binding.rvItems.getHeaderItemCount();

                getAdapter().swapData(fromPosition, toPosition);
                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。

            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

            }
        });
        binding.rvItems.setOnItemStateChangedListener(new OnItemStateChangedListener() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                    DebugLog.e("状态：拖拽");
                    // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
//                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(StoryListActivity.this, R.color.grayef));
                } else if (actionState == OnItemStateChangedListener.ACTION_STATE_SWIPE) {
                    DebugLog.e("状态：滑动删除");
                } else if (actionState == OnItemStateChangedListener.ACTION_STATE_IDLE) {
                    DebugLog.e("状态：手指松开");
                    // 在手松开的时候还原背景。
//                ViewCompat.setBackground(viewHolder.itemView, ContextCompat.getDrawable(RecordOrderPadActivity.this, R.drawable.white));
                }
            }
        });
    }

    protected boolean isSelectMode() {
        return getArguments().getBoolean(KEY_SELECT_MODE);
    }

    protected void onSelectId(long id) {
        holder.onSelectId(id);
    }

    protected abstract void loadData();

    protected abstract ModuleAdapter getAdapter();

    public abstract void addNewItem();

    /**
     * 子类选择覆盖
     * @param delete
     */
    public void setDeleteMode(boolean delete) {
        getAdapter().setSelect(delete);
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 子类选择覆盖
     * @param drag
     */
    public void setDragMode(boolean drag) {
        getAdapter().setDrag(drag);
        getAdapter().notifyDataSetChanged();
    }

}
