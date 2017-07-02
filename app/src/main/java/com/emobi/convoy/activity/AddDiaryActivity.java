package com.emobi.convoy.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emobi.convoy.R;
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

public class AddDiaryActivity extends AppCompatActivity implements WebServicesCallBack{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.et_diary)
    EditText et_diary;
    @BindView(R.id.ll_edit)
    LinearLayout ll_edit;

    private KeyListener originalKeyListener;

    private final String TAG=getClass().getSimpleName();
    private static final String ADD_DIARY_API="add_diary_api";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Add Notes");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_diary.getText().toString().length()>0){
                    callDiaryAPI(et_diary.getText().toString());
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Enter Something",Toast.LENGTH_LONG).show();
                }
            }
        });
        originalKeyListener = et_diary.getKeyListener();
        et_diary.setKeyListener(null);
        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_diary.setKeyListener(originalKeyListener);
                // Focus the field.
                et_diary.requestFocus();
                // Show soft keyboard for the user to enter the value.
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_diary, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    public void callDiaryAPI(String text){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("diary_user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        nameValuePairs.add(new BasicNameValuePair("diary_msg", text));
        new WebServiceBase(nameValuePairs, this, ADD_DIARY_API).execute(WebServicesUrls.ADD_DIARY);
    }
    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case ADD_DIARY_API:
                parseResponse(response);
                break;
        }
    }
    public void parseResponse(String response){
        Log.d(TAG,"response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response.toString());
            String success=jsonObject.optString("success");
            if(success.equals("true")){
                Toast.makeText(getApplicationContext(),"Note Added Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"No Note Added",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
}
