package com.github.xzwj87.mineflea.market.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;

/**
 * Created by jason on 11/17/16.
 */

public class ResetPasswordDialog extends DialogFragment{
    private static final String LOG_TAG = "ResetPasswordDialog";

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";

    private static final int COUNT_DOWNS = 60;
    private static final int COUNT_DOWN_TIME = COUNT_DOWNS*1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private EditText mEtUserAccount;
    private EditText mEtAuthCode;
    private Button mBtnGetAuthCode;


    private DialogButtonClickCallback mListener;

    public ResetPasswordDialog(){}

    public static ResetPasswordDialog newInstance(){
        return new ResetPasswordDialog();
    }

    public interface DialogButtonClickCallback {
        void onResetPwd(String account);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            mListener = (DialogButtonClickCallback)context;
        }catch (ClassCastException e){
            e.printStackTrace();
            Log.e(LOG_TAG,"call back has to be implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedSate){
        String title = getString(R.string.reset_your_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);

        View view = LayoutInflater.from(getActivity())
                                  .inflate(R.layout.dialog_reset_password,null);

        mEtUserAccount = (EditText)view.findViewById(R.id.et_input_email_or_tel);
        mEtAuthCode = (EditText)view.findViewById(R.id.et_input_auth_code);
        mBtnGetAuthCode = (Button)view.findViewById(R.id.btn_send_auth_code);

        mBtnGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mEtUserAccount.getText().toString();
                if(UserInfoUtils.isEmailValid(account)){
                    mListener.onResetPwd(account);
                    showToast();
                    //dismiss();
                }else if(UserInfoUtils.isTelNumberValid(account)){
                    mListener.onResetPwd(account);
                    // ok, we want to count 60s to get the auth code
                    startCountDown();
                    mEtAuthCode.setVisibility(View.VISIBLE);
                }else{
                    mEtUserAccount.setError(getString(R.string.error_invalid_account));
                }
            }
        });

        return  builder.setView(view)
                .setTitle(title)
                .create();
    }


    public void resetPwdFail(){
        Log.v(LOG_TAG,"resetPwdFail()");
        mEtAuthCode.setVisibility(View.GONE);
    }

    public void resetPwdSuccess(){
        Log.v(LOG_TAG,"resetPwdSuccess()");
        dismiss();
    }


    private void showToast(){
        Toast.makeText(getActivity(),R.string.reset_password_email_sent,Toast.LENGTH_SHORT)
             .show();
    }

    private void startCountDown(){
        mBtnGetAuthCode.setText(String.valueOf(COUNT_DOWNS));
        new CountDownTimer(COUNT_DOWN_TIME, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnGetAuthCode.setText(String.valueOf(millisUntilFinished/COUNT_DOWNS));
            }

            @Override
            public void onFinish() {
                mBtnGetAuthCode.setText(R.string.send_auth_code);
            }
        }.start();
    }
}
