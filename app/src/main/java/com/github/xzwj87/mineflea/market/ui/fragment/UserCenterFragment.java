package com.github.xzwj87.mineflea.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.ui.activity.LoginActivity;
import com.github.xzwj87.mineflea.market.ui.activity.UserDetailActivity;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jason on 10/9/16.
 */

public class UserCenterFragment extends BaseFragment{
    public static final String TAG = UserCenterFragment.class.getSimpleName();

    public static final int REQUEST_LOGIN = 1;

    @BindView(R.id.civ_user_header) CircleImageView mCivHeader;
    @BindView(R.id.header_container) LinearLayout mHeaderLayout;
    @BindView(R.id.tv_user_nick_name) TextView mTvNickName;
    @BindView(R.id.tv_user_email) TextView mTvUserEmail;


    public UserCenterFragment(){
    }

    public static UserCenterFragment newInstance(){
        UserCenterFragment fragment = new UserCenterFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate){
        View root = inflater.inflate(R.layout.fragment_user_center,container,false);

        ButterKnife.bind(this,root);

        initView();

        return root;
    }

    @OnClick(R.id.header_container)
    public void login(){
        Log.v(TAG,"login()");

        boolean isLogin = UserPrefsUtil.getBoolean(UserInfo.IS_LOGIN,false);

        if(isLogin){
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    @OnClick(R.id.civ_user_header)
    public void pickUserHeadIcon(){
        Log.v(TAG,"pickUserHeadIcon()");

    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);

        switch (request){
            case REQUEST_LOGIN:
                if(result == Activity.RESULT_OK && data != null) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setLoginState(true);
                    userInfo.setNickName(data.getStringExtra(UserInfo.USER_NICK_NAME));
                    userInfo.setUserName(data.getStringExtra(UserInfo.USER_NAME));
                    userInfo.setHeadIconUrl(data.getStringExtra(UserInfo.USER_HEAD_ICON));
                    userInfo.setUerEmail(data.getStringExtra(UserInfo.UER_EMAIL));
                    userInfo.setUserPwd(data.getStringExtra(UserInfo.USER_PWD));
                    userInfo.setUserTelNumber(data.getStringExtra(UserInfo.USER_TEL));

                    if(!TextUtils.isEmpty(userInfo.getHeadIconUrl())) {
                        Picasso.with(getActivity())
                                .load(Uri.fromFile(new File(userInfo.getHeadIconUrl())))
                                .resize(512,512)
                                .centerCrop()
                                .into(mCivHeader);
                    }
                    mTvNickName.setText(userInfo.getNickName());
                    mTvUserEmail.setVisibility(View.VISIBLE);
                    mTvUserEmail.setText(userInfo.getUserEmail());
                    // save user data
                    UserPrefsUtil.saveUserLoginInfo(userInfo);
                    Log.v(TAG,"onActivityResult(): user detail = " + userInfo);
                }
                break;
            default:
                break;
        }
    }

    private void initView(){
        Log.v(TAG,"initView()");
        if(UserPrefsUtil.isLogin()) {
            mTvNickName.setText(UserPrefsUtil.getString(UserInfo.USER_NICK_NAME, ""));
            mTvUserEmail.setText(UserPrefsUtil.getString(UserInfo.UER_EMAIL,""));

            String headIcon = UserPrefsUtil.getString(UserInfo.USER_HEAD_ICON,"");
            if(!TextUtils.isEmpty(headIcon)) {
                Picasso.with(getActivity())
                        .load(Uri.fromFile(new File(headIcon)))
                        .resize(512,512)
                        .centerCrop()
                        .into(mCivHeader);
            }else{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mCivHeader.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_account_circle_grey_72dp, null));
                }else{
                    mCivHeader.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_account_circle_grey_72dp));
                }
            }
        }
    }
}
