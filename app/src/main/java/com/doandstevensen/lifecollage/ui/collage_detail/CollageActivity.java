package com.doandstevensen.lifecollage.ui.collage_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollageActivity extends BaseActivity implements CollageContract.MvpView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    TextView emptyView;

    private CollagePresenter mPresenter;
    private String mCurrentPhotoPath;
    private PicturesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int collageId = intent.getIntExtra("collageId", -1);
        String collageTitle = intent.getStringExtra("collageTitle");
        boolean load = intent.getBooleanExtra("load", true);

        initRecyclerViewAdapter();
        setActionBarTitle(collageTitle);

        mPresenter = new CollagePresenter(this, this, collageId);
        if (load) {
            mPresenter.loadCollage();
        } else {
            setEmptyViewVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setEmptyViewVisibility(int visibility) {
        emptyView.setVisibility(visibility);
    }

    public void initRecyclerViewAdapter() {
        mAdapter = new PicturesRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
    }

    public void setRecyclerViewPictures(ArrayList<PictureResponse> pictures) {
        mAdapter.setPictures(pictures);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab)
    public void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                                                        "com.doandstevensen.fileprovider",
                                                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
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
        if (mAdapter != null) {
            mAdapter.detach();
        }
        super.onDestroy();
    }
}
