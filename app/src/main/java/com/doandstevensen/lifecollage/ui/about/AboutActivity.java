package com.doandstevensen.lifecollage.ui.about;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.base.BaseDrawerActivity;

public class AboutActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, mFrameLayout);

        initToolbar();

        setNavViewCheckedItem(R.id.nav_about);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_about);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About");
        }
    }
}
