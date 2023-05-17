
package com.example.xinbookkeeping.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.common.CommonUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.Map;


/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvDate;
    private final TextView tvMoney;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvDate = findViewById(R.id.tv_date);
        tvMoney = findViewById(R.id.tv_money);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        String date = (String) e.getData();
        tvDate.setText(date);
        tvMoney.setText("¥" + CommonUtils.subZeroAndDot(String.valueOf(e.getY())));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
