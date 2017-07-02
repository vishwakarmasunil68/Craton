package com.emobi.convoy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.PublicPost.PublicPostResponsePOJO;
import com.emobi.convoy.pojo.PublicPost.publicPostPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.emobi.convoy.webservices.WebServicesUrls.NEWS_FEED_API;
import static com.emobi.convoy.webservices.WebServicesUrls.UPDATE_PROFILE_API;

public class FriendProfileActivity extends AppCompatActivity implements WebServicesCallBack,View.OnClickListener{
    private final String TAG = getClass().getName();

    @BindView(R.id.ll_scroll_news)
    LinearLayout ll_scroll_news;
    @BindView(R.id.iv_cover_pic)
    ImageView iv_cover_pic;
    @BindView(R.id.iv_profile_pic)
    CircleImageView iv_profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String user_id=bundle.getString("user_id");
            String profile_pic_url=bundle.getString("log_pic");
            if(!user_id.isEmpty()){
                Log.d(TAG,"profile_id:-"+ user_id);
                Picasso.with(getApplicationContext())
                        .load(WebServicesUrls.IMAGE_BASE_URL+profile_pic_url)
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(iv_profile_pic);
                callNewFeedApi(user_id);
                iv_profile_pic.setOnClickListener(this);

            }
            else{
                finish();
            }
        }

    }

    public void callNewFeedApi(String user_id) {
//        callNewFeedApi();
        Log.d(TAG,"profile_pic:-"+ Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,""));
//
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
        new WebServiceBase(nameValuePairs, this, NEWS_FEED_API).execute(NEWS_FEED_API);

    }
    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];

        switch (apicall) {
            case NEWS_FEED_API:
                parseNewsFeedData(response);
                break;
            case  UPDATE_PROFILE_API:
                Log.d(TAG,"response:-"+response);
        }
    }

    public void parseNewsFeedData(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            Gson gson = new Gson();
            publicPostPOJO publicPostPOJOobj = gson.fromJson(response, publicPostPOJO.class);
            if (publicPostPOJOobj != null) {
                if (publicPostPOJOobj.getSuccess().equals("true")) {
                    if (publicPostPOJOobj.getList_news() != null && publicPostPOJOobj.getList_news().size() > 0) {
                        inflateResponse(publicPostPOJOobj.getList_news());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No post to show", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            Toast.makeText(getApplicationContext(), "SOmething went wrong"+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void inflateResponse(List<PublicPostResponsePOJO> post_response) {
        for (int i = 0; i < post_response.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.inflate_profile_news_feed, null);
            CircleImageView iv_profile = (CircleImageView) v.findViewById(R.id.iv_profile);
            TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
            TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
            TextView tv_post = (TextView) v.findViewById(R.id.tv_post);
            ImageView iv_post_image = (ImageView) v.findViewById(R.id.iv_post_image);


            String image_url = WebServicesUrls.IMAGE_BASE_URL + post_response.get(i).getLog_pics();
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(getApplicationContext())
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_profile);
            tv_post.setText(post_response.get(i).getPost_msg());

            tv_type.setText(post_response.get(i).getPost_cat_id());

            Picasso.with(getApplicationContext())
                    .load(WebServicesUrls.IMAGE_BASE_URL + post_response.get(i).getPost_image())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_post_image);

            ll_scroll_news.addView(v);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_profile_pic:
                break;
        }
    }

}
