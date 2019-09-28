package com.mel.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mel.minitwitter.R;
import com.mel.minitwitter.data.TweetViewModel;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Usaremos el patron mvvm para comunicar el frgment con la activity por eso eliminamos
 * el listener generado para realizar lo comentado anteriormente
 */
public class TweetListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private TweetViewModel tweetViewModel;
    private Unbinder unbinder;


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

        //Asi conectamos esta vista con el view model
        tweetViewModel= ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        unbinder=ButterKnife.bind(this, view);

        Context context = view.getContext();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                loadNewData();
            }
        });
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
        recyclerView.setAdapter(adapter);
        loadTweetData();
        return view;
    }

    /**
     * La primera vez se llama a este metodo ya que ya estan los datos
     */
    private void loadTweetData() {
        /**
         * La invocacion al viewmodel estara aqui. Cuando recibamos informacion del repositorio atravez del view model
         * podremos cargar el adapter  con la lista de tweets
         */
        tweetViewModel.getAllTweets().observe(getActivity(), tweets -> {
            tweetList=tweets;
            adapter.setData(tweetList);
        });
    }

    /**
     * Cada vez que queramos refrescar
     */
    private void loadNewData() {
        /**
         * La invocacion al viewmodel estara aqui. Cuando recibamos informacion del repositorio atravez del view model
         * podremos cargar el adapter  con la lista de tweets
         */
        tweetViewModel.getNewAllTweets().observe(getActivity(), tweets -> {
            tweetList=tweets;
            adapter.setData(tweetList);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
