package com.emobi.convoy.fcm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.ChatActivity;
import com.emobi.convoy.activity.FriendRequestActivity;
import com.emobi.convoy.activity.ShowSingleFeedActivity;
import com.emobi.convoy.pojo.chatpojo.ChatResultPOJO;
import com.emobi.convoy.utility.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by sunil on 18-03-2017.
 */

public class AppNotificationManager {

    private final static String TAG="AppNotificationManager";


    public static void sendChatNot(Context context,String title,Map<String,String> messagebody){
        String message=messagebody.get("result").toString();
        Log.d(TAG,"appointment result:-"+message);
        try{
            JSONObject jsonObject=new JSONObject(message);

            Gson gson=new Gson();
            ChatResultPOJO chatResultPOJO=gson.fromJson(message,ChatResultPOJO.class);
            Intent intent = new Intent(context, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title",chatResultPOJO.getChat_title());
            intent.putExtra("message",jsonObject.optString("chat_msg"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(chatResultPOJO.getChat_title())
                    .setContentText(jsonObject.optString("chat_msg"))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            android.app.NotificationManager notificationManager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }
    public static void sendLikeNot(Context context,String title,Map<String,String> messagebody){
        String result=messagebody.get("result").toString();
        Log.d(TAG,"appointment result:-"+result);
        try{
            JSONObject jsonObject=new JSONObject(result);
            String message=jsonObject.optString("message");
            String post_id=jsonObject.optString("post_id");
            String post_image_status=jsonObject.optString("message");
            String file_type=jsonObject.optString("file_type");
            String sender_name=jsonObject.optString("sendername");




            Intent intent = new Intent(context, ShowSingleFeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("post_id",post_id);
            intent.putExtra("image_status",post_image_status);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(sender_name)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            android.app.NotificationManager notificationManager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

            updateActivity(context, StringUtils.LIKE,result);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }


    public static void sendPostNot(Context context,String title,Map<String,String> messagebody){
        String result=messagebody.get("result").toString();
        Log.d(TAG,"appointment result:-"+result);
        try{
            JSONObject jsonObject=new JSONObject(result);
            String message=jsonObject.optString("message");
            String post_id=jsonObject.optString("post_id");
            String post_image_status=jsonObject.optString("message");
            String file_type=jsonObject.optString("file_type");
            String sender_name=jsonObject.optString("sendername");




            Intent intent = new Intent(context, ShowSingleFeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("post_id",post_id);
            intent.putExtra("image_status",post_image_status);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(sender_name)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            android.app.NotificationManager notificationManager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

            updateActivity(context, StringUtils.NEW_POST,result);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }


    public static void sendFriendRequestNot(Context context,String title,Map<String,String> messagebody){
        String result=messagebody.get("result").toString();
        Log.d(TAG,"appointment result:-"+result);
        try{
            JSONObject jsonObject=new JSONObject(result);
            String log_name=jsonObject.optString("log_name");




            Intent intent = new Intent(context, FriendRequestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(log_name)
                    .setContentText(log_name+" sent you a friend request")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            android.app.NotificationManager notificationManager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

            updateActivity(context, StringUtils.FRIEND_REQUEST,result);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }
    public static void sendTestimonialNot(Context context,String title,Map<String,String> messagebody){
        String result=messagebody.get("result").toString();
        Log.d(TAG,"appointment result:-"+result);
        try{
            JSONObject jsonObject=new JSONObject(result);
            String log_name=jsonObject.optString("log_name");




            Intent intent = new Intent(context, FriendRequestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(log_name)
                    .setContentText(log_name+" sent you a Testimonial")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            android.app.NotificationManager notificationManager =
                    (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

            updateActivity(context, StringUtils.TESTIMONIAL,result);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }


    public static void updateActivity(Context context, String client, String message) {
        Intent intent = new Intent(client);

        //put whatever data you want to send, if any
        intent.putExtra("message", message);

        //send broadcast
        context.sendBroadcast(intent);
    }
}
