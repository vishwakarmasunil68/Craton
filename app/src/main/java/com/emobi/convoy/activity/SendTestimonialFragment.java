package com.emobi.convoy.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by sunil on 08-05-2017.
 */

public class SendTestimonialFragment extends Fragment implements WebServicesCallBack{

    private final String TAG=getClass().getSimpleName();
    private final String CALL_TESTIMONAIL_API="call_testimonial_api";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_send_testimonial)
    Button btn_send_testimonial;
    @BindView(R.id.et_test)
    EditText et_test;
    @BindView(R.id.tv_title)
    TextView tv_title;
    String fri_id;

    public SendTestimonialFragment(String fri_id){
        this.fri_id=fri_id;
    }

    public SendTestimonialFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_sendtestimonial,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed()
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.onBackPressed();
            }
        });

        btn_send_testimonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_test.getText().toString().length()>0){
                    callTestimonialAPI(et_test.getText().toString());
                }else{
                    ToastClass.showShortToast(getActivity().getApplicationContext(),"Please Enter Something");
                }
            }
        });
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(backArrow);
        tv_title.setText("Send Testimonial");
    }
    public void callTestimonialAPI(String testimonial){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tes_sender_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("tes_reciver_id", fri_id));
        nameValuePairs.add(new BasicNameValuePair("tes_msg", testimonial));
        new WebServiceBase(nameValuePairs,getActivity(), this, CALL_TESTIMONAIL_API).execute(WebServicesUrls.SEND_TESTIMONIAL_URL);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];

        switch (apicall){
            case CALL_TESTIMONAIL_API:
                parseTestimonialResponse(response);
                break;
        }
    }

    public void parseTestimonialResponse(String response){
        Log.d(TAG,"testimonial response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Testimonial Send Successfully");
                HomeActivity activity= (HomeActivity) getActivity();
                activity.onBackPressed();
            }else{
                ToastClass.showShortToast(getActivity().getApplicationContext(),"Testimonial Sending Failed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
