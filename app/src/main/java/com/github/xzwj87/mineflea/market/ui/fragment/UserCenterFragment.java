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
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.UserCenterPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.UserCenterView;
import com.github.xzwj87.mineflea.market.ui.activity.LoginActivity;
import com.github.xzwj87.mineflea.market.ui.activity.RegisterActivity;
import com.github.xzwj87.mineflea.market.ui.activity.UserDetailActivity;
import com.github.xzwj87.mineflea.market.ui.activity.UserGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.settings.SettingsActivity;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.github.xzwj87.mineflea.utils.SharePrefsHelper;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jason on 10/9/16.
 */

public class UserCenterFragment extends BaseFragment implements UserCenterView{
    public static final String TAG = UserCenterFragment.class.getSimpleName();

    public static final int REQUEST_LOGIN = 1;
    public static final int REQUEST_REGISTER = 2;

    @BindView(R.id.civ_user_header) CircleImageView mCivHeader;
    @BindView(R.id.header_container) LinearLayout mHeaderLayout;
    @BindView(R.id.tv_user_nick_name) TextView mTvNickName;
    @BindView(R.id.tv_user_email) TextView mTvUserEmail;
    @BindView(R.id.tv_published_goods) TextView mTvPublishedGoods;
    @BindView(R.id.tv_favorite_goods) TextView mTvFavorGoods;

    @Inject UserCenterPresenterImpl mPresenter;

    public UserCenterFragment(){
    }

    public static UserCenterFragment newInstance(){

        return new UserCenterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate){
        Log.v(TAG,"onCreateView()");
        View root = inflater.inflate(R.layout.fragment_user_center,container,false);

        ButterKnife.bind(this,root);

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();

        initView();
        init();
    }

    @OnClick(R.id.header_container)
    public void login(){
        Log.v(TAG,"login()");

        boolean isLogin = SharePrefsHelper.getInstance(getActivity()).getLoginState();
        if(isLogin){
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            intent.putExtra(UserInfo.USER_ID,mPresenter.getUserId());
            intent.putExtra(UserInfo.CURRENT_USER,true);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER);
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
            case REQUEST_REGISTER:
                if(result == RESULT_OK && data != null) {

                    String nickName = data.getStringExtra(UserInfo.USER_NICK_NAME);
                    //String name = data.getStringExtra(UserInfo.USER_NAME);
                    String email = data.getStringExtra(UserInfo.UER_EMAIL);
                    String iconUrl = data.getStringExtra(UserInfo.USER_HEAD_ICON);
                    PicassoUtils.loadImage(getActivity(),mCivHeader,iconUrl);
                    mTvNickName.setText(nickName);
                    mTvUserEmail.setVisibility(View.VISIBLE);
                    mTvUserEmail.setText(email);
                }
                break;
            default:
                break;
        }
    }

    private void initView(){
        Log.v(TAG,"initView()");

        boolean login = SharePrefsHelper.getInstance(getContext()).getLoginState();
        if(login) {
            Log.v(TAG,"initView(): login!!!");
            mTvUserEmail.setVisibility(View.VISIBLE);
        }else{
            mTvUserEmail.setVisibility(View.GONE);
            mTvNickName.setText(getString(R.string.logout));
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCivHeader.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_account_circle_grey_72dp, null));
        }else{
            mCivHeader.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_account_circle_grey_72dp));
        }
    }

    @OnClick({R.id.tv_favorite_goods,R.id.tv_published_goods,R.id.tv_settings})
    public void doAction(TextView tv){
        int id = tv.getId();
        boolean login = SharePrefsHelper.getInstance(getContext()).getLoginState();
        if(!login && id != R.id.tv_settings){
            showNeedLoginHint();
            return;
        }

        Intent intent = null;
        switch (id){
            case R.id.tv_favorite_goods:
                intent = new Intent(getActivity(),UserGoodsActivity.class);
                intent.putExtra("FragmentTag",UserFavoritesFragment.TAG);
                intent.putExtra("UserId",mPresenter.getUserId());
                startActivity(intent);
                break;
            case R.id.tv_published_goods:
                intent = new Intent(getActivity(),UserGoodsActivity.class);
                intent.putExtra("FragmentTag",UserPublishedGoodsFragment.TAG);
                intent.putExtra("UserId",mPresenter.getUserId());
                startActivity(intent);
                break;
            case R.id.tv_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void init(){
        if(mPresenter != null) {
            mPresenter.init();
            mPresenter.setView(this);
            mPresenter.loadUserInfo();
        }
    }

    @Override
    public void updateUserNickName(String nickName) {
        mTvNickName.setText(nickName);
    }

    @Override
    public void updateUserEmail(String email) {
        mTvUserEmail.setText(email);
    }

    @Override
    public void updateHeadIcon(String iconUrl) {
        if(URLUtil.isNetworkUrl(iconUrl)){
            Picasso.with(getActivity())
                    .load(iconUrl)
                    .resize(512, 512)
                    .centerCrop()
                    .into(mCivHeader);
        }else{
            Picasso.with(getActivity())
                    .load(Uri.fromFile(new File(iconUrl)))
                    .resize(512, 512)
                    .centerCrop()
                    .into(mCivHeader);
        }
    }

    @Override
    public void showNeedLoginHint() {
        showToast(getString(R.string.need_to_login));
    }

    @Override
    public void finishView() {
        getActivity().finish();
    }
}
