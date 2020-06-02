package com.ausadhi.mvvm.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.adapters.AdapterCallBack;
import com.ausadhi.mvvm.adapters.CartProductAdapter;
import com.ausadhi.mvvm.adapters.ProductAdapter;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.AddressModel;
import com.ausadhi.mvvm.data.network.model.OrderItemModel;
import com.ausadhi.mvvm.data.network.model.OrderModel;
import com.ausadhi.mvvm.data.network.model.ProductModel;
import com.ausadhi.mvvm.data.network.services.OrderServiceAdmin;
import com.ausadhi.mvvm.data.network.services.OrderServiceUser;
import com.ausadhi.mvvm.databinding.FragmentAddItemBinding;
import com.ausadhi.mvvm.dialog.ConfirmOrderDialog;
import com.ausadhi.mvvm.dialog.OnAlertButtonAction;
import com.ausadhi.mvvm.ui.HomeActivity;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.CountDrawable;
import com.ausadhi.mvvm.utils.FirebaseRemoteConfigHelper;
import com.ausadhi.mvvm.utils.JsonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AddItemFragment extends Fragment implements View.OnClickListener, AdapterCallBack, OnAlertButtonAction {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context mContext;
    private FragmentAddItemBinding dataBinding;
    private List<OrderItemModel> mList= new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProductAdapter productAdapter;
    private CartProductAdapter adapter;
    private Menu menu;
    AddressListBottomSheet addressListBottomSheet;
    private CollectionReference mUserOrderCollection;
    private DatabaseReference mUserOrderDocumentRefrence;

    public AddItemFragment() {
        // Required empty public constructor
    }


    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
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
        dataBinding = FragmentAddItemBinding.inflate(inflater,container,false);
        View view = dataBinding.getRoot();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        if(DataManager.getInstance().getPrefs().getCartList()!=null&&DataManager.getInstance().getPrefs().getCartList().size()>0){
            mList = new ArrayList<>(DataManager.getInstance().getPrefs().getCartList());
        }
         mUserOrderCollection = OrderServiceUser.getInstance().getmUserOrderCollection();
         mUserOrderDocumentRefrence = OrderServiceUser.getInstance().getmUserOrderDocumentRefrence();
        initAutoComplete();
        initCartProductAdapter();
        dataBinding.btnAddItem.setOnClickListener(this);
        dataBinding.btnOrderContinue.setOnClickListener(this);
        String message = FirebaseRemoteConfigHelper.getFirebaseRemoteConfigHelper(mContext).getRemoteConfigValue(AppConstants.RemoteConfig.CONTINUE_BUTTON);
        dataBinding.btnOrderContinue.setText(message);

    }

    private void initAutoComplete(){
        List<ProductModel> models = DataManager.getInstance().getPrefs().getProductList();
        if(models!=null&&models.size()>0){

            productAdapter = new ProductAdapter(mContext, R.layout.item_product, models);
            dataBinding.editTextProdName.setAdapter(productAdapter);
            dataBinding.editTextProdName.setThreshold(1);
            dataBinding.editTextProdName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   // ProductModel product = (ProductModel) adapterView.getItemAtPosition(i);
                }
            });

        }

    }

    private void initCartProductAdapter()
    {
        adapter = new CartProductAdapter(this,mContext);
        dataBinding.itemList.setLayoutManager(new LinearLayoutManager(mContext));
        dataBinding.itemList.setHasFixedSize(false);
        try {
            ((SimpleItemAnimator) dataBinding.itemList.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        dataBinding.itemList.setAdapter(adapter);
        adapter.addAllItem(mList);
        adapter.notifyDataSetChanged();
        checkEmptyLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddItem:
                String itemName = dataBinding.editTextProdName.getText().toString();
                String itemQuantity = dataBinding.editTextQuantity.getText().toString();

                if(itemName.isEmpty()){
                    ToastUtils.showErrorToast(mContext,"Enter Product Name");
                    return;
                }
                if(itemQuantity.isEmpty()){
                    ToastUtils.showErrorToast(mContext,"Enter Product Quantity");
                    return;
                }
                OrderItemModel model = new OrderItemModel(mList.size()+1,itemName,itemQuantity,"");
                mList.add(model);
                adapter.addItem(model);
                DataManager.getInstance().getPrefs().savecartList(mList);
                dataBinding.editTextProdName.setText("");
                dataBinding.editTextQuantity.setText("");
                dataBinding.editTextQuantity.clearFocus();
                dataBinding.editTextProdName.requestFocus();
                checkEmptyLayout();

                break;
            case R.id.btnOrderContinue:
                 addressListBottomSheet=AddressListBottomSheet.newInstance(mContext,this);
                addressListBottomSheet.show(((HomeActivity) getActivity()).getSupportFragmentManager(), "address_list");
                break;
        }

    }

    @Override
    public void onItemClick(Object model, View view, int position) {
        switch (view.getId()){
            case R.id.imgDelete:
                mList.remove(position);
                adapter.removeItem(position);
                DataManager.getInstance().getPrefs().savecartList(mList);
                checkEmptyLayout();
                break;
            case R.id.mainContainerAddress:
                AddressModel address = (AddressModel) model;
                if(addressListBottomSheet!=null){
                    addressListBottomSheet.dismiss();
                }
                ConfirmOrderDialog dailog = new ConfirmOrderDialog(mContext,this,mList,address);
                dailog.show();

                break;

        }

    }



    private void checkEmptyLayout(){
        if(adapter!=null){
            if(adapter.getItemCount()>0){
                dataBinding.itemList.setVisibility(View.VISIBLE);
                dataBinding.emptyView.getRoot().setVisibility(View.GONE);
                dataBinding.btnOrderContinue.setEnabled(true);
                setCount(mContext,adapter.getItemCount()+"");
            }else{
                dataBinding.itemList.setVisibility(View.GONE);
                dataBinding.emptyView.getRoot().setVisibility(View.VISIBLE);
                dataBinding.btnOrderContinue.setEnabled(false);
                setCount(mContext,"0");

            }
        }else{
            dataBinding.itemList.setVisibility(View.GONE);
            dataBinding.emptyView.getRoot().setVisibility(View.VISIBLE);
            dataBinding.btnOrderContinue.setEnabled(false);


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_item_menu, menu);
        this.menu = menu;
        if(adapter!=null){
            checkEmptyLayout();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_1:
                if(mList!=null||!mList.isEmpty()){
                    addressListBottomSheet=AddressListBottomSheet.newInstance(mContext,this);
                    addressListBottomSheet.show(((HomeActivity) getActivity()).getSupportFragmentManager(), "address_list");
                }
                break;
        }



        return false;
    }

    public void setCount(Context context, String count) {
        try{
            if(menu!=null){
                MenuItem menuItem = menu.findItem(R.id.action_1);
                LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
                CountDrawable badge;

                Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
                if (reuse != null && reuse instanceof CountDrawable) {
                    badge = (CountDrawable) reuse;
                } else {
                    badge = new CountDrawable(context);
                }

                badge.setCount(count);
                icon.mutate();
                icon.setDrawableByLayerId(R.id.ic_group_count, badge);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onOkClick(int requestCode, Object data) {
        switch (requestCode){
            case AppConstants.CONFIRM_ORDER_FROM_DIALOG:
                placeOrder((AddressModel) data);
                break;

        }

    }

    @Override
    public void onCancelClick(int requestCode, Object data) {

    }

    private void placeOrder( AddressModel address){
        dataBinding.KLoadingSpin.startAnimation();
        dataBinding.KLoadingSpin.setIsVisible(true);
        String id = mUserOrderCollection.document().getId();
        OrderModel orderModel = new OrderModel(id,String.valueOf(System.currentTimeMillis()), JsonUtils.toJson(mList),JsonUtils.toJson(address),AppConstants.OrderStatus.ORDER_PLACED,"");
        mUserOrderCollection.document(id).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    createAdminCopy(mUserOrderCollection.document(id).getPath(),orderModel,mUserOrderCollection.document(id));
                }else{
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                    dataBinding.KLoadingSpin.stopAnimation();

                }

            }
        });


    }

    private void createAdminCopy(String path,OrderModel orderModel,DocumentReference documentRefrence){
        String adminId = OrderServiceAdmin.getInstance().getmUserOrderCollection().document().getId();
        orderModel.setUserIdRef(path);
        orderModel.setAdminId(adminId);
        OrderServiceAdmin.getInstance().getmUserOrderCollection().document(adminId).set(orderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataBinding.KLoadingSpin.stopAnimation();
                if(task.isSuccessful()){
                    mList.clear();
                    adapter.clear();
                    DataManager.getInstance().getPrefs().clearCartList();
                    dataBinding.editTextProdName.setText("");
                    dataBinding.editTextQuantity.setText("");
                    dataBinding.editTextQuantity.clearFocus();
                    dataBinding.editTextProdName.requestFocus();
                    adapter.notifyDataSetChanged();
                    checkEmptyLayout();
                    documentRefrence.set(orderModel);
                    ToastUtils.showSuccessToast(mContext,"Order placed successfully");

                }else{
                documentRefrence.delete();
                    ToastUtils.showErrorToast(mContext,task.getException().getMessage());
                }

            }
        });
    }


}
