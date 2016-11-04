package com.github.xzwj87.mineflea.market.ui.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.presenter.EditPersonalInfoPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;
import com.github.xzwj87.mineflea.market.ui.dialog.UserInfoEditDialog;
import com.squareup.picasso.Picasso;

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
    private static final String KEY_ITEM_LAYOUT_ID = "item";

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
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    File file = new File(sCameraImgPath);
                    if(file.exists()){
                        mPresenter.setHeadIcon(sCameraImgPath);
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

    @OnClick({R.id.head_icon,R.id.nick_name,R.id.introduction})
    public void showDialog(View view){
        int id = view.getId();
        String title;

        switch (id){
            case R.id.head_icon:
                showGetHeadIconDialog();
                break;
            case R.id.nick_name:
                title = getString(R.string.edit_nick_name);
                showEditDialog(title,id);
                break;
            case R.id.email:
                title = getString(R.string.edit_email);
                showEditDialog(title,id);
                break;
            case R.id.tel_number:
                title = getString(R.string.edit_tel_number);
                showEditDialog(title,id);
                break;
            case R.id.introduction:
                title = getString(R.string.edit_intro);
                showEditDialog(title,id);
                break;
            default:
                break;
        }
    }

    @Override
    public void updateHeadIcon(String url) {
        Log.v(TAG,"updateHeadIcon(): url = " + url);
        if(URLUtil.isNetworkUrl(url)) {
            Picasso.with(this)
                    .load(url)
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(mCivHeadIcon);
        }else{
            Picasso.with(this)
                    .load(Uri.fromFile(new File(url)))
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(mCivHeadIcon);
        }
    }

    @Override
    public void updateNickName(String nickName) {
        Log.v(TAG,"updateNickName(): nick name = " + nickName);
        mTvNickName.setText(nickName);
    }

    @Override
    public void updateEmail(String email) {
        mTvEmail.setText(email);
    }

    @Override
    public void updateTelNumber(String tel) {
        mTvTel.setText(tel);
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
        Log.v(TAG,"onPositiveClick()");
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
                break;
            case R.id.email:
                mPresenter.setEmail(input);
                break;
            case R.id.tel_number:
                mPresenter.setTelNumber(input);
                break;
            case R.id.introduction:
                mPresenter.setIntro(input);
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

    private void showEditDialog(String title,int id){
        if(TextUtils.isEmpty(title)) return;

        UserInfoEditDialog dialog = UserInfoEditDialog.newInstance(title,id);

        dialog.show(getFragmentManager(),"UserInfoEditDialog");
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
