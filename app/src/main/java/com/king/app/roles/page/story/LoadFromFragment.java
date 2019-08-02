package com.king.app.roles.page.story;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseBindingAdapter;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.databinding.AdapterItemLoadfromBinding;
import com.king.app.roles.databinding.FragmentLoadfromBinding;
import com.king.app.roles.utils.FileUtil;
import com.king.app.roles.view.dialog.AlertDialogFragment;
import com.king.app.roles.view.dialog.DraggableHolder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/2 16:13
 */
public class LoadFromFragment extends MvvmFragment<FragmentLoadfromBinding, BaseViewModel> {

    private DraggableHolder draggableHolder;

    private List<File> list;

    private ItemAdapter itemAdapter;

    private OnDatabaseChangedListener onDatabaseChangedListener;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        if (holder instanceof DraggableHolder) {
            this.draggableHolder = (DraggableHolder) holder;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_loadfrom;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvList.setLayoutManager(manager);

        binding.tvOk.setOnClickListener(v -> confirmLoadFrom());
    }

    public void setOnDatabaseChangedListener(OnDatabaseChangedListener onDatabaseChangedListener) {
        this.onDatabaseChangedListener = onDatabaseChangedListener;
    }

    @Override
    protected void onCreateData() {
        File file = new File(AppConfig.HISTORY_BASE);
        list = Arrays.asList(file.listFiles());

        itemAdapter = new ItemAdapter();
        itemAdapter.setList(list);
        binding.rvList.setAdapter(itemAdapter);
    }

    private void confirmLoadFrom() {
        if (itemAdapter.getSelection() != -1) {
            final File file = list.get(itemAdapter.getSelection());
            new AlertDialogFragment()
                    .setMessage(getString(R.string.load_from_warning_msg))
                    .setPositiveText(getString(R.string.ok))
                    .setPositiveListener((dialogInterface, i) -> {
                        FileUtil.replaceDatabase(file);
                        if (draggableHolder != null) {
                            draggableHolder.dismiss();
                        }
                        if (onDatabaseChangedListener != null) {
                            onDatabaseChangedListener.onDatabaseChanged();
                        }
                    })
                    .setNegativeText(getString(R.string.cancel))
                    .show(getChildFragmentManager(), "AlertDialogFragment");
        }
    }

    private class ItemAdapter extends BaseBindingAdapter<AdapterItemLoadfromBinding, File> {

        private int selection = -1;

        public int getSelection() {
            return selection;
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.adapter_item_loadfrom;
        }

        public void setSelection(int selection) {
            this.selection = selection;
        }

        @Override
        protected void onBindItem(AdapterItemLoadfromBinding binding, int position, File bean) {
            binding.tvName.setText(bean.getName());
            if (position == selection) {
                binding.groupItem.setBackgroundColor(getResources().getColor(R.color.text_sub));
            }
            else {
                binding.groupItem.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }

        @Override
        protected void onClickItem(View v, int position) {
            int lastPosition = selection;
            selection = position;
            if (lastPosition != -1) {
                notifyItemChanged(lastPosition);
            }
            notifyItemChanged(selection);
        }
    }

    public interface OnDatabaseChangedListener {
        void onDatabaseChanged();
    }
}
