package com.doandstevensen.lifecollage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;

import com.doandstevensen.lifecollage.adapter.PicturesRecyclerViewAdapter;
import com.doandstevensen.lifecollage.adapter.SearchViewAdapter;
import com.doandstevensen.lifecollage.model.Picture;
import com.doandstevensen.lifecollage.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class SearchCollageActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String username = intent.getStringExtra("username");
        if (username != null && uid != null) {
            getSupportActionBar().setTitle(username);
            realm = Realm.getDefaultInstance();
            User user = realm.where(User.class).equalTo("uid", uid).findFirst();
            setupRecyclerView(user.getCollages().get(0).getPictures());
        }
        RealmResults<User> userResults = realm.where(User.class).findAll();
        SearchViewAdapter adapter = new SearchViewAdapter(this, userResults);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void setupRecyclerView(RealmList<Picture> pictures) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PicturesRecyclerViewAdapter(this, pictures));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
