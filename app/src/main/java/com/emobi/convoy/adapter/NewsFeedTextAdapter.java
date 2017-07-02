package com.emobi.convoy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.activity.ShowSingleFeedActivity;
import com.emobi.convoy.pojo.newsfeedposts.NewsTextPost;
import com.emobi.convoy.webservices.WebServicesUrls;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sunil on 08-05-2017.
 */

public class NewsFeedTextAdapter extends RecyclerView.Adapter<NewsFeedTextAdapter.MyViewHolder> {

    private List<NewsTextPost> horizontalList;
    private Activity activity;
    private final String TAG=getClass().getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cv_text_news;
        public CircleImageView cv_profile_image;
        public TextView tv_profile_name;
        public TextView tv_post_news;

        public MyViewHolder(View view) {
            super(view);
            tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            tv_post_news = (TextView) view.findViewById(R.id.tv_post_news);
            cv_profile_image = (CircleImageView) view.findViewById(R.id.cv_profile_image);
            cv_text_news = (CardView) view.findViewById(R.id.cv_text_news);
        }
    }


    public NewsFeedTextAdapter(Activity activity, List<NewsTextPost> horizontalList) {
        this.horizontalList = horizontalList;
        this.activity=activity;
    }

    @Override
    public NewsFeedTextAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_news_text_feed, parent, false);

        return new NewsFeedTextAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsFeedTextAdapter.MyViewHolder holder, final int position) {
        Glide.with(activity)
                .load(WebServicesUrls.IMAGE_BASE_URL+horizontalList.get(position).getLogPics())
                .error(R.drawable.ic_profile)
                .into(holder.cv_profile_image);

        holder.tv_post_news.setText(horizontalList.get(position).getPostMsg());
        holder.tv_profile_name.setText(horizontalList.get(position).getLogName());
        holder.cv_text_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"pojo:-"+horizontalList.get(position).toString());
                Intent intent=new Intent(activity, ShowSingleFeedActivity.class);
                intent.putExtra("post_id",horizontalList.get(position).getPostId());
                intent.putExtra("image_status",horizontalList.get(position).getPostImageStatus());
                activity.startActivity(intent);

//                HomeActivity homeActivity= (HomeActivity) activity;
//                homeActivity.showTextFeedFragment(horizontalList.get(position).getPostId(),
//                        horizontalList.get(position).getPostImageStatus());
            }
        });
    }



    @Override
    public int getItemCount() {
        if(horizontalList!=null){
            return horizontalList.size();
        }else{
            return 0;
        }
    }
}