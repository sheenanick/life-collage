package com.doandstevensen.lifecollage.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

public class AccountActivity extends BaseDrawerActivity implements AccountContract.MvpView, View.OnClickListener, DeleteAccountDialogFragment.DeleteAccountDialogListener {
    private EditText emailEditText;
    private Button saveEmailButton;
    private Button deleteButton;
    private AccountPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_account, mFrameLayout);

        initToolbar();
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        saveEmailButton = (Button) findViewById(R.id.saveEmailButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        mPresenter = new AccountPresenter(this, this);
        mPresenter.setPrivateService();

        setNavViewCheckedItem(R.id.nav_account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_account);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Account Settings");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == saveEmailButton) {
            String newEmail = emailEditText.getText().toString();
            mPresenter.updateEmail(newEmail);
        }
        if (view == deleteButton) {
            launchDeleteAccountAlertDialog();
        }
    }

    @Override
    public void emailUpdated() {
        Toast.makeText(this, "Email Updated!", Toast.LENGTH_SHORT).show();
    }

    private void launchDeleteAccountAlertDialog() {
        DeleteAccountDialogFragment dialogFragment = new DeleteAccountDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "deleteAccount");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mPresenter.deleteUser();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) { }

    @Override
    public void userDeleted() {
        Toast.makeText(this, "Your account has been deleted", Toast.LENGTH_LONG).show();
        navigateToMain();
    }

    @Override
    public void navigateToMain() {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.clearData(getBaseContext());

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
