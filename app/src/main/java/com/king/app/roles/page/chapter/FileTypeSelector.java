package com.king.app.roles.page.chapter;

import android.text.TextUtils;
import android.view.View;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.conf.AppConfig;
import com.king.app.roles.databinding.FragmentFileTypeBinding;
import com.king.app.roles.model.entity.Chapter;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.page.story.StoryInstance;
import com.king.app.roles.view.dialog.DraggableContentFragment;

import java.io.File;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/8 14:33
 */
public class FileTypeSelector extends DraggableContentFragment<FragmentFileTypeBinding, BaseViewModel> {

    private Chapter chapter;

    public OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_file_type;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            onTypeChanged();
        });
        binding.tvOk.setOnClickListener(v -> setFile());
    }

    private void setFile() {
        StringBuffer buffer = new StringBuffer();
        String key = binding.etKey.getText().toString();
        if (TextUtils.isEmpty(key.trim())) {
            binding.etKey.setError("Can't be empty");
            return;
        }
        if (binding.rbFile.isChecked()) {
            Story story = StoryInstance.getInstance().getStory();
            String parent = "ST_" + story.getId();
            buffer.append("file://").append(AppConfig.CONTENT_BASE).append(File.separator)
                    .append(parent).append(File.separator)
                    .append(key).append(AppConfig.CONTENT_EXTRA);
        }
        else {
            buffer.append("xml://").append(key);
        }
        onSelectListener.onSelectFile(buffer.toString());
        dismiss();
    }

    @Override
    protected void onCreateData() {
        Story story = StoryInstance.getInstance().getStory();
        binding.etKey.setText("ST_" + story.getId() + "_CH_" + chapter.getId());
        onTypeChanged();
    }

    private void onTypeChanged() {
        if (binding.rbFile.isChecked()) {
            binding.tvKey.setText("File name");
        }
        else {
            binding.tvKey.setText("Key");
        }
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public interface OnSelectListener {
        void onSelectFile(String file);
    }
}
