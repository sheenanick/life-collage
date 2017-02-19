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
import com.doandstevensen.lifecollage.data.remote.DataManager;
import com.doandstevensen.lifecollage.ui.base.BaseActivity;
import com.doandstevensen.lifecollage.ui.collage_detail.CollageActivity;
import com.doandstevensen.lifecollage.ui.collage_list.CollageListActivity;
import com.doandstevensen.lifecollage.ui.signin.LogInActivity;
import com.doandstevensen.lifecollage.ui.search.SearchResultsActivity;
import com.doandstevensen.lifecollage.ui.signup.SignUpActivity;

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
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setIconifiedByDefault(false);
    }

    public void setupGridViewAdapter() {
        MainImageAdapter adapter = new MainImageAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //TODO navigate to collage activity on click
            }
        });
    }

    private void navigateToCollage(String collageId, String name) {
        Intent intent = new Intent(getBaseContext(), CollageActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("collageId", collageId);
        startActivity(intent);
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
