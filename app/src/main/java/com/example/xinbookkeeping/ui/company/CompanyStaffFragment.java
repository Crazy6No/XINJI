package com.example.xinbookkeeping.ui.company;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.StaffAdapter;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.common.SPConstant;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.ui.common.SelectRoleActivity;
import com.example.xinbookkeeping.ui.user.UserAddRecordActivity;
import com.example.xinbookkeeping.uibase.BaseFragment;

import java.util.List;

public class CompanyStaffFragment extends BaseFragment {

    private StaffAdapter mAdapter;

    private CompanySqLiteHelper sqLiteHelper;

    public static CompanyStaffFragment getInstance() {
        return new CompanyStaffFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_company_staff;
    }

    @Override
    protected void init() {
        super.init();
        sqLiteHelper = new CompanySqLiteHelper(getContext());
        UserSqLiteHelper userSqLiteHelper = new UserSqLiteHelper(getContext());
        root.findViewById(R.id.btn_add).setOnClickListener(v -> startActivity(CompanyAddStaffActivity.class));

        RecyclerView rv = root.findViewById(R.id.rv);
        mAdapter = new StaffAdapter();
        mAdapter.setOnClickListener(new StaffAdapter.OnClickListener() {
            @Override
            public void onItemCallBack(StaffBean data) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(data.getNickname() + "员工信息")
                        .setMessage(
                                "工号：" + data.getId() +
                                "\n岗位：" + data.getPost() + 
                                "\n电话号码：" + data.getTelenum()
                        )
                        .setPositiveButton("确定", (dialogInterface, i) -> {

                        })
                        .create();

                dialog.show();
            }

            @Override
            public void onEditCallBack(StaffBean data) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("菜单")
                        .setMessage("处理此条内容\n删除后将无法恢复")
                        .setPositiveButton("修改岗位", (dialogInterface, i) -> {
                            Intent intent = new Intent(getActivity(), EditUserPostActivity.class);
                            intent.putExtra("Id", String.valueOf(data.getId()));
                            intent.putExtra("Post", data.getPost());
                            startActivity(intent);
                        })
                        .setNegativeButton("删除", (dialogInterface, i) -> {
                            sqLiteHelper.delete(data.getId());
                            // 清除员工绑定的公司
                            userSqLiteHelper.updateCompanyId(data.getUserId(), "");
                            queryData();
                        })
                        .setNeutralButton("取消", (dialogInterface, i) -> {

                        }).create();
                dialog.show();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        queryData();
    }

    private void queryData() {
        List<StaffBean> data = sqLiteHelper.query(UserDataManager.getInstance().getId() + "" , getContext());
        mAdapter.setNewData(data);
    }
}
