package com.example.xinbookkeeping.adapter;

import static com.example.xinbookkeeping.adapter.RecordAdapter.timeToString;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_AGREE;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_ING;
import static com.example.xinbookkeeping.bean.RequestBean.OPERATE_REFUSE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RequestBean;
import com.example.xinbookkeeping.bean.StaffBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyRequestAdapter extends RecyclerView.Adapter<CompanyRequestAdapter.SortViewHolder> {

    private List<RequestBean> mData = new ArrayList<>();

    public void setNewData(List<RequestBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_company_item, parent, false);
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
        holder.tv_agree.setOnClickListener(v -> mListener.onAgreeCallBack(bean));
        holder.tv_refuse.setOnClickListener(v -> mListener.onRefuseCallBack(bean));
    }

    public class SortViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sort;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_other;
        private TextView tv_refuse;
        private TextView tv_agree;

        public SortViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_other = itemView.findViewById(R.id.tv_other);
            tv_refuse = itemView.findViewById(R.id.tv_refuse);
            tv_agree = itemView.findViewById(R.id.tv_agree);
        }
    }

    private String timeToMMdd(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }

    private OnClickListener mListener;

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onAgreeCallBack(RequestBean data);

        void onRefuseCallBack(RequestBean data);
    }
}
