package com.example.xinbookkeeping.ui.company;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ando.widget.pickerview.builder.TimePickerBuilder;
import ando.widget.pickerview.listener.OnTimeSelectListener;
import ando.widget.pickerview.view.TimePickerView;

public class CompanyAddStaffActivity extends BaseActivity {

    private String mUserId;
    private long mTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_add_staff;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        TextView tvUser = findViewById(R.id.tv_user);
        TextView tvTelenum = findViewById(R.id.tv_telenum);
        EditText etPost = findViewById(R.id.et_post);
        CompanySqLiteHelper helper = new CompanySqLiteHelper(this);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                tvUser.setText(data.getStringExtra("nickname"));
                tvTelenum.setText(data.getStringExtra("telenum"));
                mUserId = data.getStringExtra("userId");
            }
        });

        findViewById(R.id.tv_user).setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchUserActivity.class);
            launcher.launch(intent);
        });
        TextView tvTime = findViewById(R.id.tv_time);

        Calendar calendar = Calendar.getInstance();
        TimePickerView timePicker =
                new TimePickerBuilder(this, (date, v) -> {
                    mTime = date.getTime();
                    tvTime.setText(timeToDateString(mTime));
                })
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setDate(calendar)
                        .build();


        tvTime.setOnClickListener(v -> {
            timePicker.show();
        });

        // 新增
        findViewById(R.id.tv_add).setOnClickListener(v -> {

            if (mUserId == null || TextUtils.isEmpty(mUserId)) {
                toast("请选择用户");
                return;
            }

            String post = etPost.getText().toString();

            if (TextUtils.isEmpty(post)) {
                toast("请填写岗位");
                return;
            }

            if (mTime == 0) {
                toast("请选择入职时间");
                return;
            }
            String companyId = UserDataManager.getInstance().getId() + "";
            if (helper.insertStaff(mUserId,  companyId, post, mTime) != -1) {
                UserSqLiteHelper userSqLiteHelper = new UserSqLiteHelper(this);
                userSqLiteHelper.updateCompanyId(mUserId, companyId);
                toast("新增成功");
                finish();
            } else {
                toast("新增失败");
            }
        });
    }

    public static String timeToDateString(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }
}
