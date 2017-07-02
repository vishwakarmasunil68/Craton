package com.emobi.convoy.testing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emobi.convoy.R;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealTimeTesting extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.btn_update_root)
    Button btn_update_root;
    @BindView(R.id.btn_get_all_roots)
    Button btn_get_all_roots;
    @BindView(R.id.btn_update_location)
    Button btn_update_location;

    private final String TAG=getClass().getSimpleName();
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_testing);

        ButterKnife.bind(this);

        btn_get_all_roots.setOnClickListener(this);
        btn_update_root.setOnClickListener(this);
        btn_update_location.setOnClickListener(this);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Set<String> set=new HashSet<String>();
//                Iterator i=dataSnapshot.getChildren().iterator();
//                while(i.hasNext()){
//                    set.add(((DataSnapshot)i.next()).getKey());
//                }
//                List<String> list_roots=new ArrayList<String>();
//                list_roots.addAll(set);
//
//                Log.d(TAG,"all roots:-"+list_roots.toString());
                parseChildINFO(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"error:-"+databaseError.toString());
            }
        });

//        root.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                parseChildINFO(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                parseChildINFO(dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    public void parseLatLongOfUser(DataSnapshot dataSnapshot){
        Iterator i=dataSnapshot.getChildren().iterator();
        while(i.hasNext()) {
            DataSnapshot snapshot = (DataSnapshot) i.next();
            Log.d(TAG, snapshot.getKey()+" : "+ snapshot.getValue());
            Log.d(TAG, snapshot.getKey()+" : "+ snapshot.getValue());
        }
    }

    public void parseChildINFO(DataSnapshot dataSnapshot){
        Iterator i=dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            DataSnapshot snapshot=(DataSnapshot)i.next();
//            Log.d(TAG,"Log_ID:-"+snapshot.getKey());
            if(snapshot.getKey().toString().equals("44")) {
                Iterator latlongiterator = snapshot.getChildren().iterator();
                while (latlongiterator.hasNext()) {
                    DataSnapshot snap_latlong = (DataSnapshot) latlongiterator.next();
                    Log.d(TAG, snap_latlong.getKey() + " : " + snap_latlong.getValue());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btn_get_all_roots:
                getALLRoots();
                break;
            case R.id.btn_update_root:
                updateRoot();
                break;
            case R.id.btn_update_location:
                updateLocation();
                break;
        }
    }
    int lat=0;
    int lon=2;
    public void updateLocation(){
        DatabaseReference reference=root.child(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));
        Map<String,Object> map=new HashMap<>();
        map.put("lattitude",(++lat)+"");
        map.put("longitude",(++lon)+"");
        reference.updateChildren(map);
    }

    public void getALLRoots(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chat");
        mDatabase.child("1444").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ChatRoom chatRoom= dataSnapshot.getValue(ChatRoom.class);

                Log.d(TAG, "chat class:-"+chatRoom.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public void updateRoot(){
//        Map<String ,Object> map=new HashMap<>();
//        map.put(Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,""),"");
//        root.updateChildren(map);

        String chat_room_name="1444";
        String sender_user_id="14";
        String sender_user_name="akash";
        String reciever_user_id="44";
        String reciever_user_name="sunil";
        String chat_msg="hey this is testing msg change";
        String chat_file_url="null";

//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chat");
//        ChatRoom chatRoom=new ChatRoom(sender_user_id,sender_user_name,reciever_user_id,
//                reciever_user_name,chat_msg,chat_file_url);
////        String fire_chat_name=mDatabase.push().getKey();
//        mDatabase.child(chat_room_name).setValue(chatRoom);
    }

}
