package com.example.xinbookkeeping.ui.company;

import android.content.Intent;
import android.widget.TextView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class CompanyInfoActivity extends BaseActivity {

    private TextView mTvNickname, mTvUid, mTvTelenum, mTvUpwd;
    private CompanySqLiteHelper sqLiteHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_info;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        sqLiteHelper = new CompanySqLiteHelper(this);

        mTvNickname = findViewById(R.id.tv_nickname);
        mTvUid = findViewById(R.id.tv_uid);
        mTvTelenum = findViewById(R.id.tv_telenum);
        mTvUpwd = findViewById(R.id.tv_upwd);

        findViewById(R.id.ll_info).setOnClickListener(v -> startActivity(CompanyEditUnitnameActivity.class));
        findViewById(R.id.ll_telenum).setOnClickListener(v -> startActivity(CompanyEditTelenumActivity.class));
        findViewById(R.id.ll_upwd).setOnClickListener(v -> {
            int id = UserDataManager.getInstance().getId();
            CompanyBean bean = sqLiteHelper.queryById(id);
            Intent intent = new Intent(this, CompanyEditPwdActivity.class);
            intent.putExtra("old", bean.getUpwd());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = UserDataManager.getInstance().getId();
        CompanyBean bean = sqLiteHelper.queryById(id);
        mTvNickname.setText(bean.getUnitname());
        mTvUid.setText(bean.getUid());
        mTvTelenum.setText(bean.getTeleNum());
        mTvUpwd.setText(bean.getUpwd());
    }
}
