package com.emobi.convoy.test;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebUploadService;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.Date;

import asia.ivity.android.marqueeview.MarqueeView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements WebServicesCallBack{

    @BindView(R.id.mywidget)
    TextView mywidget;
    MarqueeView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
//        callAPI();
        mywidget.setSelected(true);


//        mv = (MarqueeView) findViewById(R.id.marqueeView100);
////        mv.setPauseBetweenAnimations(500);
//        mv.setSpeed(10);
////        getWindow().getDecorView().post(new Runnable() {
////            @Override
////            public void run() {
////                mv.startMarquee();
////            }
////        });
//        mv.startMarquee();

//        final TextView textView1 = (TextView) findViewById(R.id.textView1);
//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                textView1.setText(result.optString("message"));
//                mv.startMarquee();
//            }
//        }, 1000);
        WebView webView;
        webView = (WebView)findViewById(R.id.web);
        String summary = "<html><FONT color='#fdb728' FACE='courier'><marquee behavior='scroll' direction='left' scrollamount=10 style=background-color='#000000'>"
                + "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do." + "</marquee></FONT></html>";
        webView.loadData(summary, "text/html", "utf-8"); // Set focus to the textview
    }
    public void callAPI(){
        Date date = new Date();
        final String c_tmstamp = date.getTime() + "";
        try {
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            File f=new File(Environment.getExternalStorageDirectory()+File.separator+"temp.png");
            FileBody bin1 = new FileBody(f);
            reqEntity.addPart("tsound", bin1);
            reqEntity.addPart("tvideo", new StringBody(""));
            reqEntity.addPart("time", new StringBody(c_tmstamp));
            reqEntity.addPart("effert_level", new StringBody("hello"));
            reqEntity.addPart("comment", new StringBody("comment"));
            reqEntity.addPart("login_id", new StringBody("tejas"));
            reqEntity.addPart("task", new StringBody("MorningAffirmation"));

            new WebUploadService(reqEntity, this, "api").execute("http://www.healthyhappylife.org/healthyhappylife/addaudeiovideo.php");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private final String TAG=getClass().getSimpleName();
    @Override
    public void onGetMsg(String[] msg) {
        String response=msg[1];
        Log.d(TAG,"response:-"+response);

    }
}
