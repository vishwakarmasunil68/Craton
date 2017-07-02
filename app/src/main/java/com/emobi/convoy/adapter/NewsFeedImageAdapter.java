package com.emobi.convoy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.activity.ShowSingleFeedActivity;
import com.emobi.convoy.pojo.newsfeedposts.NewsImagePost;
import com.emobi.convoy.webservices.WebServicesUrls;

import java.util.List;

/**
 * Created by sunil on 08-05-2017.
 */

public class NewsFeedImageAdapter extends RecyclerView.Adapter<NewsFeedImageAdapter.MyViewHolder> {

    private List<NewsImagePost> horizontalList;
    private Activity activity;
    private final String TAG=getClass().getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cv_card_image;
        public ImageView iv_image_feed;


        public MyViewHolder(View view) {
            super(view);

            cv_card_image = (CardView) view.findViewById(R.id.cv_card_image);
            iv_image_feed = (ImageView) view.findViewById(R.id.iv_image_feed);
        }
    }


    public NewsFeedImageAdapter(Activity activity, List<NewsImagePost> horizontalList) {
        this.horizontalList = horizontalList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_news_image_feed, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Glide.with(activity)
                .load(WebServicesUrls.IMAGE_BASE_URL+horizontalList.get(position).getPost_image())
                .into(holder.iv_image_feed);

        holder.cv_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"pojo:-"+horizontalList.get(position).toString());
                Log.d(TAG,"pojo:-"+horizontalList.get(position).toString());
                Intent intent=new Intent(activity, ShowSingleFeedActivity.class);
                intent.putExtra("post_id",horizontalList.get(position).getPost_id());
                intent.putExtra("image_status",horizontalList.get(position).getPost_image_status());
                activity.startActivity(intent);
//                HomeActivity homeActivity= (HomeActivity) activity;
//                homeActivity.showTextFeedFragment(horizontalList.get(position).getPost_id(),
//                horizontalList.get(position).getPost_image_status());
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