package com.example.xinbookkeeping.ui.company;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class CompanyEditTelenumActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_one;
    }

    @Override
    public void init() {
        super.init();
        EditText et = findViewById(R.id.et);

        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);

        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String telenum = et.getText().toString();
                if (TextUtils.isEmpty(telenum)) {
                    toast("手机号不能为空");
                    return;
                }

                if (helper.updateTeleNum(UserDataManager.getInstance().getId() + "", telenum) != -1) {
                    toast("修改成功");
                    finish();
                } else {
                    toast("修改失败");
                }

                finish();
            }
        });
    }
}
