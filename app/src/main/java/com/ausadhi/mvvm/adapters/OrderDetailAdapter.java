package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.databinding.ItemOrderDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    List<Object> mLits;
    AdapterCallBack callBack;
    Context mContext;
    int VIEW_TYPE_ITEM =1;
    public OrderDetailAdapter( AdapterCallBack callBack, Context mContext) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
    }

    public void clear(){
        mLits.clear();
        notifyDataSetChanged();
    }
    public void addItem(OrderModalResponse model){
        mLits.add(model);
        notifyDataSetChanged();
    }
    public void removeItem(int index){
        mLits.remove(index);
        notifyItemRemoved(index);
    }
    public void addAllItem(List<OrderModalResponse> models){
        mLits.addAll(models);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding =ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding.getRoot(),binding);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLits.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        private ItemOrderDetailBinding binding;

        public MyViewHolder(@NonNull View itemView,ItemOrderDetailBinding binding) {
            super(itemView);
            this.binding = binding;

        }

        public void bind(Object data,AdapterCallBack callBack,int position) {
               if(data!=null){
                   OrderModalResponse order = (OrderModalResponse) data;


               }else{
                   itemView.setVisibility(View.GONE);
               }

        }

        }
}
