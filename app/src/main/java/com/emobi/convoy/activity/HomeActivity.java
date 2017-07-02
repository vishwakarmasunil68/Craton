package com.emobi.convoy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.fragment.FriendListFragment;
import com.emobi.convoy.fragment.MapViewFragment;
import com.emobi.convoy.fragment.SingleTextPostFragment;
import com.emobi.convoy.pojo.Addr;
import com.emobi.convoy.pojo.addvisitor.AddVisitorPOJO;
import com.emobi.convoy.pojo.addvisitor.AddVisitorResultPOJO;
import com.emobi.convoy.pojo.friendlist.FriendListPOJO;
import com.emobi.convoy.pojo.friendlist.FriendListResponsePOJO;
import com.emobi.convoy.services.BackgroundLocationService;
import com.emobi.convoy.sinch.BaseActivity;
import com.emobi.convoy.sinch.PlaceCallActivity;
import com.emobi.convoy.sinch.SinchService;
import com.emobi.convoy.utility.CheckConnectivity;
import com.emobi.convoy.utility.GPSTracker;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.emobi.convoy.R.id.map;

public class HomeActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, WebServicesCallBack, SinchService.StartFailedListener {
    public final static String FRIEND_LIST_API = "friendlistapi";
    public final static String UPDATE_ADDRESS_API = "update_address_api";
    public final static int FRIEND_RETURN_ACTIVITY = 101;
    private static final String ADD_VISITOR_API = "ADD_VISITOR_API";
    private static final String LOG_OUT_CLEAR_API = "log_out_clear_api";
    private static final String CALL_POST_ANIMATE_API = "call_post_animate_api";
    private static final String CALL_UPDATE_USER = "call_update_user_api";
    private static final String CALL_VALIDITY_API = "call_validity_api";
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 10;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.ic_left_ham)
    ImageView ic_left_ham;
    @BindView(R.id.nvView_right)
    NavigationView nvView_right;
    @BindView(R.id.ic_right_ham)
    ImageView ic_right_ham;
    @BindView(R.id.frame_main_container)
    FrameLayout frame_main_container;
    LinearLayout ll_friend_list;
    private LocationManager mLocationManager = null;

    RecyclerView rv_friend_list;
    ActionBarDrawerToggle drawerToggle_right;
    GoogleMap mMap;
    GPSTracker gps;
    LinearLayout ll_news_feeds;
    List<FriendListResponsePOJO> list_friends;
    String friend_list_response = "";
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        if (!isGooglePlayServicesAvailable()) {
//            finish();
//        }

        setUpLeftNavigationDrawer();
        setUpRightNavigationDrawer();

        ic_left_ham.setOnClickListener(this);
        ic_right_ham.setOnClickListener(this);
        Log.d(TAG, "log_id:-" + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, ""));

        if (CheckConnectivity.isConnected(getApplicationContext())) {
            setUpGoogleMap();
            callPostAnimateApi();
            callUserUpdateApi();
            AdInitialize();
            startAllServices();
            checkVersionUpdate();


            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        } else {
            Log.d(TAG, "no internet connection");
            showNoconnectionDialog();
        }
    }
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "homeactivity onLocationChanged: " + location);
            mLastLocation.set(location);
            if(mMap!=null&&user_marker!=null){
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                user_marker.setPosition(latLng);
            }
