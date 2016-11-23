package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenter;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.RegisterView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 11/20/16.
 */

public class RegisterFragment extends BaseFragment{
    public static final String TAG = RegisterFragment.class.getSimpleName();

    private static final int MAX_COUNTS = 60; // 60s
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private static final int MAX_COUNT_DOWN_MS = MAX_COUNTS*COUNT_DOWN_INTERVAL;

    private WeakReference<RegisterPresenter> mPresenter;
    private CountDownTimer mDownTimer;
    private NextStepCallback mCallback;

    @BindView(R.id.et_tel_number) EditText mEtTelNumber;
    @BindView(R.id.btn_get_auth_code) Button mBtnGetAuthCode;
    @BindView(R.id.et_input_auth_code) EditText mEtInputAuthCode;

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
                }else{
                    mEtInputAuthCode.setError(getString(R.string.error_invalid_auth_code));
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_get_auth_code})
    public void getAuthCode(){
        String telNumber = mEtTelNumber.getText().toString();
        if(!TextUtils.isEmpty(telNumber)){
            startCountDown();
            // get SMS auth code
            mPresenter.get().getSmsAuthCode(telNumber);

            mEtInputAuthCode.setVisibility(View.VISIBLE);
            mEtInputAuthCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDownTimer.cancel();
                    mBtnGetAuthCode.setText(R.string.send_auth_code);
                }
            });
        }else{
            mEtTelNumber.setError(getString(R.string.error_field_required));
        }
    }

    private void startCountDown(){
        mBtnGetAuthCode.setText(String.valueOf(MAX_COUNTS));
        mDownTimer = new CountDownTimer(MAX_COUNT_DOWN_MS, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnGetAuthCode.setText(String.valueOf(millisUntilFinished/MAX_COUNTS));
            }

            @Override
            public void onFinish() {
                mBtnGetAuthCode.setText(R.string.send_auth_code);
            }
        };
    }

    private void nextStep(){
        Log.v(TAG,"nextStep");

        if(mCallback != null) {
            mCallback.onNextStep();
        }
    }

    private class RegisterViewImpl extends RegisterView {

        @Override
        public void onRegisterComplete(boolean success){
            if(!success){
                showToast(getString(R.string.error_register));
            }else{
                // success, next step
                nextStep();
            }
        }

        @Override
        public void finishView() {

        }
    }
}
