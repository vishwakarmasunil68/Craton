package com.emobi.convoy.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.AudioCallRingingActivity;
import com.emobi.convoy.activity.ChatActivity;
import com.emobi.convoy.activity.VideoCallRingActivity;
import com.emobi.convoy.utility.StringUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by sunil on 20-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        try {
            Log.d(TAG, "remote msg:-" + remoteMessage.getData().toString());
            Log.d(TAG, "success:-" + remoteMessage.getData().get("success"));
            Log.d(TAG, "message:-" + remoteMessage.getData().get("result"));
            Log.d(TAG, "type:-" + remoteMessage.getData().get("type"));
            typeCheck(remoteMessage.getData().get("type"), remoteMessage.getData());

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            try {
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            } catch (Exception e1) {
                Log.d(TAG, e1.toString());
            }
        }
//        try {
//            Log.d(TAG,"remote msg:-"+remoteMessage.getData());
//            parseData(remoteMessage.getData().get("result"));
//        }
//        catch (Exception e){
//            Log.d(TAG,e.toString());
//            try {
//
//                Log.d(TAG, "From: " + remoteMessage.getFrom());
//                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//            }
//            catch (Exception e1){
//                Log.d(TAG,e1.toString());
//            }
//        }

        //Calling method to generate notification
//        sendNotification(remoteMessage.getNotification().getBody());
    }
    public void typeCheck(String type, Map<String, String> result) {
        Log.d(TAG, "type:-" + type);
//        Log.d(TAG,"result:-"+result);
        switch (type) {
            case StringUtils.AUDIOCALL:
                startAudioRingActivity(result);
                break;
            case StringUtils.AUDIOCALLCANCEL:
                Log.d(TAG,"audiocall cancel response:-"+result.toString());
                updateMyActivity(getApplicationContext(),StringUtils.AUDIOCALLCANCEL_INCOMING,result.get("result"));
                updateMyActivity(getApplicationContext(),StringUtils.AUDIOCALLCANCEL_OUTGOING,result.get("result"));
                break;
            case StringUtils.VIDEOCALLCANCEL:
                Log.d(TAG,"call cancel response:-"+result.toString());
                updateMyActivity(getApplicationContext(),StringUtils.VIDEOCALLCANCEL_INCOMING,result.get("result"));
                updateMyActivity(getApplicationContext(),StringUtils.VIDEOCALLCANCEL_OUTGOING,result.get("result"));
                break;
            case StringUtils.VIDEOCALL:
                startVideoRingActivity(result);
                break;
            case StringUtils.CHAT:
//                AppNotificationManager.sendChat(getApplicationContext(),type,result);
//                parseNormalMessageData(result.get("result"));
                AppNotificationManager.sendChatNot(getApplicationContext(),"chat",result);
                updateMyActivity(getApplicationContext(),StringUtils.CHAT,result.get("result"));
                break;
            case StringUtils.NEW_POST:
                AppNotificationManager.sendPostNot(getApplicationContext(),StringUtils.NEW_POST,result);
                Log.d(TAG,"new Post:-"+result.toString());
                break;
            case StringUtils.SHARE:
                AppNotificationManager.sendPostNot(getApplicationContext(),StringUtils.NEW_POST,result);
                Log.d(TAG,"share:-"+result.toString());
                break;
            case StringUtils.LIKE:
                AppNotificationManager.sendLikeNot(getApplicationContext(),StringUtils.LIKE,result);
                Log.d(TAG,"like:-"+result.toString());
                break;
            case StringUtils.COMMENT:
                Log.d(TAG,"comment:-"+result.toString());
                AppNotificationManager.sendLikeNot(getApplicationContext(),StringUtils.COMMENT,result);
                break;
            case StringUtils.FRIEND_REQUEST:
                Log.d(TAG,"comment:-"+result.toString());
                AppNotificationManager.sendFriendRequestNot(getApplicationContext(),StringUtils.COMMENT,result);
                break;
            case StringUtils.TESTIMONIAL:
                Log.d(TAG,"comment:-"+result.toString());
                AppNotificationManager.sendTestimonialNot(getApplicationContext(),StringUtils.COMMENT,result);
                break;
            case StringUtils.CALL_RECEIVE:
                Log.d(TAG,"comment:-"+result.toString());
                updateMyActivity(getApplicationContext(),StringUtils.CALL_RECEIVE,StringUtils.CALL_RECEIVE);
                break;
            case StringUtils.FRIEND_REQUEST_ACCEPT:
                Log.d(TAG,"comment:-"+result.toString());
                updateMyActivity(getApplicationContext(),StringUtils.FRIEND_REQUEST_ACCEPT,StringUtils.FRIEND_REQUEST_ACCEPT);
                break;


        }
    }

    public void startAudioRingActivity(Map<String,String> result){
        try {
            JSONObject jsonObject=new JSONObject(result.get("result"));
            Intent intent = new Intent(getApplicationContext(), AudioCallRingingActivity.class);
            intent.putExtra("voicecall_id", jsonObject.optString("voicecall_id"));
            intent.putExtra("voicecall_time", jsonObject.optString("voicecall_time"));
            intent.putExtra("file_type", jsonObject.optString("file_type"));
            intent.putExtra("voicecall_fri_id", jsonObject.optString("voicecall_fri_id"));
            intent.putExtra("voicecall_status", jsonObject.optString("voicecall_status"));
            intent.putExtra("voicecall_user_id", jsonObject.optString("voicecall_user_id"));
            intent.putExtra("log_pics", jsonObject.optString("log_pics"));
            intent.putExtra("log_name", jsonObject.optString("log_name"));
//            Log.d(TAG, "channel name 1:-" + jsonObject.optString("videocall_user_id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startVideoRingActivity(Map<String, String> result){
//        Log.d(TAG,"result form act:-"+result.get("result"));
        try {
            JSONObject jsonObject=new JSONObject(result.get("result"));
            Intent intent = new Intent(getApplicationContext(), VideoCallRingActivity.class);
            intent.putExtra("videocall_id", jsonObject.optString("videocall_id"));
            intent.putExtra("videocall_time", jsonObject.optString("videocall_time"));
            intent.putExtra("file_type", jsonObject.optString("file_type"));
            intent.putExtra("videocall_fri_id", jsonObject.optString("videocall_fri_id"));
            intent.putExtra("videocall_status", jsonObject.optString("videocall_status"));
            intent.putExtra("videocall_user_id", jsonObject.optString("videocall_user_id"));
//            Log.d(TAG, "channel name 1:-" + jsonObject.optString("videocall_user_id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseData(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            String date=jsonObject.optString("date");
            String time=jsonObject.optString("time");
            String chat_user_id=jsonObject.optString("chat_user_id");
            String log_profile=jsonObject.optString("log_pic");
            String log_name=jsonObject.optString("log_name");
            String chat_fri_id=jsonObject.optString("chat_fri_id");
            String message=jsonObject.optString("message");
            String title=jsonObject.optString("title");
            String token=jsonObject.optString("token");

            sendNotification(chat_fri_id,log_profile,log_name,token,title,message);
//            updateMyActivity(getApplicationContext(),result);
        }
        catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }
    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String chat_fri_id,String log_profile,String log_name,
                                  String token,String title,String messageBody) {



        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user_id",chat_fri_id);
        intent.putExtra("log_pic",log_profile);
        intent.putExtra("log_name",log_name);
        intent.putExtra("friend_token",token);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    static void updateMyActivity(Context context,String client, String message) {

        Intent intent = new Intent(client);

        //put whatever data you want to send, if any
        intent.putExtra("message", message);

        //send broadcast
        context.sendBroadcast(intent);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}