package com.example.xinbookkeeping.adapter;

import static com.example.xinbookkeeping.adapter.RecordAdapter.timeToString;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_AGREE;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_ING;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_REFUSE;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.bean.RequestBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.SortViewHolder> {

    private List<RequestBean> mData = new ArrayList<>();

    public void setNewData(List<RequestBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_user_item, parent, false);
        return new SortViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {
        RequestBean bean = mData.get(position);
        holder.tv_sort.setText(String.valueOf(position + 1));
        holder.tv_time.setText(timeToString(bean.getTime()));
        holder.tv_name.setText(bean.getName());
        holder.tv_other.setText("岗位：" + bean.getPost() + " 入职时间：" + timeToMMdd(bean.getRequestTime()));

        String operate;
        switch (bean.getOperate()) {
            case OPERATE_ING:
                operate = "申请中";
                break;

            case OPERATE_AGREE:
                operate = "同意";
                break;

            case OPERATE_REFUSE:
                operate = "拒绝";
                break;

            default:
                operate = "已处理";
                break;
        }

        holder.tv_operate.setText(operate);
    }

    public class SortViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sort;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_operate;
        private TextView tv_other;

        public SortViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_operate = itemView.findViewById(R.id.tv_operate);
            tv_other = itemView.findViewById(R.id.tv_other);
        }
    }

    private String timeToMMdd(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }

}
