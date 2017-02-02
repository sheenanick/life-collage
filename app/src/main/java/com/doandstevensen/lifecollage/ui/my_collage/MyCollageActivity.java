package com.doandstevensen.lifecollage.ui.my_collage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.adapter.SearchViewAdapter;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.login.LogInActivity;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MyCollageActivity extends BaseActivity
        implements MyCollageContract.MvpView ,NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MyCollagePresenter mPresenter;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new MyCollagePresenter(this, getBaseContext());
        mPresenter.loadCollage();
        mPresenter.searchUsers();

        initToolbar();
        initDrawer();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_collage);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initRecyclerViewAdapter(RealmList<Picture> pictures) {
        PicturesRecyclerViewAdapter recyclerViewAdapter = new PicturesRecyclerViewAdapter(this, pictures);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void initSearchAdapter(RealmResults<User> users) {
        SearchViewAdapter adapter = new SearchViewAdapter(this, users);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_search) {

        } else if (id == R.id.nav_pass) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            RealmUserManager.logoutActiveUser();
            Intent intent = new Intent(getBaseContext(), LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.fab)
    public void launchCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(MyCollageActivity.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImagefile();
            } catch (IOException exc) {
                exc.printStackTrace();
            }

            if (photoFile != null) {
                 Uri photoURI = FileProvider.getUriForFile(this,
                                                        "com.doandstevensen.fileprovider",
                                                        photoFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImagefile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            File f = new File(mCurrentPhotoPath);
            try {
                mPresenter.uploadFile(f);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
