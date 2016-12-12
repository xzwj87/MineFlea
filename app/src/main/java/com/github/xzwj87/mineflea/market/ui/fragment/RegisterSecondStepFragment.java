package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.RegisterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.RegisterView;
import com.github.xzwj87.mineflea.utils.PicassoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jason on 11/20/16.
 */

public class RegisterSecondStepFragment extends BaseFragment{
    private static final String TAG = RegisterSecondStepFragment.class.getSimpleName();
    
    // share Presenter between different fragments
    private RegisterPresenterImpl mPresenter;
    private ProgressDialog mProgress;

    @BindView(R.id.civ_header_icon) CircleImageView mCivHeader;
    @BindView(R.id.et_nick_name) EditText mEtName;
    @BindView(R.id.et_email) EditText mEtEmail;
    @BindView(R.id.et_password) EditText mEtPwd;

    public RegisterSecondStepFragment(){}

    public static RegisterSecondStepFragment newInstance(){
        return new RegisterSecondStepFragment();
    }

    public void setPresenter(RegisterPresenterImpl presenter){
        mPresenter = presenter;
        mPresenter.setView(new RegisterViewImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedState){
        View root = inflater.inflate(R.layout.fragment_register_second_step,parent,false);

        ButterKnife.bind(this,root);

        android.app.ActionBar actionBar = getActivity().getActionBar();
        if(actionBar != null) {
            //getActivity().setTitle(R.string.previous_step);
            actionBar.setTitle(R.string.previous_step);
        }

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_register_done,menu);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                navigateToPreviousStep();
                return true;
            case R.id.menu_ok:
                // ok, register it
                mPresenter.setUserNickName(mEtName.getText().toString());
                mPresenter.setUserEmail(mEtEmail.getText().toString());
                mPresenter.setUserPwd(mEtPwd.getText().toString());
                if(mPresenter.validUserInfo()){
                    //mPresenter.signUpBySms();
                    // must do this
                    mPresenter.registerAndVerify();
                    // ToDo: make sure all information has been saved to server
                    //setResult(true);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + request);

        if(result == RESULT_OK && data != null){
            switch (request){
                case PhotoPicker.REQUEST_CODE:
                    ArrayList<String> imgList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if(imgList != null){
                        PicassoUtils.loadImage(mCivHeader,imgList.get(0));
                        // save it
                        mPresenter.setUserIconUrl(imgList.get(0));
                    }
                    break;
            }

        }
    }

    @OnClick({R.id.civ_header_icon})
    public void pickHeadIcon(){
        Log.v(TAG,"pickHeadIcon()");

        PhotoPicker.builder()
                .setPhotoCount(1)
                .setPreviewEnabled(true)
                .setShowCamera(true)
                .setShowGif(true)
                .start(getContext(),this,PhotoPicker.REQUEST_CODE);

    }



    private class RegisterViewImpl extends RegisterView {

        public void onRegisterComplete(boolean success){
            Log.v(TAG,"onRegisterComplete()");
            // now we back to parent
            setResult(success);
        }

        @Override
        public void showNameInvalidMsg() {
            Log.v(TAG, "showNameInvalidMsg()");

            mEtName.setError(getString(R.string.error_invalid_user_name));
        }

        @Override
        public void showEmailInvalidMsg() {
            Log.v(TAG, "showEmailInvalidMsg()");

            mEtEmail.setError(getString(R.string.error_invalid_email));
        }

        @Override
        public void showPwdInvalidMsg() {
            Log.v(TAG, "showPwdInvalidMsg()");

            mEtPwd.setError(getString(R.string.error_invalid_password));
        }

        @Override
        public void showHeadIconNullMsg() {
            Log.v(TAG, "showHeadIconNullMsg()");

            showToast(getString(R.string.assure_not_pick_head_icon));
        }

        @Override
        public void finishView() {
            getActivity().finish();
        }


        @Override
        public void showProgress(boolean isShowed){
            if(isShowed){
                mProgress = ProgressDialog.show(getContext(),"",
                        getString(R.string.register_progress_info));
            }else if(mProgress.isShowing()){
                mProgress.dismiss();
            }
        }

        @Override
        public void showNoNetConnectionMsg(){
            Log.v(TAG,"showNoNetConnectionMsg()");
            showToast(getString(R.string.hint_no_network_connection));
        }
    }

    private void setResult(boolean success){
        Bundle bundle = new Bundle();
        bundle.putString(UserInfo.USER_TEL,mPresenter.getTelNumber());
        if(success) {
            bundle.putString(UserInfo.USER_HEAD_ICON, mPresenter.getUserIconUrl());
            bundle.putString(UserInfo.UER_EMAIL, mPresenter.getUserEmail());
            bundle.putString(UserInfo.USER_NAME, mPresenter.getUserEmail());
            bundle.putString(UserInfo.USER_NICK_NAME, mPresenter.getUserNickName());
            bundle.putString(UserInfo.USER_PWD, mPresenter.getUserPwd());
        }

        Intent intent = new Intent();
        intent.putExtras(bundle);

        getActivity().setResult(RESULT_OK, intent);

        getActivity().finish();
    }

    private void navigateToPreviousStep(){
        FragmentManager fragmentMgr = getActivity().getSupportFragmentManager();
        RegisterFragment fragment = (RegisterFragment)fragmentMgr.findFragmentByTag(RegisterFragment.TAG);
        if(fragment == null){
            fragment = RegisterFragment.newInstance();
        }

        fragmentMgr.beginTransaction()
                .replace(R.id.fragment_container,fragment,RegisterFragment.TAG)
                .commit();
    }
}
