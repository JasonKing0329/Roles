package com.king.app.roles.page.chapter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.roles.R;
import com.king.app.roles.base.MvvmActivity;
import com.king.app.roles.databinding.ActivityWritingEditorBinding;
import com.king.app.roles.page.selector.ImageSelectorActivity;
import com.king.app.roles.utils.ScreenUtils;
import com.king.app.roles.view.dialog.AlertDialogFragment;
import com.king.app.roles.view.dialog.DraggableDialogFragment;
import com.king.app.roles.view.widget.rich.RichEditor;

/**
 * Desc:
 *
 * @author：Jing Yang
 * @date: 2019/8/8 13:41
 */
public class EditorActivity extends MvvmActivity<ActivityWritingEditorBinding, EditorViewModel> {

    public static String EXTRA_CHAPTER_ID = "chapter_id";

    private final int REQUEST_IMAGE = 101;

    private int mColor;

    @Override
    protected EditorViewModel createViewModel() {
        return ViewModelProviders.of(this).get(EditorViewModel.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_writing_editor;
    }

    @Override
    protected void initView() {
        binding.setModel(viewModel);

        initEditor();
        initActions();

        binding.actionbar.showConfirmStatus(0);
        binding.actionbar.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public boolean disableInstantDismissConfirm() {
                return true;
            }

            @Override
            public boolean disableInstantDismissCancel() {
                return true;
            }

            @Override
            public boolean onConfirm(int actionId) {
                viewModel.saveContent(binding.editor.getHtml());
                return false;
            }

            @Override
            public boolean onCancel(int actionId) {
                onBackPressed();
                return false;
            }
        });
        binding.actionbar.setOnBackListener(() -> onBackPressed());
    }

    private void initActions() {
        binding.ivSaveAs.setOnClickListener(v -> selectFileType());
        binding.ivUndo.setOnClickListener(v -> binding.editor.undo());
        binding.ivRedo.setOnClickListener(v -> binding.editor.redo());
        binding.ivBold.setOnClickListener(v -> binding.editor.setBold());
        binding.ivIndent.setOnClickListener(v -> binding.editor.setIndent());
        binding.ivText.setOnClickListener(v -> setTextSize());
        binding.ivColor.setOnClickListener(v -> setTextColor());
        binding.ivImage.setOnClickListener(v -> selectImage());
        binding.tvHtml.setOnClickListener(v -> {
            if (binding.etPreview.getVisibility() == View.VISIBLE) {
                binding.editor.setHtml(binding.etPreview.getText().toString());
                binding.etPreview.setVisibility(View.GONE);
            }
            else {
                binding.etPreview.setText(binding.editor.getHtml());
                binding.etPreview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                String imagePath = data.getStringExtra(ImageSelectorActivity.RESP_FILE_PATH);
                viewModel.insertImageToHtml(imagePath);
            }
        }
    }

    private void setTextSize() {
        String[] items = new String[] {
                "12", "14", "16", "18", "20", "22", "24", "26", "28", "30"
        };
//        String[] items = new String[] {
//                "1", "2", "3", "4", "5", "6", "7"
//        };
        new AlertDialogFragment()
                .setItems(items, (dialog, which) -> {
                    // 目前仅支持调整全部字体大小(12-30)
                    binding.editor.setEditorFontSize(12 + which * 2);

                    // 用这个可以调整局部字体大小(1~7)
//                    binding.editor.setFontSize(which + 1);
                })
                .show(getSupportFragmentManager(), "AlertDialogFragment");
    }

    private void setTextColor() {
        int initColor = mColor == 0 ? getResources().getColor(R.color.text_main):mColor;
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(initColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
//                .setOnColorSelectedListener(selectedColor -> binding.editor.setTextColor(selectedColor))
                .setPositiveButton("Ok", (dialog, selectedColor, allColors) -> {
                    mColor = selectedColor;
                    binding.editor.setTextColor(selectedColor);
                    binding.ivColor.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                })
                .setNegativeButton("Cancel", null)
                .build()
                .show();
    }

    private void initEditor() {
        binding.editor.setEditorFontSize(16);// 对应setFontSize(3)
        binding.editor.setEditorFontColor(getResources().getColor(R.color.text_main));
        int padding = ScreenUtils.dp2px(8);
        binding.editor.setPadding(padding, padding, padding, padding);
    }

    @Override
    public void onBackPressed() {
        showConfirmCancelMessage("Content change will not be saved, are you sure to leave?"
                , (dialog, which) -> finish()
                , null);
    }

    @Override
    protected void initData() {
        viewModel.selectFileType.observe(this, result -> selectFileType());
        viewModel.contentObserver.observe(this, content -> showContent(content));
        viewModel.insertImage.observe(this, bean -> binding.editor.insertImage("file://" + bean.getFilePath(), bean.getAlt(), bean.getWidth(), bean.getHeight()));
        viewModel.loadData(getChapterId());
    }

    private void selectFileType() {
        FileTypeSelector selector = new FileTypeSelector();
        selector.setChapter(viewModel.getChapter());
        selector.setOnSelectListener(file -> viewModel.saveContent(binding.editor.getHtml(), file));
        DraggableDialogFragment dialog = new DraggableDialogFragment.Builder()
                .setTitle("Select file type")
                .setContentFragment(selector)
                .build();
        dialog.show(getSupportFragmentManager(), "FileTypeSelector");
    }

    private void showContent(String content) {
        if (TextUtils.isEmpty(content)) {
            binding.editor.setPlaceholder("Input content here");
        }
        else {
            binding.editor.setHtml(content);
        }
    }

    private long getChapterId() {
        return getIntent().getLongExtra(EXTRA_CHAPTER_ID, -1);
    }
}
