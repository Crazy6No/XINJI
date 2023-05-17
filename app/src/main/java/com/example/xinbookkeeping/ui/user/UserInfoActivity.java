package com.example.xinbookkeeping.ui.user;

import android.content.Intent;
import android.widget.TextView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.UserBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

public class UserInfoActivity extends BaseActivity {

    private TextView mTvNickname, mTvUid, mTvTelenum, mTvUpwd;
    private UserSqLiteHelper mUserHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        mUserHelper = new UserSqLiteHelper(this);

        mTvNickname = findViewById(R.id.tv_nickname);
        mTvUid = findViewById(R.id.tv_uid);
        mTvTelenum = findViewById(R.id.tv_telenum);
        mTvUpwd = findViewById(R.id.tv_upwd);

        findViewById(R.id.ll_info).setOnClickListener(v -> startActivity(UserEditNicknameActivity.class));
        findViewById(R.id.ll_telenum).setOnClickListener(v -> startActivity(UserEditTelenumActivity.class));
        findViewById(R.id.ll_upwd).setOnClickListener(v -> {
            int id = UserDataManager.getInstance().getId();
            UserBean userBean = mUserHelper.queryById(this, id);
            Intent intent = new Intent(this, UserEditPwdActivity.class);
            intent.putExtra("old", userBean.getUpwd());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = UserDataManager.getInstance().getId();
        UserBean userBean = mUserHelper.queryById(this, id);
        mTvNickname.setText(userBean.getNickname());
        mTvUid.setText(userBean.getUid());
        mTvTelenum.setText(userBean.getTeleNum());
        mTvUpwd.setText(userBean.getUpwd());
    }
}
