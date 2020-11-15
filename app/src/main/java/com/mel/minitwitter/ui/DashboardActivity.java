package com.mel.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.data.ProfileViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.imgToolbarPhoto)
    ImageView imgToolbarPhoto;
    ProfileViewModel profileViewModel;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                    fab.show();
                    break;
                case R.id.navigation_tweets_like:
                    fragment=TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                    fab.hide();
                    break;
                case R.id.navigation_profile:
                    fragment=ProfileFragment.newInstance();
                    fab.hide();
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
        Glide.with(this).load(Constantes.API_MINITWITTER_FILES_URL + urlPhotoProfile).placeholder(R.drawable.ic_account_circle_azul).dontAnimate()
                //Con esto indicamos que no se utilice la cache. Ya que puede dar el caso de que se modifique la imagen pero siga apareciendo la anterior
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgToolbarPhoto);
        profileViewModel= ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getPhotoprofileObserver().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(DashboardActivity.this).load(Constantes.API_MINITWITTER_FILES_URL + s).placeholder(R.drawable.ic_account_circle_azul).dontAnimate()
                        //Con esto indicamos que no se utilice la cache. Ya que puede dar el caso de que se modifique la imagen pero siga apareciendo la anterior
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(imgToolbarPhoto);
            }
        });
    }

    @OnClick(R.id.fab)
    public void newTweetDialog() {
        NuevoTweetDialogFragment fragment = new NuevoTweetDialogFragment();
        fragment.show(getSupportFragmentManager(), "NuevoTweetDialogFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode!=RESULT_CANCELED){
            if (requestCode==Constantes.REQUEST_PHOTO_GALERY){
                if (data!=null){
                    Uri image=data.getData();//content://gallery/....
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor cursor=getContentResolver().query(image,filePathColumn,null,null,null);
                    if (cursor!=null){
                        cursor.moveToFirst();
                        int imageIndex=cursor.getColumnIndex(filePathColumn[0]);
                        String path=cursor.getString(imageIndex);
                        profileViewModel.uploadPhoto(path);
                        cursor.close();
                    }
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,Constantes.REQUEST_PHOTO_GALERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this,"No se puede seleccionar la fotografia",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
    }
}
