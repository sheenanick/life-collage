package com.doandstevensen.lifecollage.ui.base;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.about.AboutActivity;
import com.doandstevensen.lifecollage.ui.account.AccountActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    public NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.contentFrame)
    public FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_drawer);
        ButterKnife.bind(this);

        initToolbar();
        initDrawer();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setCheckedItem(R.id.nav_collage);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void setNavViewCheckedItem(int item) {
        Menu drawerMenu = mNavigationView.getMenu();
        drawerMenu.findItem(item).setChecked(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.isChecked()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        int id = item.getItemId();

        if (id == R.id.nav_collage) {
            navigateToCollageList();
        } else if (id == R.id.nav_pass) {

        } else if (id == R.id.nav_account) {
            navigateToAccount();
        } else if (id == R.id.nav_about) {
            navigateToAbout();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void navigateToCollageList() {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        startActivity(intent);
    }

    private void navigateToAccount() {
        Intent intent = new Intent(getBaseContext(), AccountActivity.class);
        startActivity(intent);
    }

    private void navigateToAbout() {
        Intent intent = new Intent(getBaseContext(), AboutActivity.class);
        startActivity(intent);
    }

    public void logout() {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.clearData(getBaseContext());

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
