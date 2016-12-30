package com.xlmiao.coolweather.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xlmiao.coolweather.R;
import com.xlmiao.coolweather.db.City;
import com.xlmiao.coolweather.db.County;
import com.xlmiao.coolweather.db.Province;

import java.util.List;

/**
 * Created by XuLeMiao on 2016/12/23.
 * ChooseAreaFragmentAdapter 城市列表adapter
 */

public class ChooseAreaFragmentAdapter extends RecyclerView.Adapter<ChooseAreaFragmentAdapter.ViewHolder> implements View.OnClickListener{



    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , Object data);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    private List<Object> mlist;

    public ChooseAreaFragmentAdapter(List<Object> mlist){
        this.mlist = mlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area_item,parent,false);
        ViewHolder mViewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object obj = mlist.get(position);

        if(obj instanceof Province){
            holder.areaName.setText(((Province) obj).getProvinceName());
        }

        if(obj instanceof City){
            holder.areaName.setText(((City) obj).getCityName());
        }

        if(obj instanceof County){
            holder.areaName.setText(((County) obj).getCountyName());
        }

        holder.itemView.setTag(obj);

    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view,(Object)view.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView areaName;
        public ViewHolder(View itemView) {
            super(itemView);
            areaName = (TextView)itemView.findViewById(R.id.area_name);
        }
    }
}
