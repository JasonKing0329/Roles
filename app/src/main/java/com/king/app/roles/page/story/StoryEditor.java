package com.king.app.roles.page.story;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.king.app.roles.R;
import com.king.app.roles.base.BaseViewModel;
import com.king.app.roles.base.RApplication;
import com.king.app.roles.databinding.FragmentStoryEditorBinding;
import com.king.app.roles.model.entity.Story;
import com.king.app.roles.view.dialog.DraggableContentFragment;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/5 14:51
 */
public class StoryEditor extends DraggableContentFragment<FragmentStoryEditorBinding, BaseViewModel> {

    private Story story;

    public OnStoryListener onStoryListener;

    public void setOnStoryListener(OnStoryListener onStoryListener) {
        this.onStoryListener = onStoryListener;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_story_editor;
    }

    @Override
    protected BaseViewModel createViewModel() {
        return null;
    }

    @Override
    protected void onCreate(View view) {
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                story.setType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.tvOk.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                binding.etName.setError("Empty name");
                return;
            }
            story.setName(name);
            RApplication.getInstance().getDaoSession().getStoryDao().insertOrReplace(story);
            RApplication.getInstance().getDaoSession().getStoryDao().detachAll();

            if (onStoryListener != null) {
                onStoryListener.onEditSuccess(story);
            }
            dismiss();
        });
    }

    @Override
    protected void onCreateData() {
        if (story != null) {
            binding.etName.setText(story.getName());
            binding.spType.setSelection(story.getType());
        }
        else {
            story = new Story();
        }
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public interface OnStoryListener {
        void onEditSuccess(Story story);
    }
}
