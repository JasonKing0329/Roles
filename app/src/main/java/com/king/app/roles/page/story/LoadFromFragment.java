package com.king.app.roles.page.story;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseBindingAdapter;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.IFragmentHolder;
import com.king.app.roles.base.MvvmFragment;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.databinding.AdapterItemLoadfromBinding;
import com.king.app.roles.databinding.FragmentLoadfromBinding;
import com.king.app.roles.model.ChapterModel;
import com.king.app.roles.model.SettingProperty;
import com.king.app.roles.utils.DBExporter;
import com.king.app.roles.utils.FileUtil;
import com.king.app.roles.view.dialog.AlertDialogFragment;
import com.king.app.roles.view.dialog.DraggableHolder;

import java.io.File;
import java.io.IOException;
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
                        String db = file.getPath() + File.separator + AppConfig.DB_NAME;
                        replaceDatabase(new File(db));
                        String xmlContent = file.getPath() + File.separator + ChapterModel.SP_CONTENT + ".xml";
                        replaceContentXml(new File(xmlContent));
                        String xmlPref = file.getPath() + File.separator + SettingProperty.SETTING_FILE + ".xml";
                        replacePrefXml(new File(xmlPref));
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

    public static boolean replaceDatabase(File source) {
        if (source == null || !source.exists()) {
            return false;
        }
        // 删除源目录database
        String parent = DBExporter.getDatabaseFolder();
        FileUtil.deleteFile(parent + File.separator + AppConfig.DB_NAME);
        FileUtil.deleteFile(parent + File.separator + AppConfig.DB_JOURNAL);
        try {
            FileUtil.copyFile(source, new File(DBExporter.getDatabasePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean replaceContentXml(File source) {
        if (source == null || !source.exists()) {
            return false;
        }
        // 删除源目录content
        String path = ChapterModel.getContentPreference();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 实测如果只deleteFile，还是会读到application缓存的替换前的xml，只有把application杀掉才能更新过来
            // 所以需要用deleteSharedPreferences
            RApplication.getInstance().deleteSharedPreferences(ChapterModel.SP_CONTENT);
        }
        else {
            // android N以前没有deleteSharedPreferences，只能deleteFile，未测过不知有没有用
            FileUtil.deleteFile(path);
        }
        try {
            FileUtil.copyFile(source, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean replacePrefXml(File source) {
        if (source == null || !source.exists()) {
            return false;
        }
        // 删除源目录pref
        String path = SettingProperty.getSharedPreference();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 实测如果只deleteFile，还是会读到application缓存的替换前的xml，只有把application杀掉才能更新过来
            // 所以需要用deleteSharedPreferences
            RApplication.getInstance().deleteSharedPreferences(SettingProperty.SETTING_FILE);
        }
        else {
            // android N以前没有deleteSharedPreferences，只能deleteFile，未测过不知有没有用
            FileUtil.deleteFile(path);
        }
        try {
            FileUtil.copyFile(source, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
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
