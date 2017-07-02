package com.emobi.convoy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.activity.NewPostActivity;
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

/**
 * Created by sunil on 08-05-2017.
 */

public class NewsFeedFragment extends Fragment implements WebServicesCallBack {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news_feed, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle("News Feed");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed()
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.onBackPressed();
            }
        });


        fab_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, NewPostActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        callNewsFeedAPI();

    }

    public void callNewsFeedAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, getActivity(), this, NEWS_FEED_API).execute(WebServicesUrls.GET_NEWS_FEED_URL);

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
                    NewsFeedTextAdapter adapter = new NewsFeedTextAdapter(getActivity(), newFeedPost.getNewsTextPostList());
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    rv_text_feeds.setLayoutManager(horizontalLayoutManagaer);
                    rv_text_feeds.setAdapter(adapter);
                }

                if(newFeedPost.getImage_data_success().equals("true")){
                    NewsFeedImageAdapter adapter = new NewsFeedImageAdapter(getActivity(), newFeedPost.getImage_data_result());
                    GridLayoutManager horizontalLayoutManagaer
                            = new GridLayoutManager(getActivity(), 3);
                    rv_images_feeds.setLayoutManager(horizontalLayoutManagaer);
                    rv_images_feeds.setAdapter(adapter);
                }
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "No Post Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getActivity().getApplicationContext(), "No Post Found");
        }
    }
}
