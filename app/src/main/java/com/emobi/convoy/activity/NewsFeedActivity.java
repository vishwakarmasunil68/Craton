package com.emobi.convoy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.newscomment.NewsComment;
import com.emobi.convoy.pojo.newscomment.NewsCommentResultPOJO;
import com.emobi.convoy.pojo.newsfeed.NewsFeedPOJO;
import com.emobi.convoy.pojo.newsfeed.NewsFeedPostResultPOJO;
import com.emobi.convoy.pojo.newsfeed.UserLikesResultPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedActivity extends AppCompatActivity implements WebServicesCallBack {
    private final static String CALL_LIKE_API = "call_like_api";
    private final static String CALL_UNLIKE_API = "call_unlike_api";
    private final static String CALL_ADD_COMMENT_API = "call_add_comment_api";
    private final static String CALL_GET_COMMENT_API = "call_get_comment_api";
    private final static String DELETE_COMMENT_API = "delete_comment_api";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_news_feed)
    RecyclerView rv_news_feed;
    @BindView(R.id.fab_add_post)
    FloatingActionButton fab_add_post;
    @BindView(R.id.ll_scroll_news)
    LinearLayout ll_scroll_news;

    private final int POST_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News Feeds");
        callNewFeedApi();

//        Log.d(TAG,"user:-"+Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));

        fab_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewsFeedActivity.this, NewPostActivity.class);
                startActivityForResult(i, POST_REQUEST);
            }
        });
    }

    public void callNewFeedApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        Log.d(TAG, "user_id:-" + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""));
        new WebServiceBase(nameValuePairs, this, "newsfeedapi").execute(WebServicesUrls.NEWS_FEED_API);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case "newsfeedapi":
                parseNewsFeedData(response);
                break;

            case CALL_LIKE_API:
                parsecallLikeAPI(response);
                break;
            case CALL_UNLIKE_API:
                parseUnlikeAPI(response);
                break;
            case CALL_ADD_COMMENT_API:
                parseAddCommentAPI(response);
                break;
            case CALL_GET_COMMENT_API:
                parseGetCommentAPI(response);
                break;
            case DELETE_COMMENT_API:
                parsedeletecommentapi(response);
                break;

        }
    }

    public void parsedeletecommentapi(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("true")){
                if(final_comment_linear!=null&&final_comment_layout!=null&&final_post_id!=null&&
                        final_post_id.length()>0){
                    final_comment_linear.removeView(final_comment_layout);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseGetCommentAPI(String response){
        Log.d(TAG,"comment_response:-"+response);
        try{
            Gson gson=new Gson();
            NewsComment newsComment=gson.fromJson(response,NewsComment.class);
            if(final_post_id!=null&&final_post_id.length()>0){
                Log.d(TAG,"newscomment:-"+newsComment.toString());
                showCommentDialog(newsComment.getNewsCommentResultPOJOList(),final_post_id);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseAddCommentAPI(String response){
        Log.d(TAG,"add comment:-"+response);
        try{
            JSONObject object=new JSONObject(response);
            if(object.optString("success").equals("true")){
                JSONObject result=object.optJSONObject("result");
                Gson gson=new Gson();
                NewsCommentResultPOJO userCommentResultPOJO=gson.fromJson(result.toString(),NewsCommentResultPOJO.class);
                userCommentResultPOJO.setLog_name(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_NAME,""));
                userCommentResultPOJO.setLog_pics(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,""));
                inflateSingleComment(final_comment_linear,userCommentResultPOJO);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parsecallLikeAPI(String response) {
        Log.d(TAG, "like response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {
                JSONObject result = jsonObject.optJSONObject("result");
                if (result.optString("like_status").equals("enable")) {
                    callNewFeedApi();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseUnlikeAPI(String response) {
        Log.d(TAG, "unlike:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {
                callNewFeedApi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equals("posted")) {
                    callNewFeedApi();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private final String TAG = getClass().getName();

    public void parseNewsFeedData(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            Gson gson = new Gson();
            NewsFeedPOJO pojo = gson.fromJson(response, NewsFeedPOJO.class);
            if (pojo != null) {
                Log.d(TAG, "newsfeed result:-" + pojo.toString());
                if (pojo.getSuccess().equals("true")) {
                    if (pojo.getList_pojo().size() > 0) {
//                        NewFeedAdapter adapter=new NewFeedAdapter(this,
//                                pojo.getList_pojo());
//                        LinearLayoutManager horizontalLayoutManagaer
//                                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//                        rv_news_feed.setLayoutManager(horizontalLayoutManagaer);
//                        rv_news_feed.setAdapter(adapter);
                        showNewFeed(pojo.getList_pojo());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "no post to show", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Smething went wrong", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "error:-" + e.toString());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SOmething went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void showNewFeed(final List<NewsFeedPostResultPOJO> list_post_result) {
        ll_scroll_news.removeAllViews();
        for (int i = 0; i < list_post_result.size(); i++) {
            final boolean[] user_likes = new boolean[list_post_result.size()];
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_news_feed, null);
            ImageView iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
            ImageView iv_post_image = (ImageView) view.findViewById(R.id.iv_post_image);
            ImageView iv_like = (ImageView) view.findViewById(R.id.iv_like);
            ImageView iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            ImageView iv_share = (ImageView) view.findViewById(R.id.iv_share);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
            TextView tv_post = (TextView) view.findViewById(R.id.tv_post);
            TextView tv_comment_number = (TextView) view.findViewById(R.id.tv_comment_number);
            TextView tv_like_size = (TextView) view.findViewById(R.id.tv_like_size);
            TextView tv_like = (TextView) view.findViewById(R.id.tv_like);
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);

            LinearLayout ll_like = (LinearLayout) view.findViewById(R.id.ll_like);
            LinearLayout ll_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
            LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
            LinearLayout ll_like_show = (LinearLayout) view.findViewById(R.id.ll_like_show);
            LinearLayout ll_like_comment_status = (LinearLayout) view.findViewById(R.id.ll_like_comment_status);


            tv_name.setText(Pref.GetStringPref(this, StringUtils.LOG_NAME, ""));
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(iv_profile);
            String image_url = Pref.GetStringPref(this, StringUtils.LOG_PICS, "");
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(this)
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_profile);
            tv_post.setText(list_post_result.get(i).getPost_msg());

            tv_type.setText(list_post_result.get(i).getPost_cat_id());
            if (list_post_result.get(i).getPost_image_status().equals("true")) {
                iv_post_image.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(WebServicesUrls.IMAGE_BASE_URL + list_post_result.get(i).getPost_image())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(iv_post_image);
            } else {
                iv_post_image.setVisibility(View.GONE);
            }
            tv_date.setText(list_post_result.get(i).getPost_datetime());
            if (list_post_result.get(i).getUser_likes_status().equals("true")) {
                ll_like_show.setVisibility(View.VISIBLE);
                List<UserLikesResultPOJO> list_user_likes = list_post_result.get(i).getUserLikesResultPOJOList();
                for (UserLikesResultPOJO userLikesResultPOJO : list_user_likes) {
                    if (userLikesResultPOJO.getLike_user_id().equals(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""))) {
                        iv_like.setImageResource(R.drawable.ic_like_active);
                        tv_like.setTextColor(Color.parseColor("#5890ff"));
                        user_likes[i] = true;
                    }
                }
                tv_like_size.setText(list_post_result.get(i).getUserLikesResultPOJOList().size() + " Likes");
                tv_like_size.setTextColor(Color.parseColor("#5890ff"));
                final int finalI1 = i;
                ll_like_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showLikeDialog(list_post_result.get(finalI1).getUserLikesResultPOJOList());
                    }
                });
            } else {
                if (list_post_result.get(i).getUser_comment_status().equals("false")
                        && list_post_result.get(i).getUser_likes_status().equals("false")) {
                    ll_like_comment_status.setVisibility(View.GONE);
                } else {
                    ll_like_show.setVisibility(View.INVISIBLE);
                }
//                iv_like.setImageResource(R.drawable.ic_like_active);
            }
            if (list_post_result.get(i).getUser_comment_status().equals("true")) {
                int comment_size = list_post_result.get(i).getUserCommentResultPOJOList().size();
                tv_comment_number.setText(list_post_result.get(i).getPost_comment() + " Comments");
                final int finalI2 = i;
                tv_comment_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callgetcommentAPI(list_post_result.get(finalI2).getPost_id());
                    }
                });
            } else {
                if (list_post_result.get(i).getUser_comment_status().equals("false")
                        && list_post_result.get(i).getUser_likes_status().equals("false")) {
                    ll_like_comment_status.setVisibility(View.GONE);
                } else {
                }
            }
            final int finalI = i;
            ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user_likes[finalI]) {
                        Log.d(TAG, "unliking");
                        user_likes[finalI] = false;
                        callUnlikeAPI(list_post_result.get(finalI).getPost_id());
                    } else {
                        Log.d(TAG, "liking");
                        user_likes[finalI] = true;
                        callLikeAPI(list_post_result.get(finalI).getPost_id());
                    }
                }
            });
            //finally add view to linear layout.
            ll_scroll_news.addView(view);
        }
    }
    String final_post_id="";
    public void callgetcommentAPI(String post_id){
        final_post_id=post_id;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", post_id));
        new WebServiceBase(nameValuePairs, this, CALL_GET_COMMENT_API).execute(WebServicesUrls.GET_COMMENT_API);
//        showCommentDialog(list_post_result.get(finalI2).getUserCommentResultPOJOList(),
//                list_post_result.get(finalI2).getPost_id());
    }
    public void showCommentDialog(final List<NewsCommentResultPOJO> list_comments, final String post_id) {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.setContentView(R.layout.dialog_comments);
        dialog1.setTitle("User Comments");
        dialog1.show();
        dialog1.setCancelable(true);
        final LinearLayout ll_scroll_news = (LinearLayout) dialog1.findViewById(R.id.ll_scroll_news);
        final EditText et_add_comment = (EditText) dialog1.findViewById(R.id.et_add_comment);
        Button btn_add_comment = (Button) dialog1.findViewById(R.id.btn_add_comment);
        final_comment_linear=ll_scroll_news;
        btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_add_comment.getText().toString().length() > 0) {
//                    String comment_user_id,String comment_post_id,String comment_msg
                    callCommentAPI(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),
                            post_id,et_add_comment.getText().toString()
                            );
                }
            }
        });
        inflateCommentLayout(ll_scroll_news, list_comments);

    }

    public void callCommentAPI(String comment_user_id,String comment_post_id,String comment_msg) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_user_id", comment_user_id));
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", comment_post_id));
        nameValuePairs.add(new BasicNameValuePair("comment_msg", comment_msg));
        new WebServiceBase(nameValuePairs, this, CALL_ADD_COMMENT_API).execute(WebServicesUrls.ADD_COMMENT_API);
    }
    LinearLayout final_comment_linear;
    public void inflateCommentLayout(final LinearLayout ll_scroll_news, final List<NewsCommentResultPOJO> list_comments) {
        for (int i = 0; i < list_comments.size(); i++) {

            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_user_comments, null);
            CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);
            TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);
            ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
            final LinearLayout ll_comment_view= (LinearLayout) view.findViewById(R.id.ll_comment_view);

