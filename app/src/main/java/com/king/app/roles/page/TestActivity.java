package com.king.app.roles.page;

import android.widget.Toast;

import com.king.app.jactionbar.OnBackListener;
import com.king.app.jactionbar.OnConfirmListener;
import com.king.app.jactionbar.OnMenuItemListener;
import com.king.app.jactionbar.OnSearchListener;
import com.king.app.jactionbar.JActionbar;
import com.king.app.roles.R;
import com.king.app.roles.base.BaseActivity;

import butterknife.BindView;

public class TestActivity extends BaseActivity {

    @BindView(R.id.jactionbar)
    JActionbar jactionbar;

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        jactionbar.setOnBackListener(new OnBackListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }
        });
        jactionbar.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public boolean disableInstantDismissConfirm() {
                return true;
            }

            @Override
            public boolean disableInstantDismissCancel() {
                return false;
            }

            @Override
            public boolean onConfirm(int actionId) {
                Toast.makeText(TestActivity.this, "onConfirm " + getResources().getResourceName(actionId), Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onCancel(int actionId) {
                return true;
            }
        });
        jactionbar.setOnMenuItemListener(new OnMenuItemListener() {
            @Override
            public void onMenuItemSelected(int menuId) {
                switch (menuId) {
                    case R.id.menu_gdb_surf_add:
                        Toast.makeText(TestActivity.this, "menu_gdb_surf_add", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_gdb_surf_delete:
                        jactionbar.showConfirmStatus(menuId);
                        Toast.makeText(TestActivity.this, "menu_gdb_surf_delete", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_gdb_surf_close:
                        Toast.makeText(TestActivity.this, "menu_gdb_surf_close", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_gdb_surf_edit:
                        Toast.makeText(TestActivity.this, "menu_gdb_surf_edit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_gdb_surf_view:
                        Toast.makeText(TestActivity.this, "menu_gdb_surf_view", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        jactionbar.setOnSearchListener(new OnSearchListener() {
            @Override
            public void onSearchWordsChanged(String words) {
                Toast.makeText(TestActivity.this, words, Toast.LENGTH_SHORT).show();
            }
        });
//        jactionbar.inflateMenu(R.menu.gdb_surf_http);
    }

}
