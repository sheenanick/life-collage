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
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.main.MainActivity;

public class BaseActivity extends AppCompatActivity implements BaseMvpView{
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
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
        DataManager dataManager = new DataManager(getBaseContext());
        dataManager.clearData();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
