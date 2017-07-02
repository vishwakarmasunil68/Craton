package com.emobi.convoy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.adapter.NewsFeedImageAdapter;
import com.emobi.convoy.adapter.NewsFeedTextAdapter;
import com.emobi.convoy.pojo.newsfeedposts.NewFeedPost;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNewsFeedActivity extends AppCompatActivity implements WebServicesCallBack{
    private final String TAG = getClass().getSimpleName();
    private final String NEWS_FEED_API = "news_feed_api";


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_text_feeds)
    RecyclerView rv_text_feeds;

    @BindView(R.id.rv_images_feeds)
    RecyclerView rv_images_feeds;

    @BindView(R.id.fab_add_post)
    FloatingActionButton fab_add_post;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news_feed);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tv_title.setText("News Feed");

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        fab_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllNewsFeedActivity.this, NewPostActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callNewsFeedAPI(false);
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.NEW_POST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("message");
            callNewsFeedAPI(true);

        }
    };


    public void callNewsFeedAPI(boolean is_dialog) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, NEWS_FEED_API,is_dialog).execute(WebServicesUrls.GET_NEWS_FEED_URL);

    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case NEWS_FEED_API:
                parseNewsFeedData(response);
                break;
        }
    }


    public void parseNewsFeedData(String response) {
        Log.d(TAG, "news feed response:-" + response);
        try {
            Gson gson = new Gson();
            NewFeedPost newFeedPost = gson.fromJson(response, NewFeedPost.class);
            if (newFeedPost.getSuccess().equals("true")) {
                if (newFeedPost.getText_data_success().equals("true")) {
                    NewsFeedTextAdapter adapter = new NewsFeedTextAdapter(AllNewsFeedActivity.this, newFeedPost.getNewsTextPostList());
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    rv_text_feeds.setLayoutManager(horizontalLayoutManagaer);
                    rv_text_feeds.setAdapter(adapter);
                }

                if(newFeedPost.getImage_data_success().equals("true")){
                    NewsFeedImageAdapter adapter = new NewsFeedImageAdapter(AllNewsFeedActivity.this, newFeedPost.getImage_data_result());
                    GridLayoutManager horizontalLayoutManagaer
                            = new GridLayoutManager(AllNewsFeedActivity.this, 3);
                    rv_images_feeds.setLayoutManager(horizontalLayoutManagaer);
                    rv_images_feeds.setAdapter(adapter);
                }
            } else {
                ToastClass.showShortToast(AllNewsFeedActivity.this.getApplicationContext(), "No Post Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(AllNewsFeedActivity.this.getApplicationContext(), "No Post Found");
        }
    }



}
