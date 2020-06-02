package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
     List<Object> mLits;
     AdapterCallBack callBack;
     Context mContext;
     int VIEW_TYPE_ITEM =1;

    public ProductListAdapter( AdapterCallBack callBack, Context mContext) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
    }

    public void clear(){
        mLits.clear();
        notifyDataSetChanged();
    }
    public void addItem(ProductModel model){
        mLits.add(model);
        notifyDataSetChanged();
    }
    public void addAllItem(List<ProductModel> models){
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
        ItemProductBinding binding =ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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
        ItemProductBinding binding;
        Context context;
        public MyViewHolder(@NonNull View itemView,ItemProductBinding binding) {
            super(itemView);
            this.binding = binding;
            context = itemView.getContext();

        }

        public void bind(Object data,AdapterCallBack callBack,int position){
            if(data!=null){
                itemView.setVisibility(View.VISIBLE);
                ProductModel model = (ProductModel) data;
                binding.textProductName.setText(model.getProductName());
                binding.mainContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
            }else{
                itemView.setVisibility(View.GONE);
            }
        }
    }
}
