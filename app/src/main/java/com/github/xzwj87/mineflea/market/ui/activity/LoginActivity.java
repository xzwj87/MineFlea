package com.github.xzwj87.mineflea.market.ui.activity;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.LoginPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.LoginView;
import com.github.xzwj87.mineflea.market.ui.dialog.ResetPasswordDialog;
import com.github.xzwj87.mineflea.utils.SharePrefsHelper;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.xzwj87.mineflea.utils.UserInfoUtils.isEmailValid;


public class LoginActivity extends BaseActivity implements LoginView,
        ResetPasswordDialog.DialogButtonClickCallback{
    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final int REQUEST_USER_REGISTER = 1;

    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.tv_forget_password) TextView mTvForgetPwd;
    @BindView(R.id.btn_login) Button mBtnLogin;

    private ProgressDialog mProgress;

    @Inject LoginPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        init();

        ThemeColorUtils.changeThemeColor(this);
    }

    @OnClick({R.id.btn_login})
    public void onButtonClick(){
        tryLogin();
    }

    @OnClick({R.id.tv_forget_password})
    public void resetPassword(){
        showResetPasswordDialog();
    }


    @Override
    public void onActivityResult(int request,int result,Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);

            switch (request) {
                case REQUEST_USER_REGISTER:
                    if(result == RESULT_OK && data != null) {
                        setResult(RESULT_OK, data);
                    }else{
                        finishView();
                    }
                    break;
            }
    }

    private void tryLogin() {
        Log.v(TAG,"tryLogin()");
        // Reset errors.
        mEtAccount.setError(null);
        mEtPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mEtAccount.getText().toString();
        String password = mEtPassword.getText().toString();

        mPresenter.setUserAccount(email);
        mPresenter.setUserPwd(password);

        // check user login info
        if(mPresenter.validLoginInfo()){
            mPresenter.login();
        }
    }

    @Override
    public void showProgress(boolean show) {

        if(show) {
            mProgress = ProgressDialog.show(this, "", getString(R.string.hint_logining));
        }else if(mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    @Override
    public void resetPwdSuccess() {
        ResetPasswordDialog dialog = (ResetPasswordDialog)getFragmentManager()
                .findFragmentByTag(ResetPasswordDialog.class.getSimpleName());
        if(dialog != null) {
            dialog.resetPwdSuccess();
        }else{
            showToast(getString(R.string.reset_pwd_success));
        }
    }

    @Override
    public void showEmailResetPwdFailMsg() {
        showToast(getString(R.string.error_reset_pwd_by_email));
        ResetPasswordDialog dialog = (ResetPasswordDialog)getFragmentManager()
                .findFragmentByTag(ResetPasswordDialog.class.getSimpleName());
        if(dialog != null){
            dialog.resetPwdFail();
        }
    }

    @Override
    public void showSmsResetPwdFailMsg() {
        showToast(getString(R.string.error_reset_pwd_by_sms));
        ResetPasswordDialog dialog = (ResetPasswordDialog)getFragmentManager()
                .findFragmentByTag(ResetPasswordDialog.class.getSimpleName());
        if(dialog != null){
            dialog.resetPwdFail();
        }
    }

    @Override
    public void onLoginSuccess() {
        Log.v(TAG,"onLoginSuccess()");

        showToast(getString(R.string.hint_login_success));

        Intent intent = new Intent();
        intent.putExtra(UserInfo.IS_LOGIN,true);
        intent.putExtra(UserInfo.USER_NICK_NAME,mPresenter.getUserNickName());
        intent.putExtra(UserInfo.UER_EMAIL,mPresenter.getUserEmail());
        intent.putExtra(UserInfo.USER_HEAD_ICON,mPresenter.getHeadIconUrl());

        // save pref value of login state
        SharePrefsHelper.getInstance(this).updateLogState(true);

        setResult(RESULT_OK,intent);

        finishView();
    }

    @Override
    public void onLoginFail() {
        Log.v(TAG,"onLoginFail()");

        showToast(getString(R.string.prompt_login_failure));
    }

    @Override
    public void showAccountInvalidMsg() {
        // Check for a valid email address.
        String email = mEtAccount.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEtAccount.setError(getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            mEtAccount.setError(getString(R.string.error_invalid_account));
        }
        mEtAccount.requestFocus();
    }

    @Override
    public void showPwdInvalidMsg() {
        mEtPassword.setError(getString(R.string.error_invalid_email));
        mEtPassword.requestFocus();
    }

    @Override
    public void finishView() {
        finish();
    }

    private void init() {
        initInjector();

        mPresenter.init();
    }

    private void initInjector(){
        mMarketComponent.inject(this);
        mPresenter.setView(this);
    }

    private void showResetPasswordDialog(){
        ResetPasswordDialog dialog = ResetPasswordDialog.newInstance();
        dialog.show(getFragmentManager(),ResetPasswordDialog.class.getSimpleName());
    }

    @Override
    public void onResetPwd(String account) {
        Log.v(TAG,"onResetPwd()");
        mPresenter.resetPwdByAccount(account);
    }
}

