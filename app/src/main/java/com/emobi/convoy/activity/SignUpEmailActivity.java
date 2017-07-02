package com.emobi.convoy.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.RegisterPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpEmailActivity extends AppCompatActivity implements View.OnClickListener, WebServicesCallBack {
    private static final String FORGOT_PASSWORD = "forgot_password";
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_background)
    ImageView iv_background;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);

        Glide.with(this).load(R.raw.login_background).asGif().into(iv_background);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginMethod();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forgot_password:
                showForgotDialog();
                break;

        }
    }

    public void showForgotDialog() {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setContentView(R.layout.dialog_forgot_password);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.setTitle("Login again");
        dialog1.setCancelable(true);
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText et_email = (EditText) dialog1.findViewById(R.id.et_email);
        Button btn_email = (Button) dialog1.findViewById(R.id.btn_email);

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_email.getText().toString().length() > 0) {
                    callForgotAPI(et_email.getText().toString());
                    dialog1.dismiss();
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "Please Enter Emaill");
                }
            }
        });
    }

    public void callForgotAPI(String email) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("reg_email", email));
        new WebServiceBase(nameValuePairs, this, FORGOT_PASSWORD).execute(WebServicesUrls.FORGOT_PASSWORD_URL);
    }

    public void loginMethod() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Fill All Box Properly", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("log_email", email));
            nameValuePairs.add(new BasicNameValuePair("log_password", password));
            nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetDeviceToken(getApplicationContext(), "")));
            nameValuePairs.add(new BasicNameValuePair("log_os_type", "android"));
            Pref.SetStringPref(getApplicationContext(), Pref.FCM_REGISTRATION_TOKEN, Pref.GetDeviceToken(getApplicationContext(), ""));
            new WebServiceBase(nameValuePairs, this, "login").execute(WebServicesUrls.LOGIN_URL);
        }
        Log.d(TAG, "device token:-" + Pref.GetStringPref(getApplicationContext(), Pref.FCM_REGISTRATION_TOKEN, ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case "login":
                parseLoginData(response);
                break;

            case FORGOT_PASSWORD:
                parseForgotPasswordAPI(response);
                break;
        }
    }

    public void parseForgotPasswordAPI(String response) {
        Log.d(TAG, "forgot password response:-" + response);
        if (response.contains("password Sent Successfully")) {
            ToastClass.showShortToast(getApplicationContext(), "Password has been sent to your email");
        } else {
            ToastClass.showShortToast(getApplicationContext(), "Email id does not exit!");
        }
    }

    private final String TAG = getClass().getName();

    public void parseLoginData(String response) {
        Log.d(TAG, "login Response:-" + response);
        try {
            Gson gson = new Gson();
            RegisterPOJO pojo = gson.fromJson(response, RegisterPOJO.class);
            if (pojo != null) {
                try {
                    if (pojo.getMessage().equals("Invalid Email Or Password")) {
                        Toast.makeText(getApplicationContext(), "Invalid Email Or Password", Toast.LENGTH_LONG).show();
                    } else {

                    }
                } catch (Exception e) {
                    try {
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ID, pojo.getLog_id());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_NAME, pojo.getLog_name());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EMAIL, pojo.getLog_email());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEN, pojo.getLog_gen());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DOB, pojo.getLog_dob());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PASSWORD, pojo.getLog_password());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MOBILE, pojo.getLog_mob());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FACEBOOK, pojo.getLog_facbook());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TWITTER, pojo.getLog_twitter());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_BIO, pojo.getLog_bio());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LOC, pojo.getLog_loc());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TAG, pojo.getLog_tag());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_LAST_LOGIN, pojo.getLog_last_login());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CREATED, pojo.getLog_created());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_PICS, pojo.getLog_pics());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_COVER_PHOTO, pojo.getLog_cover_photo());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ROOM, pojo.getLog_room());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_DEVICE_TOKEN, pojo.getLog_device_token());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ENTERTAINMENT, pojo.getLog_entertainment());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_SPORTS, pojo.getLog_sports());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TRAVELLING, pojo.getLog_travelling());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STUDY, pojo.getLog_study());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GAMING, pojo.getLog_gaming());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_TECHNOLOGY, pojo.getLog_technology());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ACTION, pojo.getLog_action());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_EVERYTHING, pojo.getLog_everything());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_INDOOR,pojo.getLog_indoor());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_OUTDOOR,pojo.getLog_outdoor());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_FAMILY, pojo.getLog_family());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS1, pojo.getLog_ans1());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS2, pojo.getLog_ans2());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS3, pojo.getLog_ans3());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS4, pojo.getLog_ans4());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_ANS5, pojo.getLog_ans5());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_MESSAGE, pojo.getLog_message());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_STATUS, pojo.getLog_status());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VISIBILITY, pojo.getVisibility());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VIDEO_CALL_STATUS, pojo.getLog_videocall_status());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_OS_TYPE, pojo.getLog_os_type());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_GEO_LOCATION, pojo.getLog_geo_location());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_CALL_TIME, pojo.getLog_call_time());
                        Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VALIDITY, pojo.getLog_validity());
                        Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, true);

                        Intent intent = new Intent(SignUpEmailActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } catch (Exception e1) {
                        ToastClass.showShortToast(getApplicationContext(), "Invalid Email or Password");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Something went wrong");
        }
    }
}
