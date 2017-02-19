package com.doandstevensen.lifecollage.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.signin.LogInActivity;

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

        mPresenter = new SignUpPresenter(this, this, new DataManager(this));
    }

    @OnClick(R.id.signUp)
    public void attemptSignUp() {
        String firstName = firstNameView.getText().toString().trim();
        String lastName = lastNameView.getText().toString().trim();
        String email = emailView.getText().toString().trim();
        String username = usernameView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String passwordConfirmation = passwordConfirmationView.getText().toString().trim();

        boolean firstNameNotEmpty = isInputEmpty(firstNameView, firstName);
        boolean lastNameNotEmpty = isInputEmpty(lastNameView, lastName);
        boolean validEmail = validateEmail(emailView, email);
        boolean usernameNotEmpty = isInputEmpty(usernameView, username);
        boolean passwordLength = checkPasswordLength(passwordView, password);
        boolean validPassword = validatePassword(passwordConfirmationView, password, passwordConfirmation);

        if (firstNameNotEmpty && lastNameNotEmpty && validEmail && usernameNotEmpty && passwordLength && validPassword) {
            mPresenter.signUp(firstName, lastName, email, username, password);
        }
    }

    private boolean checkPasswordLength(EditText passwordView, String password) {
        if (isEmpty(password) || password.length() < 6) {
            passwordView.setError("Password must be at least 6 characters long");
            return false;
        } else {
            return true;
        }
    }

    private boolean isInputEmpty(EditText view, String input) {
        if (isEmpty(input)) {
            view.setError("This field is required");
        }
        return !isEmpty(input);
    }

    private boolean validateEmail(EditText view, String email) {
        if (isEmpty(email)) {
            view.setError("This field is required");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.setError("Please enter a valid email address");
        }
        return (!isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean validatePassword(EditText view, String password, String confirmation) {
        boolean passwordsMatch = password.equals(confirmation);
        if (!passwordsMatch) {
            view.setError("Passwords do not match");
        }
        return passwordsMatch;
    }

    public void showSignUpError(String error) {
        if (error.contains("username")) {
            usernameView.setError("Username already exists");
        }
        if (error.contains("email")) {
            emailView.setError("Email already exists");
        }
    }

    @Override
    public void navigateToCollageList() {
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
