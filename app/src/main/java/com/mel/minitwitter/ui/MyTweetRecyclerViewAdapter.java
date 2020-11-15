package com.mel.minitwitter.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.common.SharedPreferenceManager;
import com.mel.minitwitter.data.TweetViewModel;
import com.mel.minitwitter.retrofit.response.Like;
import com.mel.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context ctx;
    private String username;
    private TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx=context;
        username= SharedPreferenceManager.getSomeStringValue(Constantes.PREF_USERNAME);
        tweetViewModel= ViewModelProviders.of((FragmentActivity)ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues==null){
            return;
        }
         holder.mItem = mValues.get(position);
        holder.txtUsername.setText("@"+holder.mItem.getUser().getUsername());
        holder.txtMessage.setText(mValues.get(position).getMensaje());
        int sizeLikes=mValues.get(position).getLikes()!=null?mValues.get(position).getLikes().size():0;
        holder.txtlikesCount.setText(sizeLikes+"");

        Glide.with(ctx)
                .load("https://www.minitwitter.com/apiv1/uploads/photos/"+holder.mItem.getUser().getPhotoUrl())
                .placeholder(R.drawable.logo_minitwitter_mini)
                .into(holder.imgAvatar);

        Glide.with(ctx)
                .load(R.drawable.ic_like)
                .into(holder.imgLike);
        holder.txtlikesCount.setTextColor(ctx.getResources().getColor(android.R.color.black));
        holder.txtlikesCount.setTypeface(null, Typeface.NORMAL);

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetViewModel.likeTweet(holder.mItem.getId());
            }
        });
        holder.imgMenu.setVisibility(View.GONE);
        if (holder.mItem.getUser().getUsername().equals(username)){
            holder.imgMenu.setVisibility(View.VISIBLE);
        }
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetViewModel.openDialogTweet(ctx,holder.mItem.getId());
            }
        });

        for (Like like:holder.mItem.getLikes()) {
            if (like.getUsername().equals(username)){
                Glide.with(ctx)
                        .load(R.drawable.ic_like_pink)
                        .into(holder.imgLike);
                holder.txtlikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                holder.txtlikesCount.setTypeface(null, Typeface.BOLD);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues!=null?mValues.size():0;
    }

    public void setData(List<Tweet> tweetList){
        mValues=tweetList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imgAvatar;
        public final ImageView imgLike;
        public final TextView txtUsername;
        public final TextView txtMessage;
        public final TextView txtlikesCount;
        public final ImageView imgMenu;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgAvatar = view.findViewById(R.id.imgAvatar);
            imgLike = view.findViewById(R.id.imgLike);
            imgMenu = view.findViewById(R.id.menu_bottom_tweet);
            txtUsername = view.findViewById(R.id.txtUsername);
            txtMessage = view.findViewById(R.id.txtMessage);
            txtlikesCount = view.findViewById(R.id.txtLikesCount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtUsername.getText() + "'";
        }
    }
}
