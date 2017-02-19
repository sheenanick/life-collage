package com.doandstevensen.lifecollage.ui.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.main.MainActivity;

public class BaseActivity extends AppCompatActivity implements BaseMvpView{
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public void logout() {
        DataManager dataManager = new DataManager(getBaseContext());
        dataManager.clearData();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
