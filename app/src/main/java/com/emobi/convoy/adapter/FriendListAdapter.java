package com.emobi.convoy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.ChatActivity;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.pojo.friendlist.FriendListResponsePOJO;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by sunil on 08-05-2017.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private List<FriendListResponsePOJO> horizontalList;
    Activity activity;
    boolean istestimonial;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView iv_profile;
        public TextView tv_profile_name;
        public LinearLayout ll_friends;

        public MyViewHolder(View view) {
            super(view);
            iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
            tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            ll_friends = (LinearLayout) view.findViewById(R.id.ll_friends);


        }
    }


    public FriendListAdapter(Activity activity, List<FriendListResponsePOJO> horizontalList, boolean istestimonial) {
        this.horizontalList = horizontalList;
        this.activity = activity;
        this.istestimonial = istestimonial;
    }

    @Override
    public FriendListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_friends, parent, false);

        return new FriendListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendListAdapter.MyViewHolder holder, final int position) {
        holder.tv_profile_name.setText(horizontalList.get(position).getLog_name());
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(holder.iv_profile);
        String image_url = WebServicesUrls.IMAGE_BASE_URL + horizontalList.get(position).getLog_pics();
        Log.d("sunil", "image urls:-" + image_url);
        Picasso.with(activity)
                .load(image_url)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(holder.iv_profile);

        holder.ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (istestimonial) {
                    HomeActivity homeActivity= (HomeActivity) activity;
                    homeActivity.showSendTestimonialFragment(horizontalList.get(position).getLog_id());
                } else {
                    Log.d(TAG, "pojo:-" + horizontalList.get(position));
                    Intent intent = new Intent(activity, ChatActivity.class);
                    intent.putExtra("user_id", horizontalList.get(position).getLog_id());
                    intent.putExtra("log_pic", horizontalList.get(position).getLog_pics());
                    intent.putExtra("log_name", horizontalList.get(position).getLog_name());
                    intent.putExtra("friend_token", horizontalList.get(position).getLog_device_token());
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}