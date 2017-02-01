package com.doandstevensen.lifecollage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static android.text.TextUtils.isEmpty;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        final SyncUser user = SyncUser.currentUser();
        if (user != null) {
            loginComplete();
        }

        createProgressDialog();
    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Authenticating user...");
        mProgressDialog.setCancelable(false);
    }

    private void loginComplete() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.logIn)
    public void attemptLogIn() {
        usernameView.setError(null);
        passwordView.setError(null);

        final String username = usernameView.getText().toString().trim();
        final String password = passwordView.getText().toString().trim();

        boolean cancel = false;
        if (isEmpty(username)) {
            usernameView.setError("This field is required");
            cancel = true;
        }
        if (isEmpty(password)) {
            passwordView.setError("This field is required");
            cancel = true;
        }

        if (!cancel) {
            mProgressDialog.show();
            SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, false), ThisApplication.AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    loginComplete();
                    mProgressDialog.dismiss();
                }
                @Override
                public void onError(ObjectServerError error) {
                    String errorMsg = error.toString();
                    Toast.makeText(LogInActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.signUp)
    public void signUp() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
