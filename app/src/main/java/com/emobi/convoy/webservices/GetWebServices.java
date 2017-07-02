package com.emobi.convoy.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.emobi.convoy.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sunil on 05-04-2017.
 */

public class GetWebServices extends AsyncTask<String,Void,String>{

    private final String TAG=getClass().getSimpleName();
    Activity activity;
    Fragment fragment;
    ProgressDialog progressDialog;
    String msg;
    boolean prg_status;
    String response;
    public GetWebServices( Activity activity, String msg, boolean prg_status){
        this.activity = activity;
        this.msg = msg;
        this.prg_status=prg_status;
        Log.d(TAG, this.toString());
    }
    public GetWebServices( Activity activity, String msg){
        this.activity=activity;
        this.msg=msg;
        Log.d(TAG,this.toString());
    }
    public GetWebServices(Activity activity, Fragment fragment, String msg){
        this.activity=activity;
        this.msg=msg;
        this.fragment=fragment;
        Log.d(TAG,this.toString());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!prg_status) {
            progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        InputStream inStream = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            this.response=response;
//            object = (JSONObject) new JSONTokener(response).nextValue();
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        Log.d(TAG,"response:-"+response);
        if(fragment!=null){
            WebServicesCallBack mcallback = (WebServicesCallBack) fragment;
            String rsp[] = {msg, s};
            mcallback.onGetMsg(rsp);
        }
        else {
            WebServicesCallBack mcallback = (WebServicesCallBack) activity;
            String rsp[] = {msg, s};
            mcallback.onGetMsg(rsp);
        }
    }
}
