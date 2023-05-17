package com.example.xinbookkeeping.ui.user;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.CompanyAdapter;
import com.example.xinbookkeeping.adapter.UserAdapter;
import com.example.xinbookkeeping.bean.CompanyBean;
import com.example.xinbookkeeping.bean.UserBean;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

import java.util.List;

public class SearchCompanyActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_company;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);

        RecyclerView rv = findViewById(R.id.rv);
        CompanyAdapter adapter = new CompanyAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        EditText et = findViewById(R.id.et);
        findViewById(R.id.btn).setOnClickListener(v -> {
            String key = et.getText().toString();
            if (TextUtils.isEmpty(key)) {
                toast("关键词不能为空");
                return;
            }

            List<CompanyBean> data = helper.queryList(key);
            if (data.isEmpty()) {
                toast("未搜到相关公司");
            }
            adapter.setNewData(data);

        });
    }
}
