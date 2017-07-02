package com.emobi.convoy.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.friendrequest.FrienRequestResultPOJO;
import com.emobi.convoy.pojo.friendrequest.FriendRequestPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestActivity extends AppCompatActivity implements WebServicesCallBack{

    private final String TAG=getClass().getSimpleName();
    private final static String GET_FRIEND_REQUEST_API="get_friend_request_api";
    private final static String DELETE_FRIEND_REQUEST="delete_friend_request_api";
    private final static String ACCEPT_FRIEND_REQUEST="accept_friend_request_api";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_scroll)
    LinearLayout ll_scroll;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tv_title.setText("Friend Request");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        MobileAds.initialize(this, "ca-app-pub-1163685788193118~7085127386");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4E448BFE118E3DBD390404E63D74029A")
                .build();
        mAdView.loadAd(adRequest);
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
            getFriendRequestList();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        getFriendRequestList();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.FRIEND_REQUEST));
    }
    public void getFriendRequestList(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("friend_accept", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, GET_FRIEND_REQUEST_API).execute(WebServicesUrls.GET_FRIEND_REQUEST);
    }

    public void DeleteFriendRequest(FrienRequestResultPOJO pojo){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("friend_login_user", pojo.getLog_id()));
        nameValuePairs.add(new BasicNameValuePair("friend_accept", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, DELETE_FRIEND_REQUEST).execute(WebServicesUrls.DELETE_FRIEND);
    }

    public void AcceptFriendRequest(FrienRequestResultPOJO pojo){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("friend_login_user", pojo.getLog_id()));
        nameValuePairs.add(new BasicNameValuePair("friend_accept", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, ACCEPT_FRIEND_REQUEST).execute(WebServicesUrls.UPDATE_FRIEND_API);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case GET_FRIEND_REQUEST_API:parseFriendRequestAPI(response);
                break;
            case DELETE_FRIEND_REQUEST:parseDeleteFriendAPI(response);
                break;
            case ACCEPT_FRIEND_REQUEST:parseAcceptFriendAPI(response);
                break;
        }
    }

    public void parseDeleteFriendAPI(String response){
        Log.d(TAG,"delete friend api:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("true")){
                Toast.makeText(getApplicationContext(),"friend Request deleted",Toast.LENGTH_LONG).show();
                getFriendRequestList();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseAcceptFriendAPI(String response){
        Log.d(TAG,"accept friend api:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("true")){
                Toast.makeText(getApplicationContext(),"friend Request accepted",Toast.LENGTH_LONG).show();
                getFriendRequestList();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseFriendRequestAPI(String response){
            Log.d(TAG,"response:-"+response);
        try{
            Gson gson=new Gson();
            FriendRequestPOJO pojo=gson.fromJson(response,FriendRequestPOJO.class);
            inflateRequest(pojo.getFrienRequestResultPOJOs());
        }
        catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","ok");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void inflateRequest(List<FrienRequestResultPOJO> frienRequestResultPOJOList){
        ll_scroll.removeAllViews();
        for (int i = 0; i < frienRequestResultPOJOList.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_friend_request, null);
            CircleImageView cv_profile= (CircleImageView) view.findViewById(R.id.cv_profile);
            TextView tv_profile_name= (TextView) view.findViewById(R.id.tv_profile_name);
            TextView tv_email= (TextView) view.findViewById(R.id.tv_email);
            Button btn_accept= (Button) view.findViewById(R.id.btn_accept);
            Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);

            final FrienRequestResultPOJO requestResultPOJO=frienRequestResultPOJOList.get(i);

            Glide.with(getApplicationContext()).
                    load(WebServicesUrls.IMAGE_BASE_URL+frienRequestResultPOJOList.get(i).getLog_pics())
                    .into(cv_profile);

            tv_profile_name.setText(frienRequestResultPOJOList.get(i).getLog_name());
            tv_email.setText(frienRequestResultPOJOList.get(i).getLog_email());

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AcceptFriendRequest(requestResultPOJO);
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteFriendRequest(requestResultPOJO);
                }
            });


            ll_scroll.addView(view);
        }
    }

}
