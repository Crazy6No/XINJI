package com.example.xinbookkeeping.ui.user;

import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_ING;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RequestBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.RequestSqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ando.widget.pickerview.builder.TimePickerBuilder;
import ando.widget.pickerview.view.TimePickerView;

public class UserRequestCompanyActivity extends BaseActivity {

    private String mCompanyId;
    private long mTime = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_request_company;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        RequestSqLiteHelper helper = new RequestSqLiteHelper(this);

        TextView tvCompany = findViewById(R.id.tv_company);
        TextView tvTelenum = findViewById(R.id.tv_telenum);
        EditText etPost = findViewById(R.id.et_post);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                tvCompany.setText(data.getStringExtra("unitname"));
                tvTelenum.setText(data.getStringExtra("telenum"));
                mCompanyId = data.getStringExtra("companyId");
            }
        });

        findViewById(R.id.tv_company).setOnClickListener(v -> {
            Intent intent = new Intent(this, SearchCompanyActivity.class);
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

            if (mCompanyId == null || TextUtils.isEmpty(mCompanyId)) {
                toast("请选择公司");
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
            String userId = UserDataManager.getInstance().getId() + "";
            RequestBean bean = helper.query(userId, mCompanyId);
            if (bean != null && bean.getOperate().equals(OPERATE_ING)) {
                toast("已向该公司发起过申请 请勿重新申请");
                return;
            }

            if (helper.insert(userId, mCompanyId, post, mTime, new Date().getTime()) != -1) {
                toast("申请成功");
                finish();
            } else {
                toast("申请失败");
            }
        });
    }

    public static String timeToDateString(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }
}
