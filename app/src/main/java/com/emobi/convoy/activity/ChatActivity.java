package com.emobi.convoy.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.chat.ChatDatePOJO;
import com.emobi.convoy.pojo.chat.ChatDateResultPOJO;
import com.emobi.convoy.pojo.chatpojo.ChatPOJO;
import com.emobi.convoy.pojo.chatpojo.ChatResultPOJO;
import com.emobi.convoy.testing.VideoChatActivity;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.emobi.convoy.webservices.WebUploadService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener,WebServicesCallBack{

    private int PICK_IMAGE_REQUEST = 1;

    private final String TAG=getClass().getSimpleName();
    private final String CALL_MSG_API="call_msg_api";
    private final String CALL_GET_OLD_MESSAGES="call_get_old_messages";

    @BindView(R.id.tv_friend_name)
    TextView tv_friend_name;
    @BindView(R.id.iv_profile_pic)
    ImageView iv_profile_pic;
    @BindView(R.id.ll_chat)
    LinearLayout ll_chat;
    @BindView(R.id.et_msg)
    EditText et_msg;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scroll_chat)
    ScrollView scroll_chat;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.iv_video_call)
    ImageView iv_video_call;

    String friend_token,log_name,user_id,profile_pic_url;
    List<ChatResultPOJO> list_chat_pojo_gl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            user_id = bundle.getString("user_id");
            Log.d(TAG,"fri_id:-"+user_id);
            profile_pic_url = bundle.getString("log_pic");
            log_name = bundle.getString("log_name");
            friend_token = bundle.getString("friend_token");
            Picasso.with(getApplicationContext())
                    .load(WebServicesUrls.IMAGE_BASE_URL+profile_pic_url)
                    .into(iv_profile_pic);
            tv_friend_name.setText(log_name);
            getOldChat(user_id, Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,""));
        }
        else{
//            finish();
        }

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImageMessageClick();
            }
        });
        btn_send.setOnClickListener(this);

        iv_video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                callVideoAPI(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),user_id);
                showCallDialog();
            }
        });

        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    public void showCallDialog(){
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog1.setContentView(R.layout.dialog_call_options);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.setTitle("Select");
        dialog1.setCancelable(true);
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll_voice= (LinearLayout) dialog1.findViewById(R.id.ll_voice);
        LinearLayout ll_video= (LinearLayout) dialog1.findViewById(R.id.ll_video);

        ll_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(ChatActivity.this,AudioChatActivity.class);
//                intent.putExtra("channel","121");
//                startActivity(intent);
                callAudioAPI(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),user_id);
                dialog1.dismiss();
            }
        });

        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callVideoAPI(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),user_id);
                dialog1.dismiss();
            }
        });
    }

    public void callVideoAPI(String user_id,String friend_id){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String channel=Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,"");
