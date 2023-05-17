package com.example.xinbookkeeping.ui.company;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class EditUserPostActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_one;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);
        Intent intent = getIntent();
        String old = intent.getStringExtra("Post");
        String id = intent.getStringExtra("Id");
        EditText et = findViewById(R.id.et);
        et.setHint("原岗位：" + old);

        findViewById(R.id.tv_confirm).setOnClickListener(view -> {
            String post = et.getText().toString();
            if (!TextUtils.isEmpty(post)) {
                helper.updatePost(id, post);
                toast("修改成功");
                finish();
            } else {
                toast("新岗位不能为空");
            }

        });
    }
}
