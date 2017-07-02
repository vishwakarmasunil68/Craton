package com.emobi.convoy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AudioCallRingingActivity extends AppCompatActivity implements WebServicesCallBack{

    @BindView(R.id.btn_cancel)
    ImageView btn_cancel;
    @BindView(R.id.btn_ans)
    ImageView btn_ans;
    @BindView(R.id.cv_profile)
    CircleImageView cv_profile;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;

    private final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_call_ringing);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            final String voicecall_id=bundle.getString("voicecall_id");
            final String voicecall_time=bundle.getString("voicecall_time");
            final String file_type=bundle.getString("file_type");
            voicecall_fri_id=bundle.getString("voicecall_fri_id");
            final String voicecall_status=bundle.getString("voicecall_status");
            voicecall_user_id=bundle.getString("voicecall_user_id");
            log_pics=bundle.getString("log_pics");
            log_name=bundle.getString("log_name");
            if(log_pics.length()>0){
                Glide.with(getApplicationContext()).load(WebServicesUrls.IMAGE_BASE_URL+log_pics).into(cv_profile);
            }
            tv_profile_name.setText(log_name);
            Log.d(TAG,"channel name video ring:-"+voicecall_user_id);
            btn_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    callReceiveAPI(voicecall_user_id);
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("voicecall_user_id", voicecall_user_id));
                    nameValuePairs.add(new BasicNameValuePair("voicecall_fri_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
                    new WebServiceBase(nameValuePairs, AudioCallRingingActivity.this, VIDEO_CALL_CANCEL_API,true).execute(WebServicesUrls.CANCEL_VOICE_CALL);
                }
            });
        }
        else{
            finish();
        }
    }
    public void callReceiveAPI(String fri_id){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("fri_id", fri_id));
        new WebServiceBase(nameValuePairs, AudioCallRingingActivity.this, CALL_RECEIVE_API).execute(WebServicesUrls.CALL_RECEIVE_API_URL);
    }
    private final String VIDEO_CALL_CANCEL_API="video_call_cancel_api";
    private final String CALL_RECEIVE_API="call_recieve_api";

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch(apicall){
            case VIDEO_CALL_CANCEL_API:
                finish();
                break;
            case CALL_RECEIVE_API:
                parsecallReceiveresponse(response);
                break;
        }
    }
    String voicecall_user_id,voicecall_fri_id,log_pics,log_name;
    public void parsecallReceiveresponse(String response){
        Log.d(TAG,"ring response:-"+response);
        Intent intent=new Intent(getApplicationContext(), AudioChatActivity.class);
        intent.putExtra("channel",voicecall_user_id);
        intent.putExtra("outgoing",false);
        intent.putExtra("fri_id",voicecall_fri_id);
        intent.putExtra("log_pics",log_pics);
        intent.putExtra("log_name",log_name);
        startActivity(intent);
        finish();
    }

    MediaPlayer player;
    @Override
    protected void onResume() {
        super.onResume();
        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.AUDIOCALLCANCEL_INCOMING));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player!=null) {
            player.stop();
        }
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
}