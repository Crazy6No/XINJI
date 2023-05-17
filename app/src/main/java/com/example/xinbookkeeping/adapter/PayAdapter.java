package com.example.xinbookkeeping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.StaffBean;

import java.util.ArrayList;
import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.StaffViewHolder> {

    private List<StaffBean> mData = new ArrayList<>();

    public void setNewData(List<StaffBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public List<StaffBean> getData() {
        return mData;
    }

    public void cancelChecked() {
        if (mData.size() > 0) {
            for (StaffBean bean :mData) {
                bean.setChecked(false);
            }

            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pay_slip_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        StaffBean data = mData.get(position);
        holder.tv_nickname.setText(data.getNickname());
        holder.tv_pay.setText(data.getMoney());
        holder.tv_id.setText(data.getId() + "");
        holder.itemView.setOnClickListener(view -> {
            mListener.onItemCallBack(position, data);
        });

        holder.iv_check.setOnClickListener(v -> {
            mListener.onCheckCallBack();
            boolean c = data.isChecked();
            data.setChecked(!c);
            notifyItemChanged(position);
        });

        holder.iv_check.setBackgroundResource(data.isChecked() ? R.drawable.ic_checked : R.drawable.ic_check);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nickname;
        private TextView tv_id;
        private TextView tv_pay;
        private ImageView iv_check;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_pay = itemView.findViewById(R.id.tv_pay);
            iv_check = itemView.findViewById(R.id.iv_check);
        }
    }

    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onItemCallBack(int pos, StaffBean data);

        void onCheckCallBack();
    }

}
