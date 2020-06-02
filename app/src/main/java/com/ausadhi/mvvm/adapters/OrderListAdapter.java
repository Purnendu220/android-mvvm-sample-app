package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.databinding.ItemOrderListBinding;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    List<Object> mLits;
    AdapterCallBack callBack;
    Context mContext;
    int VIEW_TYPE_ITEM =1;
    public OrderListAdapter( AdapterCallBack callBack, Context mContext) {
        this.mLits = new ArrayList<>();
        this.callBack = callBack;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderListBinding binding =ItemOrderListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mLits.get(position),callBack,position);

    }
    public void clear(){
        mLits.clear();
        notifyDataSetChanged();
    }
    public void addItem(OrderModalResponse model){
        mLits.add(model);
        notifyDataSetChanged();
    }
    public void addAllItem(List<OrderModalResponse> models){
        mLits.addAll(models);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mLits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding binding;
        Context context;
        public MyViewHolder(@NonNull View itemView, ItemOrderListBinding binding) {
            super(itemView);
            this.binding = binding;
            context = itemView.getContext();

        }

        public void bind(Object data,AdapterCallBack callBack,int position){
            if(data!=null){
                itemView.setVisibility(View.VISIBLE);
                OrderModalResponse model = (OrderModalResponse) data;
                if(DataManager.getInstance().getPrefs().getUserType().equalsIgnoreCase(AppConstants.UserType.USER)){
                    setViewForUser(model,position);

                }else{
                    setViewForAdmin(model,position);

                }
            binding.mainContainerOrder.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
            }else{
                itemView.setVisibility(View.GONE);
            }
        }

        private void setViewForUser(OrderModalResponse model,int position){
            binding.layoutUserName.setVisibility(View.VISIBLE);
            binding.linearLayoutUserCancel.setVisibility(View.VISIBLE);
            binding.layoutOrderDate.setVisibility(View.VISIBLE);
            binding.layoutOrderNumber.setVisibility(View.VISIBLE);
            binding.layoutOrderItems.setVisibility(View.VISIBLE);
            binding.layoutOrderStatus.setVisibility(View.VISIBLE);
            binding.layoutOrderTotal.setVisibility(View.GONE);
            binding.layoutOrderContact.setVisibility(View.GONE);
            binding.layoutOrderAddress.setVisibility(View.GONE);
            binding.layoutButton.setVisibility(View.GONE);
            binding.textViewName.setText(model.getOrderAddress().getName());
            binding.textOrderDate.setText(DateUtils.formatDate(Long.parseLong(model.getOrderDate())));
            binding.textOrderNumber.setText(model.getAdminId());
            binding.linearLayoutUserUpdate.setVisibility(View.GONE);

            String items = String.format(mContext.getString(R.string.order_items),model.getOrderItems().size()+"");
            binding.textOrderItems.setText(items);
            String[] stringArray = mContext.getResources().getStringArray(R.array.order_status);
            if(model.getOrderStatus()>2){
                binding.linearLayoutUserCancel.setVisibility(View.GONE);

            }else {
                binding.linearLayoutUserCancel.setVisibility(View.VISIBLE);

            }
            binding.textOrderStatus.setText(stringArray[model.getOrderStatus()]);
            binding.linearLayoutUserCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.linearLayoutUserCancel,position);
                }
            });
            binding.mainContainerOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.mainContainerOrder,position);
                }
            });



        }

        private void setViewForAdmin(OrderModalResponse model,int position){
            binding.layoutUserName.setVisibility(View.VISIBLE);
            binding.linearLayoutUserCancel.setVisibility(View.VISIBLE);
            binding.layoutOrderDate.setVisibility(View.VISIBLE);
            binding.layoutOrderNumber.setVisibility(View.VISIBLE);
            binding.layoutOrderItems.setVisibility(View.VISIBLE);
            binding.layoutOrderStatus.setVisibility(View.VISIBLE);
            binding.layoutOrderTotal.setVisibility(View.GONE);
            binding.layoutOrderContact.setVisibility(View.GONE);
            binding.layoutOrderAddress.setVisibility(View.GONE);
            binding.layoutButton.setVisibility(View.VISIBLE);
            binding.textViewName.setText(model.getOrderAddress().getName());
            binding.textOrderDate.setText(DateUtils.formatDate(Long.parseLong(model.getOrderDate())));
            binding.textOrderNumber.setText(model.getAdminId());
            String items = String.format(mContext.getString(R.string.order_items),model.getOrderItems().size()+"");
            binding.textOrderItems.setText(items);
            String[] stringArray = mContext.getResources().getStringArray(R.array.order_status);
            String[] stringArrayAdmin = mContext.getResources().getStringArray(R.array.order_status_admin);
            if(model.getOrderAmount()!=null&&!model.getOrderAmount().isEmpty()){
                try{
                    Double.parseDouble(model.getOrderAmount());
                    binding.textOrderTotal.setText(model.getOrderAmount());
                    binding.layoutOrderTotal.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    binding.layoutOrderTotal.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
            if(model.getOrderStatus()==AppConstants.OrderStatus.ORDER_DELIVERED){
                binding.layoutButton.setVisibility(View.GONE);

            }

            binding.linearLayoutUserCancel.setVisibility(View.GONE);
                binding.linearLayoutUserUpdate.setVisibility(View.GONE);
                binding.textOrderStatus.setText(stringArray[model.getOrderStatus()]);
                if(model.getOrderStatus()>0&&model.getOrderStatus()<4){
                    binding.linearLayoutUserUpdate.setVisibility(View.VISIBLE);
                    binding.textViewOrder.setText(stringArrayAdmin[model.getOrderStatus()+1]);

                }
                binding.linearLayoutUserUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.linearLayoutUserUpdate,position);
                }
            });

            binding.imgShareOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.imgShareOrder,position);
                }
            });
            binding.imgContactCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.imgContactCustomer,position);
                }
            });
            binding.imgCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.imgCancelOrder,position);
                }
            });

            binding.mainContainerOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onItemClick(model,binding.mainContainerOrder,position);
                }
            });



        }
    }
}
