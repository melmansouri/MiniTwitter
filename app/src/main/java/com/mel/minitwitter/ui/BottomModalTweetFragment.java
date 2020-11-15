package com.mel.minitwitter.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.data.TweetViewModel;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel mViewModel;
    private int idTweet;
    public static BottomModalTweetFragment newInstance(int id) {
        BottomModalTweetFragment bottom=new BottomModalTweetFragment();
        Bundle args=new Bundle();
        args.putInt(Constantes.ARG_TWEET_ID,id);
        bottom.setArguments(args);
        return bottom;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            idTweet=getArguments().getInt(Constantes.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);
        final NavigationView nav=v.findViewById(R.id.nav_bottom_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id==R.id.action_delete){
                    mViewModel.deleteTweet(idTweet);
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
        // TODO: Use the ViewModel
    }

}