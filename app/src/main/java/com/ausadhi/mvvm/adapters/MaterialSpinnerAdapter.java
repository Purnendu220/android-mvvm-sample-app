package com.ausadhi.mvvm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ausadhi.mvvm.R;


public class MaterialSpinnerAdapter extends BaseAdapter implements SpinnerAdapter  {

        private final String[] list;
        private Context mContext;
        private LayoutInflater inflter;


        public MaterialSpinnerAdapter(String[] list,Context mContext){
            this.mContext = mContext;
            this.list = list;
            inflter = (LayoutInflater.from(mContext));

        }


        @Override
        public int getCount() {
            return list.length;
        }


        @Override
        public String getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            recycle = inflter.inflate(R.layout.drop_down_item_layout, null);
            text =  recycle.findViewById(R.id.textDropDown);
            text.setTextColor(mContext.getResources().getColor(R.color.text_color_dark));
            text.setText(list[position]);
            return text;
        }


    }




