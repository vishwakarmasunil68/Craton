package com.emobi.convoy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.addvisitor.AddVisitorPOJO;
import com.emobi.convoy.pojo.addvisitor.AddVisitorResultPOJO;
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

public class FriendQuestionActivity extends AppCompatActivity implements View.OnClickListener, WebServicesCallBack {

    private static final String FRIEND_REQUEST_API = "friend_request_api";
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.tv_ans_question)
    TextView tv_ans_question;
    @BindView(R.id.tv_study_ques)
    TextView tv_study_ques;
    @BindView(R.id.tv_traveller)
    TextView tv_traveller;
    @BindView(R.id.tv_gadget_select)
    TextView tv_gadget_select;
    @BindView(R.id.tv_indoor_ques)
    TextView tv_indoor_ques;
    @BindView(R.id.tv_action_ques)
    TextView tv_action_ques;

    @BindView(R.id.ll_study_yes)
    LinearLayout ll_study_yes;
    @BindView(R.id.ll_study_no)
    LinearLayout ll_study_no;
    @BindView(R.id.ll_traveller_yes)
    LinearLayout ll_traveller_yes;
    @BindView(R.id.ll_traveller_no)
    LinearLayout ll_traveller_no;
    @BindView(R.id.ll_gadgets)
    LinearLayout ll_gadgets;
    @BindView(R.id.ll_family)
    LinearLayout ll_family;
    @BindView(R.id.ll_gadgets_both)
    LinearLayout ll_gadgets_both;
    @BindView(R.id.ll_indoor)
    LinearLayout ll_indoor;
    @BindView(R.id.ll_outdoor)
    LinearLayout ll_outdoor;
    @BindView(R.id.ll_indoor_both)
    LinearLayout ll_indoor_both;
    @BindView(R.id.ll_action)
    LinearLayout ll_action;
    @BindView(R.id.ll_entertainment)
    LinearLayout ll_entertainment;
    @BindView(R.id.ll_action_both)
    LinearLayout ll_action_both;
    @BindView(R.id.btn_add_friend)
    Button btn_add_friend;
    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    boolean study_yes = false, study_no = false, traveller_yes = false, traveller_no = false,
            gadgets = false, family = false, gadgets_both = false, indoor = false, outdoor = false,
            indoor_both = false, action = false, entertainment = false, action_both = false;


    AddVisitorPOJO addVisitorPOJO;
    AddVisitorResultPOJO addVisitorResultPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_question);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addVisitorPOJO = (AddVisitorPOJO) getIntent().getSerializableExtra("addvisitorpojo");
        if (addVisitorPOJO != null) {
            addVisitorResultPOJO = addVisitorPOJO.getAddVisitorResultPOJO();

            String friend_name = addVisitorResultPOJO.getLogName();
            tv_ans_question.setText("Answer these questions to add " + friend_name
                    + " as your friend");
            tv_study_ques.setText("Do " + friend_name + " likes study");
            tv_traveller.setText(friend_name + " is a traveller");
            tv_gadget_select.setText("What will " + friend_name
                    + " select between gadgets and family/friends");
            tv_indoor_ques.setText(friend_name + " is an indoor or outdoor game player");
            tv_action_ques.setText("Action or Entertainment what is more important for "
                    + friend_name);
        } else {
            finish();
        }

        addlisteners();
        tv_title.setText("Answer Questions");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    public void addlisteners() {
        ll_study_yes.setOnClickListener(this);
        ll_study_no.setOnClickListener(this);
        ll_traveller_yes.setOnClickListener(this);
        ll_traveller_no.setOnClickListener(this);
        ll_gadgets.setOnClickListener(this);
        ll_family.setOnClickListener(this);
        ll_gadgets_both.setOnClickListener(this);
        ll_indoor.setOnClickListener(this);
        ll_outdoor.setOnClickListener(this);
        ll_indoor_both.setOnClickListener(this);
        ll_action.setOnClickListener(this);
        ll_entertainment.setOnClickListener(this);
        ll_action_both.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_study_yes:
                checkStudyYesCond();
                break;
            case R.id.ll_study_no:
                checkStudyNoCond();
                break;
            case R.id.ll_traveller_yes:
                checkTravellerYes();
                break;
            case R.id.ll_traveller_no:
                checkTravellerNo();
                break;
            case R.id.ll_gadgets:
                checkGadgetsCond();
                break;
            case R.id.ll_family:
                checkFamilyCond();
                break;
            case R.id.ll_gadgets_both:
                checkGadgetsBothCond();
                break;
            case R.id.ll_indoor:
                checkIndoorCond();
                break;
            case R.id.ll_outdoor:
                checkOutDoor();
                break;
            case R.id.ll_indoor_both:
                checkIndoorBoth();
                break;
            case R.id.ll_action:
                CheckAction();
                break;
            case R.id.ll_entertainment:
                CheckEntertainment();
                break;
            case R.id.ll_action_both:
                CheckActionBoth();
                break;
            case R.id.btn_add_friend:
                checkAnswers();
                break;
        }
    }

    public void checkAnswers() {
        boolean correct = true;
//        boolean first=false,second=false,third=false,forth=false,fifth=false;
        boolean[] bols={false,false,false,false,false};
        if (addVisitorResultPOJO != null) {
            if (addVisitorResultPOJO.getLogStudy().length() > 0) {
                if (study_yes) {
                    bols[0]=true;
                }else{
                    bols[0]=false;
                }
            }else{
                if(study_no){
                    bols[0]=true;
                }else{
                    bols[0]=false;
                }
            }
            if (addVisitorResultPOJO.getLogTravelling().length() > 0) {
                if (traveller_yes) {
                    bols[1]=true;
                }else{
                    bols[1]=false;
                }
            }else{
                if (traveller_no) {
                    bols[1]=true;
                }else{
                    bols[1]=false;
                }
            }
            if (addVisitorResultPOJO.getLogTechnology().length() > 0 && addVisitorResultPOJO.getLogFamily().length() > 0) {
                if (gadgets_both) {
                    bols[2] = true;
                }else{
                    bols[2]=false;
                }
            } else {
                if (addVisitorResultPOJO.getLogTechnology().length() > 0) {
                    if (gadgets) {
                        bols[2] = true;
                    }else{
                        bols[2]=false;
                    }
                } else {
                    if (addVisitorResultPOJO.getLogFamily().length() > 0) {
                        if (family) {
                            bols[2] = true;
                        }else{
                            bols[2]=false;
                        }
                    }else{
                        if(!family&&!gadgets&&!gadgets_both){
                            bols[2]=true;
                        }else{
                            bols[2]=false;
                        }
                    }
                }
            }

//            if (addVisitorResultPOJO.getLogSports().length() > 0) {
//                if (indoor_both) {
//                    bols[3] = true;
//                }else{
//                    bols[3]=false;
//                }
//            }else{
//                if(!indoor_both&&!indoor&&!outdoor){
//                    bols[3]=true;
//                }else{
//                    bols[3]=false;
//                }
//            }

            if (addVisitorResultPOJO.getLog_indoor().length() > 0 && addVisitorResultPOJO.getLog_outdoor().length() > 0) {
                if (indoor_both) {
                    bols[3] = true;
                }else{
                    bols[3]=false;
                }
            } else {
                if (addVisitorResultPOJO.getLog_indoor().length() > 0) {
                    if (indoor) {
                        bols[3] = true;
                    }else{
                        bols[3]=false;
                    }
                } else {
                    if (addVisitorResultPOJO.getLog_outdoor().length() > 0) {
                        if (outdoor) {
                            bols[3] = true;
                        }else{
                            bols[3]=false;
                        }
                    }else{
                        if(!indoor&&!outdoor&&!indoor_both){
                            bols[3]=true;
                        }else{
                            bols[3]=false;
                        }
                    }
                }
            }



            if (addVisitorResultPOJO.getLogAction().length() > 0 && addVisitorResultPOJO.getLogEntertainment().length() > 0) {
                if (action_both) {
                    bols[4] = true;
                }else{
                    bols[4]=false;
                }
            } else {
                if (addVisitorResultPOJO.getLogAction().length() > 0) {
                    if (action) {
                        bols[4] = true;
                    }else{
                        bols[4]=false;
                    }
                } else {
                    if (addVisitorResultPOJO.getLogEntertainment().length() > 0) {
                        if (entertainment) {
                            bols[4] = true;
                        }else{
                            bols[4]=false;
                        }
                    }else{
                        if(!entertainment&&!action&&!action_both){
                            bols[4]=true;
                        }else{
                            bols[4]=false;
                        }
                    }
                }
            }
            int total_correct=0;
            for(int i=0;i<bols.length;i++){
                if(bols[i]){
                    total_correct++;
                }
                Log.d(TAG,"ques "+i+":-"+bols[i]);
            }
            Log.d(TAG,this.toString());
            if (total_correct>=3) {
//                ToastClass.showShortToast(getApplicationContext(),"total correct:-"+total_correct);
                callAddFriendApi();
            } else {
//                ToastClass.showShortToast(getApplicationContext(), "you have given wrong ans");
//                ToastClass.showShortToast(getApplicationContext(),"total correct:-"+total_correct);
                showAlert();
            }
        } else {

        }
    }
    public void showAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                FriendQuestionActivity.this);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder
                .setMessage("To add "+addVisitorResultPOJO.getLogName()+" as a friend you must know 3 interest of it.")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    public void callAddFriendApi() {
        if (addVisitorResultPOJO != null) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("friend_login_user", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            nameValuePairs.add(new BasicNameValuePair("friend_accept", addVisitorResultPOJO.getLogId()));
            new WebServiceBase(nameValuePairs, this, FRIEND_REQUEST_API).execute(WebServicesUrls.SEND_FRIEND_REQUEST);
        }
    }

    public void checkStudyYesCond() {
        if (study_yes) {
            ll_study_yes.setBackgroundColor(Color.parseColor("#FFFFFF"));
            study_yes = false;
        } else {
            ll_study_yes.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_study_no.setBackgroundColor(Color.parseColor("#FFFFFF"));
            study_yes = true;
            study_no = false;
        }
    }

    public void checkStudyNoCond() {
        if (study_no) {
            ll_study_no.setBackgroundColor(Color.parseColor("#FFFFFF"));
            study_no = false;
        } else {
            ll_study_no.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_study_yes.setBackgroundColor(Color.parseColor("#FFFFFF"));
            study_no = true;
            study_yes = false;
        }
    }

    public void checkTravellerYes() {
        if (traveller_yes) {
            ll_traveller_yes.setBackgroundColor(Color.parseColor("#FFFFFF"));
            traveller_yes = false;
        } else {
            ll_traveller_yes.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_traveller_no.setBackgroundColor(Color.parseColor("#FFFFFF"));
            traveller_yes = true;
            traveller_no = false;
        }
    }

    public void checkTravellerNo() {
        if (traveller_no) {
            ll_traveller_no.setBackgroundColor(Color.parseColor("#FFFFFF"));
            traveller_no = false;
        } else {
            ll_traveller_no.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_traveller_yes.setBackgroundColor(Color.parseColor("#FFFFFF"));
            traveller_no = true;
            traveller_yes = false;
        }
    }

    public void checkGadgetsCond() {
        if (gadgets) {
            ll_gadgets.setBackgroundColor(Color.parseColor("#FFFFFF"));
            gadgets = false;
        } else {
            ll_gadgets.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_family.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_gadgets_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            gadgets = true;
            family = false;
            gadgets_both = false;
        }
    }

    public void checkFamilyCond() {
        if (family) {
            ll_family.setBackgroundColor(Color.parseColor("#FFFFFF"));
            family = false;
        } else {
            ll_family.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_gadgets.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_gadgets_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            gadgets = false;
            family = true;
            gadgets_both = false;
        }
    }

    public void checkGadgetsBothCond() {
        if (gadgets_both) {
            ll_gadgets_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            gadgets_both = false;
        } else {
            ll_gadgets_both.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_family.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_gadgets.setBackgroundColor(Color.parseColor("#FFFFFF"));
            gadgets = false;
            family = false;
            gadgets_both = true;
        }
    }


