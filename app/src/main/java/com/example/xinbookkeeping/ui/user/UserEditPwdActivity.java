package com.example.xinbookkeeping.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class UserEditPwdActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_pwd;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        EditText et_old = findViewById(R.id.et_old);
        EditText et = findViewById(R.id.et);
        String old = getIntent().getStringExtra("old");
        UserSqLiteHelper helper = new UserSqLiteHelper(this);

        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldP = et_old.getText().toString();
                String newP = et.getText().toString();
                if (TextUtils.isEmpty(oldP)) {
                    toast("旧密码不能为空");
                    return;
                }

                if (TextUtils.isEmpty(newP)) {
                    toast("新密码不能为空");
                    return;
                }

                if (!oldP.equals(old)) {
                    toast("旧密码错误");
                    return;
                }

                if (helper.updateUpwd(UserDataManager.getInstance().getId() + "", newP) != -1) {
                    toast("修改成功");
                    finish();
                } else {
                    toast("修改失败");
                }
            }
        });
    }
}
