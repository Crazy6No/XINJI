package com.example.xinbookkeeping.ui.company;

import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_AGREE;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_DONE;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_ING;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_REFUSE;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.CompanyRequestAdapter;
import com.example.xinbookkeeping.adapter.UserRequestAdapter;
import com.example.xinbookkeeping.bean.RequestBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.RequestSqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.ui.user.SearchCompanyActivity;
import com.example.xinbookkeeping.ui.user.UserRequestCompanyActivity;
import com.example.xinbookkeeping.uibase.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ando.widget.pickerview.builder.TimePickerBuilder;
import ando.widget.pickerview.view.TimePickerView;

public class CompanyRequestActivity extends BaseActivity {

    private RequestSqLiteHelper helper;
    private CompanyRequestAdapter adapter;

    private UserSqLiteHelper userSqLiteHelper;
    private CompanySqLiteHelper companySqLiteHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_request;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        helper = new RequestSqLiteHelper(this);
        userSqLiteHelper = new UserSqLiteHelper(this);
        companySqLiteHelper = new CompanySqLiteHelper(this);
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompanyRequestAdapter();
        adapter.setOnClickListener(new CompanyRequestAdapter.OnClickListener() {
            @Override
            public void onAgreeCallBack(RequestBean data) {
                helper.updateOperate(data.getId() + "", OPERATE_AGREE);
                userSqLiteHelper.updateCompanyId(data.getUserId(), data.getCompanyId());
                companySqLiteHelper.insertStaff(data.getUserId(), data.getCompanyId(), data.getPost(), data.getRequestTime());
                List<RequestBean> requestBeans = helper.queryUserOtherIng(data.getUserId());
                for (int i = 0; i < requestBeans.size(); i++) {
                    RequestBean rb = requestBeans.get(i);
                    helper.updateOperate(rb.getId() + "", OPERATE_DONE);
                }
                searchData();
            }

            @Override
            public void onRefuseCallBack(RequestBean data) {
                helper.updateOperate(data.getId() + "", OPERATE_REFUSE);
                searchData();
            }
        });
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchData();
    }

    private void searchData() {
        String id = UserDataManager.getInstance().getId() + "";
        List<RequestBean> data = helper.queryCompany(id, this);
        adapter.setNewData(data);
    }
}
