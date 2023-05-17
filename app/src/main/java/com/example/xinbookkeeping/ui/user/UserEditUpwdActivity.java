package com.example.xinbookkeeping.ui.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class UserEditUpwdActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_pwd;
    }

    @Override
    public void init() {
        super.init();
        EditText et = findViewById(R.id.et);

        UserSqLiteHelper helper = new UserSqLiteHelper(this);

        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nickname = et.getText().toString();
                if (TextUtils.isEmpty(nickname)) {
                    toast("昵称不能为空");
                    return;
                }

                if (helper.updateNickname(UserDataManager.getInstance().getId() + "", nickname) != -1) {
                    toast("修改成功");
                    finish();
                } else {
                    toast("修改失败");
                }
            }
        });
    }
}
