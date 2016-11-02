package com.github.xzwj87.mineflea.market.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.presenter.EditPersonalInfoPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.EditPersonalInfoView;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by jason on 11/2/16.
 */

public class EditPersonalInfoActivity extends BaseActivity implements EditPersonalInfoView{
    private static final String TAG = EditPersonalInfoActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 0x3001;

    private FrameLayout mItemContainer;

    @BindView(R.id.head_icon) RelativeLayout mRlHeadIcon;
    @BindView(R.id.nick_name) RelativeLayout mRlNickName;
    @BindView(R.id.tel_number) RelativeLayout mRlTelNumber;
    @BindView(R.id.email) RelativeLayout mRlEmail;
    @BindView(R.id.introduction) RelativeLayout mRlIntro;

    @BindView(R.id.civ_head_icon) CircleImageView mCivHeadIcon;
    @BindView(R.id.tv_user_nick_name) TextView mTvNickName;
    @BindView(R.id.tv_user_email) TextView mTvEmail;
    @BindView(R.id.tv_user_tel) TextView mTvTel;
    @BindView(R.id.tv_introduction) TextView mTvIntro;

    @Inject EditPersonalInfoPresenterImpl mPresenter;

    private static String sCameraImgPath = AppGlobals.FILE_DIR_PARENT + "/camera/head_icon.jpg";


    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        setContentView(R.layout.activity_edit_user_info);
        mItemContainer = (FrameLayout)findViewById(R.id.item_container);

        ButterKnife.bind(this,mItemContainer);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        if(result == RESULT_OK){
            switch (request){
                case PhotoPicker.REQUEST_CODE:
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    break;
            }
        }
    }

    @OnClick({R.id.head_icon,R.id.nick_name,R.id.email,
            R.id.tel_number,R.id.introduction,})
    public void showDialog(View view){
        int id = view.getId();

        switch (id){
            case R.id.head_icon:
                break;
            case R.id.nick_name:
                break;
            case R.id.email:
                break;
            case R.id.tel_number:
                break;
            case R.id.introduction:
                break;
            default:
                break;
        }
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

    private void showEditNickNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog dialog = builder.create();

        final EditText inputText = (EditText)dialog.findViewById(R.id.et_input);

         builder.setTitle(R.string.edit_nick_name)
               .setView(R.layout.dialog_input_text)
               .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       String nickname = inputText.getText().toString();
                       mPresenter.setNickName(nickname);
                   }
               })
               .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

        dialog.show();

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

        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    private void pickImageFromFile(){
        PhotoPicker.builder()
                   .setPreviewEnabled(true)
                   .setPhotoCount(1)
                   .setShowGif(false)
                   .start(this,PhotoPicker.REQUEST_CODE);
    }

    @Override
    public void updateHeadIcon(String url) {

    }

    @Override
    public void updateNickName(String nickName) {

    }

    @Override
    public void updateEmail(String email) {

    }

    @Override
    public void updateTelNumber(String tel) {

    }

    @Override
    public void updateIntro(String intro) {

    }

    @Override
    public void finishView() {

    }
}
