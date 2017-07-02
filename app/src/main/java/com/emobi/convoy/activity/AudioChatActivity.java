package com.emobi.convoy.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class AudioChatActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String LOG_TAG = AudioChatActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    private RtcEngine mRtcEngine;// Tutorial Step 1
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            if(mediaPlayer!=null){
                mediaPlayer.stop();
            }
            Log.d(TAG,"audio:-remote video decoded");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            Log.d(TAG,"audio:-user offline");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) { // Tutorial Step 10
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"audio:-u0ser muted");
                    onRemoteUserVideoMuted(uid, muted);
                }
            });
        }
    };
    private String Channel = "";
    private final String TAG = getClass().getSimpleName();
    boolean outgoing = true;
    String fri_id, user_id;

    ImageView imageView2;
    private final static String LOUD_ON = "loud_on";
    private final static String LOUD_OFF = "loud_off";

    private String LOUD_STATUS = LOUD_ON;
    @BindView(R.id.cv_profile)
    CircleImageView cv_profile;
    @BindView(R.id.tv_profile_name)
    TextView tv_profile_name;
    @BindView(R.id.iv_video_muted)
    ImageView iv_video_muted;


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_chat);
        ButterKnife.bind(this);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Channel = bundle.getString("channel");
            Log.d(TAG, "channel name:-" + Channel);
            outgoing = bundle.getBoolean("outgoing");

            if (outgoing) {
                fri_id = bundle.getString("fri_id");
                user_id = Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "");
            } else {
                user_id = bundle.getString("fri_id");
                fri_id = Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "");
            }
            final String log_pics=bundle.getString("log_pics");
            final String log_name=bundle.getString("log_name");
            if(log_pics.length()>0){
                Log.d(TAG,"image url:-"+WebServicesUrls.IMAGE_BASE_URL+log_pics);
                Glide.with(getApplicationContext()).load(WebServicesUrls.IMAGE_BASE_URL+log_pics).into(cv_profile);
            }
            tv_profile_name.setText(log_name);
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {
            initAgoraEngineAndJoinChannel();
//            audioManager.setMode(AudioManager.MODE_NORMAL);
//            audioManager.setSpeakerphoneOn(false);

//            changeSpeakerStatus();
//            changeSpeakerStatus();
//            changeSpeakerStatus();
//            changeSpeakerStatus();
//            changeSpeakerStatus();
//            LOUD_STATUS = LOUD_ON;
//            imageView2.setImageResource(R.drawable.ic_speaker);
//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setMode(AudioManager.MODE_NORMAL);
//            audioManager.setSpeakerphoneOn(true);
//            changeSpeakerStatus();

        }

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSpeakerStatus();
            }
        });
//        onLocalVideoMuteClicked(iv_video_muted);
    }

    public void changeSpeakerStatus() {
        if (LOUD_STATUS == LOUD_ON) {
            LOUD_STATUS = LOUD_OFF;
            imageView2.setImageResource(R.drawable.ic_speaker_mute);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(false);
        } else {
            LOUD_STATUS = LOUD_ON;
            imageView2.setImageResource(R.drawable.ic_speaker);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
        }

        Log.d(TAG,"speaker status:-"+LOUD_STATUS);
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     // Tutorial Step 1
        setupVideoProfile();         // Tutorial Step 2
        setupLocalVideo();           // Tutorial Step 3
        joinChannel();               // Tutorial Step 4
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA);
                    finish();
                }
                break;
            }
        }
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    // Tutorial Step 10

    public void onLocalVideoMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalVideoStream(iv.isSelected());

//        FrameLayout container = (FrameLayout) findViewById(R.id.local_video_view_container);
//        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);
//        surfaceView.setZOrderMediaOverlay(!iv.isSelected());
//        surfaceView.setVisibility(iv.isSelected() ? View.GONE : View.VISIBLE);
    }

    // Tutorial Step 9
    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    // Tutorial Step 8
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    // Tutorial Step 6
    public void onEncCallClicked(View view) {
//        finish();
        callCancelAPI();
    }

    public void callCancelAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("voicecall_user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("voicecall_fri_id", fri_id));
        new WebServiceBase(nameValuePairs, this, CANCEL_VIDEO_CALL, true).execute(WebServicesUrls.CANCEL_VOICE_CALL);
    }

    private final String CANCEL_VIDEO_CALL = "cancel_video_call";

    // Tutorial Step 1
    private void initializeAgoraEngine() {
        if(outgoing) {
            mediaPlayer = MediaPlayer.create(this,
                    R.raw.callring);
            mediaPlayer.start();
        }
        Log.d(TAG,"audio:-initialize agora");
        mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
    }

    // Tutorial Step 2
    private void setupVideoProfile() {
        Log.d(TAG,"audio:-setup video profile");
        mRtcEngine.disableVideo();
//        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    // Tutorial Step 3
    private void setupLocalVideo() {
        Log.d(TAG,"audio:-setupvideo call");
//        FrameLayout container = (FrameLayout) findViewById(R.id.local_video_view_container);
//        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
//        surfaceView.setZOrderMediaOverlay(true);
//        container.addView(surfaceView);
//        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
    }

    // Tutorial Step 4
    private void joinChannel() {
        Log.d(TAG,"audio:-join channel");
        mRtcEngine.joinChannel(null, Channel, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
        changeSpeakerStatus();
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        Log.d(TAG,"audio:-set up remote video");
//        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);
//
//        if (container.getChildCount() >= 1) {
//            return;
//        }
//
//        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
//        container.addView(surfaceView);
//        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
//
//        surfaceView.setTag(uid); // for mark purpose
//        View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk); // optional UI
//        tipMsg.setVisibility(View.GONE);
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    // Tutorial Step 6
    private void leaveChannel() {
        Log.d(TAG, "leave channel call");
        Log.d(TAG,"audio:-leave channel");
        mRtcEngine.leaveChannel();
        callCancelAPI();
        finish();
    }

    // Tutorial Step 7
    private void onRemoteUserLeft() {
        Log.d(TAG,"audio:-remote user left");
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);
        container.removeAllViews();
        callCancelAPI();
        finish();
    }

    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        Log.d(TAG,"audio:-on remote video muted");
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case CANCEL_VIDEO_CALL:
                parseCancelVideoCall(response);
                break;
        }
    }

    public void parseCancelVideoCall(String response) {
//        try{
//            JSONObject jsonObject=new JSONObject(response);
//            String success=jsonObject.optString("success");
//            if(success.equals("true")){
//
//            }else{
//                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
//            }
//        }
//        catch (Exception e){
//            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
//        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.AUDIOCALLCANCEL_OUTGOING));
        getApplicationContext().registerReceiver(cancelring, new IntentFilter(StringUtils.CALL_RECEIVE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
        getApplicationContext().unregisterReceiver(cancelring);
    }
    private BroadcastReceiver cancelring = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("message");
            if(mediaPlayer!=null){
                mediaPlayer.stop();
            }
        }
    };
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            finish();
        }
    };
}
