package com.example.xinbookkeeping.ui.company;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class CompanyEditUnitnameActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_one;
    }

    @Override
    public void init() {
        super.init();
        EditText et = findViewById(R.id.et);

        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);

        findViewById(R.id.tv_confirm).setOnClickListener(v -> {

            String nickname = et.getText().toString();
            if (TextUtils.isEmpty(nickname)) {
                toast("公司名不能为空");
                return;
            }

            if (helper.updateUnitName(UserDataManager.getInstance().getId() + "", nickname) != -1) {
                toast("修改成功");
                finish();
            } else {
                toast("修改失败");
            }
        });
    }
}