//            if((i+1)==list_comments.size()){
//                final_comment_linear=
//            }
            final int finalI = i;
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calldeletCommentAPI(ll_scroll_news,ll_comment_view,list_comments.get(finalI).getComment_post_id());
                }
            });

            tv_profile_name.setText(list_comments.get(i).getLog_name());
            tv_comment_message.setText(list_comments.get(i).getComment_msg());
            Picasso.with(this)
                    .load(WebServicesUrls.IMAGE_BASE_URL + list_comments.get(i).getLog_pics())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(ic_profile);

            ll_scroll_news.addView(view);
        }
    }
    LinearLayout final_comment_layout;
    public void calldeletCommentAPI(LinearLayout comment_layout,LinearLayout ll_comment_view,String post_id){
        final_comment_linear=comment_layout;
        final_comment_layout=ll_comment_view;
        final_post_id=post_id;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", post_id));
        new WebServiceBase(nameValuePairs, this, DELETE_COMMENT_API).execute(WebServicesUrls.DELETE_COMMENT_API);
    }

    public void inflateSingleComment(LinearLayout ll_scroll_news,NewsCommentResultPOJO pojo){
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.inflate_user_comments, null);
        CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);
        TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
        TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);

//            if((i+1)==list_comments.size()){
//                final_comment_linear=
//            }
        final_comment_linear=ll_scroll_news;

        tv_profile_name.setText(pojo.getLog_name());
        tv_comment_message.setText(pojo.getComment_msg());
        Picasso.with(this)
                .load(WebServicesUrls.IMAGE_BASE_URL + pojo.getLog_pics())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(ic_profile);

        ll_scroll_news.addView(view);
    }


    public void showLikeDialog(List<UserLikesResultPOJO> list_likes) {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.setContentView(R.layout.dialog_likes);
        dialog1.setTitle("User Likes");
        dialog1.show();
        dialog1.setCancelable(true);
        LinearLayout ll_scroll_news = (LinearLayout) dialog1.findViewById(R.id.ll_scroll_news);
        for (int i = 0; i < list_likes.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_user_likes, null);
            ImageView iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
            TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            Picasso.with(this)
                    .load(WebServicesUrls.IMAGE_BASE_URL + list_likes.get(i).getLog_pics())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_profile);
            tv_profile_name.setText(list_likes.get(i).getLog_name());

            ll_scroll_news.addView(view);
        }


    }

    public void callUnlikeAPI(String like_post_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("like_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("like_post_id", like_post_id));
        new WebServiceBase(nameValuePairs, this, CALL_UNLIKE_API).execute(WebServicesUrls.UNLIKE_API);
    }

    public void callLikeAPI(String like_post_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("like_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("like_post_id", like_post_id));
        new WebServiceBase(nameValuePairs, this, CALL_LIKE_API).execute(WebServicesUrls.ADD_LIKE_API);
    }

    public void callDislikeAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("like_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
//        nameValuePairs.add(new BasicNameValuePair("like_post_id",like_post_id));
//        new WebServiceBase(nameValuePairs, this, CALL_LIKE_API).execute(WebServicesUrls.ADD_LIKE_API);
    }

}
