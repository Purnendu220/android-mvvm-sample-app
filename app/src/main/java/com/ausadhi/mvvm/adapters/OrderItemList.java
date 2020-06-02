package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.databinding.ItemsOrderBinding;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;



public class OrderItemList extends RecyclerView.Adapter<OrderItemList.MyViewHolder> {
    List<Object> mLits;
    AdapterCallBack callBack;
    Context mContext;
    int VIEW_TYPE_ITEM =1;

    public OrderItemList( AdapterCallBack callBack, Context mContext) {
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
        ItemsOrderBinding binding =ItemsOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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
        ItemsOrderBinding binding;
        Context context;
        public MyViewHolder(@NonNull View itemView, ItemsOrderBinding binding) {
            super(itemView);
            this.binding = binding;
            context = itemView.getContext();

        }

        public void bind(Object data,AdapterCallBack callBack,int position){
            if(data!=null){
                itemView.setVisibility(View.VISIBLE);
                OrderItemModel model = (OrderItemModel) data;
                if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                    setDataForUser(model,position);

                }else{
                    setDataForAdmin(model,position);

                }

            }else{
                itemView.setVisibility(View.GONE);
            }
        }

        private void setDataForUser( OrderItemModel model,int position){
            binding.textProductName.setText(model.getItemName());
            binding.textProductQuantity.setText(model.getItemQuantity());
            binding.mainCardContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
            binding.editTextPrice.setEnabled(false);
            binding.editTextPrice.setFocusable(false);
            if(model.getItemPrice()!=null&&!model.getItemPrice().isEmpty()){
                binding.editTextPrice.setText(model.getItemPrice());
                binding.editTextPrice.setVisibility(View.VISIBLE);
            }else{

                binding.editTextPrice.setVisibility(View.GONE);

            }

        }

        private void setDataForAdmin( OrderItemModel model,int position){
            binding.textProductName.setText(model.getItemName());
            binding.textProductQuantity.setText(model.getItemQuantity());
            binding.editTextPrice.setVisibility(View.VISIBLE);
            if(model.getItemPrice()!=null&&!model.getItemPrice().isEmpty()){
                binding.editTextPrice.setText(model.getItemPrice());
            }
           // binding.mainCardContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
            binding.editTextPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        Double.parseDouble(s.toString());
                        model.setItemPrice(s.toString());
                        callBack.onItemClick(model,binding.editTextPrice,position);


                    }catch (Exception e){
                        ToastUtils.showErrorToast(mContext,"Please Enter Valid price");
                    }


                }
            });

        }
    }
}

