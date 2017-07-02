package com.emobi.convoy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.pojo.findfriends.FindFriendPOJO;
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

public class FindFriendsActivity extends AppCompatActivity implements WebServicesCallBack{

    private final String TAG=getClass().getSimpleName();
    private final static String CALL_FRIENDS_API="call_friends_api";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.ll_scroll)
    LinearLayout ll_scroll;
    String friend_list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            friend_list=bundle.getString("friendspojo");
        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(et_search.getText().toString().length()>0){
                    callSearchAPI();
                }else{
                    ll_scroll.removeAllViews();
                }
            }
        });
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    public void callSearchAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("log_name", et_search.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, CALL_FRIENDS_API,true).execute(WebServicesUrls.SEARCH_FRIENDS);
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
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case CALL_FRIENDS_API:
                parseFriendSearchResponse(response);
                break;
        }
    }
    public void parseFriendSearchResponse(String response){
        Log.d(TAG,"response:-"+response);
        try{
            Gson gson=new Gson();
            FindFriendPOJO findFriendPOJO=gson.fromJson(response,FindFriendPOJO.class);
            inflateFindFriends(findFriendPOJO.getRegisterPOJO());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void inflateFindFriends(final List<RegisterPOJO> registerPOJOList){
        ll_scroll.removeAllViews();
        for (int i = 0; i < registerPOJOList.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_find_friends, null);
            LinearLayout ll_friend= (LinearLayout) view.findViewById(R.id.ll_friend);
            CircleImageView iv_profile= (CircleImageView) view.findViewById(R.id.iv_profile);
            TextView tv_profile_name= (TextView) view.findViewById(R.id.tv_profile_name);

            Glide.with(getApplicationContext()).load(WebServicesUrls.BASE_URL+registerPOJOList.get(i).getLog_pics())
                    .into(iv_profile);
            tv_profile_name.setText(registerPOJOList.get(i).getLog_name());
            final int finalI = i;
            ll_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(FindFriendsActivity.this,FriendProfileViewActivity.class);
                    intent.putExtra("visitor_id",registerPOJOList.get(finalI).getLog_id());
                    intent.putExtra("frientlist",friend_list);
                    startActivity(intent);
                }
            });
            ll_scroll.addView(view);
        }
    }
}
