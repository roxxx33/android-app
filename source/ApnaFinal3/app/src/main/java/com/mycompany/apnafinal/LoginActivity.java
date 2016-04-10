package com.mycompany.apnafinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.Header;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private final static String LOGIN_API_ENDPOINT_URL = "https://quiet-lake-9467.herokuapp.com/auth/sign_in";
    private SharedPreferences mPreferences;
    private String mUserEmail;
    private String mUserPassword;
    private Header uid;
    private Header expiry;
    private Header tokentype;
    private Header Client;
    private Header accesstoken;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View button) {

        EditText userEmailField = (EditText) findViewById(R.id.userEmail);
        mUserEmail = userEmailField.getText().toString();
        EditText userPasswordField = (EditText) findViewById(R.id.userPassword);
        mUserPassword = userPasswordField.getText().toString();

        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            // input fields are empty
            Toast.makeText(this, "Please complete all the fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            LoginTask loginTask = new LoginTask(LoginActivity.this);
            loginTask.setMessageLoading("Logging in...");
            loginTask.execute(LOGIN_API_ENDPOINT_URL);
        }
    }


    public class LoginTask extends UrlJsonAsyncTask {
        public LoginTask(Context context) {
            super(context);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urls[0]);
            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();
            HttpResponse response = null;
            JSONObject json = new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");
                    // add the user email and password to
                    // the params
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("email", mUserEmail));
                    nameValuePairs.add(new BasicNameValuePair("password", mUserPassword));
                    //userObj.put("email", mUserEmail);
                    //userObj.put("password", mUserPassword);
                    //holder.put("user", userObj);
                   // StringEntity se = new StringEntity(holder.toString());
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse Response = client.execute(post);
                    HttpEntity entity = Response.getEntity();
                    String retSrc = EntityUtils.toString(entity);
                    // parsing JSON


                    json = new JSONObject(retSrc);

                    // setup the request headers
                   // post.setHeader("Accept", "application/json");
                    //post.setHeader("Content-Type", "application/json");


                    accesstoken=Response.getFirstHeader("access-token");
                    if(accesstoken!=null)
                    {
                        Log.d("hey","Got access at login");
                    }
                   Log.d("access-token llogin",accesstoken.getValue());
                    uid=Response.getFirstHeader("uid");
                    expiry=Response.getFirstHeader("expiry");
                    tokentype=Response.getFirstHeader("token-type");
                    Client=Response.getFirstHeader("client");

                   // HttpEntity entity = response.getEntity();


                    //json = new JSONObject(EntityUtils.toString(entity));

                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
                    json.put("info", "Email and/or password are invalid. Retry!");
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
                //Log.d("fine","Reached here");
                if (json.length()!=0) {
                    // everything is ok
                    SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                    editor.putString("access-token", accesstoken.getValue());
                    editor.putString("uid", uid.getValue());
                    editor.putString("client", Client.getValue());
                    editor.putString("expiry", expiry.getValue());
                    editor.putString("token-type", tokentype.getValue());



                    editor.putString("Community_Id", json.getJSONObject("data").getString("community_id"));

                    editor.commit();

                    // launch the HomeActivity and close this one
                    Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
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










