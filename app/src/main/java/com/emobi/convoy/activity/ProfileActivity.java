package com.emobi.convoy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.pojo.newscomment.NewsCommentResultPOJO;
import com.emobi.convoy.pojo.newsfeed.NewsFeedPOJO;
import com.emobi.convoy.pojo.newsfeed.NewsFeedPostResultPOJO;
import com.emobi.convoy.pojo.newsfeed.UserCommentResultPOJO;
import com.emobi.convoy.pojo.newsfeed.UserLikesResultPOJO;
import com.emobi.convoy.pojo.usercomment.UserCommentPOJO;
import com.emobi.convoy.utility.ImageUtil;
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

import static com.emobi.convoy.webservices.WebServicesUrls.UPDATE_COVER_PICS;

public class ProfileActivity extends AppCompatActivity implements WebServicesCallBack, View.OnClickListener {
    private final static String NEWS_FEED_API = "news_feed_api";
    private final static String UPDATE_PROFILE_API = "update_profile_api";
    private static final int FILE_SELECT_CODE = 0;
    private static final int COVER_FILE_SELECT_CODE = 1;
    private static final String CALL_LIKE_API = "call_like_api";
    private static final String CALL_ADD_COMMENT_API = "call_add_comment_api";
    private static final String SHARE_POST_API = "share_post_api";
    private static final String UPDATE_COVER_PHOTO = "update_cover_photo";
    private static final String CALL_GET_COMMENT_API = "call_get_comment_api";

    @BindView(R.id.ll_scroll_news)
    LinearLayout ll_scroll_news;
    @BindView(R.id.iv_cover_pic)
    ImageView iv_cover_pic;
    @BindView(R.id.tv_friends)
    TextView tv_friends;
    @BindView(R.id.tv_about)
    TextView tv_about;
    @BindView(R.id.tv_edit)
    ImageView tv_edit;

