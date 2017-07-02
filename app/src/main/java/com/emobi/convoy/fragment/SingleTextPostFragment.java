package com.emobi.convoy.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.pojo.newsfeed.UserLikesResultPOJO;
import com.emobi.convoy.pojo.singlenewsfeed.SingleCommentResultPOJO;
import com.emobi.convoy.pojo.singlenewsfeed.SingleNewsFeedPOJO;
import com.emobi.convoy.pojo.singlenewsfeed.SingleNewsFeedResultPOJO;
import com.emobi.convoy.pojo.userlike.LikeResultPOJO;
import com.emobi.convoy.pojo.userlike.UserLikePOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sunil on 08-05-2017.
 */

public class SingleTextPostFragment extends Fragment implements WebServicesCallBack {

    private final String TAG = getClass().getSimpleName();
    private final String SINGLE_POST_API_CALL = "single_post_api_call";
    private final String LIKE_UNLIKE_API_CALL = "like_unlike_api_call";
    private final String ADD_COMMENT_API_CALL = "add_comment_api_call";
    private final String LIKE_SHOW_API = "like_show_api";

    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.civ_profile)
    CircleImageView civ_profile;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_post_msg)
    TextView tv_post_msg;
    @BindView(R.id.ll_like)
    LinearLayout ll_like;
    @BindView(R.id.iv_like)
    ImageView iv_like;
    @BindView(R.id.tv_like)
    TextView tv_like;
    @BindView(R.id.ll_comment)
    LinearLayout ll_comment;
    @BindView(R.id.iv_comment)
    ImageView iv_comment;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.iv_share)
    ImageView iv_share;
    @BindView(R.id.ll_comments)
    LinearLayout ll_comments;
    @BindView(R.id.iv_post_image)
    ImageView iv_post_image;
    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_shares)
    TextView tv_shares;
    @BindView(R.id.tv_share_number)
    TextView tv_share_number;
    @BindView(R.id.tv_comment_number)
    TextView tv_comment_number;
    @BindView(R.id.tv_like_size)
    TextView tv_like_size;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.ll_like_show)
    LinearLayout ll_like_show;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String post_id, image_status;

    public SingleTextPostFragment(String post_id, String image_status) {
        this.post_id = post_id;
        this.image_status = image_status;
    }

    public SingleTextPostFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_single_post, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (image_status.equals("true")) {
//            iv_post_image.setVisibility(View.VISIBLE);
//        } else {
//            iv_post_image.setVisibility(View.GONE);
//        }

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle("post");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed()
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.onBackPressed();
            }
        });
        callSinglePostAPI();


