package com.emobi.convoy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.emobi.convoy.R;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;

public class SinchLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinch_login);
    }

    private void loginClicked() {
        String userName = Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME,"");

//        if (userName.isEmpty()) {
//            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if (!getSinchServiceInterface().isStarted()) {
//            getSinchServiceInterface().startClient(userName);
//            showSpinner();
//        } else {
//            openPlaceCallActivity();
//        }
    }

}
