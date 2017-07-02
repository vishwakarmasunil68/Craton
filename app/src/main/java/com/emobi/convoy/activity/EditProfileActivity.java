package com.emobi.convoy.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity implements WebServicesCallBack,com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{

    private final String TAG=getClass().getSimpleName();
    private final String UPDATE_PROFILE_API="update_profile_api";


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_location)
    EditText et_location;
    @BindView(R.id.et_gender)
    EditText et_gender;
    @BindView(R.id.et_facebook_page)
    EditText et_facebook_page;
    @BindView(R.id.et_twitter_page)
    EditText et_twitter_page;
    @BindView(R.id.et_bio)
    EditText et_bio;
    @BindView(R.id.et_dob)
    EditText et_dob;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.switch_visibility)
    Switch switch_visibility;
    String string_visibility="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        tv_title.setText("Edit Profile");

        et_name.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME,""));
        et_location.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_LOC,""));
        et_gender.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_GEN,""));
        et_facebook_page.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK,""));
        et_twitter_page.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_TWITTER,""));
        et_bio.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_BIO,""));
        et_dob.setText(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_DOB,""));

        et_dob.setFocusable(false);
        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });

        if(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_VISIBILITY,"").equals("public")){
            switch_visibility.setChecked(true);
            string_visibility="public";
        }else{
            string_visibility="private";
        }

        switch_visibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    string_visibility="public";
                    Log.d(TAG,"visibility:-public");
                }else{
                    string_visibility="private";
                    Log.d(TAG,"visibility:-private");
                }
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEditAPI();
            }
        });
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDate(){
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                EditProfileActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
    public void callEditAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,"")));
        nameValuePairs.add(new BasicNameValuePair("log_name", et_name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_gen", et_gender.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_dob", et_dob.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_facbook", et_facebook_page.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_twitter", et_twitter_page.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_bio", et_bio.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("log_status", ""));
        nameValuePairs.add(new BasicNameValuePair("log_loc", et_location.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("visibility", string_visibility));
        new WebServiceBase(nameValuePairs, this, UPDATE_PROFILE_API,true).execute(WebServicesUrls.UPDATE_ACCOUNT_API);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case UPDATE_PROFILE_API:
                parseEditProfileResponse(response);
                break;
        }
    }

    public void parseEditProfileResponse(String response){
        Log.d(TAG,"update response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                JSONObject result=jsonObject.optJSONObject("result");
                String log_name=result.optString("log_name");
                String log_email=result.optString("log_email");
                String log_gen=result.optString("log_gen");
                String log_dob=result.optString("log_dob");
                String log_pics=result.optString("log_pics");
                String log_facbook=result.optString("log_facbook");
                String log_twitter=result.optString("log_twitter");
                String log_loc=result.optString("log_loc");
                String log_bio=result.optString("log_bio");
                String log_status=result.optString("log_status");

                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_NAME,log_name);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_EMAIL,log_email);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_GEN,log_gen);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_DOB,log_dob);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_PICS,log_pics);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_FACEBOOK,log_facbook);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_TWITTER,log_twitter);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_LOC,log_loc);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_BIO,log_bio);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_STATUS,log_status);
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_VISIBILITY,string_visibility);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(),"Editing failed",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
        et_dob.setText(date);
    }
}
