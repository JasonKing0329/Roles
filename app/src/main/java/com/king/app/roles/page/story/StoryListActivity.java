package com.king.app.roles.page.story;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseRecyclerAdapter;
import com.king.app.roles.base.MvpActivity;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.utils.DebugLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemStateChangedListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/3/23 16:39
 */
public class StoryListActivity extends MvpActivity<StoryListPresenter> implements StoryListView {

    @BindView(R.id.rv_stories)
    SwipeMenuRecyclerView rvStories;
    @BindView(R.id.group_icon)
    LinearLayout groupIcon;
    @BindView(R.id.group_confirm)
    LinearLayout groupConfirm;

    private StoryListAdapter storyListAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_story_list;
    }

    @Override
    protected void initView() {

        groupConfirm.setVisibility(View.GONE);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvStories.setLayoutManager(manager);
        rvStories.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                // 不同的ViewType不能拖拽换位置。
                if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
                    return false;
                }

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                int fromPosition = srcHolder.getAdapterPosition() - rvStories.getHeaderItemCount();
                int toPosition = targetHolder.getAdapterPosition() - rvStories.getHeaderItemCount();

                storyListAdapter.swapData(fromPosition, toPosition);
                return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。

            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

            }
        });
        rvStories.setOnItemStateChangedListener(new OnItemStateChangedListener() {
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == OnItemStateChangedListener.ACTION_STATE_DRAG) {
                    DebugLog.e("状态：拖拽");
                    // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(StoryListActivity.this, R.color.drag_bg));
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

    @Override
    protected StoryListPresenter createPresenter() {
        return new StoryListPresenter();
    }

    @Override
    protected void initData() {
        presenter.loadStories();
    }

    @Override
    public void showStories(List<Story> stories) {
        if (storyListAdapter == null) {
            storyListAdapter = new StoryListAdapter();
            storyListAdapter.setList(stories);
            storyListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<Story>() {
                @Override
                public void onClickItem(int position, Story data) {

                }
            });
            storyListAdapter.setSwipeMenuRecyclerView(rvStories);
            rvStories.setAdapter(storyListAdapter);
        } else {
            storyListAdapter.setList(stories);
            storyListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.iv_add, R.id.iv_delete, R.id.iv_move, R.id.tv_cancel, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                break;
            case R.id.iv_delete:
                storyListAdapter.setSelect(true);
                groupConfirm.setVisibility(View.VISIBLE);
                groupIcon.setVisibility(View.GONE);
                break;
            case R.id.iv_move:
                storyListAdapter.setDrag(true);
                groupConfirm.setVisibility(View.VISIBLE);
                groupIcon.setVisibility(View.GONE);
                break;
            case R.id.tv_cancel:
                storyListAdapter.setDrag(false);
                storyListAdapter.setSelect(false);
                storyListAdapter.notifyDataSetChanged();
                groupConfirm.setVisibility(View.GONE);
                groupIcon.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_ok:
                storyListAdapter.setDrag(false);
                storyListAdapter.setSelect(false);
                storyListAdapter.notifyDataSetChanged();
                groupConfirm.setVisibility(View.GONE);
                groupIcon.setVisibility(View.VISIBLE);
                break;
        }
    }

}
