package com.example.xinbookkeeping.ui.user;

import android.text.Editable;
import android.text.TextUtils;

import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.uibase.BaseActivity;


import java.util.Calendar;
import java.util.Date;

/**
 * 新增账单
 */
public class UserAddRecordActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_user_add_record;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();

        RecordSqLiteHelper helper = new RecordSqLiteHelper(this);

        String id = getIntent().getStringExtra("Id");


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 金额
        EditText et_money = findViewById(R.id.et_money);
        // 设置只能输入两位小数
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除.后面超过两位的数字
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_money.setText(s);
                        et_money.setSelection(s.length());
                    }
                }

                //如果.在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_money.setText(s);
                    et_money.setSelection(2);
                }

                //如果起始位置为0并且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_money.setText(s.subSequence(0, 1));
                        et_money.setSelection(1);
                        return;
                    }
                }

                //不允许输入两个"."
                if (s.toString().trim().length() > 2) {
                    int firstIndex = s.toString().trim().indexOf(".");
                    int lastIndex = s.toString().trim().lastIndexOf(".");
                    if (lastIndex - firstIndex == 1) {
                        et_money.setText(s.subSequence(0, lastIndex));
                        et_money.setSelection(lastIndex);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 备注
        EditText et_desc = findViewById(R.id.et_desc);

        RadioGroup rg_type = findViewById(R.id.rg_type);
        RadioGroup rg_state = findViewById(R.id.rg_state);
        TextView tv_add = findViewById(R.id.tv_add);
        boolean isEdit = false;
        if (id != null && !id.equals("")) {
            isEdit = true;
            tv_add.setText("修改");
            RecordBean old = helper.queryById(id);
            if (old == null) {
                toast("数据错误");
                finish();
            } else {
                et_desc.setText(old.getRecordDesc());
                et_money.setText(old.getMoney());
                int stateId;
                switch (old.getState()) {
                    case "购物":
                        stateId = R.id.rb_state_shopping;
                        break;
                    case "旅游":
                        stateId = R.id.rb_state_travel;
                        break;
                    case "餐饮":
                        stateId = R.id.rb_state_food;
                        break;
                    case "交通":
                        stateId = R.id.rb_state_traffic;
                        break;
                    case "住宿":
                        stateId = R.id.rb_state_accommodation;
                        break;
                    case "烟酒":
                        stateId = R.id.rb_state_taw;
                        break;
                    default:
                        stateId = R.id.rb_state_other;
                        break;
                }

                RadioButton rbState = findViewById(stateId);
                rbState.setChecked(true);

                int typeId = old.getType().equals("支出") ? R.id.rb_type_expend : R.id.rb_type_income;
                RadioButton rbType = findViewById(typeId);
                rbType.setChecked(true);
            }

        }

        // 新增
        boolean finalIsEdit = isEdit;
        tv_add.setOnClickListener(view -> {
            // 类型
            int typeId = rg_type.getCheckedRadioButtonId();
            String type = typeId == R.id.rb_type_expend ? "支出" : "收入";
            // 金额
            String money = et_money.getText().toString();
            if (TextUtils.isEmpty(money)) {
                toast("请输入金额");
                return;
            }
            // 分类
            int stateId = rg_state.getCheckedRadioButtonId();
            String state;
            switch (stateId) {
                case R.id.rb_state_shopping:
                    state = "购物";
                    break;
                case R.id.rb_state_travel:
                    state = "旅游";
                    break;
                case R.id.rb_state_food:
                    state = "餐饮";
                    break;
                case R.id.rb_state_traffic:
                    state = "交通";
                    break;

                case R.id.rb_state_accommodation:
                    state = "住宿";
                    break;

                case R.id.rb_state_taw:
                    state = "烟酒";
                    break;

                default:
                    state = "其他";
                    break;
            }

            // 备注可为空
            String desc = et_desc.getText().toString();
            // 入表的时间戳
            long time = new Date().getTime();
            String userId = String.valueOf(UserDataManager.getInstance().getId());
            long result = finalIsEdit ? helper.update(id, type, money, state, desc) : helper.insert(userId,
                    type, money, state, desc, year, month, day, time);
            if (result == -1) {
                toast(finalIsEdit ? "修改失败" : "新增失败");
            } else {
                toast(finalIsEdit ? "修改成功" : "新增成功");
                finish();
            }
        });
    }
}
