package com.example.xinbookkeeping.adapter;

import static com.example.xinbookkeeping.adapter.RecordAdapter.timeToString;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RecordBean;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortViewHolder> {

    private List<RecordBean> mData = new ArrayList<>();

    public void setNewData(List<RecordBean> data) {
        mData.clear();
        if (data.size() > 10) {
            List<RecordBean> copy = data.subList(0, 10);
            mData.addAll(copy);
        } else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sort_item, parent, false);
        return new SortViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {
        RecordBean bean = mData.get(position);
        holder.tv_sort.setText(String.valueOf(position + 1));
        holder.tv_time.setText(timeToString(bean.getTime()));
        holder.tv_remake.setText(bean.getRecordDesc());
        holder.tv_money.setText("¥" + bean.getMoney());
        int color;
        if (position == 0) {
            color = Color.parseColor("#FF9800");
        } else if (position == 1) {
            color = Color.parseColor("#E6E8FA");
        } else if (position == 2) {
            color = Color.parseColor("#B87333");
        } else {
            color = Color.BLACK;
        }

        holder.tv_sort.setTextColor(color);
    }

    public class SortViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sort;
        private TextView tv_remake;
        private TextView tv_time;
        private TextView tv_money;

        public SortViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_remake = itemView.findViewById(R.id.tv_remake);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_money = itemView.findViewById(R.id.tv_money);
        }
    }

}
