package com.doandstevensen.lifecollage.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;

public class LogInActivity extends BaseActivity implements LogInContract.MvpView {
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;

    private LogInPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        mPresenter = new LogInPresenter(this, getBaseContext());

        String uid = RealmUserManager.getCurrentUserId();
        if (uid != null) {
            navigateToMain(uid);
        }
    }

    @OnClick(R.id.logIn)
    public void attemptLogIn() {
        usernameView.setError(null);
        passwordView.setError(null);

        final String username = usernameView.getText().toString().trim();
        final String password = passwordView.getText().toString().trim();

        boolean validUsername = validateInput(usernameView, username);
        boolean validPassword = validateInput(passwordView, password);

        if (validUsername && validPassword) {
            mPresenter.logIn(username, password);
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
    public void navigateToMain(String uid) {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        intent.putExtra("uid", uid);
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