    @BindView(R.id.iv_profile_pic)
    CircleImageView iv_profile_pic;
    @BindView(R.id.fab_new_post)
    FloatingActionButton fab_new_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Log.d(TAG, "profile_id:-" + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""));


        fab_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, NewPostActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadViews();
        callNewFeedApi(false);
    }

    public void loadViews() {
        Log.d(TAG, "loading views");
        Picasso.with(getApplicationContext())
                .load(WebServicesUrls.IMAGE_BASE_URL + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""))
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(iv_profile_pic);

        Glide.with(getApplicationContext())
                .load(WebServicesUrls.IMAGE_BASE_URL + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_COVER_PHOTO, ""))
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .dontAnimate()
                .into(iv_cover_pic);
        iv_profile_pic.setOnClickListener(this);
        iv_cover_pic.setOnClickListener(this);


        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AboutActivity.class);
                intent.putExtra("profilepic", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""));
                intent.putExtra("name", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, ""));
                intent.putExtra("gender", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_GEN, ""));
                intent.putExtra("dob", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_DOB, ""));
                intent.putExtra("fblink", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, ""));
                intent.putExtra("twitterlink", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_TWITTER, ""));
                intent.putExtra("email", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, ""));
                intent.putExtra("bio", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_BIO, ""));
                startActivity(intent);
            }
        });

        tv_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, FriendListActivity.class));
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
    }

    public void callNewFeedApi(boolean is_dialog) {
//        callNewFeedApi();
        Log.d(TAG, "profile_pic:-" + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""));
        Picasso.with(getApplicationContext())
                .load(WebServicesUrls.IMAGE_BASE_URL + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""))
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(iv_profile_pic);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, NEWS_FEED_API,is_dialog).execute(WebServicesUrls.NEWS_FEED_API);
    }

    public void SelectPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, FILE_SELECT_CODE);
    }

    public void SelectCoverPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, COVER_FILE_SELECT_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            Log.d("sun", "on activity result");
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    Log.d(TAG, "image path:-" + selectedImagePath);
                    Bitmap bmImg = BitmapFactory.decodeFile(selectedImagePath);
                    if (bmImg != null) {
                        Glide.with(getApplicationContext())
                                .load(selectedImagePath)
                                .error(R.drawable.ic_profile)
                                .into(iv_profile_pic);
                        UpdateImageProfile(ImageUtil.encodeTobase64(bmImg));
                    }
                } else {
                    Toast.makeText(this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        } else {
            if (requestCode == COVER_FILE_SELECT_CODE) {
                Log.d(TAG, "on cover photo");
                if (resultCode == Activity.RESULT_OK) {
                    if (null == data)
                        return;
                    Uri selectedImageUri = data.getData();
                    System.out.println(selectedImageUri.toString());
                    // MEDIA GALLERY
                    String selectedImagePath = getPath(
                            this, selectedImageUri);
                    Log.d("sun", "" + selectedImagePath);
                    if (selectedImagePath != null && selectedImagePath != "") {
                        Log.d(TAG, "image path:-" + selectedImagePath);
                        Bitmap bmImg = BitmapFactory.decodeFile(selectedImagePath);
                        if (bmImg != null) {
                            Glide.with(getApplicationContext())
                                    .load(bmImg)
                                    .error(R.drawable.ic_profile)
                                    .into(iv_cover_pic);
                            UpdateCoverPhoto(ImageUtil.encodeTobase64(bmImg));

                        }
                    } else {
                        Toast.makeText(this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Image Path =" + selectedImagePath);
                }
            }
        }
    }

    public void UpdateImageProfile(String image_base_64) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("log_pics", image_base_64));
        nameValuePairs.add(new BasicNameValuePair("log_password", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD, "")));
        nameValuePairs.add(new BasicNameValuePair("log_name", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, "")));
        nameValuePairs.add(new BasicNameValuePair("log_email", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, "")));
        nameValuePairs.add(new BasicNameValuePair("log_mob", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE, "")));
        nameValuePairs.add(new BasicNameValuePair("log_facbook", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, "")));
        nameValuePairs.add(new BasicNameValuePair("log_tag", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_TAG, "")));
        nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN, "")));
        nameValuePairs.add(new BasicNameValuePair("log_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, UPDATE_PROFILE_API).execute(WebServicesUrls.UPDATE_PROFILE_API);
    }

    public void UpdateCoverPhoto(String image_base_64) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("log_cover_photo", image_base_64));
        nameValuePairs.add(new BasicNameValuePair("album_type", ""));
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, UPDATE_COVER_PHOTO).execute(UPDATE_COVER_PICS);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];

        switch (apicall) {
            case NEWS_FEED_API:
                parseNewsFeedData(response);
                break;
            case UPDATE_PROFILE_API:
                Log.d(TAG, "update response:-" + response);
                parseUpdateProfileView(response);
                break;
            case CALL_LIKE_API:
                parseCallLikeAPI(response);
                break;
            case CALL_ADD_COMMENT_API:
                parseAddCommentAPI(response);
                break;
            case SHARE_POST_API:
                parseShareAPI(response);
                break;
            case UPDATE_COVER_PHOTO:
                parseCoverPhoto(response);
                break;
            case CALL_GET_COMMENT_API:
                parseCommentResponse(response);
                break;
        }
    }


    public void parseCommentResponse(String response) {
        Log.d(TAG, "comment response:-" + response);
        try {
            Gson gson = new Gson();
            UserCommentPOJO newsComment = gson.fromJson(response, UserCommentPOJO.class);
            Log.d(TAG, "comments news:-" + newsComment.toString());
            if (comment_layout != null) {
                inflateCommentLayout(comment_layout, newsComment.getUserCommentResultPOJOList());
            } else {
                Log.d(TAG, "comment is null");
            }
//                showCommentDialog(newsComment.getNewsCommentResultPOJOList(),final_post_id);
        } catch (Exception e) {
            Log.d(TAG, "comments error:-" + e.toString());
            e.printStackTrace();
        }
    }

    public void parseCoverPhoto(String response) {
        Log.d(TAG, "cover response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                Gson gson = new Gson();
                RegisterPOJO pojo = gson.fromJson(response, RegisterPOJO.class);
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID,pojo.getLog_id());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME,pojo.getLog_name());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL,pojo.getLog_email());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEN,pojo.getLog_gen());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DOB,pojo.getLog_dob());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD,pojo.getLog_password());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE,pojo.getLog_mob());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK,pojo.getLog_facbook());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TWITTER,pojo.getLog_twitter());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_BIO,pojo.getLog_bio());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LOC,pojo.getLog_loc());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TAG,pojo.getLog_tag());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LAST_LOGIN,pojo.getLog_last_login());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CREATED,pojo.getLog_created());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PICS,pojo.getLog_pics());
                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_COVER_PHOTO, pojo.getLog_cover_photo());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ROOM,pojo.getLog_room());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN,pojo.getLog_device_token());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ENTERTAINMENT,pojo.getLog_entertainment());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_SPORTS,pojo.getLog_sports());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TRAVELLING,pojo.getLog_travelling());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STUDY,pojo.getLog_study());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GAMING,pojo.getLog_gaming());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TECHNOLOGY,pojo.getLog_technology());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ACTION,pojo.getLog_action());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EVERYTHING,pojo.getLog_everything());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FAMILY,pojo.getLog_family());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS1,pojo.getLog_ans1());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS2,pojo.getLog_ans2());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS3,pojo.getLog_ans3());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS4,pojo.getLog_ans4());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS5,pojo.getLog_ans5());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MESSAGE,pojo.getLog_message());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STATUS,pojo.getLog_status());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VISIBILITY,pojo.getVisibility());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VIDEO_CALL_STATUS,pojo.getLog_videocall_status());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_OS_TYPE,pojo.getLog_os_type());
