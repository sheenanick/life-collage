package com.doandstevensen.lifecollage.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.login.LogInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

public class SignUpActivity extends BaseActivity implements SignUpContract.MvpView {
    @BindView(R.id.firstName)
    EditText firstNameView;
    @BindView(R.id.lastName)
    EditText lastNameView;
    @BindView(R.id.username)
    EditText usernameView;
    @BindView(R.id.email)
    EditText emailView;
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

        mPresenter = new SignUpPresenter(this, this);
    }

    @OnClick(R.id.signUp)
    public void attemptSignUp() {
        usernameView.setError(null);
        passwordView.setError(null);
        passwordConfirmationView.setError(null);

        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String email = emailView.getText().toString();
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
            mPresenter.signUp(firstName, lastName, email, username, password);
        }
    }

    public void showSignUpError(String error) {
        usernameView.setError(error);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.logIn)
    public void navigateToLogIn() {
        Intent logInIntent = new Intent(getBaseContext(), LogInActivity.class);
        startActivity(logInIntent);
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
