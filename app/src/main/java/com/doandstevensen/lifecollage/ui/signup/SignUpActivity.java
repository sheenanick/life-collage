package com.doandstevensen.lifecollage.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

public class SignUpActivity extends BaseActivity implements SignUpContract.MvpView {
    @BindView(R.id.username)
    EditText usernameView;
    @BindView(R.id.password)
    EditText passwordView;
    @BindView(R.id.confirmPassword)
    EditText passwordConfirmationView;

    private SignUpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mPresenter = new SignUpPresenter(this);
    }

    @OnClick(R.id.signUp)
    public void attemptSignUp() {
        usernameView.setError(null);
        passwordView.setError(null);
        passwordConfirmationView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirmation = passwordConfirmationView.getText().toString();

        boolean validated = true;
        if (isEmpty(username)) {
            usernameView.setError("This field is required");
            validated = false;
        }
        if (isEmpty(password)) {
            passwordView.setError("This field is required");
            validated = false;
        }
        if (isEmpty(passwordConfirmation)) {
            passwordConfirmationView.setError("This field is required");
            validated = false;
        }
        if (!password.equals(passwordConfirmation)) {
            passwordConfirmationView.setError("Passwords do not match");
            validated = false;
        }
        if (validated) {
            mPresenter.signUp(username, password);
        }
    }

    public void showSignUpError(String error) {
        usernameView.setError(error);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
