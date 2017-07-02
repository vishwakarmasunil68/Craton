package com.emobi.convoy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.rssfeeds.Articles;
import com.emobi.convoy.pojo.rssfeeds.RssFeeds;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.emobi.convoy.utility.StringUtils.LOG_TAG;

public class NewsFeedAPIActivity extends AppCompatActivity implements WebServicesCallBack{
    String[] menu= new String[]{"Abc News Aus","Ars Technica","Associated Press","BBC News","BBC Sport","Bloomberg","Business Insider","Business Insider UK","Buzzfeed","CNBC","CNN","Daily Mail","Engadget","Entertainment Weekly","ESPN","ESPN Cric Info","Financial Times","Focus","Football Italia","Fortune","Four Four Two","Fox Sports","Gruenderszene","Hacker News","Independent","Mashable","Metro","Mirror","Mtv News","Mtv News UK","National Geographic","New Scientist","New York Magazine","Nfl News","Polygon","Recode","Reuters","Sky Sports News","Spiegel Online","T3n","Talksport","Techcrunch","Techradar","The Economist","The Guardian Aus","The Guardian UK","The Hindu","The Huffington Post","The Lad Bible","The New York Times","The Sport Bible","The Verge","The Wall Street Journal"};
    String[] menu_codes= new String[]{"abc-news-au","ars-technica","associated-press","bbc-news","bbc-sport","bloomberg","business-insider","business-insider-uk","buzzfeed","cnbc","cnn","daily-mail","engadget","entertainment-weekly","espn","espn-cric-info","financial-times","focus","football-italia","fortune","four-four-two","fox-sports","gruenderszene","hacker-news","independent","mashable","metro","mirror","mtv-news","mtv-news-uk","national-geographic","new-scientist","new-york-magazine","nfl-news","polygon","recode","reuters","sky-sports-news","spiegel-online","t3n","talksport","techcrunch","techradar","the-economist","the-guardian-au","the-guardian-uk","the-hindu","the-huffington-post","the-lad-bible","the-new-york-times","the-sport-bible","the-verge","the-wall-street-journal"};

    private final String TAG=getClass().getSimpleName();
    private final static String GET_RSS_FEEDS="get_rss_feeds";

    @BindView(R.id.ll_scroll)
    LinearLayout ll_scroll;
    @BindView(R.id.spinner_news_cat)
    Spinner spinner_news_cat;
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_api);
        ButterKnife.bind(this);

        CallRssFeedsAPI(menu_codes[0]);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, menu);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_news_cat.setAdapter(dataAdapter);

        spinner_news_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(position!=i){
                    CallRssFeedsAPI(menu_codes[i]);
                    position=i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MobileAds.initialize(this, "ca-app-pub-1163685788193118~7085127386");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4E448BFE118E3DBD390404E63D74029A")
                .build();
        mAdView.loadAd(adRequest);
    }
    public void CallRssFeedsAPI(String type){
        String url="https://newsapi.org/v1/articles?source="+type+"&apiKey=311c1e78e72c4bb9a64542528d871871";
        Log.d(TAG,"url:-"+url);
//        new GetWebServices(this, GET_RSS_FEEDS).execute(url);
        new CallRssService(this,url).execute();
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case GET_RSS_FEEDS:
                parseRssFeeds(response);
                break;
        }
    }

    public void parseRssFeeds(String response){
        Log.d(TAG,"response:-"+response);
        try{
            Gson gson=new Gson();
            RssFeeds rssFeeds=gson.fromJson(response,RssFeeds.class);
            inflateRssFeeds(rssFeeds.getArticlesList());
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,e.toString());
        }
    }
    public void inflateRssFeeds(List<Articles> list_articles){
        ll_scroll.removeAllViews();
        for (int i = 0; i < list_articles.size(); i++) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.inflate_rss_feeds, null);
            LinearLayout ll_rss= (LinearLayout) view.findViewById(R.id.ll_rss);
            ImageView iv_rss_image= (ImageView) view.findViewById(R.id.iv_rss_image);
            TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
            TextView tv_description= (TextView) view.findViewById(R.id.tv_description);


            final Articles articles=list_articles.get(i);
            tv_title.setText(articles.getTitle());
            tv_description.setText(articles.getDescription());
            Glide.with(getApplicationContext()).load(articles.getUrlToImage()).into(iv_rss_image);

            ll_rss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(NewsFeedAPIActivity.this,SingleRssFeedActivity.class);
                    intent.putExtra("url",articles.getUrl());
                    startActivity(intent);
                }
            });
            ll_scroll.addView(view);
        }
    }

    public class CallRssService extends AsyncTask<Void,Void,Void>{
        String url_string;
        String response;
        ProgressDialog progressDialog;
        Activity activity;
        CallRssService(Activity activity, String url){
            this.activity=activity;
            this.url_string=url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url=createUrl(url_string);
            String jsonString=null;
            try {
                jsonString = makeHttpRequest(url);
                response=jsonString;
                Log.d(TAG,"result:-"+jsonString);
            }catch (Exception e){
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            return null;
        }
        private URL createUrl(String requestedURL) {
            URL url=null;
            try {
                url=new URL(requestedURL);
            } catch (MalformedURLException e) {
//            e.printStackTrace();
                Log.e(LOG_TAG, "Error with creating URL ", e);
            }
            return url;
        }
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse="";
            if (url==null){
                return null;
            }
            HttpURLConnection conn=null;
            InputStream inputStream=null;

            try {
                conn= (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode()==200){
                    inputStream=conn.getInputStream();
                    jsonResponse=readFromStream(inputStream);
                }else {
                    Log.e(LOG_TAG, "Error response code: " + conn.getResponseCode());
                }

            } catch (IOException e) {
//            e.printStackTrace();
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            }finally {
                if (conn!= null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close();
                }
            }

            return jsonResponse;
        }
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output=new StringBuilder();
            if (inputStream!=null){
                InputStreamReader inputReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader=new BufferedReader(inputReader);
                String line=bufferedReader.readLine();
                while(line!=null){
                    output.append(line);
                    line=bufferedReader.readLine();
                }
            }

            return output.toString();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG,"response call:-"+response);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            parseRssFeeds(response);
        }
    }
}
