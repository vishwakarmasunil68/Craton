package com.emobi.convoy.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.friendlist.FriendListPOJO;
import com.emobi.convoy.pojo.friendlist.FriendListResponsePOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListActivity extends AppCompatActivity implements WebServicesCallBack{
    private final String TAG=getClass().getSimpleName();
    private final static String FRIEND_LIST_API="friend_list_api";

    @BindView(R.id.ll_scoll)
    LinearLayout ll_scoll;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Friend List");
        callFriendAPI();
        tv_title.setText("Friend List");

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    public void callFriendAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, FRIEND_LIST_API).execute(WebServicesUrls.FRIEND_LIST_URL);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case FRIEND_LIST_API:
                parseFriendListAPI(response);
                break;
        }
    }
    public void parseFriendListAPI(String response){
        Log.d(TAG,"friend_list:-"+response.toString());
        Gson gson=new Gson();
        FriendListPOJO friendListPOJO=gson.fromJson(response,FriendListPOJO.class);
        if(friendListPOJO!=null){
            try {
                if (friendListPOJO.getList_friends() != null && friendListPOJO.getList_friends().size() > 0) {

                    List<FriendListResponsePOJO> list_friends=new ArrayList<>();
                    for(FriendListResponsePOJO friendListResponsePOJO:friendListPOJO.getList_friends()){
                        if(friendListResponsePOJO.getFriend_status().equals("enable")){
                            list_friends.add(friendListResponsePOJO);
                        }
                    }
                    showFriendList(list_friends);
                }
            }
            catch (Exception e){
                Log.d(TAG,"error:-"+e.toString());
            }
        }
    }

    public void showFriendList(final List<FriendListResponsePOJO> listResponsePOJOs){
        ll_scoll.removeAllViews();
        for (int i = 0; i < listResponsePOJOs.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_friend_list, null);
            CircleImageView iv_profile= (CircleImageView) view.findViewById(R.id.iv_profile);
            TextView tv_profile_name= (TextView) view.findViewById(R.id.tv_profile_name);
            LinearLayout ll_friends= (LinearLayout) view.findViewById(R.id.ll_friends);

            tv_profile_name.setTextColor(Color.parseColor("#000000"));

            Glide.with(getApplicationContext())
                    .load(WebServicesUrls.IMAGE_BASE_URL+listResponsePOJOs.get(i).getLog_pics())
                    .placeholder(R.drawable.ic_profile)
                    .dontAnimate()
                    .error(R.drawable.ic_profile)
                    .into(iv_profile);

            tv_profile_name.setText(listResponsePOJOs.get(i).getLog_name());

            final int finalI = i;
            ll_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(FriendListActivity.this,AboutActivity.class);
                    intent.putExtra("profilepic",listResponsePOJOs.get(finalI).getLog_pics());
                    intent.putExtra("name",listResponsePOJOs.get(finalI).getLog_name());
                    intent.putExtra("gender",listResponsePOJOs.get(finalI).getLog_gen());
                    intent.putExtra("dob",listResponsePOJOs.get(finalI).getLog_dob());
                    intent.putExtra("fblink",listResponsePOJOs.get(finalI).getLog_facbook());
                    intent.putExtra("twitterlink",listResponsePOJOs.get(finalI).getLog_twitter());
                    intent.putExtra("email",listResponsePOJOs.get(finalI).getLog_email());
                    intent.putExtra("bio",listResponsePOJOs.get(finalI).getLog_bio());
                    startActivity(intent);
                }
            });


            ll_scoll.addView(view);
        }
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
}
