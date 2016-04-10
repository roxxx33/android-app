package com.mycompany.apnafinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private final static String REGISTER_API_ENDPOINT_URL = "https://quiet-lake-9467.herokuapp.com/auth";
    private SharedPreferences mPreferences;
    private String mUserEmail;
    private String mUserName;
    private String mUserPassword;
    private String mUserPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }


    public void registerNewAccount(View button) {
        EditText userEmailField = (EditText) findViewById(R.id.userEmail);
        mUserEmail = userEmailField.getText().toString();
        EditText userNameField = (EditText) findViewById(R.id.userName);
        mUserName = userNameField.getText().toString();
        EditText userPasswordField = (EditText) findViewById(R.id.userPassword);
        mUserPassword = userPasswordField.getText().toString();
        EditText userPasswordConfirmationField = (EditText) findViewById(R.id.userPasswordConfirmation);
        mUserPasswordConfirmation = userPasswordConfirmationField.getText().toString();

        if (mUserEmail.length() == 0 || mUserName.length() == 0 || mUserPassword.length() == 0 || mUserPasswordConfirmation.length() == 0) {
            // input fields are empty
            Toast.makeText(this, "Please complete all the fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!mUserPassword.equals(mUserPasswordConfirmation)) {
                // password doesn't match confirmation
                Toast.makeText(this, "Your password doesn't match confirmation, check again",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                //nameValuePairs.add(new BasicNameValuePair("email", mUserEmail));
                //nameValuePairs.add(new BasicNameValuePair("name", mUserName));
                //nameValuePairs.add(new BasicNameValuePair("password", mUserPassword));
                //nameValuePairs.add(new BasicNameValuePair("password_confirmation", mUserPasswordConfirmation));
               // ServiceHandler sh = new ServiceHandler();
                Log.d("reached","REached here");
               // String jsonStr = sh.makeServiceCall(REGISTER_API_ENDPOINT_URL, ServiceHandler.POST,nameValuePairs);
                // everything is ok!
                RegisterTask registerTask = new RegisterTask(RegisterActivity.this);
                registerTask.setMessageLoading("Registering new account...");
                registerTask.execute(REGISTER_API_ENDPOINT_URL);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RegisterTask extends UrlJsonAsyncTask {
        public RegisterTask(Context context) {
            super(context);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
           // urls[0]+="?"+"name="+mUserName+"&password="+mUserPassword+"&email="+mUserEmail+"&password_confirmation="+mUserPasswordConfirmation;
            HttpPost post = new HttpPost(urls[0]);
            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();
            String response = null;
            JSONObject json = new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("email", mUserEmail));
                    nameValuePairs.add(new BasicNameValuePair("name", mUserName));
                    nameValuePairs.add(new BasicNameValuePair("password", mUserPassword));
                    nameValuePairs.add(new BasicNameValuePair("password_confirmation", mUserPasswordConfirmation));


                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    // Execute HTTP Post Request
                    HttpResponse Response = client.execute(post);

                    // add the users's info to the post params
                   // userObj.put("email", mUserEmail);
                    //userObj.put("name", mUserName);
                    //userObj.put("password", mUserPassword);
                    //userObj.put("password_confirmation", mUserPasswordConfirmation);
                    //holder.put("user", userObj);
                    //StringEntity se = new StringEntity(holder.toString());
                    //post.setEntity(se);

                    // setup the request headers
                  // post.setHeader("Accept", "application/json");
                   //post.setHeader("Content-Type", "application/json");

                   // ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    //response = client.execute(post, responseHandler);
                    HttpEntity entity = Response.getEntity();
                    String retSrc = EntityUtils.toString(entity);
                    // parsing JSON


                    json = new JSONObject(retSrc);


                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO", "" + e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "" + e);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {

                if (json.length()!=0) {
                    // everything is ok
                    //SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                    //editor.putString("AuthToken", json.getJSONObject("data").getString("auth_token"));
                    //editor.commit();

                    // launch the HomeActivity and close this one
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(context, json.getString("info"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // something went wrong: show a Toast
                // with the exception message
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }
}




