package com.emobi.convoy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emobi.convoy.R;
import com.emobi.convoy.contactspicker.Contact;
import com.emobi.convoy.contactspicker.ContactsPickerActivity;
import com.emobi.convoy.utility.ToastClass;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvitationActivity extends AppCompatActivity {
    TextView contactsDisplay;
    Button pickContacts;
    final int CONTACT_PICK_REQUEST = 1000;
    String contact_numbers = "";
    @BindView(R.id.btn_send_invitation)
    Button btn_send_invitation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Send Invitation");
        tv_title.setText("Send Invitation");
        contactsDisplay = (TextView) findViewById(R.id.txt_selected_contacts);
        pickContacts = (Button) findViewById(R.id.btn_pick_contacts);

        pickContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentContactPick = new Intent(InvitationActivity.this, ContactsPickerActivity.class);
                startActivityForResult(intentContactPick, CONTACT_PICK_REQUEST);
            }
        });
        btn_send_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact_numbers.equals("")) {
                    ToastClass.showShortToast(getApplicationContext(), "Please Select Contacts");
                } else {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + contact_numbers));
                    smsIntent.putExtra("sms_body", "Check out craton for your smartphone. Download it today from play store.");
                    startActivity(smsIntent);
                }
            }
        });
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_back);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICK_REQUEST && resultCode == RESULT_OK) {

            ArrayList<Contact> selectedContacts = data.getParcelableArrayListExtra("SelectedContacts");

            String display = "";
            contact_numbers = "";
            for (int i = 0; i < selectedContacts.size(); i++) {
                if ((i + 1) == selectedContacts.size()) {
                    contact_numbers = contact_numbers + selectedContacts.get(i).phone;
                } else {
                    contact_numbers = contact_numbers + selectedContacts.get(i).phone + ",";
                }
                display += (i + 1) + ". " + selectedContacts.get(i).toString() + "\n";

            }
            Log.d(TAG, "contact numbers:-" + contact_numbers);
            contactsDisplay.setText("Selected Contacts : \n\n" + display);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final String TAG = getClass().getSimpleName();
}
