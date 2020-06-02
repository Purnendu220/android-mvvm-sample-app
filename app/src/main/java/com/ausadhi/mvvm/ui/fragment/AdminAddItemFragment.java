package com.ausadhi.mvvm.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.ProductListAdapter;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.services.ProductListService;
import com.ausadhi.mvvm.databinding.FragmentAdminAddItemBinding;
import com.ausadhi.mvvm.utils.LogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAddItemFragment extends Fragment implements View.OnClickListener, AdapterCallBack {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  CollectionReference mProductDatabaseCollection;
    private  DatabaseReference mProductDatabase;
    private FragmentAdminAddItemBinding dataBinding;
    private Context mContext;
    ProductListAdapter adapter;
    List<ProductModel> productModelList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminAddItemFragment() {
        // Required empty public constructor
    }


    public static AdminAddItemFragment newInstance(String param1, String param2) {
        AdminAddItemFragment fragment = new AdminAddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         dataBinding = FragmentAdminAddItemBinding.inflate(inflater,container,false);
        View view = dataBinding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataBinding.btnAddItem.setOnClickListener(this);
        mProductDatabaseCollection = ProductListService.getInstance().getProductDataCollectionRefrence();
        mProductDatabase = ProductListService.getInstance().getProductDatabaseRefrence();
        mContext = getActivity();
        productModelList = new ArrayList<>();
        setDatabaseRefrenceChangeListener();
        setRecyclerView();


    }
    private void setRecyclerView(){
        adapter = new ProductListAdapter(this,mContext);
        dataBinding.productList.setLayoutManager(new LinearLayoutManager(mContext));
        dataBinding.productList.setHasFixedSize(false);
        try {
            ((SimpleItemAnimator) dataBinding.productList.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        dataBinding.productList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.btnAddItem:
             if(!dataBinding.editTextProdName.getText().toString().isEmpty()){
                 setIsLoading(true);
                 String id = mProductDatabaseCollection.document().getId();
                 mProductDatabaseCollection.document(id).set(new ProductModel(id,dataBinding.editTextProdName.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             setIsLoading(false);

                         }else{
                             String error = task.getException().getMessage();
                             setIsLoading(false);
                             Toast.makeText(mContext,error,Toast.LENGTH_LONG);


                         }
                     }
                 });;
             }
             break;
     }
    }
    private void setDatabaseRefrenceChangeListener(){
        mProductDatabaseCollection.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                productModelList.clear();
                adapter.clear();
                for (DocumentSnapshot productSnapShot:queryDocumentSnapshots.getDocuments()) {
                    productModelList.add(new ProductModel(productSnapShot.getId(),(String) productSnapShot.getData().get("productName")));
                }
                adapter.addAllItem(productModelList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setIsLoading(boolean status){
        if (status) {
            dataBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            dataBinding.progressBar.setVisibility(View.GONE);
        }


    }

    @Override
    public void onItemClick(Object model, View view, int position) {

    }
}
