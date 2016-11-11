package com.github.xzwj87.mineflea.market.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.RegisterView;
import com.github.xzwj87.mineflea.utils.SharePrefsHelper;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by jason on 10/13/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterView{
    public static final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.et_user_name) EditText mEtName;
    @BindView(R.id.atv_email) AutoCompleteTextView mAtvEmail;
    @BindView(R.id.et_tel_number) EditText mEtTelNumber;
    @BindView(R.id.et_password) EditText mEtPwd;
    @BindView(R.id.user_header_picker) RoundedImageView mHeaderPicker;

    @Inject RegisterPresenterImpl mPresenter;
    private ProgressDialog mProgress;
    private ArrayList<String> mHeadIconUrl;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initInjector();
        init();
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPresenter.onDestroy();
    }

    @OnClick({R.id.btn_register})
    public void onButtonClick(Button button){
        Log.v(TAG,"onButtonClick(): id = " + button.getId());

        String name = mEtName.getText().toString();
        String email = mAtvEmail.getText().toString();
        String tel = mEtTelNumber.getText().toString();
        String pwd = mEtPwd.getText().toString();

        mPresenter.setUserNickName(name);
        mPresenter.setUserEmail(email);
        mPresenter.setUserTel(tel);
        mPresenter.setUserPwd(pwd);

        if(mHeadIconUrl != null) {
            mPresenter.setUserIconUrl(mHeadIconUrl.get(0));
        }else{
            mPresenter.setUserIconUrl("");
        }

        if(mPresenter.validUserInfo()){
            mPresenter.register();
            showProgress();
        }
    }

    @OnClick(R.id.user_header_picker)
    public void pickHeadIcon(){
        Log.v(TAG,"pickHeadIcon()");

        PhotoPicker.builder()
                   .setPhotoCount(1)
                   .setPreviewEnabled(true)
                   .setShowCamera(true)
                   .setShowGif(true)
                   .start(this,PhotoPicker.REQUEST_CODE);

    }

    @Override
    public void onRegisterComplete(boolean success) {
        Log.v(TAG,"onRegisterComplete(): " + (success ? "success" : "fail"));

        if(mProgress.isShowing()){
            mProgress.dismiss();
        }

        if(success) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(UserInfo.USER_NICK_NAME, mEtName.getText().toString());
            bundle.putString(UserInfo.UER_EMAIL, mAtvEmail.getText().toString());
            bundle.putString(UserInfo.USER_TEL, mEtTelNumber.getText().toString());
            bundle.putString(UserInfo.USER_PWD, mEtPwd.getText().toString());

            if (mHeadIconUrl != null) {
                bundle.putString(UserInfo.USER_HEAD_ICON, mHeadIconUrl.get(0));
            }

            data.putExtras(bundle);
            setResult(RESULT_OK, data);

            SharePrefsHelper.getInstance(this)
                            .updateLogState(true);
        }else{
            setResult(RESULT_CANCELED);
        }
    }

    @Override
    public void showNameInvalidMsg() {
        Log.v(TAG,"showNameInvalidMsg()");

        mEtName.setError(getString(R.string.error_invalid_user_name));
        mEtName.requestFocus();
        //mEtName.setError("");
    }

    @Override
    public void showEmailInvalidMsg() {
        Log.v(TAG,"showEmailInvalidMsg()");

        mAtvEmail.setText(getString(R.string.error_invalid_email));
        mAtvEmail.requestFocus();
        //mAtvEmail.setError("");
    }

    @Override
    public void showTelInvalidMsg() {
        Log.v(TAG,"ShowTelInvalidMsg()");

        mEtTelNumber.setError(getString(R.string.error_invalid_user_tel));
        mEtTelNumber.requestFocus();
        //mEtTelNumber.setError("");
    }

    @Override
    public void showPwdInvalidMsg() {
        Log.v(TAG,"showPwdInvalidMsg()");

        mEtPwd.setError(getString(R.string.error_invalid_password));
        mEtPwd.requestFocus();
        //mEtPwd.setError("");
    }

    @Override
    public void showHeadIconNullDialog() {
        Log.v(TAG,"showHeadIconNullDialog()");

        showToast(getString(R.string.assure_not_pick_head_icon));
    }

    @Override
    public void showProgress() {
        mProgress = ProgressDialog.
                show(this,"",getString(R.string.register_progress_info));
    }

    @Override
    public void finishView() {
        finish();
    }

    private void init(){
        mPresenter.init();
        mHeadIconUrl = null;
    }

    private void initInjector(){
        mMarketComponent.inject(this);

        mPresenter.setView(this);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);

        if(result == RESULT_OK && data != null){
            switch (request){
                case PhotoPicker.REQUEST_CODE:
                    mHeadIconUrl = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    break;
            }
            Log.v(TAG,mHeadIconUrl.size() + " photos are picked: url " + mHeadIconUrl.get(0));

            if(mHeadIconUrl != null) {
                Picasso.with(this)
                       .load(Uri.fromFile(new File(mHeadIconUrl.get(0))))
                       .resize(512,512) // pixels
                       .centerCrop()
                       .into(mHeaderPicker);

            }
        }
    }
}
