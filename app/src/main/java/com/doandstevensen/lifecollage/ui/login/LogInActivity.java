package com.doandstevensen.lifecollage.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

public class LogInActivity extends BaseActivity implements LogInContract.MvpView {
    @BindView(R.id.email) EditText emailView;
    @BindView(R.id.password) EditText passwordView;

    private LogInPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        mPresenter = new LogInPresenter(this, getBaseContext());
    }

    @OnClick(R.id.logIn)
    public void attemptLogIn() {
        emailView.setError(null);
        passwordView.setError(null);

        final String email = emailView.getText().toString().trim();
        final String password = passwordView.getText().toString().trim();

        boolean validEmail = validateInput(emailView, email);
        boolean validPassword = validateInput(passwordView, password);

        if (validEmail && validPassword) {
            mPresenter.logIn(email, password);
        }
    }

    public boolean validateInput(EditText view, String input) {
        if (isEmpty(input)) {
            view.setError("This field is required");
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.signUp)
    public void signUp() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToCollageList() {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
