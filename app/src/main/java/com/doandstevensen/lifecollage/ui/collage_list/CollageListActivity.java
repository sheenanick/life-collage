package com.doandstevensen.lifecollage.ui.collage_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.CollageResponse;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.account.AccountActivity;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.util.UserDataSharedPrefsHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class CollageListActivity extends BaseActivity
        implements CollageListContract.MvpView, NavigationView.OnNavigationItemSelectedListener, CollageSearchAdapter.ClickListener, CollageListRecyclerViewAdapter.ClickListener, NewCollageDialogFragment.NewCollageDialogListener, DeleteCollageDialogFragment.DeleteCollageDialogListener{
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CollageListPresenter mPresenter;
    private CollageListRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_list);

        ButterKnife.bind(this);

        initToolbar();
        initDrawer();
        initRecyclerViewAdapter();

        mPresenter = new CollageListPresenter(this, this);
        mPresenter.setPrivateService();
        mPresenter.loadCollageList();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_collage);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setNavViewCheckedItem(boolean checked) {
        Menu drawerMenu = navigationView.getMenu();
        drawerMenu.findItem(R.id.nav_collage).setChecked(checked);
        drawerMenu.findItem(R.id.nav_account).setChecked(false);
    }

    private void initRecyclerViewAdapter() {
        mAdapter = new CollageListRecyclerViewAdapter(this);
        mAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateRecyclerView(ArrayList<CollageResponse> collages) {
        mAdapter.setCollages(collages);
        mAdapter.notifyDataSetChanged();
    }

    public void populateRecyclerView(String uid) {
        mPresenter.loadCollageList();
    }

    public void setupSearchAdapter(RealmResults<User> users) {
        CollageSearchAdapter adapter = new CollageSearchAdapter(this, users);
        autoCompleteTextView.setAdapter(adapter);
        adapter.setClickListener(this);
    }

    private void clearSearchView() {
        autoCompleteTextView.setText("");
    }

    @Override
    public void onSearchClick(String uid) {
        clearSearchView();
        populateRecyclerView(uid);
    }

    @Override
    public void onCollageClick(int collageId, String collageTitle) {
        navigateToCollage(collageId, collageTitle);
    }

    @Override
    public void navigateToCollage(int collageId, String collageTitle) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
        intent.putExtra("collageTitle", collageTitle);
        intent.putExtra("collageId", collageId + "");
        startActivity(intent);
    }

    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, "Collage Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFabVisibility(int visibility) {
        fab.setVisibility(visibility);
    }

    @OnClick(R.id.fab)
    public void addNewCollage() {
        launchAlertDialog();
    }

    private void launchAlertDialog() {
        NewCollageDialogFragment dialogFragment = new NewCollageDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "newCollage");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String title) {
        mPresenter.createNewCollage(title);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void onMenuClick(MenuItem item, int collageId) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            Toast.makeText(this, "Can't edit yet!", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.delete) {
            launchDeleteAlertDialog(collageId);
        }
    }

    private void launchDeleteAlertDialog(int collageId) {
        DeleteCollageDialogFragment dialogFragment = new DeleteCollageDialogFragment();
        dialogFragment.setCollageId(collageId);
        dialogFragment.show(getSupportFragmentManager(), "deleteCollage");
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog, int collageId) {
        mPresenter.deleteCollage(collageId);
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_collage) {

        } else if (id == R.id.nav_pass) {

        } else if (id == R.id.nav_account) {
            navigateToAccount();
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void navigateToAccount() {
        Intent intent = new Intent(getBaseContext(), AccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {
        UserDataSharedPrefsHelper helper = new UserDataSharedPrefsHelper();
        helper.clearData(this);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mAdapter != null) {
            mAdapter = null;
        }
        super.onDestroy();
    }
}
