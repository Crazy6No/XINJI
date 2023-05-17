package com.example.xinbookkeeping.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.RecordAdapter;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseFragment;

import java.util.List;

/**
 *
 * 账簿子页面
 * */
public class UserRecordFragment extends BaseFragment {

    private RecordAdapter userAdapter;
    private RecordSqLiteHelper userHelper;

    public static UserRecordFragment getInstance() {
        return new UserRecordFragment();
    }

    private UserRecordFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_record;
    }

    private int mType;

    @Override
    protected void init() {
        super.init();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt("type");
        }

        root.findViewById(R.id.btn_add).setOnClickListener(v -> {
            startActivity(UserAddRecordActivity.class);
        });

        RecyclerView rv = root.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        userHelper = new RecordSqLiteHelper(getContext());
        userAdapter = new RecordAdapter(getContext());
        userAdapter.setOnLongClickListener(data -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("菜单")
                    .setMessage("处理此条内容\n删除后将无法恢复")
                    .setPositiveButton("编辑", (dialogInterface, i) -> {
                        Intent intent = new Intent(getActivity(), UserAddRecordActivity.class);
                        intent.putExtra("Id", String.valueOf(data.getId()));
                        startActivity(intent);
                    })
                    .setNegativeButton("删除", (dialogInterface, i) -> {
                        userHelper.delete(data.getId());
                        searchData();
                    })
                    .setNeutralButton("取消", (dialogInterface, i) -> {

                    }).create();
            dialog.show();
        });
        rv.setAdapter(userAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchData();
    }

    private void searchData() {
        List<RecordBean> data = userHelper.queryByUserId(String.valueOf(UserDataManager.getInstance().getId()));
        userAdapter.setNewData(data);
    }
}
