package com.example.xinbookkeeping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.ChartStateBean;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {

    private List<ChartStateBean> mData = new ArrayList<>();

    public void setNewData(List<ChartStateBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_state_item, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
        ChartStateBean data = mData.get(position);
        holder.tv_state.setText(data.getState());
        holder.tv_ratio.setText((data.getRatio() * 100) + "%(" + data.getNum() + "笔)");
        holder.tv_money.setText("¥" +  data.getMoney());

        holder.line.setProgress(BigDecimal.valueOf(data.getRatio()).multiply(new BigDecimal(1000)).intValue());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class StateViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_state;
        private TextView tv_ratio;
        private TextView tv_money;
        private LinearProgressIndicator line;

        public StateViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_ratio = itemView.findViewById(R.id.tv_ratio);
            tv_money = itemView.findViewById(R.id.tv_money);
            line = itemView.findViewById(R.id.line);
        }
    }

}
