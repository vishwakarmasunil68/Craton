package com.emobi.convoy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.emobi.convoy.R;
import com.emobi.convoy.testing.VideoChatActivity;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCallRingActivity extends AppCompatActivity implements WebServicesCallBack,TextureView.SurfaceTextureListener{

    @BindView(R.id.btn_cancel)
    ImageView btn_cancel;
    @BindView(R.id.btn_ans)
    ImageView btn_ans;
    @BindView(R.id.texture_view)
    TextureView texture_view;
    private Camera mCamera;

    private final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call_ring);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            final String videocall_id=bundle.getString("videocall_id");
            final String videocall_time=bundle.getString("videocall_time");
            final String file_type=bundle.getString("file_type");
            final String videocall_fri_id=bundle.getString("videocall_fri_id");
            final String videocall_status=bundle.getString("videocall_status");
            final String videocall_user_id=bundle.getString("videocall_user_id");
            Log.d(TAG,"channel name video ring:-"+videocall_user_id);
            btn_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(), VideoChatActivity.class);
//                    intent.putExtra("videocall_id",videocall_id);
//                    intent.putExtra("videocall_time",videocall_time);
//                    intent.putExtra("file_type",file_type);
//                    intent.putExtra("videocall_fri_id",videocall_fri_id);
//                    intent.putExtra("videocall_status",videocall_status);
                    intent.putExtra("channel",videocall_user_id);
                    intent.putExtra("outgoing",false);
                    intent.putExtra("fri_id",videocall_fri_id);
                    startActivity(intent);
                    finish();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    finish();
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("videocall_user_id", videocall_user_id));
                    nameValuePairs.add(new BasicNameValuePair("videocall_fri_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
                    new WebServiceBase(nameValuePairs, VideoCallRingActivity.this, VIDEO_CALL_CANCEL_API,true).execute(WebServicesUrls.CANCEL_VIDEO_CALL);
                }
            });
        }
        else{
            finish();
        }

        texture_view.setSurfaceTextureListener(this);
    }

    private final String VIDEO_CALL_CANCEL_API="video_call_cancel_api";

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch(apicall){
            case VIDEO_CALL_CANCEL_API:
                finish();
                break;
        }
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
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.VIDEOCALLCANCEL_INCOMING));
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

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera = Camera.open();

        try {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);

            //front camera code
            mCamera.stopPreview();
            mCamera.release();
            mCamera = getFrontCameraInstance();
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
            mCamera.setDisplayOrientation(90);
        } catch (IOException ioe) {
            // Something bad happened
        }
    }
    int currentCameraId=0;
    public Camera getFrontCameraInstance(){
        Camera c = null;
        try {
            currentCameraId=1;
            c = Camera.open(currentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
