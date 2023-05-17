package com.example.xinbookkeeping.ui.user;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.UserRequestAdapter;
import com.example.xinbookkeeping.bean.RequestBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.db.RequestSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

import java.util.List;

public class UserRequestActivity extends BaseActivity {

    private RequestSqLiteHelper helper;
    private UserRequestAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_request;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        helper = new RequestSqLiteHelper(this);
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserRequestAdapter();
        rv.setAdapter(adapter);

        findViewById(R.id.tv_request).setOnClickListener(v -> {
            startActivity(UserRequestCompanyActivity.class);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String id = UserDataManager.getInstance().getId() + "";
        List<RequestBean> data = helper.queryUser(id, this);
        adapter.setNewData(data);
    }
}
