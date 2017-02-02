package com.doandstevensen.lifecollage.ui.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ThisApplication;
import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static android.text.TextUtils.isEmpty;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.confirmPassword) EditText passwordConfirmationView;

    private Realm realm;
    private String username;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        createProgressDialog();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Authenticating user...");
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.signUp)
    public void attemptSignUp() {
        usernameView.setError(null);
        passwordView.setError(null);
        passwordConfirmationView.setError(null);

        username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirmation = passwordConfirmationView.getText().toString();

        boolean cancel = false;
        if (isEmpty(username)) {
            usernameView.setError("This field is required");
            cancel = true;
        }
        if (isEmpty(password)) {
            passwordView.setError("This field is required");
            cancel = true;
        }
        if (isEmpty(passwordConfirmation)) {
            passwordConfirmationView.setError("This field is required");
            cancel = true;
        }
        if (!password.equals(passwordConfirmation)) {
            passwordConfirmationView.setError("Passwords do not match");
            cancel = true;
        }

        if (!cancel) {
            progressDialog.show();
            SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, true), ThisApplication.AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    progressDialog.dismiss();
                    signUpComplete(user);
                }
                @Override
                public void onError(ObjectServerError error) {
                    progressDialog.dismiss();
                    String errorMsg = error.toString();
                    Log.d("error", errorMsg);
                    usernameView.setError("Username already exists");
                }
            });
        }
    }

    private void signUpComplete(SyncUser user) {
        createUserObject(user);
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createUserObject(final SyncUser currentUser) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm){
                User user = realm.createObject(User.class, currentUser.getIdentity());
                user.setUsername(username);

                Collage collage = new Collage();
                collage.setName("Test Collage");

                Picture picture = new Picture();
                collage.addPicture(picture);
                collage.addPicture(picture);
                collage.addPicture(picture);

                user.addCollage(collage);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
