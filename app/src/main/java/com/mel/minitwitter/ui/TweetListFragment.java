package com.mel.minitwitter.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.data.TweetViewModel;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Customize parameters
    private int tweetListType = 1;
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
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asi conectamos esta vista con el view model
        tweetViewModel= ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
        if (getArguments() != null) {
            tweetListType = getArguments().getInt(Constantes.TWEET_LIST_TYPE);
            Toast.makeText(getActivity(),"sdfsdf "+tweetListType,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        unbinder=ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAzul));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (tweetListType==Constantes.TWEET_LIST_ALL){
                    loadNewData();
                }else{
                    loadNewFavData();
                }
            }
        });
        adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
        recyclerView.setAdapter(adapter);
        if (tweetListType==Constantes.TWEET_LIST_ALL){
            loadTweetData();
        }else{
            loadFavTweetData();
        }
        return view;
    }

    /**
     * Para hacerlo mas eficiente no vamos a atacar el servidor para obtenerlos
     * si no que vamos a recorrer aquellos tweets que tenmos y sobre ellos vamos a filtrar aquellos en los que el usuario ha marcado un like
     * Metodo invocado por el swipetorefresh
     */
    private void loadNewFavData() {
        //Trae nuevos tweets del servidor y los favoritos
        LiveData<List<Tweet>>liveDataFavTweets= tweetViewModel.getNewFavTweets();
        liveDataFavTweets.observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                adapter.setData(tweetList);
                swipeRefreshLayout.setRefreshing(false);
                liveDataFavTweets.removeObserver(this);
            }
        });
    }

    /**
     * Para hacerlo mas eficiente no vamos a atacar el servidor para obtenerlos
     * si no que vamos a recorrer aquellos tweets que tenmos y sobre ellos vamos a filtrar aquellos en los que el usuario ha marcado un like
     */
    private void loadFavTweetData() {
        tweetViewModel.getFavTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                adapter.setData(tweetList);
            }
        });
    }

    /**
     * La primera vez se llama a este metodo ya que ya estan los datos
     */
    private void loadTweetData() {
        /**
         * La invocacion al viewmodel estara aqui. Cuando recibamos informacion del repositorio atravez del view model
         * podremos cargar el adapter  con la lista de tweets
         * ??>
         */
        tweetViewModel.getAllTweets().observe(getActivity(), tweets -> {
            tweetList=tweets;
            adapter.setData(tweetList);
        });
    }

    /**
     * Cada vez que queramos refrescar
     *Invocado por el swipetorefresh
     */
    private void loadNewData() {
        /**
         * La invocacion al viewmodel estara aqui. Cuando recibamos informacion del repositorio atravez del view model
         * podremos cargar el adapter  con la lista de tweets.
         * ??>
         * No podemos dejar este observador pendiente de la lista de tweet ya que tendriamos en total
         * 2 obeservadores que se lanzarian a manejar el recyclerview y esi estarua nak.
         * Para evitar esto hay que desactivar el observer
         */
        LiveData<List<Tweet>> liveDataTweets= tweetViewModel.getNewAllTweets();
        liveDataTweets.observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList=tweets;
                adapter.setData(tweetList);
                swipeRefreshLayout.setRefreshing(false);
                liveDataTweets.removeObserver(this);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
