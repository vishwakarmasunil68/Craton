package com.emobi.convoy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.diary.DiaryPOJO;
import com.emobi.convoy.pojo.diary.DiaryResultPOJO;
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

public class DiaryActivity extends AppCompatActivity implements WebServicesCallBack{

    private final String TAG=getClass().getSimpleName();
    private static final String GET_DIARY_API="get_diary_api";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ll_scroll)
    LinearLayout ll_scroll;
    @BindView(R.id.fab_add_diary)
    FloatingActionButton fab_add_diary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Diaries");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        tv_title.setText("Personal Notes");


        fab_add_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DiaryActivity.this,AddDiaryActivity.class));
            }
        });


        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callDiaryAPI();
    }

    public void callDiaryAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, GET_DIARY_API).execute(WebServicesUrls.GET_DIARY);
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
            case GET_DIARY_API:
                parseResponse(response);
                break;
        }
    }
    public void parseResponse(String response){
        Log.d(TAG,"response:-"+response);
        try{
            Gson gson=new Gson();
            DiaryPOJO diaryPOJO=gson.fromJson(response,DiaryPOJO.class);
            if(diaryPOJO.getSuccess().equals("true")) {
                inflateDiary(diaryPOJO.getDiaryResultPOJOs());
            }
            else{
                Toast.makeText(getApplicationContext(),"No Personal Notes Found",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void inflateDiary(List<DiaryResultPOJO> list_diaries){
        ll_scroll.removeAllViews();
        for (int i = 0; i < list_diaries.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_diaries, null);
            DiaryResultPOJO diaryPOJO=list_diaries.get(i);

            TextView tv_diary_note= (TextView) view.findViewById(R.id.tv_diary_note);
            TextView tv_date= (TextView) view.findViewById(R.id.tv_date);


            tv_diary_note.setText(diaryPOJO.getDiary_msg());
            tv_date.setText(list_diaries.get(i).getDiary_datetime());

            ll_scroll.addView(view);
        }
    }
}
