package com.doandstevensen.lifecollage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doandstevensen.lifecollage.adapter.PicturesRecyclerViewAdapter;
import com.doandstevensen.lifecollage.model.Picture;
import com.doandstevensen.lifecollage.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class SearchCollageActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_collage);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        if (uid != null) {
            try {
                realm = Realm.getDefaultInstance();
                User user = realm.where(User.class).equalTo("uid", uid).findFirst();
                setupRecyclerView(user.getCollages().get(0).getPictures());
            } finally {
                if(realm != null) {
                    realm.close();
                }
            }

        }
    }

    private void setupRecyclerView(RealmList<Picture> pictures) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PicturesRecyclerViewAdapter(this, pictures));
        recyclerView.setHasFixedSize(true);
    }
}
