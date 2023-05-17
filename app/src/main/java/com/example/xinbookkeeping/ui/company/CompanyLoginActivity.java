package com.example.xinbookkeeping.ui.company;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;

import com.example.xinbookkeeping.ui.user.UserRegisterActivity;
import com.example.xinbookkeeping.uibase.BaseLoginActivity;


/**
 * 公司端
 * 登录页面
 */
public class CompanyLoginActivity extends BaseLoginActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_company_login;
    }

    @Override
    public void init() {
        super.init();
        SharedPreferences sharedPreferences = getSharedPreferences(SPConstant.COMMON, MODE_PRIVATE);
        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);
        // 注册
        findViewById(R.id.tv_register).setOnClickListener(v -> startActivity(CompanyRegisterActivity.class));
        EditText et_uid = findViewById(R.id.et_uid);
        EditText et_upwd = findViewById(R.id.et_upwd);
        CheckBox cb_auto_login = findViewById(R.id.cb_auto_login);
        TextView tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(v -> {
            String uid = et_uid.getText().toString().trim();
            String upwd = et_upwd.getText().toString().trim();
            if (TextUtils.isEmpty(uid)) {
                toast("账号不能为空");
                return;
            }

            if (TextUtils.isEmpty(upwd)) {
                toast("密码称不能为空");
                return;
            }

            CompanyBean bean = helper.query(uid);
            if (bean == null) {
                toast("公司不存在");
                return;
            }

            if (upwd.equals(bean.getUpwd())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (cb_auto_login.isChecked()) {
                    editor.putBoolean(SPConstant.IS_AUTO_LOGIN, true);
                    editor.putString(SPConstant.UID, uid);
                    editor.putString(SPConstant.UPWD, upwd);
                } else {
                    editor.putBoolean(SPConstant.IS_AUTO_LOGIN, false);
                    editor.putString(SPConstant.UID, "");
                    editor.putString(SPConstant.UPWD, "");
                }
                UserDataManager.getInstance().setUserData(bean.getId(), bean.getUid());
                editor.apply();
                toast("登录成功");
                startActivity(CompanyMainActivity.class);
            } else {
                toast("账号/密码错误");
                return;
            }

            finish();
        });

        // 是否自动登录
        boolean isAutoLogin = sharedPreferences.getBoolean(SPConstant.IS_AUTO_LOGIN, false);
        cb_auto_login.setChecked(isAutoLogin);
        if (isAutoLogin) {
            String uid = sharedPreferences.getString(SPConstant.UID, "");
            String upwd = sharedPreferences.getString(SPConstant.UPWD, "");
            et_uid.setText(uid);
            et_upwd.setText(upwd);
            tv_login.performClick();
        }
    }
}
