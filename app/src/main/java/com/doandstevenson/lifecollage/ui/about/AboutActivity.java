package com.doandstevenson.lifecollage.ui.about;

import android.os.Bundle;

import com.doandstevenson.lifecollage.R;
import com.doandstevenson.lifecollage.ui.base.BaseDrawerActivity;

public class AboutActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_about, mFrameLayout);

        setActionBarTitle("About");
        initDrawer();
        setNavViewCheckedItem(R.id.nav_about, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavViewCheckedItem(R.id.nav_about, true);
    }
}
