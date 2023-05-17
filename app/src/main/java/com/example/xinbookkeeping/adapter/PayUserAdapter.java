package com.example.xinbookkeeping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.PayBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PayUserAdapter extends RecyclerView.Adapter<PayUserAdapter.RecordViewHolder> {

    private List<PayBean> mData = new ArrayList<>();

    public void setNewData(List<PayBean> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pay_user_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        PayBean bean = mData.get(position);
        holder.tv_money.setText("+" + bean.getMoney());
        holder.tv_time.setText(timeToString(bean.getTime()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_money;
        private TextView tv_time;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    public static String timeToString(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }
}
