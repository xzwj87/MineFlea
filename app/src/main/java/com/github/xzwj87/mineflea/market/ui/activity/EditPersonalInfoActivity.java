package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.presenter.EditPersonalInfoPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;
import com.github.xzwj87.mineflea.market.ui.dialog.UserInfoEditDialog;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.github.xzwj87.mineflea.utils.ThemeColorUtils;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by jason on 11/2/16.
 */

public class EditPersonalInfoActivity extends BaseActivity
        implements EditPersonalInfoView,UserInfoEditDialog.DialogButtonListener{
    private static final String TAG = EditPersonalInfoActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 0x3001;
    private static final String KEY_ITEM_LAYOUT_ID = "id";

    @BindView(R.id.head_icon) RelativeLayout mRlHeadIcon;
    @BindView(R.id.nick_name) LinearLayout mRlNickName;
    @BindView(R.id.tel_number) LinearLayout mRlTelNumber;
    @BindView(R.id.email) LinearLayout mRlEmail;
    @BindView(R.id.introduction) LinearLayout mRlIntro;

    @BindView(R.id.civ_head_icon) CircleImageView mCivHeadIcon;
    @BindView(R.id.tv_nick_name) TextView mTvNickName;
    @BindView(R.id.tv_email) TextView mTvEmail;
    @BindView(R.id.tv_tel) TextView mTvTel;
    @BindView(R.id.tv_introduction) TextView mTvIntro;

    @Inject EditPersonalInfoPresenterImpl mPresenter;

    private static String sCameraImgPath = AppGlobals.FILE_DIR_PARENT + "/camera/head_icon.jpg";


    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setContentView(R.layout.activity_edit_user_info);
        FrameLayout itemContainer = (FrameLayout)findViewById(R.id.item_container);

        if(itemContainer != null) {
            ButterKnife.bind(this, itemContainer);
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
        }

        getComponent().inject(this);

        ThemeColorUtils.changeThemeColor(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        init();
    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        if(result == RESULT_OK){
            switch (request){
                case PhotoPicker.REQUEST_CODE:
                    if(data != null) {
                        ArrayList<String> urlList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        mPresenter.setHeadIcon(urlList.get(0));
                        PicassoUtils.loadImage(this,mCivHeadIcon,urlList.get(0));
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    File file = new File(sCameraImgPath);
                    if(file.exists()){
                        mPresenter.setHeadIcon(sCameraImgPath);
                        PicassoUtils.loadImage(this,mCivHeadIcon,sCameraImgPath);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finishView();
                break;
            default:
                break;
        }

        return false;
    }

    @OnClick({R.id.head_icon,R.id.email,R.id.tel_number,
            R.id.nick_name,R.id.introduction})
    public void showDialog(View view){
        int id = view.getId();
        String title;

        switch (id){
            case R.id.head_icon:
                showGetHeadIconDialog();
                break;
            case R.id.nick_name:
                title = getString(R.string.edit_nick_name);
                showEditDialog(id,title,mTvNickName.getText().toString());
                break;
            case R.id.email:
                title = getString(R.string.edit_email);
                showEditDialog(id,title,mTvEmail.getText().toString());
                break;
            case R.id.tel_number:
                title = getString(R.string.edit_tel_number);
                showEditDialog(id,title,mTvTel.getText().toString());
                break;
            case R.id.introduction:
                title = getString(R.string.edit_intro);
                showEditDialog(id,title,mTvIntro.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void updateHeadIcon(String url) {
        Log.v(TAG,"updateHeadIcon(): url = " + url);

        PicassoUtils.loadImage(this,mCivHeadIcon,url);
    }

    @Override
    public void updateNickName(String nickName) {
        Log.v(TAG,"updateNickName(): nick name = " + nickName);
        mTvNickName.setText(nickName);
    }

    @Override
    public void updateEmail(String email) {
        if(mPresenter.isEmailVerified()) {
            String verified = getString(R.string.already_verified);
            mTvEmail.setText(email + "(" + verified + ")");
        }else{
            String noVerified = getString(R.string.not_verified);
            mTvEmail.setText(email+ "(" + noVerified + ")");
        }
    }

    @Override
    public void updateTelNumber(String tel) {
        if(mPresenter.isTelVerified()) {
            String verified = getString(R.string.already_verified);
            mTvTel.setText(tel + "(" + verified + ")");
        }else{
            String noVerified = getString(R.string.not_verified);
            mTvTel.setText(tel + "(" + noVerified + ")");
        }
    }

    @Override
    public void updateIntro(String intro) {
        mTvIntro.setText(intro);
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {
        int id = dialog.getArguments().getInt(KEY_ITEM_LAYOUT_ID);

        EditText et = (EditText)dialog.getDialog().findViewById(R.id.et_input);
        String input = et.getText().toString();

        Log.v(TAG,"onPositiveClick(): input = " + input);
        if(TextUtils.isEmpty(input) && id != R.id.introduction){
            et.setError(getString(R.string.error_invalid_input));
            return;
        }

        switch (id){
            case R.id.nick_name:
                mPresenter.setNickName(input);
                mTvNickName.setText(input);
                break;
            case R.id.email:
                mPresenter.setEmail(input);
                mTvEmail.setText(input);
                break;
            case R.id.tel_number:
                mPresenter.setTelNumber(input);
                mTvTel.setText(input);
                break;
            case R.id.introduction:
                mPresenter.setIntro(input);
                mTvIntro.setText(input);
                break;
            default:
                break;
        }

        dialog.dismiss();
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {
        Log.v(TAG,"onNegativeClick()");

        dialog.dismiss();
    }

    private void showGetHeadIconDialog(){
        Log.v(TAG,"showGetHeadIconDialog()");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(R.array.user_head_icon_capture, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    startCamera();
                }else{
                    pickImageFromFile();
                }

                dialog.dismiss();
            }
        })
                .create()
                .show();
    }

    private void showEditDialog(int id,String title,String content){
        if(TextUtils.isEmpty(title)) return;

        UserInfoEditDialog dialog = UserInfoEditDialog.newInstance(
                id,title,content);

        dialog.show(getSupportFragmentManager(),"UserInfoEditDialog");
    }

    private void startCamera(){
        File file = new File(sCameraImgPath);
        if(!file.exists()){
            File parent = file.getParentFile();
            parent.mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImageFromFile(){
        PhotoPicker.builder()
                .setPreviewEnabled(true)
                .setPhotoCount(1)
                .setShowGif(false)
                .start(this,PhotoPicker.REQUEST_CODE);
    }

    private void init(){
        mPresenter.init();
        mPresenter.setView(this);
    }
}
