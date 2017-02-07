package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;

public class CollageActivity extends BaseActivity implements CollageContract.MvpView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CollagePresenter mPresenter;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);

        mPresenter = new CollagePresenter(this, getBaseContext());

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String name = intent.getStringExtra("name");
        populateRecyclerView(uid, name);
    }

    public void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setFabVisibility(int visibility) {
        fab.setVisibility(visibility);
    }

    public void populateRecyclerView(String uid, String title) {
        mPresenter.loadCollage(uid, title);
    }

    public void setupRecyclerViewAdapter(RealmList<Picture> pictures) {
        PicturesRecyclerViewAdapter recyclerViewAdapter = new PicturesRecyclerViewAdapter(this, pictures);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
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
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

}
