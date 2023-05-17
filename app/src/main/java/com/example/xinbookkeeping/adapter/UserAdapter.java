package com.example.xinbookkeeping.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private AppCompatActivity appCompatActivity;

    public UserAdapter(AppCompatActivity activity) {
        this.appCompatActivity = activity;
    }

    private List<UserBean> mData = new ArrayList<>();

    public void setNewData(List<UserBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserBean data = mData.get(position);
        holder.tv_nickname.setText(data.getNickname());
        holder.tv_uid.setText(data.getUid() + "  ID:" + data.getId() + "  手机号：" + data.getTeleNum());
        if (TextUtils.isEmpty(data.getUnitName())) {
            holder.tv_company.setText("暂无绑定公司");
            holder.tv_chose.setVisibility(View.VISIBLE);
        } else {
            holder.tv_company.setText(data.getUnitName());
            holder.tv_chose.setVisibility(View.GONE);
        }

        holder.tv_chose.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("nickname", data.getNickname());
            intent.putExtra("telenum", data.getTeleNum());
            intent.putExtra("userId", data.getId() + "");
            appCompatActivity.setResult(Activity.RESULT_OK, intent);
            appCompatActivity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nickname;
        private TextView tv_uid;
        private TextView tv_company;
        private TextView tv_chose;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_uid = itemView.findViewById(R.id.tv_uid);
            tv_company = itemView.findViewById(R.id.tv_company);
            tv_chose = itemView.findViewById(R.id.tv_chose);
        }
    }

}
