package com.example.xinbookkeeping.ui.user;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.view.View;

import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.SortAdapter;
import com.example.xinbookkeeping.adapter.StateAdapter;
import com.example.xinbookkeeping.bean.ChartStateBean;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.common.CommonUtils;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.ui.common.MyMarkerView;
import com.example.xinbookkeeping.uibase.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ando.widget.pickerview.builder.TimePickerBuilder;
import ando.widget.pickerview.listener.OnTimeSelectListener;
import ando.widget.pickerview.view.TimePickerView;

public class UserChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    private int mYear, mMonth;
    private TextView mTvDate, mTvMoneyExpend, mTvNumExpend, mTvMoneyIncome, mTvNumIncome;
    private TextView mTvExpendBtn, mTvIncomeBtn;
    private TextView mTvStateTitle, mTvDayTitle, mTvSortTitle;
    private RecordSqLiteHelper helper;

    private StateAdapter mStateAdapter;

    private SortAdapter mSortAdapter;

    private LineChart mChart;

    private UserChartFragment() {

    }

    public static UserChartFragment getInstance() {
        UserChartFragment fragment = new UserChartFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_chart;
    }

    @Override
    protected void init() {
        super.init();
        helper = new RecordSqLiteHelper(getContext());

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        TimePickerView timePicker =
                new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH) + 1;
                        setDateText();
                        queryData();
                    }
                })
                        .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setDate(calendar)
                        .setRangDate(startDate, calendar)
                        .build();

        mTvDate = root.findViewById(R.id.tv_date);
        mTvDate.setOnClickListener(v -> timePicker.show(v));

        mTvMoneyExpend = root.findViewById(R.id.tv_money_expend);
        mTvNumExpend = root.findViewById(R.id.tv_num_expend);
        mTvMoneyIncome = root.findViewById(R.id.tv_money_income);
        mTvNumIncome = root.findViewById(R.id.tv_num_income);
        mTvExpendBtn = root.findViewById(R.id.tv_expend_btn);
        mTvIncomeBtn = root.findViewById(R.id.tv_income_btn);
        mTvExpendBtn.setTag(true);
        mTvIncomeBtn.setTag(false);

        Drawable btnO = ContextCompat.getDrawable(getContext(), R.drawable.bg_pl);
        Drawable btnW = ContextCompat.getDrawable(getContext(), R.drawable.bg_white);
        int colorO = ContextCompat.getColor(getContext(), R.color.white);
        int colorW = ContextCompat.getColor(getContext(), R.color.black);

        mTvStateTitle = root.findViewById(R.id.tv_state_title);
        mTvSortTitle = root.findViewById(R.id.tv_sort_title);
        mTvDayTitle = root.findViewById(R.id.tv_day_title);

        mTvExpendBtn.setOnClickListener(view -> {
            mTvExpendBtn.setTag(true);
            mTvIncomeBtn.setTag(false);
            mTvExpendBtn.setBackground(btnO);
            mTvExpendBtn.setTextColor(colorO);
            mTvIncomeBtn.setBackground(btnW);
            mTvIncomeBtn.setTextColor(colorW);
            setOtherData();
        });

        mTvIncomeBtn.setOnClickListener(view -> {
            mTvIncomeBtn.setTag(true);
            mTvExpendBtn.setTag(false);
            mTvExpendBtn.setBackground(btnW);
            mTvExpendBtn.setTextColor(colorW);
            mTvIncomeBtn.setBackground(btnO);
            mTvIncomeBtn.setTextColor(colorO);
            setOtherData();
        });

        // 分类
        RecyclerView rvState = root.findViewById(R.id.rv_state);
        rvState.setLayoutManager(new LinearLayoutManager(getContext()));
        mStateAdapter = new StateAdapter();
        rvState.setAdapter(mStateAdapter);

        setDateText();

        // 设置表格基本属性
        {
            mChart = root.findViewById(R.id.chart);
            mChart.setBackgroundColor(Color.WHITE);
            mChart.getDescription().setEnabled(false);
            mChart.setTouchEnabled(true);
            mChart.setOnChartValueSelectedListener(this);
            mChart.setDrawGridBackground(false);

            // 设置浮窗
            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
            mv.setChartView(mChart);
            mChart.setMarker(mv);
            // 移动拖动
            mChart.setDragEnabled(false);
            mChart.setScaleEnabled(false);
            // 缩放
            mChart.setPinchZoom(false);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    float realValue = value + 1;
                    return CommonUtils.subZeroAndDot(String.valueOf(realValue));
                }
            });
            // 获取左侧y轴
            YAxis axisRight = mChart.getAxisRight();
            YAxis axisLeft = mChart.getAxisLeft();
            // 禁止绘制y轴标签
            axisRight.setDrawGridLines(false);
            axisLeft.setDrawGridLines(false);
            axisRight.setDrawLabels(false);
            axisLeft.setDrawLabels(false);
            // 禁止绘制y轴
            axisRight.setDrawAxisLine(false);
            axisLeft.setDrawAxisLine(false);

            Legend l = mChart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            mChart.animateX(1500);
        }

        // 支出排行
        RecyclerView rvSort = root.findViewById(R.id.rv_sort);
        rvSort.setLayoutManager(new LinearLayoutManager(getContext()));
        mSortAdapter = new SortAdapter();
        rvSort.setAdapter(mSortAdapter);
    }

    /**
     * 设置表格数据
     */
    private void setChartData(boolean isInCome) {
        List<Float> month = isInCome ? mIncomeMonth : mExpendMonth;

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < month.size(); i++) {
            float val = month.get(i);
            String date = mMonth + "月" + (i + 1) + "日";
            values.add(new Entry(i, val, date));
        }

        LineDataSet set1;

        set1 = new LineDataSet(values, "日消费");
        set1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return CommonUtils.subZeroAndDot(String.valueOf(value));
            }
        });
        set1.setDrawIcons(false);

        set1.enableDashedLine(10f, 5f, 0f);
        int color = ContextCompat.getColor(getContext(), R.color.purple_700);
        set1.setColor(color);
        set1.setCircleColor(color);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(true);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        set1.setValueTextSize(9f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setDrawFilled(true);
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mChart.getAxisLeft().getAxisMinimum();
            }
        });

        if (Utils.getSDKInt() >= 18) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_orange);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    private void setDateText() {
        mTvDate.setText(mYear + "年" + (mMonth > 9 ? mMonth : ("0" + mMonth)) + "月");
    }

    // 支出 收入 总数据
    private List<RecordBean> mExpendBeans = new ArrayList<>();
    private List<RecordBean> mIncomeBeans = new ArrayList<>();
    // 分类数据缓存
    private List<ChartStateBean> mExpendChartStateBeans = new ArrayList<>();
    private List<ChartStateBean> mIncomeChartStateBeans = new ArrayList<>();
    // 每日支出 收入 数据
    private List<Float> mExpendMonth = new ArrayList<>();
    private List<Float> mIncomeMonth = new ArrayList<>();

    /**
     * 搜索数据
     */
    private void queryData() {
        mExpendBeans.clear();
        mIncomeBeans.clear();
        mExpendChartStateBeans.clear();
        mIncomeChartStateBeans.clear();
        mExpendMonth.clear();
        mIncomeMonth.clear();
        String id = String.valueOf(UserDataManager.getInstance().getId());
        List<RecordBean> data = helper.query(id, String.valueOf(mYear), String.valueOf(mMonth));
        setTotalData(data);
        setOtherData();
    }

    /**
     * 设置总支出 收入数据
     */
    private void setTotalData(List<RecordBean> data) {
        double expend = 0;
        double income = 0;
        int expendNum = 0;
        int incomeNum = 0;

        // 月天数
        int days = CommonUtils.getMonthLastDay(mYear, mMonth);
        for (int i = 0; i < days; i++) {
            mExpendMonth.add(0F);
            mIncomeMonth.add(0F);
        }

        if (data != null && data.size() > 0) {
            for (RecordBean bean : data) {
                if (bean.getType().equals("支出")) {
                    mExpendBeans.add(bean);
                    BigDecimal bigDecimal1 = new BigDecimal(expend);
                    BigDecimal bigDecimal2 = new BigDecimal(bean.getMoney());
                    expend = bigDecimal1.add(bigDecimal2).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    expendNum++;

                    // 设置每日数据
                    BigDecimal bigDecimal3 = new BigDecimal(mExpendMonth.get(bean.getDay() - 1));
                    BigDecimal bigDecimal4 = new BigDecimal(bean.getMoney());
                    Float day = bigDecimal3.add(bigDecimal4).setScale(2, RoundingMode.HALF_UP).floatValue();
                    mExpendMonth.set(bean.getDay() - 1, day);
                } else {
                    mIncomeBeans.add(bean);
                    BigDecimal bigDecimal1 = new BigDecimal(income);
                    BigDecimal bigDecimal2 = new BigDecimal(bean.getMoney());
                    income = bigDecimal1.add(bigDecimal2).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    incomeNum++;

                    BigDecimal bigDecimal3 = new BigDecimal(mIncomeMonth.get(bean.getDay() - 1));
                    BigDecimal bigDecimal4 = new BigDecimal(bean.getMoney());
                    Float day = bigDecimal3.add(bigDecimal4).setScale(2, RoundingMode.HALF_UP).floatValue();
                    mIncomeMonth.set(bean.getDay() - 1, day);
                }
            }
        }

        mIncomeBeans.sort(Comparator.comparing(t -> new BigDecimal(t.getMoney())));
        mExpendBeans.sort(Comparator.comparing(t -> new BigDecimal(t.getMoney())));
        Collections.reverse(mIncomeBeans);
        Collections.reverse(mExpendBeans);

        // 设置数据
        mTvMoneyExpend.setText(String.valueOf(expend));
        mTvNumExpend.setText("共" + expendNum + "笔");
        mTvMoneyIncome.setText(String.valueOf(income));
        mTvNumIncome.setText("共" + incomeNum + "笔");
    }

    /**
     * 处理 收入 或者 支出数据
     */
    private void setOtherData() throws NullPointerException {
        List<RecordBean> recordBeans;
        //  isInCome  true 收入 false 支出
        boolean isInCome = (boolean) mTvIncomeBtn.getTag();
        String title = isInCome ? "收入" : "支出";
        mTvDayTitle.setText("每日" + title);
        mTvSortTitle.setText(title + "排行");
        mTvStateTitle.setText(title + "分类");
        // 设置每日数据
        setChartData(isInCome);

        if (isInCome) {
            recordBeans = mIncomeBeans;
        } else {
            recordBeans = mExpendBeans;
        }

        // 设置排行数据
        mSortAdapter.setNewData(recordBeans);

        if ((isInCome && mIncomeChartStateBeans.size() > 0) || (!isInCome && mExpendChartStateBeans.size() > 0)) {
            //相关数据已经执行过 不在执行
            System.out.println();
        } else {
            Map<String, Double> money = new HashMap<>();
            Map<String, Integer> num = new HashMap<>();
            // 分类占比数据
            for (int i = 0; i < recordBeans.size(); i++) {
                RecordBean bean = recordBeans.get(i);
                boolean nB = num.get(bean.getState()) != null;
                boolean mB = money.get(bean.getState()) != null;
                if (nB && mB) {
                    Double m = money.get(bean.getState());
                    Integer n = num.get(bean.getState());
                    BigDecimal bigDecimal1 = new BigDecimal(m);
                    BigDecimal bigDecimal2 = new BigDecimal(bean.getMoney());
                    double realM = bigDecimal1.add(bigDecimal2).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    n++;
                    money.put(bean.getState(), realM);
                    num.put(bean.getState(), n);
                } else {
                    money.put(bean.getState(), Double.parseDouble(bean.getMoney()));
                    num.put(bean.getState(), 1);
                }
            }
            // 确保数据正确
            if (!money.isEmpty() && (money.size() == num.size())) {
                List<ChartStateBean> data = new ArrayList<>();
                for (Map.Entry<String, Double> entry : money.entrySet()) {
                    String key = entry.getKey();
                    Double m = entry.getValue();
                    int n = num.get(key);
                    BigDecimal bigDecimal1 = new BigDecimal(n);
                    BigDecimal bigDecimal2 = new BigDecimal(recordBeans.size());
                    Double ratio = bigDecimal1.divide(bigDecimal2, 4, RoundingMode.HALF_UP).doubleValue();
                    data.add(new ChartStateBean(key, n, m, ratio));
                }

                data.sort(Comparator.comparing(t -> new BigDecimal(t.getNum())));
                Collections.reverse(data);
                if (isInCome) {
                    mIncomeChartStateBeans.addAll(data);
                } else {
                    mExpendChartStateBeans.addAll(data);
                }
            }
        }
        setStateData();
    }

    /**
     * 设置分类数据
     */
    private void setStateData() {
        boolean isInCome = (boolean) mTvIncomeBtn.getTag();
        // 收入
        if (isInCome) {
            mStateAdapter.setNewData(mIncomeChartStateBeans);
        } else {
            mStateAdapter.setNewData(mExpendChartStateBeans);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        queryData();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
