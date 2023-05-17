package com.example.xinbookkeeping.uibase;

import android.content.Intent;
import android.view.View;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.ui.common.SelectRoleActivity;

public abstract class BaseLoginActivity extends BaseActivity{

    @Override
    public void init() {
        super.init();
        setBackToolBar();
    }

    @Override
    protected void setBackToolBar() {
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            Intent intent = new Intent(BaseLoginActivity.this, SelectRoleActivity.class);
            intent.putExtra("FROM", "LOGIN" );
            startActivity(intent);
            finish();
        });
    }
}