//            savetoServer(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    public void showNoconnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No Internet Connection!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finishAffinity();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void startAllServices() {
        Log.d(TAG, "background service:-" + isMyServiceRunning(BackgroundLocationService.class));
        if (!isMyServiceRunning(BackgroundLocationService.class)) {
            startService(new Intent(HomeActivity.this, BackgroundLocationService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    boolean isfirst = true;

    public void loadAd() {
        if (!isfirst) {
//           showInterstitial();
        } else {
            isfirst = false;
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void AdInitialize() {
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId("ca-app-pub-1163685788193118/8164907784");
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("4E448BFE118E3DBD390404E63D74029A")
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
//                showInterstitial();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onresume");
        if(CheckConnectivity.isConnected(getApplicationContext())){
            callFriendListAPI();
        }
        if (ic_left_profile != null) {
            Log.d(TAG, "log pic:-" + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""));
            Glide.with(getApplicationContext())
                    .load(WebServicesUrls.IMAGE_BASE_URL + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, ""))
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .dontAnimate()
                    .into(ic_left_profile);
        }
        loadAd();
        getApplicationContext().registerReceiver(mFriendListReceiver, new IntentFilter(StringUtils.FRIEND_REQUEST_ACCEPT));
    }

    private BroadcastReceiver mFriendListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("firebase", "calling friend request");

            callFriendListAPI();
        }
    };

    public void callFriendListAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, FRIEND_LIST_API, true).execute(WebServicesUrls.FRIEND_LIST_URL);
    }

    public void callPostAnimateApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_POST_ANIMATE_API, true).execute(WebServicesUrls.GET_POST_ANIMATE);
    }

    public void callUserUpdateApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("log_device_token", Pref.GetDeviceToken(getApplicationContext(), "")));
        nameValuePairs.add(new BasicNameValuePair("os_type", "android"));
        new WebServiceBase(nameValuePairs, this, CALL_UPDATE_USER, true).execute(WebServicesUrls.UPDATE_USER_URL);
    }


    public void setUpGoogleMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    public void setUpLeftNavigationDrawer() {
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer.setItemIconTintList(null);
        setFirstItemNavigationView();
        drawerToggle.setDrawerIndicatorEnabled(false);
        setUpLeftHeaderLayout();
        setUpRightHeaderLayout();
    }

    private final String TAG = getClass().getSimpleName();
    CircleImageView ic_left_profile;

    public void setUpLeftHeaderLayout() {
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.inflate_home_left_header);

        ic_left_profile = (CircleImageView) headerLayout.findViewById(R.id.ic_left_profile);
        TextView tv_profile_name = (TextView) headerLayout.findViewById(R.id.tv_profile_name);
        LinearLayout ll_about = (LinearLayout) headerLayout.findViewById(R.id.ll_about);

        String user_name = Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, "profile_name");
        //Log.d(TAG, "user_name:-" + user_name);
        tv_profile_name.setText(user_name);
        Glide.with(this).load(WebServicesUrls.IMAGE_BASE_URL + Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_PICS, "")).error(R.drawable.ic_profile).into(ic_left_profile);
        ic_left_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setUpRightHeaderLayout() {
        View headerLayout = nvView_right.inflateHeaderView(R.layout.inflate_home_right_header);

        ImageView iv_search = (ImageView) headerLayout.findViewById(R.id.iv_search);
        EditText et_search = (EditText) headerLayout.findViewById(R.id.et_search);
        LinearLayout ll_find_friends = (LinearLayout) headerLayout.findViewById(R.id.ll_find_friends);
        rv_friend_list = (RecyclerView) headerLayout.findViewById(R.id.rv_friend_list);
        ll_news_feeds = (LinearLayout) headerLayout.findViewById(R.id.ll_news_feeds);
        LinearLayout ll_invites = (LinearLayout) headerLayout.findViewById(R.id.ll_invites);
        ll_find_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FindFriendsActivity.class);
                intent.putExtra("friendspojo", friend_list_response);
                startActivity(intent);
            }
        });
        LinearLayout ll_write_testimonial = (LinearLayout) headerLayout.findViewById(R.id.ll_write_testimonial);

        ll_news_feeds.setOnClickListener(this);


        ll_friend_list = (LinearLayout) headerLayout.findViewById(R.id.ll_friend_list);
        ll_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFriendListFragment(false);
            }
        });

        ll_write_testimonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFriendListFragment(true);
            }
        });
        ll_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, InvitationActivity.class));
            }
        });
    }

    public void setUpRightNavigationDrawer() {
        setupRightDrawerContent(nvView_right);
        drawerToggle_right = setupDrawerToggleRight();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle_right);

        nvView_right.setItemIconTintList(null);
        setFirstRightItemNavigationView();
        drawerToggle_right.setDrawerIndicatorEnabled(false);
    }

    private void setFirstItemNavigationView() {
//        nvDrawer.setCheckedItem(R.id.nav_whovisited);
//        nvDrawer.getMenu().performIdentifierAction(R.id.nav_whovisited, 0);
    }

    private void setFirstRightItemNavigationView() {
//        nvView_right.setCheckedItem(R.id.nav_whovisited);
//        nvView_right.getMenu().performIdentifierAction(R.id.nav_whovisited, 0);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private ActionBarDrawerToggle setupDrawerToggleRight() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void setupRightDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectRightDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_whovisited:
                Intent friendintent = new Intent(HomeActivity.this, WhoVisitedYouActivity.class);
                friendintent.putExtra("friendspojo", friend_list_response);
                startActivity(friendintent);
//                startActivity(new Intent(HomeActivity.this, AdMobActivity.class));
                break;
            case R.id.nav_rss:
                startActivity(new Intent(HomeActivity.this, NewsFeedAPIActivity.class));
                break;
            case R.id.nav_interest:
                startActivity(new Intent(HomeActivity.this, InterestActivity.class));
                break;
            case R.id.nav_diaries:
                startActivity(new Intent(HomeActivity.this, DiaryActivity.class));
                break;
            case R.id.nav_in_app:
                callValidityAPI();
//                startActivity(new Intent(HomeActivity.this, MainActivity1.class));
                break;
            case R.id.nav_testimonials:
                startActivity(new Intent(HomeActivity.this, TestimonialActivity.class));
                break;
            case R.id.nav_friend_request:
                Intent intent = new Intent(HomeActivity.this, FriendRequestActivity.class);
                startActivityForResult(intent, FRIEND_RETURN_ACTIVITY);
                break;
            case R.id.nav_logout:
                callLogoutAPI();

                break;
            case R.id.nav_bookings:
                startActivity(new Intent(HomeActivity.this, BookingActivity.class));
                break;

            default:
        }

        mDrawer.closeDrawers();
    }

    public void callLogoutAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, LOG_OUT_CLEAR_API).execute(WebServicesUrls.LOG_OUT_CLEAR);
    }

    public void selectRightDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_whovisited:
                break;
            case R.id.nav_rss:
                break;
            case R.id.nav_interest:
                break;
            case R.id.nav_diaries:
                break;
            case R.id.nav_in_app:
                break;
            case R.id.nav_testimonials:
                break;
            case R.id.nav_friend_request:
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, SignupActivity.class));
                finishAffinity();
                break;
            case R.id.nav_bookings:
                break;

            default:
        }

        mDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
        drawerToggle_right.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
        drawerToggle_right.onConfigurationChanged(newConfig);
    }

    public void SetTitleString(String title) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_left_ham:
                mDrawer.openDrawer(nvDrawer);
                break;
            case R.id.ic_right_ham:
                mDrawer.openDrawer(nvView_right);
                break;
            case R.id.ll_news_feeds:
                startNewFeedFragment();
