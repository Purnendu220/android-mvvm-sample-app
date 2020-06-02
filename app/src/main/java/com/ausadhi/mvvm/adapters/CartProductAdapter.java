package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.databinding.ItemCartBinding;
import com.ausadhi.mvvm.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;


public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyViewHolder> {
    List<Object> mLits;
    AdapterCallBack callBack;
    Context mContext;
    int VIEW_TYPE_ITEM =1;

    public CartProductAdapter( AdapterCallBack callBack, Context mContext) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
    }

    public void clear(){
        mLits.clear();
        notifyDataSetChanged();
    }
    public void addItem(OrderItemModel model){
        mLits.add(model);
        notifyDataSetChanged();
    }
    public void removeItem(int index){
        mLits.remove(index);
        notifyItemRemoved(index);
    }
    public void addAllItem(List<OrderItemModel> models){
        mLits.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding =ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mLits.get(position),callBack,position);
    }

    @Override
    public int getItemCount() {
        return mLits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        Context context;
        public MyViewHolder(@NonNull View itemView, ItemCartBinding binding) {
            super(itemView);
            this.binding = binding;
            context = itemView.getContext();

        }

        public void bind(Object data,AdapterCallBack callBack,int position){
            if(data!=null){
                itemView.setVisibility(View.VISIBLE);
                OrderItemModel model = (OrderItemModel) data;
                binding.textProductName.setText(model.getItemName());
                binding.textProductQuantity.setText(model.getItemQuantity());
                binding.mainCardContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
                binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onItemClick(model,binding.imgDelete,position);
                    }
                });
            }else{
                itemView.setVisibility(View.GONE);
            }
        }
    }
}

