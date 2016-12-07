package com.github.xzwj87.mineflea.market.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserInfo;

/**
 * Created by jason on 11/3/16.
 */

public class UserInfoEditDialog extends DialogFragment{

    private DialogButtonListener mListener;

    public UserInfoEditDialog(){}

    public static UserInfoEditDialog newInstance(int id,String title,String content){
        UserInfoEditDialog fragment = new UserInfoEditDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        bundle.putString("title",title);
        bundle.putString("content",content);
        fragment.setArguments(bundle);

        return fragment;
    }

    public interface DialogButtonListener{
        void onPositiveClick(DialogFragment dialog);
        void onNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try {
            mListener = (DialogButtonListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException("activity must implement click listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedState){
        View root = inflater.inflate(R.layout.dialog_input_text,container,false);

        return root;
    }


    @Override
    public void onResume(){
        super.onResume();

        String content = getArguments().getString("content");
        Dialog dialog = getDialog();
        if(dialog != null) {
            EditText editText = (EditText) dialog.findViewById(R.id.et_input);
            editText.setText(content);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedState){

        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);

        Dialog dialog = builder.setView(R.layout.dialog_input_text)
               .setTitle(title)
               .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       mListener.onPositiveClick(UserInfoEditDialog.this);
                   }
               })
               .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       mListener.onNegativeClick(UserInfoEditDialog.this);
                   }
               }).create();

        return dialog;
    }
}
