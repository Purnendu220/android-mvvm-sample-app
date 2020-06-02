package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.databinding.ItemAddressBindingBinding;

import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.MyViewHolder> {

    List<Object> mLits;
    AdapterCallBack callBack;
    Context mContext;
    int VIEW_TYPE_ITEM =1;
    int OPEN_FROM = -1;

    public AddressListAdapter( AdapterCallBack callBack, Context mContext) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
    }

    public AddressListAdapter( AdapterCallBack callBack, Context mContext,int OPEN_FROM) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
        this.OPEN_FROM = OPEN_FROM;
    }

    public void clear(){
        mLits.clear();
        notifyDataSetChanged();
    }
    public void addItem(AddressModel model){
        mLits.add(model);
        notifyDataSetChanged();
    }
    public void addAllItem(List<AddressModel> models){
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
        ItemAddressBindingBinding binding =ItemAddressBindingBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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
        ItemAddressBindingBinding binding;
        Context context;
        public MyViewHolder(@NonNull View itemView, ItemAddressBindingBinding binding) {
            super(itemView);
            this.binding = binding;
            context = itemView.getContext();

        }

        public void bind(Object data,AdapterCallBack callBack,int position){
            if(data!=null){
                itemView.setVisibility(View.VISIBLE);
                AddressModel model = (AddressModel) data;
                binding.textAddressName.setText(model.getName());
                binding.textAddressPhone.setText(model.getPhone());
                binding.textAddressHouseNo.setText(model.getHouseno());
                binding.mainContainerAddress.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
                if(OPEN_FROM==-1){
                    binding.imgDelete.setVisibility(View.GONE);
                    binding.imgEdit.setVisibility(View.GONE);

                }else{
                    binding.imgDelete.setVisibility(View.VISIBLE);
                    binding.imgEdit.setVisibility(View.VISIBLE);

                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onItemClick(data,binding.mainContainerAddress,position);
                    }
                });
                binding.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onItemClick(data,binding.imgDelete,position);

                    }
                });
                binding.imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.onItemClick(data,binding.imgEdit,position);

                    }
                });

            }else{
                itemView.setVisibility(View.GONE);
            }
        }
    }
}