//                startActivity(new Intent(this, NewsFeedActivity.class));
                break;
        }
    }

    List<Fragment> activeCenterFragments = new ArrayList<>();

    public void startNewFeedFragment() {
//        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.frame_main_container, newsFeedFragment, "newsFeedFragment");
//        transaction.addToBackStack(null);
//        transaction.commit();
//        activeCenterFragments.add(newsFeedFragment);
        startActivity(new Intent(HomeActivity.this, AllNewsFeedActivity.class));
    }

    public void showMapProfileFragment(AddVisitorResultPOJO addVisitorResultPOJO) {
        MapViewFragment mapViewFragment = new MapViewFragment(addVisitorResultPOJO);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_container, mapViewFragment, "newsFeedFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        activeCenterFragments.add(mapViewFragment);
    }

    public void showTextFeedFragment(String post_id, String image_status) {
        SingleTextPostFragment singleTextPostFragment = new SingleTextPostFragment(post_id, image_status);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_container, singleTextPostFragment, "singleTextPostFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        activeCenterFragments.add(singleTextPostFragment);
    }

    public void showFriendListFragment(boolean is_testimonial) {
        FriendListFragment friendListFragment = new FriendListFragment(is_testimonial);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_container, friendListFragment, "friendListFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        activeCenterFragments.add(friendListFragment);
    }

    public void showSendTestimonialFragment(String fri_id) {
        SendTestimonialFragment sendTestimonialFragment = new SendTestimonialFragment(fri_id);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_container, sendTestimonialFragment, "sendTestimonialFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        activeCenterFragments.add(sendTestimonialFragment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        getLocation();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                int position = (int)(marker.getTag());
//
                final int dX = getResources().getDimensionPixelSize(R.dimen.map_dx);
                // Calculate required vertical shift for current screen density
                final int dY = getResources().getDimensionPixelSize(R.dimen.map_dy);
                final Projection projection = mMap.getProjection();
                final Point markerPoint = projection.toScreenLocation(
                        marker.getPosition()
                );
                // Shift the point we will use to center the map
                markerPoint.offset(dX, dY);
                final LatLng newLatLng = projection.fromScreenLocation(markerPoint);
                // Buttery smooth camera swoop :)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
                // Show the info window (as the overloaded method would)
                marker.showInfoWindow();

                //Using position get Value from arraylist
                try {
                    UserMarker userMarker = (UserMarker) marker.getTag();
                    //Log.d(TAG, "usermarker:-" + userMarker.toString());
                    callVisitorAPI(userMarker.getLog_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    public void callVisitorAPI(String search_id) {
        if (search_id.length() > 0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
            nameValuePairs.add(new BasicNameValuePair("search_id", search_id));
            SimpleDateFormat sdf_date = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm a");
            Date date = new Date();
            String stringdate = sdf_date.format(date);
            String stringtime = sdf_time.format(date);
            //Log.d(TAG, "date:-" + stringdate);
            //Log.d(TAG, "time:-" + stringtime);
            nameValuePairs.add(new BasicNameValuePair("visit_time", stringtime));
            nameValuePairs.add(new BasicNameValuePair("visit_date", stringdate));
            new WebServiceBase(nameValuePairs, this, ADD_VISITOR_API).execute(WebServicesUrls.ADD_VISITOR);
        }
    }
    Marker user_marker;
    public void getLocation() {
        if (mMap != null) {
            gps = new GPSTracker(HomeActivity.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                LatLng latlng = new LatLng(latitude, longitude);
                String address = getAddress(latitude, longitude);
                //Log.d(TAG, "location address:-" + address);
                updateLocation(address);
                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(address));
                user_marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                user_marker.setTag("userown");
                user_marker.showInfoWindow();

                float zoomLevel = 16.0f; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel));
                showFriendsOnMAP(list_friends);
                Addr addr = new Addr(latitude, longitude);
                Log.d(TAG, "location:-" + latitude + "\n" + longitude);
                root.child(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")).setValue(addr);
            } else {
                gps.showSettingsAlert();
            }
        }
    }


    public void updateLocation(String location_address) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        nameValuePairs.add(new BasicNameValuePair("log_geo_location", location_address));
        new WebServiceBase(nameValuePairs, this, UPDATE_ADDRESS_API, true).execute(WebServicesUrls.UPDATE_LOCATION_ADDRESS);

    }

    public String getAddress(double latitude, double longitude) {
        String address = "";
//                    LocationAddress.getAddressFromLocation(latitude,longitude,LocationService.this,new GeocoderHandler());
        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Log.d("sunil", strReturnedAddress.toString());
                address = strReturnedAddress.toString();
                return address;
            } else {
                Log.d("sunil", "No Address returned!");
                return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("sunil", "Canont get Address!");
            return "Current Location";
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case FRIEND_LIST_API:
                parseFriendListResponse(response);
                break;
            case UPDATE_ADDRESS_API:
                parseLocationAddressAPI(response);
                break;
            case ADD_VISITOR_API:
                parseAddVisitorResponse(response);
                break;
            case LOG_OUT_CLEAR_API:
                parseLogoutResponse();
                break;
            case CALL_POST_ANIMATE_API:
                parserpostAnimateAPI(response);
                break;
            case CALL_UPDATE_USER:
                parsecallupdateresponse(response);
                break;
            case CALL_VALIDITY_API:
                parseCallValidityAPIResponse(response);
                break;

        }
    }

    public void parseCallValidityAPIResponse(String response) {
        Log.d(TAG, "call response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {
                JSONArray jsonArray = jsonObject.optJSONArray("result");
                JSONObject resObject = jsonArray.optJSONObject(0);
                String log_validity = resObject.optString("log_validity");
                Pref.SetStringPref(getApplicationContext(), StringUtils.LOG_VALIDITY, log_validity);
                String[] validity = log_validity.split(":");
                int hour = Integer.parseInt(validity[0]);
                int min = Integer.parseInt(validity[1]);
                int sec = Integer.parseInt(validity[2]);

                long totalseconds = (hour * 60 * 60) + (min * 60) + sec;
                if (totalseconds > 0) {
                    sinchCallBack();
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "Your balance is 0 please recharge your account");
                    startActivity(new Intent(HomeActivity.this, com.emobi.convoy.test.InAppPurchaseActivity.class));
                }
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void parsecallupdateresponse(String response) {
        Log.d(TAG, "Login reposne:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.optString("success");
            if (success.equals("true")) {

            } else {
                String message = jsonObject.optString("message");
                if (message.equals("login again")) {
                    showLoginDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoginDialog() {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setContentView(R.layout.dialog_login_again);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.setTitle("Login again");
        dialog1.setCancelable(false);
        dialog1.show();
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Button btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseLogoutResponse();
            }
        });
    }

    public void parserpostAnimateAPI(String response) {
        WebView webView;
        webView = (WebView) findViewById(R.id.web);
        Log.d(TAG, "parse post animate response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optString("success").equals("true")) {
                JSONObject result = jsonObject.optJSONObject("Result");
                webView.loadData(result.optString("message"), "text/html", "utf-8");
                loadwebview(webView, "congratulations!! Welcome to Craton stay tuned for exciting events heading your way.");

            } else {
                loadwebview(webView, "congratulations!! Welcome to Craton stay tuned for exciting events heading your way.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loadwebview(webView, "congratulations!! Welcome to Craton stay tuned for exciting events heading your way.");
        }
    }

    public void loadwebview(WebView webView, String data) {
        webView.setVisibility(View.VISIBLE);
        String text = "<html>\n" +
                "<head>\n" +
                "<title>HTML marquee Tag</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<marquee style=\"color:#000000;border:none;padding:0;margin:0;\"><b>" + data + "</b></marquee>\n" +
                "</body>\n" +
                "</html>";
        Log.d(TAG, "marquee:-" + text);
        webView.loadData(text, "text/html", "utf-8");
    }

    public void parseLogoutResponse() {
        Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_LOGIN, false);
        Pref.clearSharedPreference(getApplicationContext());
        stopService(new Intent(this, BackgroundLocationService.class));
        startActivity(new Intent(this, SignupActivity.class));
        finishAffinity();
    }


    public void parseAddVisitorResponse(String response) {
        try {
            Gson gson = new Gson();
            AddVisitorPOJO addVisitorPOJO = gson.fromJson(response, AddVisitorPOJO.class);
            if (addVisitorPOJO.getSuccess().equals("true")) {
                showMapProfileFragment(addVisitorPOJO.getAddVisitorResultPOJO());
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void parseLocationAddressAPI(String response) {
        //Log.d(TAG, "Location Address api resposne:-" + response);
    }

    public void parseFriendListResponse(String response) {
        //Log.d(TAG, "friend_list:-" + response.toString());
//        try {
//            mMap.clear();
//            list_of_markers.clear();
//            getLocation();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            friend_list_response = response;
            Gson gson = new Gson();
            FriendListPOJO friendListPOJO = gson.fromJson(response, FriendListPOJO.class);
            if (friendListPOJO != null) {
                try {
                    if (friendListPOJO.getList_friends() != null && friendListPOJO.getList_friends().size() > 0) {
                        list_friends = friendListPOJO.getList_friends();

                        List<FriendListResponsePOJO> list_friends = new ArrayList<>();
                        for (FriendListResponsePOJO friendListResponsePOJO : friendListPOJO.getList_friends()) {
                            if (friendListResponsePOJO.getFriend_status().equals("enable")) {
                                list_friends.add(friendListResponsePOJO);
                            }
                        }

                        showFriendsOnMAP(list_friends);

//                    FriendListAdapter adapter = new FriendListAdapter(getApplicationContext(), list_friends);
//                    LinearLayoutManager horizontalLayoutManagaer
//                            = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//                    rv_friend_list.setLayoutManager(horizontalLayoutManagaer);
//                    rv_friend_list.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    //Log.d(TAG, "error:-" + e.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    public void showFriendsOnMAP(final List<FriendListResponsePOJO> list_friends) {
        if (list_friends != null) {
            if (mMap != null) {
                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG,"datasnaphot:-"+dataSnapshot.toString());
                        parseChildINFO(dataSnapshot, list_friends, mMap);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.d(TAG, "error:-" + databaseError.toString());
                    }
                });
            } else {
                //Log.d(TAG, "map is initialized");
            }
        }
    }

    public void parseChildINFO(DataSnapshot dataSnapshot, List<FriendListResponsePOJO> list_friends, GoogleMap mMap) {
        List<String> list_friend_name = new ArrayList<>();
        List<String> list_friend_id = new ArrayList<>();

        for (FriendListResponsePOJO pojo : list_friends) {
            list_friend_name.add(pojo.getLog_name());
            list_friend_id.add(pojo.getLog_id());
        }

        Iterator i = dataSnapshot.getChildren().iterator();
        List<Map<String, UserMarker>> list_lat_lng = new ArrayList<>();
        while (i.hasNext()) {
            DataSnapshot snapshot = (DataSnapshot) i.next();
//            //Log.d(TAG,"Log_ID:-"+snapshot.getKey());
            if (list_friend_id.contains(snapshot.getKey().toString())) {
                int index = list_friend_id.indexOf(snapshot.getKey().toString());
                Iterator latlongiterator = snapshot.getChildren().iterator();
                double latitude = 0.0, longitude = 0.0;
                while (latlongiterator.hasNext()) {
                    DataSnapshot snap_latlong = (DataSnapshot) latlongiterator.next();
                    if (snap_latlong.getKey().toString().equals("lattitude")) {
                        try {
                            latitude = Double.parseDouble(snap_latlong.getValue().toString());
                        } catch (Exception e) {
                            //Log.d(TAG, "number parsing error:-" + e.toString());
                        }
                    }
                    if (snap_latlong.getKey().toString().equals("longitude")) {
                        try {
                            longitude = Double.parseDouble(snap_latlong.getValue().toString());
                        } catch (Exception e) {
                            //Log.d(TAG, "number parsing error:-" + e.toString());
                        }
                    }
                    //Log.d(TAG, snap_latlong.getKey() + " : " + snap_latlong.getValue());
                }
                LatLng latLng = new LatLng(latitude, longitude);
                UserMarker userMarker = new UserMarker();
                userMarker.setLog_id(snapshot.getKey().toString());
                userMarker.setLog_name(list_friend_name.get(index));
                userMarker.setLatLng(latLng);
                Map<String, UserMarker> map = new HashMap<>();
                map.put(list_friend_name.get(index), userMarker);
                list_lat_lng.add(map);

            }
        }

//        if(mMap!=null&&list_lat_lng.size()>0){
//            try{
//                mMap.clear();
//                list_of_markers.clear();
//                getLocation();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
        showFriendOnMap(list_lat_lng);
    }

    List<Marker> list_of_markers = new ArrayList<>();

    public void showFriendOnMap(List<Map<String, UserMarker>> list_lat_lng) {
        //Log.d(TAG, "friends latlongs:-" + list_lat_lng.toString());
        if (list_lat_lng.size() > 0) {
            for (int i = 0; i < list_lat_lng.size(); i++) {
                Map<String, UserMarker> map = list_lat_lng.get(i);
                for (String key : map.keySet()) {

                    try {
                        Marker marker = list_of_markers.get(i);
                        marker.setPosition(map.get(key).getLatLng());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(map.get(key).getLatLng())
                                .title(key).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        marker.setTag(map.get(key));
                        list_of_markers.add(marker);
                    }

//                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }

    class UserMarker {
        String log_id;
        String log_name;
        LatLng latLng;

        public String getLog_id() {
            return log_id;
        }

        public void setLog_id(String log_id) {
            this.log_id = log_id;
        }

        public String getLog_name() {
            return log_name;
        }

        public void setLog_name(String log_name) {
            this.log_name = log_name;
        }

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public String toString() {
            return "UserMarker{" +
                    "log_id='" + log_id + '\'' +
                    ", log_name='" + log_name + '\'' +
                    ", latLng=" + latLng +
                    '}';
        }
    }


    public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

        private List<FriendListResponsePOJO> horizontalList;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView iv_profile;
            public TextView tv_profile_name;
            public LinearLayout ll_friends;

            public MyViewHolder(View view) {
                super(view);
                iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
                tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
                ll_friends = (LinearLayout) view.findViewById(R.id.ll_friends);


            }
        }


        public FriendListAdapter(Context context, List<FriendListResponsePOJO> horizontalList) {
            this.horizontalList = horizontalList;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_friend_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv_profile_name.setText(horizontalList.get(position).getLog_name());
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(holder.iv_profile);
            String image_url = WebServicesUrls.IMAGE_BASE_URL + horizontalList.get(position).getLog_pics();
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(context)
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.iv_profile);

            holder.ll_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d(TAG, "pojo:-" + horizontalList.get(position));
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("user_id", horizontalList.get(position).getLog_id());
                    intent.putExtra("log_pic", horizontalList.get(position).getLog_pics());
                    intent.putExtra("log_name", horizontalList.get(position).getLog_name());
                    intent.putExtra("friend_token", horizontalList.get(position).getLog_device_token());

                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FRIEND_RETURN_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
//                callFriendListAPI();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    public void callValidityAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_VALIDITY_API).execute(WebServicesUrls.VALIDITY_API_URL);
    }

    public void sinchCallBack() {
        String userName = Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME, "");

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
            openPlaceCallActivity();
        }
    }

    private ProgressDialog mSpinner;

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
        getApplicationContext().unregisterReceiver(mFriendListReceiver);
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    boolean adShown = false;

    @Override
    public void onBackPressed() {
        if (!adShown) {
            showInterstitial();
            adShown = true;
        } else {
            super.onBackPressed();
        }
    }

    String currentVersion = "";

    public void checkVersionUpdate() {
        try {
            currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            new GetVersionCode().execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + HomeActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show dialog
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle("Update Available");
                    alertDialog.setMessage("Do you want to update it?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
//                                mInterstitialAd.loadAd(adRequest);
                        }
                    });
                    alertDialog.show();
                    //Log.d("update","yess");
                } else {
//                        mInterstitialAd.loadAd(adRequest);
                }
            }
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private String vicinity;
        LinearLayout ll_info_window;
        public MyInfoWindowAdapter(String vicinity) {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.custom_info_contents, null);
            this.vicinity = vicinity;
        }

        @Override
        public View getInfoContents(Marker marker) {

//            TextView tvTitle = ((TextView) myContentsView
//                    .findViewById(R.id.title));
//            tvTitle.setText(name);
            ll_info_window= (LinearLayout) myContentsView.findViewById(R.id.ll_info_window);
            TextView tvSnippet = ((TextView) myContentsView
                    .findViewById(R.id.snippet));
            tvSnippet.setText(vicinity);

            return myContentsView;
        }
        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            try {
                String markertag = (String) marker.getTag();
                if (markertag.equals("userown")){
                    ll_info_window.setVisibility(View.VISIBLE);
                }else{
                    ll_info_window.setVisibility(View.GONE);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                try {
                    ll_info_window = (LinearLayout) myContentsView.findViewById(R.id.ll_info_window);
                    ll_info_window.setVisibility(View.GONE);
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            return null;
        }

    }
}
