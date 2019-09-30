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
import androidx.fragment.app.Fragment;

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
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    break;
                case R.id.navigation_tweets_like:
                    fragment=TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    break;
                case R.id.navigation_profile:
                    break;
            }

            return startFragment(fragment);
        }
    };

    private boolean startFragment(Fragment fragment){
        boolean result=false;
        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment)
                    .commit();
            result=true;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().hide();
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_home);
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
