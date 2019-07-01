package com.mel.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mel.minitwitter.R;
import com.mel.minitwitter.retrofit.AuthTwitterClient;
import com.mel.minitwitter.retrofit.AuthTwitterService;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Usaremos el patron mvvm para comunicar el frgment con la activity por eso eliminamos
 * el listener generado para realizar lo comentado anteriormente
 */
public class TweetListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.rootFragmentListTweet)
    ConstraintLayout rootFragmentListTweet;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TweetListFragment newInstance(int columnCount) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        retrofitInit();
        loadTweetData();
        return view;
    }

    private void retrofitInit() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
    }

    private void loadTweetData() {
        Call<List<Tweet>> call = authTwitterService.getAllTweet();
        //Metodo que nos permite ejecutar en segundo plano la peticion al servidor
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful()) {
                    tweetList = response.body();
                    if (tweetList != null) {
                        adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Snackbar.make(rootFragmentListTweet, "Algo fue mal, revise sus datos de acceso", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Snackbar.make(rootFragmentListTweet, "Problemas de conexión. Intentelo de nuevo", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}
