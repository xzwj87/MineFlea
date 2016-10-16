package com.github.xzwj87.mineflea.market.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.model.UserInfo;
import com.github.xzwj87.mineflea.market.presenter.LoginPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.LoginView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import static android.Manifest.permission.READ_CONTACTS;
import static com.github.xzwj87.mineflea.utils.UserInfoUtils.isEmailValid;


public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor>,
        LoginView{
    public static final String TAG = LoginActivity.class.getSimpleName();
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_USER_REGISTER = 1;

    @BindView(R.id.atv_email) AutoCompleteTextView mEmailView;
    @BindView(R.id.et_password) EditText mPasswordView;
    @BindView(R.id.progress_login) View mProgressView;
    @BindView(R.id.form_login) View mLoginFormView;
    @BindView(R.id.btn_register) Button mBtnRegister;
    @BindView(R.id.btn_sign_in) Button mBtnEmailSignIn;

    @Inject LoginPresenterImpl mPresenter;
    private String mHeadIconUrl;
    private String mNickName;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    @OnEditorAction(R.id.et_password)
    public boolean onPwdInputComplete(TextView textView,int id,KeyEvent keyEvent){
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            tryLogin();
            return true;
        }

        return false;
    }

    @OnClick({R.id.btn_register,R.id.btn_sign_in})
    public void onButtonClick(Button button){
        switch (button.getId()){
            case R.id.btn_sign_in:
                tryLogin();
                break;
            case R.id.btn_register:
                tryRegister();
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int request,int result,Intent data){
        Log.v(TAG,"onActivityResult(): result = " + result);

            switch (request) {
                case REQUEST_USER_REGISTER:
                    if(result == RESULT_OK && data != null) {
                        setResult(RESULT_OK, data);
                    }
                    break;
            }
            finish();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void tryLogin() {
        Log.v(TAG,"tryLogin()");
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mPresenter.setUserAccount(email);
        mPresenter.setUserPwd(password);

        // check user login info
        if(mPresenter.validLoginInfo()){
            mPresenter.login();
            showProgress(true);
        }else{
            showProgress(false);
        }
    }

    private void tryRegister(){
        Log.v(TAG,"tryRegister()");

        // start another activity
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivityForResult(intent,REQUEST_USER_REGISTER);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onLoginSuccess() {
        Log.v(TAG,"onLoginSuccess()");

        showProgress(false);

        Intent intent = new Intent();
        intent.putExtra(UserInfo.IS_LOGIN,true);
        intent.putExtra(UserInfo.USER_NICK_NAME,mNickName);
        intent.putExtra(UserInfo.UER_EMAIL,mEmail);
        intent.putExtra(UserInfo.USER_HEAD_ICON,mHeadIconUrl);

        setResult(RESULT_OK,intent);

        finishView();
    }

    @Override
    public void onLoginFail() {
        Log.v(TAG,"onLoginFail()");

        showToast(getString(R.string.prompt_login_incorrect_email_password));
    }

    @Override
    public void showAccountInvalidMsg() {
        // Check for a valid email address.
        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
        }
        mEmailView.requestFocus();
    }

    @Override
    public void showPwdInvalidMsg() {
        mPasswordView.setError(getString(R.string.error_invalid_email));
        mPasswordView.requestFocus();
    }

    @Override
    public void updateUserHeadIcon(String url) {
        mHeadIconUrl = url;
    }

    @Override
    public void updateUserNickName(String nickName) {
        mNickName = nickName;
    }

    @Override
    public void updateUserEmail(String email) {
        mEmail = email;
    }

    @Override
    public void finishView() {
        finish();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void init() {
        initInjector();

        mPresenter.init();
    }

    private void initInjector(){
        DaggerMarketComponent.builder()
                             .appComponent(getAppComponent())
                             .activityModule(getActivityModule())
                             .build()
                             .inject(this);
        mPresenter.setView(this);
    }
}

