package com.ausadhi.mvvm.utils;

import android.content.Context;
import android.widget.EditText;

import com.ausadhi.mvvm.R;
import com.google.android.material.textfield.TextInputLayout;

public class TextUtils {
    Context mContext;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public TextUtils(Context mContext){
          this.mContext = mContext;
      }
     public boolean isEmpty(String text){
        if(text==null||text.isEmpty()){
            return true;
        }
     return false;
    }
     public boolean isEmpty(String text, EditText edtText){
        if(text==null||text.isEmpty()){
            if(edtText.getTag()!=null)
            edtText.setError(String.format(mContext.getString(R.string.feild_required),edtText.getTag()));
            else
                edtText.setError(mContext.getString(R.string.feild_required_common));

            return true;
        }
        return false;
    }
    public boolean isEmpty(String text, TextInputLayout txtInput){
        if(text==null||text.isEmpty()){
            if(txtInput.getTag()!=null)
                txtInput.setError(String.format(mContext.getString(R.string.feild_required),txtInput.getTag()));
            else
                txtInput.setError(mContext.getString(R.string.feild_required_common));

            return true;
        }
        return false;
    }
    public boolean isEmpty(String text, TextInputLayout txtInput,String errorMsg){
        if(text==null||text.isEmpty()){
            txtInput.setError(errorMsg);
            return true;
        }
        return false;
    }
    public boolean isEmpty(String text, EditText edtText,String errorMsg){
        if(text==null||text.isEmpty()){
                edtText.setError(errorMsg);
                return true;
        }
        return false;
    }

    public boolean isEmailValid(String text, EditText edtEmail) {
        if(text!=null||text.matches(emailPattern)){
            edtEmail.setError(null);
            return true;
        }else{
            edtEmail.setError(mContext.getString(R.string.email_invalid));
        }
        return false;
    }
}
