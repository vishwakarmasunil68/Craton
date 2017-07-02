package com.emobi.convoy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.activity.ChatActivity;
import com.emobi.convoy.activity.FriendProfileViewActivity;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.pojo.addvisitor.AddVisitorResultPOJO;
import com.emobi.convoy.testing.VideoChatActivity;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.emobi.convoy.webservices.WebServicesUrls.VIDEO_CALL_API;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sunil on 12-05-2017.
 */

public class MapViewFragment extends Fragment implements WebServicesCallBack{

    @BindView(R.id.cv_profile_image)
    CircleImageView cv_profile_image;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.iv_friend_profile)
    ImageView iv_friend_profile;
    @BindView(R.id.iv_chat_activity)
    ImageView iv_chat_activity;
    @BindView(R.id.iv_call)
    ImageView iv_call;
    @BindView(R.id.ll_frag_main)
    LinearLayout ll_frag_main;
    @BindView(R.id.tv_address)
    TextView tv_address;


    AddVisitorResultPOJO addVisitorResultPOJO;

    public MapViewFragment(){

    }
    public MapViewFragment(AddVisitorResultPOJO addVisitorResultPOJO){
        this.addVisitorResultPOJO=addVisitorResultPOJO;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_map_profile,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity().getApplicationContext())
                .load(WebServicesUrls.IMAGE_BASE_URL+addVisitorResultPOJO.getLogPics())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(cv_profile_image);
        tv_profile_name.setText(addVisitorResultPOJO.getLogName());
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity= (HomeActivity) getActivity();
                homeActivity.onBackPressed();
            }
        });
        iv_friend_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FriendProfileViewActivity.class);
                intent.putExtra("visitor_id",addVisitorResultPOJO.getLogId());
                intent.putExtra("frientlist","");
                startActivity(intent);
            }
        });
        iv_chat_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user_id",addVisitorResultPOJO.getLogId());
                intent.putExtra("log_pic",addVisitorResultPOJO.getLogPics());
                intent.putExtra("log_name",addVisitorResultPOJO.getLogName());
                intent.putExtra("friend_token",addVisitorResultPOJO.getLogDeviceToken());
                startActivity(intent);
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVideoAPI(Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.LOG_ID,""),addVisitorResultPOJO.getLogId());
            }
        });
        ll_frag_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tv_address.setText(addVisitorResultPOJO.getLogGeoLocation());


    }
    public void callVideoAPI(String user_id,String friend_id){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String channel=Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,"")+":"+user_id;
        nameValuePairs.add(new BasicNameValuePair("message", channel));
        nameValuePairs.add(new BasicNameValuePair("title", "video Call test"));
        nameValuePairs.add(new BasicNameValuePair("videocall_user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("videocall_fri_id", friend_id));
        nameValuePairs.add(new BasicNameValuePair("videocall_status", "false"));
        nameValuePairs.add(new BasicNameValuePair("file_type", "videoCall"));
        new WebServiceBase(nameValuePairs, getActivity(),this, VIDEO_CALL_API).execute(VIDEO_CALL_API);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case VIDEO_CALL_API:
                parseVideoCallAPI(response);
                break;
        }
    }
    public void parseVideoCallAPI(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("1")){
                Intent intent=new Intent(getActivity(),VideoChatActivity.class);
                intent.putExtra("channel",Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));
                intent.putExtra("outgoing",true);
                intent.putExtra("fri_id",addVisitorResultPOJO.getLogId());
                startActivity(intent);
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"User is not reachable",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),"User is busy",Toast.LENGTH_LONG).show();
        }
    }

}
