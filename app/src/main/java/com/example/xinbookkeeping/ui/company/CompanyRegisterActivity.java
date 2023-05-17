package com.example.xinbookkeeping.ui.company;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

/**
 * 公司端
 * 注册页面
 * */
public class CompanyRegisterActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_company_register;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);
        EditText et_unitname = findViewById(R.id.et_unitname);
        EditText et_uid = findViewById(R.id.et_uid);
        EditText et_telenum = findViewById(R.id.et_telenum);
        EditText et_upwd = findViewById(R.id.et_upwd);
        EditText et_upwd_confirm = findViewById(R.id.et_upwd_confirm);

        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitname = et_unitname.getText().toString().trim();
                String uid = et_uid.getText().toString().trim();
                String telenum = et_telenum.getText().toString().trim();
                String upwd = et_upwd.getText().toString().trim();
                String upwd_confirm = et_upwd_confirm.getText().toString().trim();

                if (TextUtils.isEmpty(unitname)) {
                    toast("公司名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(uid)) {
                    toast("用户名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(upwd)) {
                    toast("密码不能为空");
                    return;
                }

                int l = upwd.length();

                if (l < 6 || l > 13) {
                    toast("密码长度为6-13位");
                    return;
                }

                if (!upwd.equals(upwd_confirm)) {
                    toast("两次密码输入不一致");
                    return;
                }

                if (helper.insert(unitname, uid, telenum, upwd) != -1) {
                    toast("注册成功");
                    finish();
                } else {
                    toast("账号已存在，请修改账号");
                }
            }
        });
    }

}
