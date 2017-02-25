package com.doandstevensen.lifecollage.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.ui.main.MainActivity;

public class BaseActivity extends AppCompatActivity implements BaseMvpView{
    private ProgressDialog mProgressDialog;
    private ProgressDialog mConnectingDialog;
    private BasePresenterClass mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPresenter = new BasePresenterClass(this, this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);

        mConnectingDialog = new ProgressDialog(this);
        mConnectingDialog.setMessage("Connecting...");
    }

    @Override
    public boolean displayLoadingAnimation() {
        mProgressDialog.show();
        return false;
    }

    @Override
    public void hideLoadingAnimation() {
        mProgressDialog.dismiss();
    }

    @Override
    public boolean displayConnectingAnimation() {
        mConnectingDialog.show();
        return false;
    }

    @Override
    public void hideConnectingAnimation() {
        mConnectingDialog.dismiss();
    }

    @Override
    public void enableUpButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void setFont(TextView view) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.FONT);
        view.setTypeface(font);
    }

    @Override
    public void logout() {
        mPresenter.logout();

        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachBase();
        }
        mProgressDialog = null;
    }
}
