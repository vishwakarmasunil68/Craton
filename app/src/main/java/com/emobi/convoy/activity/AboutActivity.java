package com.emobi.convoy.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.webservices.WebServicesUrls;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cv_profile)
    CircleImageView cv_profile;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.tv_fb_link)
    TextView tv_fb_link;
    @BindView(R.id.tv_twitter_link)
    TextView tv_twitter_link;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_bio)
    TextView tv_bio;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){

            String profilepic=bundle.getString("profilepic");
            String name=bundle.getString("name");
            String gender=bundle.getString("gender");
            String dob=bundle.getString("dob");
            String fblink=bundle.getString("fblink");
            String twitterlink=bundle.getString("twitterlink");
            String email=bundle.getString("email");
            String bio=bundle.getString("bio");

            Glide.with(getApplicationContext()).load(WebServicesUrls.IMAGE_BASE_URL+profilepic).into(cv_profile);
            tv_name.setText(name);
            tv_gender.setText(gender);
            tv_dob.setText(dob);
            tv_fb_link.setText(fblink);
            tv_twitter_link.setText(twitterlink);
            tv_email.setText(email);
            tv_bio.setText(bio);

        }
        tv_title.setText("About");
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
}
