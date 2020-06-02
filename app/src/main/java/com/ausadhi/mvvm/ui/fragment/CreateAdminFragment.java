package com.ausadhi.mvvm.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ausadhi.mvvm.R;
import com.ausadhi.mvvm.data.DataManager;
import com.ausadhi.mvvm.data.network.model.OrderModalResponse;
import com.ausadhi.mvvm.data.network.model.UserModel;
import com.ausadhi.mvvm.data.network.services.OrderServiceAdmin;
import com.ausadhi.mvvm.data.network.services.SignupService;
import com.ausadhi.mvvm.databinding.FragmentCreateAdminBinding;
import com.ausadhi.mvvm.databinding.FragmentOrderListBinding;
import com.ausadhi.mvvm.fcm.FCMHandler;
import com.ausadhi.mvvm.utils.AppConstants;
import com.ausadhi.mvvm.utils.CommonUtils;
import com.ausadhi.mvvm.utils.LogUtils;
import com.ausadhi.mvvm.utils.RefrenceWrapper;
import com.ausadhi.mvvm.utils.TextUtils;
import com.ausadhi.mvvm.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateAdminFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    TextUtils mTextUtils;
    RefrenceWrapper mRefrenceWraper;
    Context mContext;
    String email;
    String password;


    FragmentCreateAdminBinding dataBinding;

    public CreateAdminFragment() {
        // Required empty public constructor
    }


    public static CreateAdminFragment newInstance(String param1, String param2) {
        CreateAdminFragment fragment = new CreateAdminFragment();
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
        // Inflate the layout for this fragment
        dataBinding = FragmentCreateAdminBinding.inflate(inflater,container,false);
        View view = dataBinding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mRefrenceWraper = RefrenceWrapper.getInstance(mContext);
        mTextUtils = mRefrenceWraper.getTextUtils();
        dataBinding.btnRegister.setOnClickListener(this);
        dataBinding.btnSahre.setOnClickListener(this);
        dataBinding.btnSahre.setVisibility(View.GONE);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                createAdminUser();
                break;
            case R.id.btnSahre:
                shareViaWhatsApp();
                break;
        }

    }

    private void createAdminUser() {

        String name =dataBinding.edtName.getText().toString();
        String mobile = dataBinding.edtMobile.getText().toString();
         email = dataBinding.edtEmail.getText().toString();
         password = dataBinding.edtPassword.getText().toString();
        String userType = AppConstants.UserType.ADMIN;

        if(!mTextUtils.isEmpty(name,dataBinding.edtName)&&
                !mTextUtils.isEmpty(mobile,dataBinding.edtMobile)&&
                !mTextUtils.isEmpty(email,dataBinding.edtEmail)&&
                !mTextUtils.isEmpty(password,dataBinding.edtPassword)&&
                mTextUtils.isEmailValid(email,dataBinding.edtEmail)){
            SignupService.getInstance().getauthRefrence().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = task.getResult().getUser().getUid();
                        String token = DataManager.getInstance().getPrefs().getToken();
                        UserModel data = new UserModel(id,name,mobile,email,password,userType,token);
                        SignupService.getInstance().getDatabasepRefrence().document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    ToastUtils.showSuccessToast(mContext,"Admin created successfully.");
                                    if(dataBinding.btnSahre.getVisibility()==View.GONE){
                                    startTimer();
                                    }
                                    dataBinding.btnSahre.setVisibility(View.VISIBLE);

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(mContext,error,Toast.LENGTH_LONG);


                                }
                            }
                        });



                    }else {
                        String error = task.getException().getMessage();

                        Toast.makeText(mContext,error,Toast.LENGTH_LONG);


                    }
                }
            });



        }
    }

    void startTimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataBinding.btnSahre.setVisibility(View.GONE);
             dataBinding.edtName.setText("");
               dataBinding.edtMobile.setText("");
            dataBinding.edtEmail.setText("");
              dataBinding.edtPassword.setText("");

            }
            }, 5*1000);
    }

    public void shareViaWhatsApp() {
        String emailToSahre =  "*"+email+"*";
        String passwordToSahre = "*"+password+"*";
        String message = "UserId: "+emailToSahre+" "+"\n"+"Password: "+passwordToSahre;
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            Objects.requireNonNull(getActivity()).startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
        }
    }
}
