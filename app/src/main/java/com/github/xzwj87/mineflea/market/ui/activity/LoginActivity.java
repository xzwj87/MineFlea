package com.github.xzwj87.mineflea.market.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import butterknife.OnFocusChange;

import static com.github.xzwj87.mineflea.utils.UserInfoUtils.isEmailValid;


public class LoginActivity extends BaseActivity implements LoginView,
        ResetPasswordDialog.ResetPwdCallback {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private static final int REQUEST_USER_REGISTER = 1;

    @BindView(R.id.iv_account) ImageView mIvAccountIcon;
    @BindView(R.id.iv_password) ImageView mIvPwdIcon;
    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.tv_forget_password) TextView mTvForgetPwd;
    @BindView(R.id.iv_show_pwd) ImageView mIvShowPwd;
    @BindView(R.id.btn_login) Button mBtnLogin;

    private ProgressDialog mProgress;
    private boolean mIsPwdShow = false;

    @Inject LoginPresenterImpl mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        ThemeColorUtils.changeThemeColor(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.btn_login})
    void onLogin(){
        tryLogin();
    }

    @OnClick({R.id.tv_forget_password})
    void onTextClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.tv_forget_password:
                resetPassword();
                break;
            default:
                break;
        }
    }

    private void resetPassword(){
        Log.v(TAG,"resetPassword()");
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
        if(mProgress == null) return;

        if(show) {
            mProgress = ProgressDialog.show(this, "", getString(R.string.hint_logining));
        }else if(mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    @Override
    public void resetPwdSuccess() {
        Log.v(TAG,"resetPwdSuccess()");
        showToast(getString(R.string.sms_auth_code_has_been_sent));
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
    public void onSendSmsOrEmail(String account) {
        Log.v(TAG,"onSendSmsOrEmail()");
        mPresenter.sendAuthCodeByAccount(account);
    }

    @Override
    public void onResetPwdBySms(String authCode, String pwd) {
        Log.v(TAG,"onResetPwdBySms()");
        if(!TextUtils.isEmpty(authCode) && !TextUtils.isEmpty(pwd)) {
            mPresenter.resetPwdBySms(authCode, pwd);
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    private void init() {
        initInjector();

        mPresenter.init();
    }

    @OnFocusChange({R.id.et_account,R.id.et_password})
    void changeViewStyle(EditText view, boolean hasFocus){
        int id = view.getId();

        if(hasFocus || !TextUtils.isEmpty(view.getText())) {
            view.setHint("");

            if(id == R.id.et_account) {
                mIvAccountIcon.getDrawable().setColorFilter(view.getHighlightColor(), PorterDuff.Mode.SRC_IN);
            }else if(id == R.id.et_password){
                mIvPwdIcon.getDrawable().setColorFilter(view.getHighlightColor(), PorterDuff.Mode.SRC_IN);
            }
        }else{
            if(TextUtils.isEmpty(view.getText())) {
                if (id == R.id.et_account) {
                    mEtAccount.setHint(R.string.prompt_account_login);
                } else if (id == R.id.et_password) {
                    mEtPassword.setHint(R.string.prompt_password);
                }
            }

            if (id == R.id.et_account) {
                // remove background
                mIvAccountIcon.getDrawable().setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_IN);
            } else if (id == R.id.et_password) {
                mIvPwdIcon.getDrawable().setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @OnClick(R.id.iv_show_pwd)
    void showPassword(ImageView iv){
        if(!mIsPwdShow) {
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIvShowPwd.setBackgroundResource(R.drawable.ic_visibility_off_grey_24dp);
        }else{
            mIvShowPwd.setBackgroundResource(R.drawable.ic_visibility_grey_24dp);
            // tricky
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD + 0x01);
        }

        mEtPassword.setSelection(mEtPassword.getText().length());
        mIsPwdShow = !mIsPwdShow;
    }

    private void initInjector(){
        mMarketComponent.inject(this);
        mPresenter.setView(this);
    }

    private void showResetPasswordDialog(){
        ResetPasswordDialog dialog = ResetPasswordDialog.newInstance();
        dialog.show(getFragmentManager(),ResetPasswordDialog.class.getSimpleName());
        dialog.setButtonClickCallback(this);
    }
}

