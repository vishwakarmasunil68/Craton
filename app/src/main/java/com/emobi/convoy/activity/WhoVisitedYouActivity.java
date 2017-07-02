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
import com.emobi.convoy.pojo.visitor.VisitorPOJO;
import com.emobi.convoy.pojo.visitor.VisitorResultPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class WhoVisitedYouActivity extends AppCompatActivity implements WebServicesCallBack{

    private final String TAG=getClass().getSimpleName();
    private final String WHO_VISITED_API="who_visited_api";
    @BindView(R.id.ll_scroll)
    LinearLayout ll_scroll;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    String friend_list="";

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_visited_you);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        getSupportActionBar().setTitle("Who Visited You");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            friend_list=bundle.getString("friendspojo");
        }
        tv_title.setText("Who Visited You");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        callGetVisitorAPI();
        MobileAds.initialize(this, "ca-app-pub-1163685788193118~7085127386");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4E448BFE118E3DBD390404E63D74029A")
                .build();
        mAdView.loadAd(adRequest);
    }

    public void callGetVisitorAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("vis_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, WHO_VISITED_API).execute(WebServicesUrls.WHO_VISITED_API);
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
            case WHO_VISITED_API:
                parseWhoVisitedAPI(response);
                break;
        }
    }

    public void parseWhoVisitedAPI(String response){
        Log.d(TAG,"visited response:-"+response);
        try{
            Gson gson=new Gson();
            VisitorPOJO visitorPOJO=gson.fromJson(response,VisitorPOJO.class);
            if(visitorPOJO.getSuccess().equals("true")){
                inflateFindFriends(visitorPOJO.getVisitorResultPOJOList());
            }else{
                ToastClass.showShortToast(getApplicationContext(),"No Visitors Found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"No Visitors Found");
        }
    }
    public void inflateFindFriends(final List<VisitorResultPOJO> visitorResultPOJOList){
        ll_scroll.removeAllViews();
        for (int i = 0; i < visitorResultPOJOList.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_find_friends, null);
            LinearLayout ll_friend= (LinearLayout) view.findViewById(R.id.ll_friend);
            CircleImageView iv_profile= (CircleImageView) view.findViewById(R.id.iv_profile);
            TextView tv_profile_name= (TextView) view.findViewById(R.id.tv_profile_name);

            Glide.with(getApplicationContext())
                    .load(WebServicesUrls.BASE_URL+visitorResultPOJOList.get(i).getLogPics())
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .dontAnimate()
                    .into(iv_profile);
            tv_profile_name.setText(visitorResultPOJOList.get(i).getLogName());
            final int finalI = i;
            ll_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(WhoVisitedYouActivity.this,FriendProfileViewActivity.class);
                    intent.putExtra("visitor_id",visitorResultPOJOList.get(finalI).getLogId());
                    intent.putExtra("frientlist",friend_list);
                    startActivity(intent);
                }
            });
            ll_scroll.addView(view);
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