//                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEO_LOCATION,pojo.getLog_geo_location());
//                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN,true);

            } else {
                ToastClass.showShortToast(getApplicationContext(), "Failed to update");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void parseShareAPI(String response) {
        ToastClass.showShortToast(getApplicationContext(), "You shared this post");
        callNewFeedApi(false);
    }


    public void parseAddCommentAPI(String response) {
        Log.d(TAG, "add comment:-" + response);
        try {
            JSONObject object = new JSONObject(response);
            if (object.optString("success").equals("true")) {
                callNewFeedApi(true);

                JSONObject result = object.optJSONObject("result");
                Gson gson = new Gson();
                NewsCommentResultPOJO userCommentResultPOJO = gson.fromJson(result.toString(), NewsCommentResultPOJO.class);
                userCommentResultPOJO.setLog_name(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, ""));
                userCommentResultPOJO.setLog_pics(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""));
//                try {
                    inflateSingleComment(comment_layout, userCommentResultPOJO);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCallLikeAPI(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                String message = jsonObject.optString("message");
                if (message.equals("liked")) {
                    ToastClass.showShortToast(getApplicationContext(), "You Liked the post");
                } else {
                    if (message.equals("unliked")) {
                        ToastClass.showShortToast(getApplicationContext(), "You Unliked the post");
                    }
                }
                callNewFeedApi(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseUpdateProfileView(String response) {
        Log.d(TAG,"update response:-"+response);
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                JSONObject result = jsonObject.optJSONObject("result");
                RegisterPOJO registerPOJO = gson.fromJson(result.toString(), RegisterPOJO.class);
                updateProfile(registerPOJO);
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Please Try Again Later.");
            }
        } catch (Exception e) {
            ToastClass.showShortToast(getApplicationContext(), "Please Try Again Later.");
            e.printStackTrace();
        }
    }

    public void updateProfile(RegisterPOJO pojo) {
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID, pojo.getLog_id());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME, pojo.getLog_name());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, pojo.getLog_email());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEN, pojo.getLog_gen());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DOB, pojo.getLog_dob());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD, pojo.getLog_password());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE, pojo.getLog_mob());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, pojo.getLog_facbook());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TWITTER, pojo.getLog_twitter());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_BIO, pojo.getLog_bio());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LOC, pojo.getLog_loc());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TAG, pojo.getLog_tag());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LAST_LOGIN, pojo.getLog_last_login());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CREATED, pojo.getLog_created());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PICS, pojo.getLog_pics());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_COVER_PHOTO, pojo.getLog_cover_photo());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ROOM, pojo.getLog_room());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN, pojo.getLog_device_token());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ENTERTAINMENT, pojo.getLog_entertainment());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_SPORTS, pojo.getLog_sports());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TRAVELLING, pojo.getLog_travelling());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STUDY, pojo.getLog_study());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GAMING, pojo.getLog_gaming());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TECHNOLOGY, pojo.getLog_technology());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ACTION, pojo.getLog_action());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EVERYTHING, pojo.getLog_everything());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FAMILY, pojo.getLog_family());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS1, pojo.getLog_ans1());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS2, pojo.getLog_ans2());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS3, pojo.getLog_ans3());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS4, pojo.getLog_ans4());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS5, pojo.getLog_ans5());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MESSAGE, pojo.getLog_message());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STATUS, pojo.getLog_status());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VISIBILITY, pojo.getVisibility());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VIDEO_CALL_STATUS, pojo.getLog_videocall_status());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_OS_TYPE, pojo.getLog_os_type());
        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEO_LOCATION, pojo.getLog_geo_location());
        Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, true);


        loadViews();
        ToastClass.showShortToast(getApplicationContext(), "Profile Updated Successfully");
    }

    private final String TAG = getClass().getName();

    public void parseNewsFeedData(String response) {
        Log.d(TAG, "news feed response:-" + response);
        try {
            Gson gson = new Gson();
            NewsFeedPOJO pojo = gson.fromJson(response, NewsFeedPOJO.class);
            if (pojo != null) {
                Log.d(TAG, "newsfeed result:-" + pojo.toString());
                if (pojo.getSuccess().equals("true")) {
                    if (pojo.getList_pojo().size() > 0) {
                        showNewFeed(pojo.getList_pojo());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "no post to show", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Smething went wrong", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "error:-" + e.toString());
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

            CardView card_feed = (CardView) view.findViewById(R.id.card_feed);

            final int finalI2 = i;
            card_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "post_id:-" + list_post_result.get(finalI2).getPost_id());
                }
            });

            if (list_post_result.get(i).getUser_share().equals("true")) {
                String share_text = Pref.GetStringPref(this, StringUtils.LOG_NAME, "") + " shared " +
                        list_post_result.get(i).getShare_user_name() + " post's";
                tv_name.setText(share_text);
            } else {
                tv_name.setText(Pref.GetStringPref(this, StringUtils.LOG_NAME, ""));
            }
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(iv_profile);
            String image_url = Pref.GetStringPref(this, StringUtils.LOG_PICS, "");
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(this)
                    .load(WebServicesUrls.IMAGE_BASE_URL + image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(iv_profile);
            tv_post.setText(list_post_result.get(i).getPost_msg());
//            try{
//
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//                Date d=simpleDateFormat.parse(list_post_result.get(i).getPost_datetime());
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            tv_date.setText(list_post_result.get(i).getPost_date()+" "+
            list_post_result.get(i).getPost_time());
//            tv_type.setText(list_post_result.get(i).getPost_cat_id());
            tv_type.setText("");
            if (list_post_result.get(i).getPost_image_status().equals("true")) {
                Picasso.with(this)
                        .load(WebServicesUrls.IMAGE_BASE_URL + list_post_result.get(i).getPost_image())
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .into(iv_post_image);
                iv_post_image.setVisibility(View.VISIBLE);
            } else {
                iv_post_image.setVisibility(View.GONE);
            }
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
//                if (list_post_result.get(i).getUser_comment_status().equals("false")
//                        && list_post_result.get(i).getUser_likes_status().equals("false")) {
////                    ll_like_comment_status.setVisibility(View.GONE);
//                } else {
////                    ll_like_show.setVisibility(View.INVISIBLE);
//                }
//                iv_like.setImageResource(R.drawable.ic_like_active);
            }
            ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callShareAPI(list_post_result.get(finalI2).getPost_id(),
                            Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""),
                            list_post_result.get(finalI2).getLog_name());
                }
            });
            if (list_post_result.get(i).getUser_comment_status().equals("true")) {
                int comment_size = list_post_result.get(i).getUserCommentResultPOJOList().size();
                tv_comment_number.setText(comment_size + " Comments");

                tv_comment_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCommentDialog(list_post_result.get(finalI2).getPost_id(), list_post_result.get(finalI2).getUserCommentResultPOJOList());
                    }
                });
            } else {
                if (list_post_result.get(i).getUser_comment_status().equals("false")
                        && list_post_result.get(i).getUser_likes_status().equals("false")) {
//                    ll_like_comment_status.setVisibility(View.GONE);
                } else {
                }
            }
            ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog(list_post_result.get(finalI2).getPost_id(), list_post_result.get(finalI2).getUserCommentResultPOJOList());
                }
            });
            final int finalI = i;
            ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user_likes[finalI]) {
                        Log.d(TAG, "unliking");
                        user_likes[finalI] = false;
                        callLikeAPI(list_post_result.get(finalI).getPost_id());
                    } else {
                        Log.d(TAG, "liking");
                        user_likes[finalI] = true;
                        callLikeAPI(list_post_result.get(finalI).getPost_id());
                    }
                }
            });
            tv_comment_number.setText(list_post_result.get(i).getPost_comment()+" comments");
            //finally add view to linear layout.
            ll_scroll_news.addView(view);
        }
    }

    public void callShareAPI(String post_id, String user_id, String share_name) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("post_id", post_id));
        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("share_name", share_name));
        new WebServiceBase(nameValuePairs, this, SHARE_POST_API).execute(WebServicesUrls.SHARE_API_URL);

    }

    public void showCommentDialog(final String post_id, List<UserCommentResultPOJO> list_comments) {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog1.setContentView(R.layout.dialog_comments);
        dialog1.setTitle("User Comments");
        dialog1.show();
        dialog1.setCancelable(true);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout ll_scroll_news = (LinearLayout) dialog1.findViewById(R.id.ll_scroll_news);
        final EditText et_add_comment = (EditText) dialog1.findViewById(R.id.et_add_comment);
        Button btn_add_comment = (Button) dialog1.findViewById(R.id.btn_add_comment);

        btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_add_comment.getText().toString().length() > 0) {
                    callCommentAPI(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")
                            , post_id, et_add_comment.getText().toString());
                    et_add_comment.setText("");
                }
            }
        });
        comment_layout = ll_scroll_news;
        callgetcommentAPI(post_id);
