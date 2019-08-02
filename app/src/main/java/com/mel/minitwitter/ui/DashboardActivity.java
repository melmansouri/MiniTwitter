package com.mel.minitwitter.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.imgToolbarPhoto)
    ImageView imgToolbarPhoto;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_tweets_like:
                    return true;
                case R.id.navigation_profile:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().hide();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new TweetListFragment())
                .commit();

        String urlPhotoProfile = SharedPreferenceManager.getSomeStringValue(Constantes.PREF_PHOTO_URL);
        if (!TextUtils.isEmpty(urlPhotoProfile)) {
            Glide.with(this).load(Constantes.API_MINITWITTER_FILES_URL + urlPhotoProfile).into(imgToolbarPhoto);
        }
    }

    @OnClick(R.id.fab)
    public void newTweetDialog() {
        NuevoTweetDialogFragment fragment = new NuevoTweetDialogFragment();
        fragment.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
    }

}
