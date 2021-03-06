package com.doandstevenson.lifecollage.ui.account;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doandstevenson.lifecollage.R;
import com.doandstevenson.lifecollage.ui.base.BaseDrawerActivity;
import com.doandstevenson.lifecollage.util.DialogBuilder;

public class AccountActivity extends BaseDrawerActivity implements AccountContract.MvpView, View.OnClickListener, DialogBuilder.AccountDialogListener {
    private EditText emailEditText;
    private Button saveEmailButton;
    private Button deleteButton;
    private AccountPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_account, mFrameLayout);

        setActionBarTitle("Account Settings");
        initDrawer();

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        saveEmailButton = (Button) findViewById(R.id.saveEmailButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        mPresenter = new AccountPresenter(this, this);
        mPresenter.getEmail();

        saveEmailButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        setNavViewCheckedItem(R.id.nav_account, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_account, true);
    }

    @Override
    public void onClick(View view) {
        if (view == saveEmailButton) {
            String newEmail = emailEditText.getText().toString();
            mPresenter.updateEmail(newEmail);
        }
        if (view == deleteButton) {
            DialogBuilder.DeleteAccountDialogFragment(this, this).show();
        }
    }

    @Override
    public void onDialogPositiveClick() {
        mPresenter.deleteUser();
    }

    @Override
    public void onDialogNegativeClick() { }

    @Override
    public void setEmail(String email) {
        emailEditText.setText(email);
    }

    @Override
    public void emailUpdated() {
        Toast.makeText(this, "Email Updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userDeleted() {
        Toast.makeText(this, "Your account has been deleted", Toast.LENGTH_LONG).show();
        logout();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
