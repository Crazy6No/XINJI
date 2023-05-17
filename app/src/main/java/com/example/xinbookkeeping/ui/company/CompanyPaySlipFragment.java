package com.example.xinbookkeeping.ui.company;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.PayAdapter;

import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;

import com.example.xinbookkeeping.db.PaySqLiteHelper;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CompanyPaySlipFragment extends BaseFragment {

    private CompanySqLiteHelper sqLiteHelper;
    private PayAdapter mAdapter;
    private int mYear, mMonth, mDay;

    public static CompanyPaySlipFragment getInstance() {
        return new CompanyPaySlipFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_company_pay_slip;
    }

    private RecordSqLiteHelper mRecordSqLiteHelper;
    private PaySqLiteHelper mPaySqLiteHelper;

    @Override
    protected void init() {
        super.init();
        sqLiteHelper = new CompanySqLiteHelper(getContext());

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        TextView tv_time = root.findViewById(R.id.tv_time);
        String time = mYear + "年" + (mMonth > 9 ? mMonth : ("0" + mMonth)) + "月";
        tv_time.setText(time);

        RecyclerView rv = root.findViewById(R.id.rv);
        mAdapter = new PayAdapter();

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                int pos = data.getIntExtra("pos", -99);
                String money = data.getStringExtra("money");
                mAdapter.getData().get(pos).setMoney(money);
                mAdapter.notifyItemChanged(pos);
            }
        });

        mAdapter.setOnClickListener(new PayAdapter.OnClickListener() {
            @Override
            public void onItemCallBack(int pos, StaffBean data) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("工资编辑")
                        .setMessage(
                                "工号：" + data.getId() +
                                        "\n名称：" + data.getNickname()
                        )
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            Intent intent = new Intent(getContext(), EditPayActivity.class);
                            intent.putExtra("pos", pos);
                            launcher.launch(intent);
                        }).setNegativeButton("取消", (dialogInterface, i) -> {

                        })
                        .create();

                dialog.show();
            }

            @Override
            public void onCheckCallBack() {

            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(mAdapter);

        queryData();

        root.findViewById(R.id.tv_update_list).setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage(
                            "更新员工列表后将重置选择和填写的工资信息"
                    )
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        queryData();
                    }).setNegativeButton("取消", (dialogInterface, i) -> {

                    })
                    .create();

            dialog.show();
        });

        root.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("提示")
                    .setMessage(
                            "取消所有选中的员工"
                    )
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        mAdapter.cancelChecked();
                    }).setNegativeButton("取消", (dialogInterface, i) -> {

                    })
                    .create();

            dialog.show();
        });
        root.findViewById(R.id.tv_send).setOnClickListener(v -> {
            try {
                double total = 0;
                int num = 0;
                List<StaffBean> send = new ArrayList<>();
                for (StaffBean bean : mAdapter.getData()) {
                    if (bean.isChecked()) {
                        num++;
                        if (bean.getMoney() == null || bean.getMoney().equals("")) {
                            throw new Exception("员工" + bean.getNickname() + "尚未设置工资");
                        }
                        BigDecimal bigDecimal1 = new BigDecimal(total);
                        BigDecimal bigDecimal2 = new BigDecimal(bean.getMoney());
                        total = bigDecimal1.add(bigDecimal2).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        send.add(bean);
                    }
                }

                if (num == 0) {
                    throw new Exception("尚没有选中的员工");
                }

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("工资大纲")
                        .setMessage(
                                "发放员工数：" + num + "\n发放总额：" + total
                        )
                        .setPositiveButton("确定", (dialogInterface, i) -> {

                            if (mRecordSqLiteHelper == null) {
                                mRecordSqLiteHelper = new RecordSqLiteHelper(getContext());
                            }

                            if (mPaySqLiteHelper == null) {
                                mPaySqLiteHelper = new PaySqLiteHelper(getContext());
                            }
                            // 开始发放工资
                            for (StaffBean user : send) {
                                mRecordSqLiteHelper.insert(user.getUserId(), "收入", user.getMoney(),
                                        "其他", (time + " 工资"), mYear, mMonth, mDay, new Date().getTime());
                                mPaySqLiteHelper.insert(user.getUserId(), UserDataManager.getInstance().getId() + "",
                                        user.getMoney(), mYear, mMonth, mDay, new Date().getTime());
                            }
                            queryData();
                            toast("发送完毕");
                        }).setNegativeButton("取消", (dialogInterface, i) -> {

                        })
                        .create();

                dialog.show();
            } catch (Exception e) {
                toast(e.getMessage());
            }
        });
    }

    private void queryData() {
        List<StaffBean> data = sqLiteHelper.query(UserDataManager.getInstance().getId() + "", getContext());
        mAdapter.setNewData(data);
    }
}
