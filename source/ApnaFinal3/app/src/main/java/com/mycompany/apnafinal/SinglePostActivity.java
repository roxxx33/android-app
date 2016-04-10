package com.mycompany.apnafinal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SinglePostActivity extends AppCompatActivity {
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_CREATED_AT = "created_at";
    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTDATA = "postdata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        Intent in = getIntent();

        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_CREATED_AT);
        String email = in.getStringExtra(TAG_POSTDATA);
       // String mobile = in.getStringExtra(TAG_BODY);

        // Displaying all values on the screen
        TextView lblcreated = (TextView) findViewById(R.id.created_at);
        TextView lblpostdata = (TextView) findViewById(R.id.postdata);
       // TextView lblMobile = (TextView) findViewById(R.id.body_label);

        lblcreated.setText(name);
        lblpostdata.setText(email);
       // lblMobile.setText(mobile);
    }
}


