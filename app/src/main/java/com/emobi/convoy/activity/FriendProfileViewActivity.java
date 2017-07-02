package com.emobi.convoy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.pojo.addvisitor.AddVisitorPOJO;
import com.emobi.convoy.pojo.visitor.VisitorResultPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendProfileViewActivity extends AppCompatActivity implements WebServicesCallBack {

    private final String TAG = getClass().getSimpleName();
    private final String FRIEND_REQUEST_API = "friend_request_send_api";
    private final String DELETE_FRIEND_API = "delete_friend_api";
    private final String ADD_VISITOR_API = "add_visitor_api";

    RegisterPOJO registerPOJO;
    VisitorResultPOJO visitorResultPOJO;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.tv_profile_name_txt)
    TextView tv_profile_name_txt;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_friend_count)
    TextView tv_friend_count;
    @BindView(R.id.cv_profile)
    CircleImageView cv_profile;
    @BindView(R.id.btn_add_friend)
    Button btn_add_friend;

    String friend_list = "";
    String visitor_id = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        tv_title.setText("Friend Profile");


        friend_list = getIntent().getStringExtra("frientlist");
        visitor_id = getIntent().getStringExtra("visitor_id");

        callAddVisitorAPI();


        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void callAddVisitorAPI() {
        if (visitor_id.length() > 0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            nameValuePairs.add(new BasicNameValuePair("search_id", visitor_id));
            SimpleDateFormat sdf_date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a");
            Date date = new Date();
            String stringdate = sdf_date.format(date);
            String stringtime = sdf_time.format(date);
            Log.d(TAG, "date:-" + stringdate);
            Log.d(TAG, "time:-" + stringtime);
            nameValuePairs.add(new BasicNameValuePair("visit_time", stringtime));
            nameValuePairs.add(new BasicNameValuePair("visit_date", stringdate));
            new WebServiceBase(nameValuePairs, this, ADD_VISITOR_API).execute(WebServicesUrls.ADD_VISITOR);
        }
    }

    public void DeleteFriendListener() {
        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriendAPI();
            }
        });
    }

    public void AddFriendListener() {
        btn_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddFriendAPI();
            }
        });
    }

    public void callAddFriendAPI() {
        if (visitor_id.length() > 0&& addVisitorPOJO!=null) {
                Intent intent=new Intent(FriendProfileViewActivity.this,FriendQuestionActivity.class);
                intent.putExtra("addvisitorpojo",addVisitorPOJO);
            startActivityForResult(intent, 1);
//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("friend_login_user", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
//            nameValuePairs.add(new BasicNameValuePair("friend_accept", visitor_id));
//            new WebServiceBase(nameValuePairs, this, FRIEND_REQUEST_API).execute(WebServicesUrls.SEND_FRIEND_REQUEST);
        }
    }

    public void deleteFriendAPI() {
        if (visitor_id.length() > 0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("friend_login_user", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            nameValuePairs.add(new BasicNameValuePair("friend_accept", visitor_id));
            new WebServiceBase(nameValuePairs, this, DELETE_FRIEND_API).execute(WebServicesUrls.DELETE_FRIEND);
        }
    }


    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case FRIEND_REQUEST_API:
                parseFriendRequest(response);
                break;
            case DELETE_FRIEND_API:
                parseDeleteFriendAPI(response);
                break;
            case ADD_VISITOR_API:
                parseAddVisitorAPI(response);
                break;
        }
    }
    AddVisitorPOJO addVisitorPOJO;
    public void parseAddVisitorAPI(String response) {
        Log.d(TAG,"visitor response:-"+response);
        try {
            Gson gson = new Gson();
            addVisitorPOJO = gson.fromJson(response, AddVisitorPOJO.class);
            if (addVisitorPOJO.getSuccess().equals("true")) {
                Glide.with(getApplicationContext())
                        .load(WebServicesUrls.IMAGE_BASE_URL + addVisitorPOJO.getAddVisitorResultPOJO().getLogPics())
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(cv_profile);
                tv_profile_name.setText(addVisitorPOJO.getAddVisitorResultPOJO().getLogName());
                tv_profile_name_txt.setText(addVisitorPOJO.getAddVisitorResultPOJO().getLogName());
                tv_phone.setText(addVisitorPOJO.getAddVisitorResultPOJO().getLogMob());
                tv_email.setText(addVisitorPOJO.getAddVisitorResultPOJO().getLogEmail());
                tv_friend_count.setText(String.valueOf(addVisitorPOJO.getAddVisitorResultPOJO().getNooffriends()));

                if(addVisitorPOJO.getAddVisitorResultPOJO().getFriend().equals("friend")){
                    btn_add_friend.setText("Delete Friend");
                    DeleteFriendListener();
                }else {
                    if(addVisitorPOJO.getAddVisitorResultPOJO().getFriend().equals("received")){
                        btn_add_friend.setText("Friend Request Received");
                        DeleteFriendListener();
                    }else{
                        if(addVisitorPOJO.getAddVisitorResultPOJO().getFriend().equals("sent")){
                            btn_add_friend.setText("Friend Request sent");
                            DeleteFriendListener();
                        }else{
                            btn_add_friend.setText("Add Friend");
                            AddFriendListener();
                        }
                    }
                }

            } else {
                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void parseFriendRequest(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String friend_id = jsonObject.optString("friend_id");
            String friend_login_user = jsonObject.optString("friend_login_user");
            String friend_accept = jsonObject.optString("friend_accept");
            String friend_status = jsonObject.optString("friend_status");
            String friend_message = jsonObject.optString("friend_message");
            btn_add_friend.setText("cancel Friend Request");
            DeleteFriendListener();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public void parseDeleteFriendAPI(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {
                btn_add_friend.setText("Add Friend");
                AddFriendListener();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                switch (result){
                    case "0":
                        break;
                    case "1":
                        btn_add_friend.setText("cancel Friend Request");
                        DeleteFriendListener();
                        break;
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}

