package com.example.xinbookkeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinbookkeeping.R;
import com.example.xinbookkeeping.bean.RecordBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<RecordBean> mData = new ArrayList<>();
    private Context mContext;

    public RecordAdapter(Context context) {
        mContext = context;
    }

    public void setNewData(List<RecordBean> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<RecordBean> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_record_item, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordBean bean = mData.get(position);
        boolean isInCome = bean.getType().equals("收入");
        holder.tv_desc.setText(bean.getRecordDesc());
        holder.tv_money.setText((isInCome ? "+" : "-") + bean.getMoney());
        holder.tv_money.setTextColor(ContextCompat.getColor(mContext, isInCome ? R.color.purple_200 : R.color.black));
        holder.tv_state.setText(bean.getState());
        holder.tv_time.setText(timeToString(bean.getTime()));

        int res;
        switch (bean.getState()) {
            case "购物":
                res = R.drawable.ic_shopping;
                break;

            case "旅游":
                res = R.drawable.ic_travel;
                break;

            case "餐饮":
                res = R.drawable.ic_food;
                break;
            case "交通":
                res = R.drawable.ic_traffic;
                break;
            case "住宿":
                res = R.drawable.ic_accommdation;
                break;
            case "烟酒":
                res = R.drawable.ic_taw;
                break;
            default:
                res = R.drawable.ic_other;
                break;
        }

        holder.iv.setBackgroundResource(res);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mListener != null) {
                    mListener.onCallBack(bean);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_desc;
        private TextView tv_money;
        private TextView tv_state;
        private TextView tv_time;
        private ImageView iv;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

    private OnLongClickListener mListener;
    public void setOnLongClickListener(OnLongClickListener listener) {
        mListener = listener;
    }

    public interface OnLongClickListener {
        void onCallBack(RecordBean data);
    }

    public static String timeToString(long currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }
}
