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
import android.widget.Button;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InterestActivity extends AppCompatActivity implements View.OnClickListener, WebServicesCallBack {

    private final String TAG = getClass().getSimpleName();
    private final String INTEREST_UPDATE_API_CALL = "interest_update_api_call";

    @BindView(R.id.tv_entertainment)
    TextView tv_entertainment;
    @BindView(R.id.tv_travelling)
    TextView tv_travelling;
    @BindView(R.id.tv_family)
    TextView tv_family;
    @BindView(R.id.tv_study)
    TextView tv_study;
    @BindView(R.id.tv_everything)
    TextView tv_everything;
    @BindView(R.id.tv_gaming)
    TextView tv_gaming;
    @BindView(R.id.tv_sports)
    TextView tv_sports;
    @BindView(R.id.tv_technology)
    TextView tv_technology;
    @BindView(R.id.tv_action)
    TextView tv_action;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_indoor)
    TextView tv_indoor;
    @BindView(R.id.tv_outdoor)
    TextView tv_outdoor;

    boolean bol_entertainment = true, bol_travelling = true, bol_family = true, bol_study = true,
            bol_everything = true, bol_gaming = true, bol_sports = true, bol_technology = true, bol_action = true;
    boolean[] bols = new boolean[]{true, true, true, true, true, true, true, true, true,true,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Interest Select Minimum Two");
        tv_title.setText("Interest Minimum Two");


        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        setSelection(tv_entertainment, 0);
        setSelection(tv_travelling, 1);
        setSelection(tv_family, 2);
        setSelection(tv_study, 3);
        setSelection(tv_everything, 4);
        setSelection(tv_gaming, 5);
        setSelection(tv_sports, 6);
        setSelection(tv_technology, 7);
        setSelection(tv_action, 8);
        setSelection(tv_indoor, 9);
        setSelection(tv_outdoor, 10);

        tv_entertainment.setOnClickListener(this);
        tv_travelling.setOnClickListener(this);
        tv_family.setOnClickListener(this);
        tv_study.setOnClickListener(this);
        tv_everything.setOnClickListener(this);
        tv_gaming.setOnClickListener(this);
        tv_sports.setOnClickListener(this);
        tv_technology.setOnClickListener(this);
        tv_action.setOnClickListener(this);
        tv_indoor.setOnClickListener(this);
        tv_outdoor.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        MobileAds.initialize(this, "ca-app-pub-1163685788193118~7085127386");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4E448BFE118E3DBD390404E63D74029A")
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSelection(TextView textView, int index) {
        if (bols[index]) {
            bols[index] = false;
            textView.setBackgroundResource(R.drawable.interest_option_non_selected);
            textView.setTextColor(Color.parseColor("#000000"));
            Log.d(TAG, "selected " + index + "  " + bols[index]);
        } else {
            bols[index] = true;
            textView.setBackgroundResource(R.drawable.interest_option_back_selected);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            Log.d(TAG, "unselected " + index + "  " + bols[index]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_entertainment:
                setSelection(tv_entertainment, 0);
                break;
            case R.id.tv_travelling:
                setSelection(tv_travelling, 1);
                break;
            case R.id.tv_family:
                setSelection(tv_family, 2);
                break;
            case R.id.tv_study:
                setSelection(tv_study, 3);
                break;
            case R.id.tv_everything:
                setSelection(tv_everything, 4);
                break;
            case R.id.tv_gaming:
                setSelection(tv_gaming, 5);
                break;
            case R.id.tv_sports:
                setSelection(tv_sports, 6);
                break;
            case R.id.tv_technology:
                setSelection(tv_technology, 7);
                break;
            case R.id.tv_action:
                setSelection(tv_action, 8);
                break;
            case R.id.tv_indoor:
                setSelection(tv_indoor, 9);
                break;
            case R.id.tv_outdoor:
                setSelection(tv_outdoor, 10);
                break;

            case R.id.btn_submit:
                callInterestAPI();
                break;


        }
    }

    public boolean checkInterestCount() {
        boolean first = false, second = false;
        for (boolean bol : bols) {
            if (bol) {
                if (first) {
                    second = true;
                } else {
                    first = true;
                }
            } else {

            }
        }
        if (first && second) {
            return true;
        } else {
            return false;
        }
    }

    public void callInterestAPI() {
        if(checkInterestCount()) {
            String entertainment = "", sports = "", travelling = "", study = "", gaming = "", technology = "",
                    action = "", everything = "", family = "",indoor="",outdoor="";

            if (bols[0]) {
                entertainment = "entertainment";
            }
            if (bols[1]) {
                travelling = "travelling";
            }
            if (bols[2]) {
                family = "family";
            }
            if (bols[3]) {
                study = "study";
            }
            if (bols[4]) {
                everything = "everything";
            }
            if (bols[5]) {
                gaming = "gaming";
            }
            if (bols[6]) {
                sports = "sports";
            }
            if (bols[7]) {
                technology = "technology";
            }
            if (bols[8]) {
                action = "action";
            }
            if (bols[9]) {
                indoor = "indoor";
            }
            if (bols[10]) {
                outdoor = "outdoor";
            }


            Log.d(TAG, "entertainment:-" + entertainment);
            Log.d(TAG, "sports:-" + sports);
            Log.d(TAG, "travelling:-" + travelling);
            Log.d(TAG, "study:-" + study);
            Log.d(TAG, "gaming:-" + gaming);
            Log.d(TAG, "technology:-" + technology);
            Log.d(TAG, "action:-" + action);
            Log.d(TAG, "everything:-" + everything);
            Log.d(TAG, "family:-" + family);
            Log.d(TAG, "indoor:-" + indoor);
            Log.d(TAG, "outdoor:-" + outdoor);


            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("log_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            nameValuePairs.add(new BasicNameValuePair("log_entertainment", entertainment));
            nameValuePairs.add(new BasicNameValuePair("log_sports", sports));
            nameValuePairs.add(new BasicNameValuePair("log_travelling", travelling));
            nameValuePairs.add(new BasicNameValuePair("log_study", study));
            nameValuePairs.add(new BasicNameValuePair("log_gaming", gaming));
            nameValuePairs.add(new BasicNameValuePair("log_technology", technology));
            nameValuePairs.add(new BasicNameValuePair("log_action", action));
            nameValuePairs.add(new BasicNameValuePair("log_everything", everything));
            nameValuePairs.add(new BasicNameValuePair("log_family", family));
            nameValuePairs.add(new BasicNameValuePair("log_indoor", indoor));
            nameValuePairs.add(new BasicNameValuePair("log_outdoor", outdoor));
            new WebServiceBase(nameValuePairs, this, INTEREST_UPDATE_API_CALL).execute(WebServicesUrls.INTEREST_UPDATE_URL);
        }
        else{
            ToastClass.showShortToast(getApplicationContext(),"Please Select Minimum two interest");
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case INTEREST_UPDATE_API_CALL:
                parseInterestAPIResponse(response);
                break;
        }
    }

    public void parseInterestAPIResponse(String response) {
        Log.d(TAG, "respoinse:-" + response);
        ToastClass.showShortToast(getApplicationContext(), "Interest Updated Successfully");
        finish();
    }
}
