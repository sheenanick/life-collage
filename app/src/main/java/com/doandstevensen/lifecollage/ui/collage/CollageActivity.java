package com.doandstevensen.lifecollage.ui.collage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.Collage;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.account.AccountActivity;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.main.MainActivity;
import com.doandstevensen.lifecollage.util.RealmUserManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CollageActivity extends BaseActivity
        implements CollageContract.MvpView ,NavigationView.OnNavigationItemSelectedListener {
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
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.titleTextView)
    TextView titleTextView;

    private CollagePresenter mPresenter;
    private String mCurrentPhotoPath;
    private String mCurrentCollageId;
    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);

        mCurrentUser = RealmUserManager.getCurrentUserId();

        initToolbar();
        initDrawer();

        mPresenter = new CollagePresenter(this, getBaseContext());

        String uid = getIntent().getStringExtra("uid");
        populateRecyclerView(uid);

        mPresenter.searchUsers();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    public void setToolbarTitle(String title) {
        titleTextView.setText(title);
    }

    private void initDrawer() {
        if (mCurrentUser != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setCheckedItem(R.id.nav_collage);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    public void setNavViewCheckedItem(boolean checked) {
        Menu drawerMenu = navigationView.getMenu();
        drawerMenu.findItem(R.id.nav_collage).setChecked(checked);
        drawerMenu.findItem(R.id.nav_account).setChecked(false);

    }

    public void setFabVisibility(int visibility) {
        fab.setVisibility(visibility);
    }

    public void populateRecyclerView(String uid) {
        mCurrentCollageId = uid;
        mPresenter.loadCollage(uid);
    }

    public void setupRecyclerViewAdapter(RealmList<Picture> pictures) {
        PicturesRecyclerViewAdapter recyclerViewAdapter = new PicturesRecyclerViewAdapter(this, pictures);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void setupSearchAdapter(RealmResults<User> users) {
        CollageSearchAdapter adapter = new CollageSearchAdapter(this, users);
        autoCompleteTextView.setAdapter(adapter);
    }

    public void clearSearchView() {
        autoCompleteTextView.setText("");
    }

    @OnClick(R.id.fab)
    public void launchCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(CollageActivity.this.getPackageManager()) != null) {
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
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
            mPresenter.uploadFile(f);
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_collage) {
            if (!mCurrentCollageId.equals(mCurrentUser)) {
                populateRecyclerView(mCurrentUser);
            }
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

    private void navigateToAccount() {
        Intent intent = new Intent(CollageActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private void logout() {
        RealmUserManager.logoutActiveUser();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        setNavViewCheckedItem(mCurrentCollageId.equals(mCurrentUser));
        super.onStart();
    }
}
