package com.emobi.convoy.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.emobi.convoy.R;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class InAppPurchaseActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler,WebServicesCallBack {
    private static final String UPDATE_SERVER_TIME = "update_server_time";
    BillingProcessor bp;
    Button button2,button3;
    private final String TAG=getClass().getSimpleName();
    String server_key="IIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs/nOB+c8176KQMDcx1MSCx7R7nTA+xYwf/vnshMsqdHzpGqU2NM/f0kELxCNm1NnxYf5o1ELpq+tX39UxYgwaVPtUt+twRk0NVnJw/nYJM8B4kE3EjhYWXeoC00dTQrR3wAPUlPrnGHa4gf5LvdwsVe8OrLSGmb9VOIB3rGYrdsA1QLsTbXq6o+D/uzuBeUzuRElDR5QDptr9sE/A7YpLwOQ2QKiMkMdt4OBT5jlgjt02yIVXQrUsllc2Chw9UP/tSZ/GTfExAXQKQu8Hx4OitNG2KR195jQxabeHuqjvGaUWnfqW79HqiaOWdanRuvEFTtJ5vRMwQjXXL9eg7ntbwIDAQAB";
    private static final String CALL_VALIDITY_API = "call_validity_api";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_purchase);
        bp = new BillingProcessor(this, server_key, this);
        bp.initialize();
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"is purchased1:-"+bp.isPurchased(ITEM_SKU3));
                bp.purchase(InAppPurchaseActivity.this, ITEM_SKU3);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"is purchased2:-"+bp.isPurchased(ITEM_SKU2));
                bp.purchase(InAppPurchaseActivity.this, ITEM_SKU2);
            }
        });

    }
    String ITEM_SKU1="com.emobi.convoy.product10";
    String ITEM_SKU2="com.emobi.convoy.2dollar";
    String ITEM_SKU3="android.test.purchased";

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if(productId.equals(ITEM_SKU2)){
            callValidityAPI();
        }
    }
    public void callValidityAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_VALIDITY_API).execute(WebServicesUrls.VALIDITY_API_URL);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {
        Log.d(TAG,"billing initialized");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case CALL_VALIDITY_API:parseCallValidityAPIResponse(response);
                break;
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
                Toast.makeText(getApplicationContext(),"Congratulations your account has been credited with 60 mins.",Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        startActivity(new Intent(InAppPurchaseActivity.this, HomeActivity.class));
        finishAffinity();
    }

    public void parseCallValidityAPIResponse(String response){
        Log.d(TAG,"call response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            String success=jsonObject.optString("success");
            if(success.equals("true")){
                JSONArray jsonArray=jsonObject.optJSONArray("result");
                JSONObject resObject=jsonArray.optJSONObject(0);
                String log_validity=resObject.optString("log_validity");
                Pref.SetStringPref(getApplicationContext(),StringUtils.LOG_VALIDITY,log_validity);
                String[] validity=log_validity.split(":");
                int hour=Integer.parseInt(validity[0]);
                int min=Integer.parseInt(validity[1]);
                int sec=Integer.parseInt(validity[2]);

                long totalseconds=(hour*60*60)+(min*60)+sec;
                totalseconds=totalseconds+(60*60);
                long totalSecs = totalseconds;
                long hours = totalSecs / 3600;
                long minutes = (totalSecs % 3600) / 60;
                long seconds = totalSecs % 60;
                String string_time = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_call_time", ""));
                nameValuePairs.add(new BasicNameValuePair("log_validity", string_time));
                nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
                new WebServiceBase(nameValuePairs, this, UPDATE_SERVER_TIME).execute(WebServicesUrls.UPDATE_CALL_TIME);
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Something went wrong");
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Something went wrong");
        }
    }
}
