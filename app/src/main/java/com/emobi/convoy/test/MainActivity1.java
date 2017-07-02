package com.emobi.convoy.test;

import android.support.v7.app.AppCompatActivity;


public class MainActivity1 extends AppCompatActivity {}
//    private final String TAG=getClass().getSimpleName();
//    IabHelper mHelper;
//    static final String ITEM_SKU="com.emobi.convoy.product10";
//    Button btn_click,btn_buy;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
//        btn_click= (Button) findViewById(R.id.btn_click);
//        btn_buy= (Button) findViewById(R.id.btn_buy);
//
//        String public_key="IIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs/nOB+c8176KQMDcx1MSCx7R7nTA+xYwf/vnshMsqdHzpGqU2NM/f0kELxCNm1NnxYf5o1ELpq+tX39UxYgwaVPtUt+twRk0NVnJw/nYJM8B4kE3EjhYWXeoC00dTQrR3wAPUlPrnGHa4gf5LvdwsVe8OrLSGmb9VOIB3rGYrdsA1QLsTbXq6o+D/uzuBeUzuRElDR5QDptr9sE/A7YpLwOQ2QKiMkMdt4OBT5jlgjt02yIVXQrUsllc2Chw9UP/tSZ/GTfExAXQKQu8Hx4OitNG2KR195jQxabeHuqjvGaUWnfqW79HqiaOWdanRuvEFTtJ5vRMwQjXXL9eg7ntbwIDAQAB";
//
//        mHelper=new IabHelper(this,public_key);
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            @Override
//            public void onIabSetupFinished(IabResult result) {
//                if(result.isSuccess()){
//                    Log.d(TAG,"in app purchase success");
//                }else{
//                    Log.d(TAG,"in app purchase failure");
//                }
//            }
//        });
//
//        btn_click.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mHelper.launchPurchaseFlow(MainActivity1.this,ITEM_SKU,10001,null,"mypurchasetoken");
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if(!mHelper.handleActivityResult(requestCode,resultCode,data)){
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener=new IabHelper.OnIabPurchaseFinishedListener() {
//        @Override
//        public void onIabPurchaseFinished(IabResult result, Purchase info) {
//            if(result.isSuccess()){
//                Log.d(TAG,"purchase success");
//                consumeItem();
//            }else{
//                Log.d(TAG,"purchase failure");
//                return;
//            }
//        }
//    };
//
//
//    public void consumeItem(){
//        mHelper.queryInventoryAsync(queryInventoryAsync);
//    }
//
//    IabHelper.QueryInventoryFinishedListener queryInventoryAsync=new IabHelper.QueryInventoryFinishedListener() {
//        @Override
//        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
//                if(result.isSuccess()){
//                    Log.d(TAG,"query success");
//                }else{
//                    Log.d(TAG,"query fail");
//                }
//        }
//    };
//
//    IabHelper.OnConsumeFinishedListener mOnConsumeFinishedListener=new IabHelper.OnConsumeFinishedListener() {
//        @Override
//        public void onConsumeFinished(Purchase purchase, IabResult result) {
//            if(result.isSuccess()){
//                Log.d(TAG,"item consumed");
//            }else{
//                Log.d(TAG,"item not consumed");
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(mHelper!=null){
//            mHelper.dispose();
//            mHelper=null;
//        }
//    }
//}
