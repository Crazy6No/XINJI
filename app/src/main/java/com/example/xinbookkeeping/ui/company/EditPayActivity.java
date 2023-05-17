package com.example.xinbookkeeping.ui.company;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.uibase.BaseActivity;

/**
 *
 * 编辑员工工资
 * */
public class EditPayActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_pay;
    }

    @Override
    public void init() {
        super.init();
        setBackToolBar();
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", -99);
        if (pos == -99) {
            toast("员工信息错误");
            finish();
            return;
        }

        EditText et_money = findViewById(R.id.et);
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

        findViewById(R.id.tv_confirm).setOnClickListener(view -> {
            String money = et_money.getText().toString();
            if (TextUtils.isEmpty(money)) {
                toast("请输入工资金额");
                return;
            }
            intent.putExtra("money", money);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
