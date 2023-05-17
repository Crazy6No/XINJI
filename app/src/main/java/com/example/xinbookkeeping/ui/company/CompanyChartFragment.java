package com.example.xinbookkeeping.ui.company;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.adapter.PaySortAdapter;
import com.example.xinbookkeeping.adapter.SortAdapter;
import com.example.xinbookkeeping.adapter.StateAdapter;
import com.example.xinbookkeeping.bean.ChartStateBean;
import com.example.xinbookkeeping.bean.PayBean;
import com.example.xinbookkeeping.bean.PaySortBean;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.bean.StaffBean;
import com.example.xinbookkeeping.common.CommonUtils;
import com.example.xinbookkeeping.common.UserDataManager;
import com.example.xinbookkeeping.db.CompanySqLiteHelper;
import com.example.xinbookkeeping.db.PaySqLiteHelper;
import com.example.xinbookkeeping.db.RecordSqLiteHelper;
import com.example.xinbookkeeping.db.UserSqLiteHelper;
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

public class CompanyChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    private int mYear, mMonth;
    private TextView mTvDate;
    private PaySqLiteHelper helper;

    private LineChart mChart;
    private CompanySqLiteHelper companySqLiteHelper;
    private PaySortAdapter sortAdapter;

    private CompanyChartFragment() {

    }

    public static CompanyChartFragment getInstance() {
        CompanyChartFragment fragment = new CompanyChartFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_company_chart;
    }

    @Override
    protected void init() {
        super.init();
        helper = new PaySqLiteHelper(getContext());
        companySqLiteHelper = new CompanySqLiteHelper(getContext());

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        Calendar startDate = Calendar.getInstance();
        startDate.set(mYear, 0, 1);
        TimePickerView timePicker =
                new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        mMonth = c.get(Calendar.MONTH) + 1;
                        setDateText();
                        doMonthData();
                    }
                })
                        .setType(new boolean[]{false, true, false, false, false, false})// 默认全部显示
                        .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                        .setCancelColor(Color.BLACK)//取消按钮文字颜色
                        .setDate(calendar)
                        .setRangDate(startDate, calendar)
                        .build();

        mTvDate = root.findViewById(R.id.tv_date);
        mTvDate.setOnClickListener(v -> timePicker.show(v));

        RecyclerView rv = root.findViewById(R.id.rv_sort);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        sortAdapter = new PaySortAdapter();
        rv.setAdapter(sortAdapter);

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

    }

    /**
     * 设置表格数据
     */
    private void setChartData(List<Float> yearData) {

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < yearData.size(); i++) {
            float val = yearData.get(i);
            String date = mYear + "年" + (i + 1) + "月";
            values.add(new Entry(i, val, date));
        }

        LineDataSet set1;

        set1 = new LineDataSet(values, "月总额");
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

    /**
     * 搜索数据
     */
    private void searchData() {
        // 年数据
        doYearData();
        // 月数据
        doMonthData();
    }

    /**
     * 设置年数据
     */
    private void doYearData() {
        String id = String.valueOf(UserDataManager.getInstance().getId());
        List<PayBean> data = helper.queryByCompanyYear(id, mYear);
        List<Float> yearData = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            yearData.add( 0F);
        }

        if (data != null && data.size() > 0) {
            for (PayBean bean : data) {
                int pos = bean.getMonth() - 1;
                Float v1 = yearData.get(pos);
                Float v = new BigDecimal(v1).add(new BigDecimal(bean.getMoney())).setScale(2, RoundingMode.HALF_UP).floatValue();
                yearData.set(pos, v);
            }
        }
        setChartData(yearData);
    }

    /**
     *
     * 设置月数据
     */
    private void doMonthData() {
        String id = String.valueOf(UserDataManager.getInstance().getId());
        List<PayBean> monthData = helper.queryByCompanyMonth(id, mYear, mMonth);
        monthData.sort(Comparator.comparing(t -> new BigDecimal(t.getMoney())));
        Collections.reverse(monthData);

        List<PaySortBean> list = new ArrayList<>();

        for (int i = 0; i < monthData.size(); i++) {
            PayBean bean = monthData.get(i);
            StaffBean staffBean = companySqLiteHelper.queryOne(bean.getUserId(), getContext());
            if (staffBean != null) {
                list.add(new PaySortBean(bean, staffBean.getNickname(), staffBean.getPost()));
            }
        }

        sortAdapter.setNewData(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchData();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
