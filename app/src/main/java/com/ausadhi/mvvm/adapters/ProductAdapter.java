package com.ausadhi.mvvm.adapters;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.databinding.ItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<ProductModel> {
    private Context context;
    private int resourceId;
    private List<ProductModel> items, tempItems, suggestions;
    public ProductAdapter(@NonNull Context context, int resourceId, List<ProductModel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        try {
            ItemProductBinding binding =ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            view = binding.getRoot();
            ProductModel prod = getItem(position);
            binding.textProductName.setText(prod.getProductName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public ProductModel getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }
    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductModel fruit = (ProductModel) resultValue;
            return fruit.getProductName();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ProductModel fruit: tempItems) {
                    if (fruit.getProductName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        suggestions.add(fruit);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ProductModel> tempValues = (ArrayList<ProductModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ProductModel fruitObj : tempValues) {
                    add(fruitObj);
                }
                notifyDataSetChanged();
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}