//        try {
//            inflateCommentLayout(ll_scroll_news, list_comments);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    LinearLayout comment_layout;

    public void callCommentAPI(String comment_user_id, String comment_post_id, String comment_msg) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_user_id", comment_user_id));
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", comment_post_id));
        nameValuePairs.add(new BasicNameValuePair("comment_msg", comment_msg));
        new WebServiceBase(nameValuePairs, this, CALL_ADD_COMMENT_API).execute(WebServicesUrls.ADD_COMMENT_API);
    }


    public void callgetcommentAPI(String post_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("comment_post_id", post_id));
        new WebServiceBase(nameValuePairs, this, CALL_GET_COMMENT_API).execute(WebServicesUrls.GET_COMMENT_API);
    }

    LinearLayout final_comment_layout;

    public void inflateSingleComment(LinearLayout ll_scroll_news, NewsCommentResultPOJO pojo) {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.inflate_user_comments, null);
        CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);
        TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
        TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);

//            if((i+1)==list_comments.size()){
//                final_comment_linear=
//            }
//        final_comment_linear=ll_scroll_news;

        tv_profile_name.setText(pojo.getLog_name());
        tv_comment_message.setText(pojo.getComment_msg());
        Picasso.with(this)
                .load(WebServicesUrls.IMAGE_BASE_URL + pojo.getLog_pics())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(ic_profile);

        ll_scroll_news.addView(view);
    }

    public void inflateCommentLayout(LinearLayout ll_scroll_news, List<com.emobi.convoy.pojo.usercomment.UserCommentResultPOJO> list_comments) {
        final_comment_layout = ll_scroll_news;
        for (int i = 0; i < list_comments.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_user_comments, null);
            CircleImageView ic_profile = (CircleImageView) view.findViewById(R.id.ic_profile);
            TextView tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            TextView tv_comment_message = (TextView) view.findViewById(R.id.tv_comment_message);

            tv_profile_name.setText(list_comments.get(i).getLogName());
            tv_comment_message.setText(list_comments.get(i).getCommentMsg());
            Picasso.with(this)
                    .load(WebServicesUrls.IMAGE_BASE_URL + list_comments.get(i).getLogPics())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(ic_profile);
            Log.d(TAG, "comment:-" + list_comments.get(i).toString());
            ll_scroll_news.addView(view);
        }
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

    public void callLikeAPI(String like_post_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("post_id", like_post_id));
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_LIKE_API).execute(WebServicesUrls.LIKE_UNLIKE_URL);
    }

    public void callDislikeAPI(String like_post_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("post_id", like_post_id));
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_LIKE_API).execute(WebServicesUrls.LIKE_UNLIKE_URL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_profile_pic:
                SelectPictureFromGallery();
                break;
            case R.id.iv_cover_pic:
                Log.d(TAG, "cover update");
                SelectCoverPictureFromGallery();
                break;

        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }
}
