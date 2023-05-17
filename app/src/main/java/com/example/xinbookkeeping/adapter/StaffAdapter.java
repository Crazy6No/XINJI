package com.example.xinbookkeeping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RecordBean;
import com.example.xinbookkeeping.bean.StaffBean;

import java.util.ArrayList;
import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private List<StaffBean> mData = new ArrayList<>();

    public void setNewData(List<StaffBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_staff_item, parent, false);
        return new StaffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        StaffBean data = mData.get(position);
        holder.tv_sort.setText(String.valueOf(position + 1));
        holder.tv_nickname.setText(data.getNickname());
        holder.tv_id.setText(data.getId() + "");
        holder.tv_post.setText(data.getPost());
        holder.itemView.setOnClickListener(view -> {
            mListener.onItemCallBack(data);
        });

        holder.tv_edit.setOnClickListener(view -> {
            mListener.onEditCallBack(data);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class StaffViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sort;
        private TextView tv_nickname;
        private TextView tv_id;
        private TextView tv_post;
        private TextView tv_edit;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sort = itemView.findViewById(R.id.tv_sort);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_post = itemView.findViewById(R.id.tv_post);
            tv_edit = itemView.findViewById(R.id.tv_edit);

        }
    }

    private OnClickListener mListener;
    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onItemCallBack(StaffBean data);
        void onEditCallBack(StaffBean data);
    }

}
