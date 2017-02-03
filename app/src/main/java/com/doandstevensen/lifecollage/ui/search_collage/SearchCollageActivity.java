package com.doandstevensen.lifecollage.ui.search_collage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;

import com.doandstevensen.lifecollage.R;
import com.doandstevensen.lifecollage.ui.collage.SearchViewAdapter;
import com.doandstevensen.lifecollage.data.model.Picture;
import com.doandstevensen.lifecollage.data.model.User;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage.PicturesRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SearchCollageActivity extends BaseActivity implements SearchCollageContract.MvpView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String mUsername;
    private SearchCollagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        mUsername = intent.getStringExtra("username");

        initToolbar();

        mPresenter = new SearchCollagePresenter(this);
        mPresenter.loadCollage(uid);
        mPresenter.searchUsers();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (mUsername != null) {
            getSupportActionBar().setTitle(mUsername);
        }
    }

    public void setupRecyclerView(RealmList<Picture> pictures) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PicturesRecyclerViewAdapter(this, pictures));
        recyclerView.setHasFixedSize(true);
    }

    public void setupSearchAdapter(RealmResults<User> userResults) {
        SearchViewAdapter adapter = new SearchViewAdapter(this, userResults);
        autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
