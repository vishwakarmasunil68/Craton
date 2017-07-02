package com.emobi.convoy.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.activity.HomeActivity;
import com.emobi.convoy.adapter.FriendListAdapter;
import com.emobi.convoy.pojo.friendlist.FriendListPOJO;
import com.emobi.convoy.pojo.friendlist.FriendListResponsePOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.utility.ToastClass;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunil on 08-05-2017.
 */

public class FriendListFragment extends Fragment implements WebServicesCallBack {

    private final String TAG = getClass().getSimpleName();
    private final String FRIEND_LIST_API = "friend_list_api";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_friend_list)
    RecyclerView rv_friend_list;
    boolean is_testimonial=false;
    @BindView(R.id.tv_title)
    TextView tv_title;
    public FriendListFragment(boolean is_testimonial){
        this.is_testimonial=is_testimonial;
    }

    public FriendListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_friend_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle("Friend List");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed()
                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.onBackPressed();
            }
        });
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(backArrow);
        tv_title.setText("Contacts");
        callFriendListAPI();
    }

    public void callFriendListAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getActivity().getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, getActivity(), this, FRIEND_LIST_API).execute(WebServicesUrls.FRIEND_LIST_URL);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case FRIEND_LIST_API:
                parseFriendListAPI(response);
                break;
        }
    }

    public void parseFriendListAPI(String response) {
        Log.d(TAG, "friend list response:-" + response);
        Gson gson = new Gson();
        FriendListPOJO friendListPOJO = gson.fromJson(response, FriendListPOJO.class);
        if (friendListPOJO != null) {
            try {
                if (friendListPOJO.getList_friends() != null && friendListPOJO.getList_friends().size() > 0) {
                    List<FriendListResponsePOJO> ist_friends = friendListPOJO.getList_friends();

                    List<FriendListResponsePOJO> list_friends = new ArrayList<>();
                    for (FriendListResponsePOJO friendListResponsePOJO : friendListPOJO.getList_friends()) {
                        if (friendListResponsePOJO.getFriend_status().equals("enable")) {
                            list_friends.add(friendListResponsePOJO);
                        }
                    }

                    FriendListAdapter adapter = new FriendListAdapter(getActivity(), list_friends,is_testimonial);
                    GridLayoutManager horizontalLayoutManagaer
                            = new GridLayoutManager(getActivity(), 2);
                    rv_friend_list.setLayoutManager(horizontalLayoutManagaer);
                    rv_friend_list.setAdapter(adapter);
                }
            } catch (Exception e) {
                Log.d(TAG, "error:-" + e.toString());
                ToastClass.showShortToast(getActivity().getApplicationContext(),"No Friend Found");
            }
        }
    }
}