//            tv_post_msg.setText(newsTextPost.getPostMsg());
//            tv_time.setText(newsTextPost.getPostTime());
//            tv_profile_name.setText(newsTextPost.getLogName());
//
//            if(newsTextPost.)
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_comment.getText().toString().length() > 0) {
                    callCommentAPI(et_comment.getText().toString());
                } else {
                    ToastClass.showShortToast(getActivity().getApplicationContext(), "Please enter comment first");
                }
            }
        });
        ll_like_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likes_count>0){
                    callLikeShowAPI();
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"No Likes");
                }
            }
        });
    }

    public void callLikeShowAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("like_post_id", post_id));
        new WebServiceBase(nameValuePairs, getActivity(), this, LIKE_SHOW_API).execute(WebServicesUrls.GET_LIKES);
    }


    public void callCommentAPI(String comment) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", post_id));
        nameValuePairs.add(new BasicNameValuePair("comment_msg", comment));
        new WebServiceBase(nameValuePairs, getActivity(), this, ADD_COMMENT_API_CALL).execute(WebServicesUrls.ADD_COMMENT_API);
    }

    public void callSinglePostAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, getActivity(), this, SINGLE_POST_API_CALL).execute(WebServicesUrls.SINGLE_POST_API_URL);
    }


    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case SINGLE_POST_API_CALL:
                parseSinglePostData(response);
                break;
            case LIKE_UNLIKE_API_CALL:
                parseLikeUnLikeAPI(response);
                break;
            case ADD_COMMENT_API_CALL:
                parseAddCommentResponse(response);
                break;
            case LIKE_SHOW_API:
                parseLikeShowAPI(response);
                break;
        }
    }
    public void parseLikeShowAPI(String response){
        Log.d(TAG,"like show api:-"+response);
        try{
            Gson gson=new Gson();
            UserLikePOJO userLikePOJO=gson.fromJson(response,UserLikePOJO.class);
            if(userLikePOJO.getSucces().equals("true")){
                showLikeDialog(userLikePOJO.getUserLikesResultPOJOList());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void parseAddCommentResponse(String response) {
        Log.d(TAG, "add comment response:-" + response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {
                SingleCommentResultPOJO singleCommentResultPOJO = new SingleCommentResultPOJO(
                        jsonObject.optJSONObject("comment_data").optString("comment_id"),
                        jsonObject.optJSONObject("comment_data").optString("comment_user_id"),
                        jsonObject.optJSONObject("comment_data").optString("comment_post_id"),
                        jsonObject.optJSONObject("comment_data").optString("comment_msg"),
                        jsonObject.optJSONObject("comment_data").optString("date"),
                        jsonObject.optJSONObject("comment_data").optString("time"),
                        jsonObject.optJSONObject("user_data").optString("log_name"),
                        jsonObject.optJSONObject("user_data").optString("log_pics")
                );
                inflateSingleComments(singleCommentResultPOJO);
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
        }
        et_comment.setText("");
    }

    public void parseLikeUnLikeAPI(String response) {
        Log.d(TAG, "like response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                String message = jsonObject.optString("message");
                if (message.equals("liked")) {
                    likes_count++;
                    tv_like_size.setText(likes_count + " likes");
                    iv_like.setImageResource(R.drawable.ic_like_active);
                } else {
                    if (message.equals("unliked")) {
                        likes_count--;
                        tv_like_size.setText(likes_count + " likes");
                        iv_like.setImageResource(R.drawable.ic_like);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int likes_count = -1;

    public void parseSinglePostData(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            Gson gson = new Gson();
            SingleNewsFeedPOJO singleNewsFeedPOJO = gson.fromJson(response, SingleNewsFeedPOJO.class);
            if (singleNewsFeedPOJO.getSuccess().equals("true")) {
                SingleNewsFeedResultPOJO singleNewsFeedResultPOJO = singleNewsFeedPOJO.getSingleNewsFeedResultPOJO();
                tv_profile_name.setText(singleNewsFeedResultPOJO.getLogName());
                tv_time.setText(singleNewsFeedResultPOJO.getPostTime());
                tv_post_msg.setText(singleNewsFeedResultPOJO.getPostMsg());
                Glide.with(getActivity())
                        .load(WebServicesUrls.IMAGE_BASE_URL + singleNewsFeedResultPOJO.getLogPics())
                        .error(R.drawable.ic_profile)
                        .into(civ_profile);
                if (image_status.equals("true")) {
                    Log.d(TAG, "image url:-" + WebServicesUrls.IMAGE_BASE_URL + singleNewsFeedResultPOJO.getPostImage());
                    Glide.with(getActivity())
                            .load(WebServicesUrls.IMAGE_BASE_URL + singleNewsFeedResultPOJO.getPostImage())
                            .into(iv_post_image);
//                    iv_post_image.setVisibility(View.VISIBLE);
                    Log.d(TAG, "image true");
                } else {
//                    iv_post_image.setVisibility(View.GONE);
                    Log.d(TAG, "image false");
                }

                if (singleNewsFeedResultPOJO.getCommentsStatus()) {
                    inflateComments(singleNewsFeedResultPOJO.getSingleCommentResultPOJOList());

                } else {
                    Log.d(TAG, "comments false");
                }

                if (singleNewsFeedResultPOJO.getLiked()) {
                    iv_like.setImageResource(R.drawable.ic_like_active);
                }
                try {
                    int total_likes = Integer.parseInt(singleNewsFeedResultPOJO.getLikesCount());
                    if (total_likes > 0) {
                        tv_like_size.setText(total_likes + " likes");
                        likes_count = total_likes;
                    } else {
                        likes_count = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    likes_count = 0;
                }
                try {
                    int total_comments = Integer.parseInt(singleNewsFeedResultPOJO.getTotalComments());
                    if (total_comments > 0) {
                        tv_comment_number.setText(total_comments + " comments");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    int total_shares = Integer.parseInt(singleNewsFeedResultPOJO.getShareCount());
                    if (total_shares > 0) {
                        tv_share_number.setText(total_shares + " shares");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ll_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callLikeUnlikeAPI();
                    }
                });
            } else {
                ToastClass.showShortToast(getActivity().getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLikeDialog(List<LikeResultPOJO> list_likes) {
        final Dialog dialog1 = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.setContentView(R.layout.dialog_likes);
        dialog1.setTitle("User Likes");
        dialog1.show();
        dialog1.setCancelable(true);
        LinearLayout ll_scroll_news = (LinearLayout) dialog1.findViewById(R.id.ll_scroll_news);
        for (int i = 0; i < list_likes.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_user_likes, null);
            ImageView iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
            TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            Picasso.with(getActivity())
                    .load(WebServicesUrls.IMAGE_BASE_URL + list_likes.get(i).getLog_pics())
                    .error(R.drawable.ic_profile)
                    .into(iv_profile);
            tv_profile_name.setText(list_likes.get(i).getLog_name());

            ll_scroll_news.addView(view);
        }


    }

    public void callLikeUnlikeAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, getActivity(), this, LIKE_UNLIKE_API_CALL).execute(WebServicesUrls.LIKE_UNLIKE_URL);
    }

    public void inflateComments(List<SingleCommentResultPOJO> singleCommentResultPOJOList) {

        ll_comments.removeAllViews();

        for (int i = 0; i < singleCommentResultPOJOList.size(); i++) {

            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_user_comments, null);
            TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);
            CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);

            Glide.with(getActivity())
                    .load(WebServicesUrls.IMAGE_BASE_URL + singleCommentResultPOJOList.get(i).getLogPics())
                    .error(R.drawable.ic_profile)
                    .into(ic_profile);

            tv_profile_name.setText(singleCommentResultPOJOList.get(i).getLogName());
            tv_comment_message.setText(singleCommentResultPOJOList.get(i).getCommentMsg());

            ll_comments.addView(view);
        }
    }

    public void inflateSingleComments(SingleCommentResultPOJO singleCommentResultPOJO) {

        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.inflate_user_comments, null);
        TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
        TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);
        CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);

        Glide.with(getActivity())
                .load(WebServicesUrls.IMAGE_BASE_URL + singleCommentResultPOJO.getLogPics())
                .error(R.drawable.ic_profile)
                .into(ic_profile);

        tv_profile_name.setText(singleCommentResultPOJO.getLogName());
        tv_comment_message.setText(singleCommentResultPOJO.getCommentMsg());

        ll_comments.addView(view);
    }
}
