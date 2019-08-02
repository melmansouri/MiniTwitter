package com.mel.minitwitter.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.data.TweetViewModel;
import com.mel.minitwitter.retrofit.AuthTwitterClient;
import com.mel.minitwitter.retrofit.AuthTwitterService;
import com.mel.minitwitter.retrofit.request.RequestCreateTweet;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class NuevoTweetDialogFragment extends DialogFragment {

    @BindView(R.id.edtTweet)
    EditText edtTweet;
    @BindView(R.id.btnTwittear)
    Button btnTwittear;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    private Unbinder unbinder;
    private AuthTwitterClient authTwitterClient;
    private AuthTwitterService authTwitterService;
    private TweetViewModel viewModel;


    public NuevoTweetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        viewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nuevo_tweet_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        String urlPhotoProfile=SharedPreferenceManager.getSomeStringValue(Constantes.PREF_PHOTO_URL);
        if (!TextUtils.isEmpty(urlPhotoProfile)){
            Glide.with(getActivity()).load(Constantes.API_MINITWITTER_FILES_URL+urlPhotoProfile).into(imgAvatar);
        }
        return view;
    }

    @OnTextChanged(R.id.edtTweet)
    public void onTextChanged(CharSequence text) {
        btnTwittear.setEnabled(!TextUtils.isEmpty(text));
    }

    @OnClick(R.id.btnTwittear)
    public void addTweet() {
        String tweet = edtTweet.getText().toString();
        viewModel.createTweet(new RequestCreateTweet(tweet));
        getDialog().dismiss();
    }

    @OnClick(R.id.imgCloseDialog)
    public void close() {
        String tweet = edtTweet.getText().toString();
        if(TextUtils.isEmpty(tweet)){
            getDialog().dismiss();
        }else{
            showDialogConfirm();
        }
    }

    private void showDialogConfirm(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Seguro que quieres canelar el tweet? El mensaje se borrará")
                .setTitle("Cancelar tweet");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
