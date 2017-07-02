package com.emobi.convoy.sinch;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity implements WebServicesCallBack{

    static final String TAG = CallScreenActivity.class.getSimpleName();
    private static final String UPDATE_SERVER_TIME = "update_server_time";

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId;

    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;
    long toal_seconds_left=0;

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case UPDATE_SERVER_TIME:
                parseUpdateResponse(response);
                break;
        }
    }

    public void parseUpdateResponse(String response){
        Log.d(TAG,"call update response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                JSONObject result=jsonObject.optJSONObject("result");
                String log_call_time=result.optString("log_call_time");
                String log_validity=result.optString("log_validity");

                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_CALL_TIME,log_call_time);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_VALIDITY,log_validity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        startActivity(new Intent(CallScreenActivity.this, HomeActivity.class));
        finishAffinity();
    }

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);

        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        Button endCallButton = (Button) findViewById(R.id.hangupButton);

        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        toal_seconds_left=getTotalSeconds();
    }

    public long getTotalSeconds(){

        String total_seconds= Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_VALIDITY,"");
        try{
            String[] validity=total_seconds.split(":");
            int hour=Integer.parseInt(validity[0]);
            int min=Integer.parseInt(validity[1]);
            int sec=Integer.parseInt(validity[2]);

            long totalseconds=(hour*60*60)+(min*60)+sec;
            if(totalseconds>0){
                return totalseconds;
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Your balance is 0 please recharge your account");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            mCallerName.setText(call.getRemoteUserId());
            mCallState.setText(call.getState().toString());
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
            if(!isservice_stated) {
                isservice_stated=true;
                toal_seconds_left = toal_seconds_left - call.getDetails().getDuration();
                updateCallServerTime(toal_seconds_left);
            }else{

            }
        }else{
            finish();
        }
//        finish();
    }
    boolean isservice_stated=false;

    public void updateCallServerTime(long toal_seconds_left){
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_call_time", ""));
            if(time_over){
                nameValuePairs.add(new BasicNameValuePair("log_validity", "00:00:00"));
            }else {
                long totalSecs = toal_seconds_left;
                long hours = totalSecs / 3600;
                long minutes = (totalSecs % 3600) / 60;
                long seconds = totalSecs % 60;
                String string_time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//            System.out.println(String.format("%02d:%02d:%02d", hours, minutes, seconds));

                nameValuePairs.add(new BasicNameValuePair("log_validity", string_time));
            }
            nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            new WebServiceBase(nameValuePairs, this, UPDATE_SERVER_TIME).execute(WebServicesUrls.UPDATE_CALL_TIME);
        }
        catch (Exception e){
            finish();
        }
    }

    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }
    boolean time_over=false;
    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            Log.d(TAG,"updating call duration:-"+call.getDetails().getDuration());
            Log.d(TAG,"establishing time:-"+call.getDetails().getEstablishedTime());
            if(call.getDetails().getDuration()>=toal_seconds_left){
//                toal_seconds_left=toal_seconds_left+call.getDetails().getDuration();
                time_over=true;
                endCall();
            }
            mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endMsg = "Call ended: " + call.getDetails().toString();
            Log.d(TAG,"calldetail duration:-"+call.getDetails().getDuration());
            Log.d(TAG,"calldetail ended time:-"+call.getDetails().getEndedTime());
            Log.d(TAG,"calldetail started time:-"+call.getDetails().getStartedTime());
            Toast.makeText(CallScreenActivity.this, "ending msg:-"+endMsg, Toast.LENGTH_LONG).show();
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

    }
}