//    check indoor

    public void checkIndoorCond() {
        if (indoor) {
            ll_indoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            indoor = false;
        } else {
            ll_indoor.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_outdoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_indoor_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            indoor = true;
            outdoor = false;
            indoor_both = false;
        }
    }

    public void checkOutDoor() {
        if (outdoor) {
            ll_outdoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            outdoor = false;
        } else {
            ll_outdoor.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_indoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_indoor_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            indoor = false;
            outdoor = true;
            indoor_both = false;
        }
    }

    public void checkIndoorBoth() {
        if (indoor_both) {
            ll_indoor_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            indoor_both = false;
        } else {
            ll_indoor_both.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_indoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_outdoor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            indoor = false;
            outdoor = false;
            indoor_both = true;
        }
    }


    public void CheckAction() {
        if (action) {
            ll_action.setBackgroundColor(Color.parseColor("#FFFFFF"));
            action = false;
        } else {
            ll_action.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_entertainment.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_action_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            action = true;
            entertainment = false;
            action_both = false;
        }
    }

    public void CheckEntertainment() {
        if (entertainment) {
            ll_entertainment.setBackgroundColor(Color.parseColor("#FFFFFF"));
            entertainment = false;
        } else {
            ll_entertainment.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_action.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_action_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            action = false;
            entertainment = true;
            action_both = false;
        }
    }

    public void CheckActionBoth() {
        if (action_both) {
            ll_action_both.setBackgroundColor(Color.parseColor("#FFFFFF"));
            action_both = false;
        } else {
            ll_action_both.setBackgroundColor(Color.parseColor("#FFFF00"));
            ll_entertainment.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll_action.setBackgroundColor(Color.parseColor("#FFFFFF"));
            action = false;
            entertainment = false;
            action_both = true;
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case FRIEND_REQUEST_API:
                parseFriendRequest(response);
                break;
        }
    }

    public void parseFriendRequest(String response) {
        Log.d(TAG, "response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String friend_id = jsonObject.optString("friend_id");
            String friend_login_user = jsonObject.optString("friend_login_user");
            String friend_accept = jsonObject.optString("friend_accept");
            String friend_status = jsonObject.optString("friend_status");
            String friend_message = jsonObject.optString("friend_message");
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result","1");
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public String toString() {
        return "FriendQuestionActivity{" +
                "TAG='" + TAG + '\'' +

                ", study_yes=" + study_yes +
                ", study_no=" + study_no +
                ", traveller_yes=" + traveller_yes +
                ", traveller_no=" + traveller_no +
                ", gadgets=" + gadgets +
                ", family=" + family +
                ", gadgets_both=" + gadgets_both +
                ", indoor=" + indoor +
                ", outdoor=" + outdoor +
                ", indoor_both=" + indoor_both +
                ", action=" + action +
                ", entertainment=" + entertainment +
                ", action_both=" + action_both +

                '}';
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
}
