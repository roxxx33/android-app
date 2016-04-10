package com.mycompany.apnafinal;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activity1 extends ListActivity {;
    private ProgressDialog pdialog;
    private static String url="https://quiet-lake-9467.herokuapp.com/communities/";
    private static final String TAG_USER = "USER";
    private static final String TAG_ID = "id";
    private static final String TAG_POSTDATA = "postdata";
    private static final String TAG_CREATED_AT = "created_at";
    private SharedPreferences mPreferences;
    private String description;
    private String title;
    private String accesstoken;
    private String uid;
    private String expiry;
    private String Client;
    private String tokentype;
    private Header Uid;
    private Header Expiry;
    private Header Tokentype;
    private Header Clients;
    private Header Accesstoken;
    private ProgressDialog pDialog;





    JSONObject post=null;
    ArrayList<HashMap<String,String>> postList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);


        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        postList=new ArrayList<HashMap<String, String>>();
        ListView lv=getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String createdat = ((TextView) view.findViewById(R.id.created_at))
                        .getText().toString();
                String postdata = ((TextView) view.findViewById(R.id.postdata))
                        .getText().toString();
                Intent in = new Intent(getApplicationContext(),
                        SinglePostActivity.class);
                //in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_CREATED_AT, createdat);
                in.putExtra(TAG_POSTDATA, postdata);
               // in.putExtra(TAG_ID, user);
                //in.putExtra(TAG_EMAIL, cost);
                //in.putExtra(TAG_PHONE_MOBILE, description);
                startActivity(in);


            }
        });
        GetPostsTask postsTask = new GetPostsTask(Activity1.this);
        postsTask.execute(url);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity1, menu);
        return true;
    }










    public class GetPostsTask extends UrlJsonAsyncTask {
        public GetPostsTask(Context context) {
            super(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
           // pDialog = new ProgressDialog(Activity1.this);
           // pDialog.setMessage("Please wait...");
           // pDialog.setCancelable(false);
           // pDialog.show();

        }


        @Override
        protected JSONObject doInBackground(String... urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            //SharedPreferences.Editor editor = mPreferences.edit();
            String commid=mPreferences.getString("Community_Id", "1");
            urls[0]+=commid;
            HttpGet get = new HttpGet(urls[0]);
            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();
            HttpResponse response = null;
            JSONObject json = new JSONObject();
            JSONArray json1=new JSONArray();
            JSONObject post=new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                   // json.put("success", false);
                    //json.put("info", "Something went wrong. Retry!");
                    // add the user email and password to
                    // the params
                   // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    //nameValuePairs.add(new BasicNameValuePair("email", mUserEmail));
                    //nameValuePairs.add(new BasicNameValuePair("password", mUserPassword));
                    //userObj.put("email", mUserEmail);
                    //userObj.put("password", mUserPassword);
                    //holder.put("user", userObj);
                    // StringEntity se = new StringEntity(holder.toString());
                    //post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    accesstoken=mPreferences.getString("access-token","0");
                    uid=mPreferences.getString("uid","0");
                    Client=mPreferences.getString("client","0");
                    expiry=mPreferences.getString("expiry","0");
                    tokentype=mPreferences.getString("token-type","0");

                    get.setHeader("access-token",accesstoken);
                    get.setHeader("client",Client);
                    get.setHeader("uid",uid);
                    get.setHeader("expiry",expiry);
                    get.setHeader("token-type",tokentype);

                    HttpResponse Response = client.execute(get);
                    HttpEntity entity = Response.getEntity();
                    String retSrc = EntityUtils.toString(entity);
                    //Log.e("Actual Output",retSrc);
                    // parsing JSON


                    json1 = new JSONArray(retSrc);
                    json=json1.getJSONObject(0);

                    Log.e("Output",retSrc);
                    Header [] a = Response.getAllHeaders();
                    for(int i=0;i<a.length;i++)
                        Log.e("Token"+ i, a[i].getName() + " " + a[i].getValue());
                    Accesstoken=Response.getFirstHeader("access-token");
                    if(Accesstoken!=null) {
                        Log.e("hey", "Got access here I want");
                        Log.e("access-token naya wala",Accesstoken.getValue());
                    }
                    Uid=Response.getFirstHeader("Uid");
                    Expiry=Response.getFirstHeader("Expiry");
                    Tokentype=Response.getFirstHeader("Token-Type");
                    Clients=Response.getFirstHeader("Client");






                    if (retSrc != null)
                        try {
                            //JSONObject jsonObj = new JSONObject(jsonStr);
                            //JSONArray jsonArr = new JSONArray(retSrc);
                            for (int i = 0; i < json1.length(); i++) {    // Getting JSON Array node
                                //contacts = jsonObj.getJSONArray(TAG_CONTACTS);
                                post = json1.getJSONObject(i);
                                Log.d("error:", "tfjyfjvfj");
                                //Log.d("object  ", persons.getString(TAG_TITLE));
                                // looping through All Contacts
                                //for (int i = 0; i < contacts.length(); i++) {

                                //	JSONObject c = contacts.getJSONObject(i);

                                String postdata = post.getString(TAG_POSTDATA);
                                Log.d("postdata",postdata);
                               // String title = post.getString(TAG_TITLE);
                                String created_at = post.getString(TAG_CREATED_AT);
                                Log.d("Created_at",created_at);
                                //String address = c.getString(TAG_ADDRESS);
                                //String gender = c.getString(TAG_GENDER);


                                // tmp hashmap for single contact
                                //HashMap<String, String> contact = new HashMap<String, String>();
                                HashMap<String, String> Post = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                               // person.put(TAG_ID, id);
                                Post.put(TAG_POSTDATA, postdata);
                                Post.put(TAG_CREATED_AT, created_at);
                                //contact.put(TAG_PHONE_MOBILE, mobile);

                                // adding contact to contact list
                                postList.add(Post);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                    }

                    // setup the request headers
                    // post.setHeader("Accept", "application/json");
                    //post.setHeader("Content-Type", "application/json");




                    // HttpEntity entity = response.getEntity();


                    //json = new JSONObject(EntityUtils.toString(entity));

                } catch (HttpResponseException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol", "" + e);
                    //json.put("info", "Email and/or password are invalid. Retry!");
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
                Log.d("fine","Reached here");
                if (json.length()!=0) {
                    // everything is ok
                    SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                  //  editor.putString("access-token", Accesstoken.getValue());
                   // editor.putString("uid", Uid.getValue());
                    //editor.putString("client", Clients.getValue());
                   // editor.putString("expiry", Expiry.getValue());
                   // editor.putString("token-type", Tokentype.getValue());



                    //editor.putString("Community_Id", json.getJSONObject("data").getString("community_id"));

                    editor.commit();

                    // launch the HomeActivity and close this one
                   // Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                    //startActivity(intent);
                    //finish();
                   // if (pDialog.isShowing())
                     //   pDialog.dismiss();
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            Activity1.this, postList,
                            R.layout.activity_list_item, new String[] {TAG_POSTDATA,TAG_CREATED_AT
                    }, new int[] {R.id.postdata,R.id.created_at});

                    {

                    }
                    setListAdapter(adapter);
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


    public void SwitchcreatePost(View button)
    {
        Intent intent = new Intent(getApplicationContext(), CreatePostActivity.class);
        startActivity(intent);
        finish();
    }

}