package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenter;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.RegisterView;
import com.github.xzwj87.mineflea.market.ui.activity.LoginActivity;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.github.xzwj87.mineflea.utils.UserInfoUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jason on 11/20/16.
 */

public class RegisterFragment extends BaseFragment{
    public static final String TAG = RegisterFragment.class.getSimpleName();

    public static final int REQUEST_LOGIN = 1;
    private static final int MAX_COUNTS = 60; // 60s
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private static final int MAX_COUNT_DOWN_MS = MAX_COUNTS*COUNT_DOWN_INTERVAL;

    private WeakReference<RegisterPresenter> mPresenter;

    private static boolean sIsTimerStarted = false;
    private CountDownTimer mDownTimer = null;
    private NextStepCallback mCallback;

    @BindView(R.id.et_tel_number) EditText mEtTelNumber;
    @BindView(R.id.tv_get_auth_code) TextView mTvGetAuthCode;
    @BindView(R.id.et_input_auth_code) EditText mEtInputAuthCode;
    @BindView(R.id.input_auth) LinearLayout mInputAuthLayout;
    @BindView(R.id.tv_login) TextView mTvLogin;

    public RegisterFragment(){}

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    // tell parent we are read for next step
    public interface NextStepCallback {
        void onNextStep();
    }

    @SuppressWarnings("unchecked")
    public void setPresenter(RegisterPresenterImpl presenter){
        mPresenter = new WeakReference(presenter);
        mPresenter.get().setView(new RegisterViewImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_register,parent,false);

        ButterKnife.bind(this,root);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallback = (NextStepCallback)context;
        }catch (ClassCastException e){
            e.printStackTrace();
            throw new IllegalArgumentException("activity must implement this callback");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_register,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.next_step:
                String authCode = mEtInputAuthCode.getText().toString();
                mPresenter.get().setSmsAuthCode(authCode);
                if(!TextUtils.isEmpty(authCode)){
                    mPresenter.get().signUpBySms();
                }else {
                    mEtInputAuthCode.setError(getString(R.string.error_invalid_auth_code));
                }

                if(mDownTimer != null) {
                    mDownTimer.onFinish();
                    mDownTimer.cancel();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_get_auth_code})
    public void getAuthCode(){
        String telNumber = mEtTelNumber.getText().toString();
        if(!TextUtils.isEmpty(telNumber) && !sIsTimerStarted){
            startCountDown();
            // get SMS auth code
            mPresenter.get().getSmsAuthCode(telNumber);
            mEtInputAuthCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sIsTimerStarted = false;
                    mDownTimer.onFinish();
                    mDownTimer.cancel();
                    mTvGetAuthCode.setText(R.string.send_auth_code);
                }
            });
        }else if(TextUtils.isEmpty(telNumber)){
            mEtTelNumber.setError(getString(R.string.error_field_required));
        }
    }

    @OnClick({R.id.tv_login})
    public void login(){
        Log.v(TAG,"login()");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    @OnTextChanged(value = R.id.et_tel_number,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTelNumberChanged(Editable editable){
        String tel = editable.toString();
        if(UserInfoUtils.isTelNumber(tel)){
            if(mDownTimer != null) {
                mDownTimer.onFinish();
                mDownTimer.cancel();
            }
        }
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);
    }

    private void startCountDown(){
        sIsTimerStarted = true;
        final String orig = mTvGetAuthCode.getText().toString();
        //float textSize = getResources().getDimension(R.dimen.button_text_size_small);
        //mTvGetAuthCode.setTextSize(Dimension.SP,textSize);
        mTvGetAuthCode.setText(orig + "(" + String.valueOf(MAX_COUNTS) + ")");
        mDownTimer = new CountDownTimer(MAX_COUNT_DOWN_MS, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvGetAuthCode.setText(orig + "(" +
                        String.valueOf(millisUntilFinished/COUNT_DOWN_INTERVAL) + ")");
            }

            @Override
            public void onFinish() {
                mTvGetAuthCode.setText(R.string.send_auth_code);
                sIsTimerStarted = false;
            }
        }.start();
    }

    private void nextStep(){
        Log.v(TAG,"nextStep");

        if(mCallback != null) {
            mCallback.onNextStep();
        }
    }

    private class RegisterViewImpl extends RegisterView {

        @Override
        public void onLoginBySmsComplete(boolean success){
            if(!success){
                mEtInputAuthCode.setError(getString(R.string.error_wrong_auth_code));
            }else{
                nextStep();
            }
        }

        @Override
        public void onRegisterComplete(boolean success){
            if(!success){
                showToast(getString(R.string.error_register));
            }/*else{
                // success, next step
                nextStep();
            }*/
        }

        @Override
        public void finishView() {

        }
    }
}