//                +":"+user_id;
        nameValuePairs.add(new BasicNameValuePair("message", "Vedeo call"));
        nameValuePairs.add(new BasicNameValuePair("title", "video Call test"));
        nameValuePairs.add(new BasicNameValuePair("videocall_user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("videocall_fri_id", friend_id));
        nameValuePairs.add(new BasicNameValuePair("videocall_status", "false"));
        nameValuePairs.add(new BasicNameValuePair("file_type", "videoCall"));
        new WebServiceBase(nameValuePairs, this, VIDEO_CALL_API).execute(WebServicesUrls.VIDEO_CALL_API);
    }

    public void callAudioAPI(String user_id,String friend_id){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String channel=Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,"")+":"+user_id;
        nameValuePairs.add(new BasicNameValuePair("message", "Voice call"));
        nameValuePairs.add(new BasicNameValuePair("title", "video Call test"));
        nameValuePairs.add(new BasicNameValuePair("voicecall_user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("voicecall_fri_id", friend_id));
        nameValuePairs.add(new BasicNameValuePair("voicecall_status", "false"));
        nameValuePairs.add(new BasicNameValuePair("file_type", "voiceCall"));

        new WebServiceBase(nameValuePairs, this, VOICE_CALL_API).execute(WebServicesUrls.VOICE_CALL_API);
    }



    private final String VIDEO_CALL_API="video_call_api";
    private final String VOICE_CALL_API="voice_call_api";
    public void sendImageMessageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void getOldChat(String friend_id,String user_id){
        Log.d(TAG,"friend_id:-"+friend_id);
        Log.d(TAG,"user_id:-"+user_id);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("chat_fri_id", friend_id));
        nameValuePairs.add(new BasicNameValuePair("chat_user_id", user_id));
        new WebServiceBase(nameValuePairs, this, CALL_GET_OLD_MESSAGES,true).execute(WebServicesUrls.GET_OLD_CHAT_MESSAGE_API);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        ChatActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    Log.d(TAG, "selected path:-" + selectedImagePath);
                    Date d=new Date();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    try{
                        String date=sdf.format(d).split(" ")[0];
                        String time=sdf.format(d).split(" ")[1];
                        sendFileMessage(friend_token,log_name,user_id,Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),
                                date,time,profile_pic_url,log_name,selectedImagePath);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d(TAG, et_msg.toString());
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btn_send:

                    Date d=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try{
                    String date=sdf.format(d).split(" ")[0];
                    String time=sdf.format(d).split(" ")[1];
                    sendMessage(friend_token,log_name,user_id,Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""),
                            date,time,profile_pic_url,log_name);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d(TAG, et_msg.toString());
                }

                break;
        }
    }
    public void sendFileMessage(String token,String title,
                                String chat_fri_id,String chat_user_id,String date,String time,String log_pic,String log_name,String image_path){
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody bin1 = new FileBody(new File(image_path));
            reqEntity.addPart("chat_fri_id", new StringBody(chat_fri_id));
            reqEntity.addPart("chat_user_id", new StringBody(chat_user_id));
            reqEntity.addPart("date", new StringBody(date));
            reqEntity.addPart("time", new StringBody(time));
            reqEntity.addPart("chat_msg", new StringBody(""));
            reqEntity.addPart("chat_title", new StringBody(log_name));
            reqEntity.addPart("file_type", new StringBody("image"));
            FileBody bin = new FileBody(new File(image_path));
            reqEntity.addPart("chat_file", bin);
            new WebUploadService(reqEntity, this, CALL_MSG_API).execute(WebServicesUrls.SEND_MESSAGE_API);


            ChatResultPOJO chatResultPOJO = new ChatResultPOJO("", chat_fri_id, chat_user_id, date,
                    time, "", title, image_path, "image");

            showSingleChatMessages(chatResultPOJO);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void sendMessage(String token,String title,
                            String chat_fri_id,String chat_user_id,String date,String time,String log_pic,String log_name){
        String message=et_msg.getText().toString();
        if(!message.isEmpty()){
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("chat_fri_id", chat_fri_id));
            nameValuePairs.add(new BasicNameValuePair("chat_user_id", chat_user_id));
            nameValuePairs.add(new BasicNameValuePair("date", date));
            nameValuePairs.add(new BasicNameValuePair("time", time));
            nameValuePairs.add(new BasicNameValuePair("chat_msg", message));
            nameValuePairs.add(new BasicNameValuePair("chat_title", log_name));
            nameValuePairs.add(new BasicNameValuePair("file_type", "text"));
            nameValuePairs.add(new BasicNameValuePair("chat_file", ""));
            new WebServiceBase(nameValuePairs, this, CALL_MSG_API,true).execute(WebServicesUrls.SEND_MESSAGE_API);

            ChatResultPOJO chatResultPOJO=new ChatResultPOJO("",chat_fri_id,chat_user_id,date,
                    time,message,title,"","text");

            showSingleChatMessages(chatResultPOJO);
        }
        et_msg.setText("");
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case CALL_GET_OLD_MESSAGES:
                parseGetOldMessages(response);
                break;

            case CALL_MSG_API:
                parseSendMessageResponse(response);
                break;

            case VIDEO_CALL_API:
                parseVideoCallAPI(response);
                break;

            case VOICE_CALL_API:
                parseVoiceCallAPI(response);
                break;
        }
    }
    public void parseVoiceCallAPI(String response){
        Log.d(TAG,"voice response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("1")){
                Intent intent=new Intent(ChatActivity.this,AudioChatActivity.class);
                intent.putExtra("channel",Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));
                intent.putExtra("outgoing",true);
                intent.putExtra("fri_id",user_id);
                intent.putExtra("log_pics",profile_pic_url);
                intent.putExtra("log_name",log_name);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"User is not reachable",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"User is busy",Toast.LENGTH_LONG).show();
        }
    }

    public void parseVideoCallAPI(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("1")){
                Intent intent=new Intent(ChatActivity.this,VideoChatActivity.class);
                intent.putExtra("channel",Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));
                intent.putExtra("outgoing",true);
                intent.putExtra("fri_id",user_id);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"User is not reachable",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"User is busy",Toast.LENGTH_LONG).show();
        }
    }

    private void parseGetOldMessages(String response){
        Log.d(TAG,"response:-"+response);
        Gson gson=new Gson();
        ChatPOJO chatPOJO=gson.fromJson(response,ChatPOJO.class);

        try{
            if(chatPOJO.getSuccess().equals("true")){
                list_chat_pojo_gl=chatPOJO.getList_chat();
//                showChatMessages(chatPOJO.getList_chat());
                orderchatToDate(chatPOJO.getList_chat());
            }
            else{
                Toast.makeText(getApplicationContext(),"No Chat Message Found",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Log.d(TAG,"error:-"+e.toString());
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
        }
    }


    List<ChatResultPOJO> list_chat_messages;
    public void orderchatToDate(List<ChatResultPOJO> list_chat){
        list_chat_messages = list_chat;
        List<String> list_dates = new ArrayList<>();
        Set<String> set_date = new HashSet<>();
        for (ChatResultPOJO chatResultPOJO : list_chat) {
            list_dates.add(chatResultPOJO.getDate());
        }
        set_date.addAll(list_dates);
        List<String> list_string = new ArrayList<>();
        list_string.addAll(set_date);

        Collections.sort(list_string, new Comparator<String>() {

            @Override
            public int compare(String arg0, String arg1) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(arg0);
                    Date arg1Date = format.parse(arg1);
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (Exception e) {
                    e.printStackTrace();
                    compareResult = arg0.compareTo(arg1);
                }
                return compareResult;
            }
        });


        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (String s : list_string) {

            JSONObject date_object = new JSONObject();
            JSONArray date_array = new JSONArray();

            for (ChatResultPOJO chatResultPOJO : list_chat) {
                if (s.equals(chatResultPOJO.getDate())) {
                    try {
                        JSONObject object1 = new JSONObject();
                        object1.put("chat_id", chatResultPOJO.getChat_id());
                        object1.put("chat_fri_id", chatResultPOJO.getChat_fri_id());
                        object1.put("chat_user_id", chatResultPOJO.getChat_user_id());
                        object1.put("date", chatResultPOJO.getDate());
                        object1.put("time", chatResultPOJO.getTime());
                        object1.put("chat_msg", chatResultPOJO.getChat_msg());
                        object1.put("chat_title", chatResultPOJO.getChat_title());
                        object1.put("chat_file", chatResultPOJO.getChat_file());
                        object1.put("file_type", chatResultPOJO.getFile_type());

                        date_array.put(object1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                }
            }
            try {
                date_object.put("date", s);
                date_object.put("result", date_array);

                array.put(date_object);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
            }
        }

        try {
            if (array.length() > 0) {
                object.put("success", true);
                object.put("result", array);
            } else {
                object.put("success", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "json:-" + object.toString());
        try {
            Gson gson = new Gson();
            ChatDatePOJO chatDatePOJO=gson.fromJson(object.toString(),ChatDatePOJO.class);
            inflateChatMessage(chatDatePOJO.getChatDateResultPOJOs());
//            UrgentChatPatientPOJO urgentChatPatientPOJO = gson.fromJson(object.toString(), UrgentChatPatientPOJO.class);
//            inflateChatMessage(urgentChatPatientPOJO.getUrgentChatPatientResultPOJOList());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }
    LinearLayout final_chat_ll;
    public void inflateChatMessage(List<ChatDateResultPOJO> list_chat_date) {
        for (int i = 0; i < list_chat_date.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_chat_layout, null);
            LinearLayout ll_chat_scroll = (LinearLayout) view.findViewById(R.id.ll_chat_scroll);
            if ((i + 1) == list_chat_date.size()) {
                final_chat_ll = ll_chat_scroll;
            }
            TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_date.setText(list_chat_date.get(i).getDate());
            List<ChatResultPOJO> list_chats = list_chat_date.get(i).getChatResultPOJOList();
            for (int k = 0; k < list_chats.size(); k++) {
                final LayoutInflater inflater_chat = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view_chat = inflater_chat.inflate(R.layout.inflate_chat_result, null);

                LinearLayout ll_main_msg = (LinearLayout) view_chat.findViewById(R.id.ll_main_msg);
                LinearLayout ll_msg = (LinearLayout) view_chat.findViewById(R.id.ll_msg);
                TextView tv_msg = (TextView) view_chat.findViewById(R.id.tv_msg);
                ImageView iv_chat_msg = (ImageView) view_chat.findViewById(R.id.iv_chat_msg);
                ChatResultPOJO chatResultPOJO=list_chats.get(k);

                if(chatResultPOJO.getFile_type().equals("text")) {
                    tv_msg.setText(chatResultPOJO.getChat_msg());
                    tv_msg.setVisibility(View.VISIBLE);
                    iv_chat_msg.setVisibility(View.GONE);
                }
                else{
                    tv_msg.setVisibility(View.GONE);
                    iv_chat_msg.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load("http://craton.ch/conway/user/"+chatResultPOJO.getChat_file()).
                            into(iv_chat_msg);
                }


                if(chatResultPOJO.getChat_user_id().equals(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""))){
                    ll_msg.setBackgroundResource(R.drawable.chat_send_linear_back);
                    ll_main_msg.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                }
                else{
                    ll_msg.setBackgroundResource(R.drawable.chat_receive_linear_back);
                    ll_main_msg.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                }

                ll_chat_scroll.addView(view_chat);
            }
            ll_chat.addView(view);
        }

        scroll_chat.post(new Runnable() {
            @Override
            public void run() {
                scroll_chat.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void showChatMessages(final List<ChatResultPOJO> list_chat_pojo) {
        ll_chat.removeAllViews();
        for (int i = 0; i < list_chat_pojo.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_chat_result, null);
            LinearLayout ll_main_msg = (LinearLayout) view.findViewById(R.id.ll_main_msg);
            LinearLayout ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            ImageView iv_chat_msg = (ImageView) view.findViewById(R.id.iv_chat_msg);
            ChatResultPOJO chatResultPOJO=list_chat_pojo.get(i);

            if(chatResultPOJO.getFile_type().equals("text")) {
                tv_msg.setText(chatResultPOJO.getChat_msg());
                tv_msg.setVisibility(View.VISIBLE);
                iv_chat_msg.setVisibility(View.GONE);
            }
            else{
                tv_msg.setVisibility(View.GONE);
                iv_chat_msg.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load("http://craton.ch/conway/user/"+chatResultPOJO.getChat_file()).
                        into(iv_chat_msg);
            }



            if(chatResultPOJO.getChat_user_id().equals(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""))){
                ll_msg.setBackgroundResource(R.drawable.chat_send_linear_back);
                ll_main_msg.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            }
            else{
                ll_msg.setBackgroundResource(R.drawable.chat_receive_linear_back);
                ll_main_msg.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            }


            ll_chat.addView(view);
        }
        scroll_chat.post(new Runnable() {
            @Override
            public void run() {
                scroll_chat.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void showSingleChatMessages(final ChatResultPOJO chatResultPOJO) {
        if(final_chat_ll!=null) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_chat_result, null);
            LinearLayout ll_main_msg = (LinearLayout) view.findViewById(R.id.ll_main_msg);
            LinearLayout ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            ImageView iv_chat_msg = (ImageView) view.findViewById(R.id.iv_chat_msg);


            if(chatResultPOJO.getFile_type().equals("text")) {
                tv_msg.setText(chatResultPOJO.getChat_msg());
                tv_msg.setVisibility(View.VISIBLE);
                iv_chat_msg.setVisibility(View.GONE);
            }
            else{
                tv_msg.setVisibility(View.GONE);
                iv_chat_msg.setVisibility(View.VISIBLE);
                if(chatResultPOJO.getChat_file().contains("upload/")) {
                    Glide.with(getApplicationContext()).load("http://craton.ch/conway/user/" + chatResultPOJO.getChat_file()).
                            into(iv_chat_msg);
                }
                else{
                    Glide.with(getApplicationContext()).load(chatResultPOJO.getChat_file()).
                            into(iv_chat_msg);
                }
            }

            if (chatResultPOJO.getChat_user_id().equals(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""))) {
                ll_msg.setBackgroundResource(R.drawable.chat_send_linear_back);
                ll_main_msg.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            } else {
                ll_msg.setBackgroundResource(R.drawable.chat_receive_linear_back);
                ll_main_msg.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }
            tv_msg.setText(chatResultPOJO.getChat_msg());

            final_chat_ll.addView(view);
        }else{
            List<ChatResultPOJO> list=new ArrayList<>();
            list.add(chatResultPOJO);
            orderchatToDate(list);
        }
        scroll_chat.post(new Runnable() {
            @Override
            public void run() {
                scroll_chat.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void parseSendMessageResponse(String response){
        Log.d(TAG,"chat response:-"+response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("1")){
                getOldChat(user_id,Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,""));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.CHAT));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("message");

            try{
                Gson gson=new Gson();
                ChatResultPOJO chatResultPOJO=gson.fromJson(result,ChatResultPOJO.class);

                if(user_id.equals(chatResultPOJO.getChat_fri_id())||user_id.equals(chatResultPOJO.getChat_user_id())){
//                    ChatResultPOJO pojo = new ChatResultPOJO("", chat_fri_id, chat_user_id, date, time, message,
//                            title, message, "", "");
//                    list_chat_pojo_gl.add(pojo);
                    showSingleChatMessages(chatResultPOJO);
//                    showChatMessages(list_chat_pojo_gl);
                }
                else {

                }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d(TAG,e.toString());
            }
        }
    };


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }
}
