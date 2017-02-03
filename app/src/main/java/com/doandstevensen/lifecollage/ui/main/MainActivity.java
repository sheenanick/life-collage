package com.doandstevensen.lifecollage.ui.main;

import android.os.Bundle;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.main.MainContract.MvpView;

public class MainActivity extends BaseActivity implements MvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void navigateToCollage() {

    }

    @Override
    public void navigateToSignUp() {

    }

    @Override
    public void navigateToLogIn() {

    }
}
