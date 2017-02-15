package com.doandstevensen.lifecollage.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.widget.EditText;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BaseActivity implements AccountContract.MvpView, DeleteAccountDialogFragment.DeleteAccountDialogListener {
    @BindView(R.id.emailEditText)
    EditText emailEditText;

    private AccountPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        initToolbar();

        mPresenter = new AccountPresenter(this, this);
        mPresenter.setPrivateService();
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Account Settings");
        }
    }

    @OnClick(R.id.saveEmailButton)
    public void updateEmail() {
        String newEmail = emailEditText.getText().toString();
        mPresenter.updateEmail(newEmail);
    }

    @Override
    public void emailUpdated() {
        Toast.makeText(this, "Email Updated!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.deleteButton)
    public void deleteUser() {
        launchDeleteAccountAlertDialog();
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
