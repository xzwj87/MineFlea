package com.github.xzwj87.mineflea.market.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView mTvGetAuthCode;
    private EditText mEtNewPwd;

    private CountDownTimer mDownTimer;
    private ResetPwdCallback mListener;

    public ResetPasswordDialog(){}

    public static ResetPasswordDialog newInstance(){
        return new ResetPasswordDialog();
    }

    public interface ResetPwdCallback {
        void onSendSmsOrEmail(String account);
        void onResetPwdBySms(String authCode, String pwd);
    }

    public void setButtonClickCallback(ResetPwdCallback callback){
        mListener = callback;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.v(LOG_TAG,"onAttach()");
        try{
            mListener = (ResetPwdCallback)context;
        }catch (ClassCastException e){
            e.printStackTrace();
            Log.e(LOG_TAG,"call back has to be implemented");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedSate){
        Log.v(LOG_TAG,"onCreateDialog()");
        String title = getString(R.string.find_your_password);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);

        View view = LayoutInflater.from(getActivity())
                                  .inflate(R.layout.dialog_reset_password,null);

        mEtUserAccount = (EditText)view.findViewById(R.id.et_input_email_or_tel);
        mEtAuthCode = (EditText)view.findViewById(R.id.et_input_auth_code);
        mTvGetAuthCode = (TextView) view.findViewById(R.id.tv_send_auth_code);
        mEtNewPwd = (EditText)view.findViewById(R.id.et_input_new_pwd);

        mTvGetAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = mEtUserAccount.getText().toString();
                if(UserInfoUtils.isEmailValid(account)){
                    if(mListener != null) {
                        mListener.onSendSmsOrEmail(account);
                    }
                    showToast();
                    //dismiss();
                }else if(UserInfoUtils.isTelNumberValid(account)){
                    if(mListener != null) {
                        mListener.onSendSmsOrEmail(account);
                    }
                    // ok, we want to count 60s to get the auth code
                    startCountDown();
                    mEtAuthCode.setVisibility(View.VISIBLE);
                    mEtNewPwd.setVisibility(View.VISIBLE);
                }else{
                    mEtUserAccount.setError(getString(R.string.error_invalid_account));
                }
            }
        });

        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onResetPwdBySms(mEtUserAccount.getText().toString(),
                        mEtNewPwd.getText().toString());
            }
        });

        return  builder.setView(view)
                .setTitle(title)
                .create();
    }

    public void resetPwdFail(){
        Log.v(LOG_TAG,"resetPwdFail()");
        mDownTimer.onFinish();
        mDownTimer.cancel();
    }

    private void showToast(){
        Toast.makeText(getActivity(),R.string.reset_password_email_sent,Toast.LENGTH_LONG)
             .show();
    }

    private void startCountDown(){
        final String orig = mTvGetAuthCode.getText().toString();
        mTvGetAuthCode.setText(orig + "(" + String.valueOf(COUNT_DOWNS) + ")");
        mDownTimer =  new CountDownTimer(COUNT_DOWN_TIME, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvGetAuthCode.setText(orig + "(" + String.valueOf(millisUntilFinished/COUNT_DOWN_INTERVAL)
                        + ")");
            }

            @Override
            public void onFinish() {
                mTvGetAuthCode.setText(R.string.send_auth_code);
            }
        }.start();
    }
}
