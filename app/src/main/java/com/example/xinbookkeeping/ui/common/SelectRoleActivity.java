package com.example.xinbookkeeping.ui.common;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.ui.company.CompanyLoginActivity;
import com.example.xinbookkeeping.ui.user.UserLoginActivity;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class SelectRoleActivity extends BaseActivity {

    public static final String ROLE_USER = "user";
    public static final String ROLE_COMPANY = "company";
    // 记住选择
    private CheckBox mCbRemember;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_role;
    }

    @Override
    public void init() {
        super.init();
        mCbRemember = findViewById(R.id.cb_remember);
        findViewById(R.id.tv_user).setOnClickListener(this::showDialog);
        findViewById(R.id.tv_company).setOnClickListener(this::showDialog);

        Intent intent = getIntent();
        String from = intent.getStringExtra("FROM");
        if (from != null && from.equals("LOGIN")) {
            // 从登录页面返回 不自动跳转
            return;
        }
        // 判断是否记住角色
        Class<?> cls = null;
        SharedPreferences common = getSharedPreferences(SPConstant.COMMON, MODE_PRIVATE);
        String role = common.getString(SPConstant.REMEMBER_ROLE, "");
        switch (role) {
            // 公司
            case SelectRoleActivity.ROLE_COMPANY:
                cls = CompanyLoginActivity.class;
                break;
            // 用户
            case SelectRoleActivity.ROLE_USER:
                cls = UserLoginActivity.class;
                break;

            default:
                break;
        }

        if (cls != null) {
            startActivity(cls);
            finish();
        }
    }

    private void showDialog(View view) {
        String role = view.getId() == R.id.tv_company ? ROLE_COMPANY : ROLE_USER;
        boolean isCompany = role.equals(ROLE_COMPANY);
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setTitle("提示")
                .setMessage("是否确认当前选择：" + (isCompany ? "公司" : "用户"))
                .setNegativeButton("取消", (dialog, which) -> {

                })
                .setPositiveButton("确认", (dialog, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences(SPConstant.COMMON, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // 记住选项是否勾选
                    if (mCbRemember.isChecked()) {
                        editor.putString(SPConstant.REMEMBER_ROLE, role);
                    } else {
                        editor.putString(SPConstant.REMEMBER_ROLE, "");
                    }
                    if (isCompany) {
                        startActivity(CompanyLoginActivity.class);
                    } else {
                        startActivity(UserLoginActivity.class);
                    }
                    editor.apply();
                })
                .create();

        alertDialog.show();
    }
}
