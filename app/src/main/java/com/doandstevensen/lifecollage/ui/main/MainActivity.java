package com.doandstevensen.lifecollage.ui.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.PictureResponse;
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.signin.LogInActivity;
import com.doandstevensen.lifecollage.ui.search.SearchResultsActivity;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.MvpView {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.searchView)
    SearchView searchView;
    public static final String TAG = MainActivity.class.getSimpleName();
    private MainPresenter mPresenter;
    private MainImageAdapter mAdapter;
    private ArrayList<PictureResponse> mPictures;
    private String[] mCollageTitles = {"My Dog Collage", "Winter Wonderland", "Food Collage", "Travel Collage", "Pretty Flowers", "Good Times"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DataManager dataManager = new DataManager(this);
        ApplicationToken token = dataManager.getUserToken();
        if (token.getAccessToken() != null) {
            navigateToCollageList();
        }

        initSearchView();
        setupGridViewAdapter();

        mPresenter = new MainPresenter(this, this);
        mPresenter.getGridViewUsers();
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(false);
    }

    public void setupGridViewAdapter() {
        mAdapter = new MainImageAdapter(this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int collageId = mPictures.get(position).getCollageId();
                String title = mCollageTitles[position];
                navigateToCollage(collageId, title);
            }
        });
    }

    @Override
    public void updateGridView(ArrayList<PictureResponse> pictures) {
        mPictures = pictures;
        mAdapter.setPictures(pictures);
        mAdapter.notifyDataSetChanged();
    }

    private void navigateToCollage(int collageId, String name) {

    }

    @Override
    public void navigateToCollageList() {
        Intent intent = new Intent(getBaseContext(), CollageListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.signUpButton)
    @Override
    public void navigateToSignUp() {
        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logInButton)
    @Override
    public void navigateToLogIn() {
        Intent intent = new Intent(getBaseContext(), LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }

}
