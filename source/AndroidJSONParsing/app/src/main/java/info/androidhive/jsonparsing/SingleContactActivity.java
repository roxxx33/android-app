package info.androidhive.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import info.androidhive.jsonparsing.R;

public class SingleContactActivity  extends Activity {
	
	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_BODY = "body";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_ID);
        String email = in.getStringExtra(TAG_TITLE);
        String mobile = in.getStringExtra(TAG_BODY);
        
        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.id_label);
        TextView lblEmail = (TextView) findViewById(R.id.title_label);
        TextView lblMobile = (TextView) findViewById(R.id.body_label);
        
        lblName.setText(name);
        lblEmail.setText(email);
        lblMobile.setText(mobile);
    }
}
