package info.androidhive.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get posts from public  JSON api

	private static String url1 = "http://jsonplaceholder.typicode.com/posts";
	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_BODY = "body";
	private static final String TAG_TITLE = "title";

	// posts JSON OBJECT

	JSONObject persons=null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;
	ArrayList<HashMap<String,String>> personList;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("hr", "print");
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#77aaff")));
		contactList = new ArrayList<HashMap<String, String>>();
		personList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				//String name = ((TextView) view.findViewById(R.id.name))
				//		.getText().toString();
				String title = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				String body = ((TextView) view.findViewById(R.id.body))
						.getText().toString();
				String id1 = ((TextView) view.findViewById(R.id.id))
						.getText().toString();
				//String cost = ((TextView) view.findViewById(R.id.email))
				//		.getText().toString();
				//String description = ((TextView) view.findViewById(R.id.mobile))
				//		.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				//in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_TITLE, title);
				in.putExtra(TAG_BODY, body);
				in.putExtra(TAG_ID, id1);
				//in.putExtra(TAG_EMAIL, cost);
				//in.putExtra(TAG_PHONE_MOBILE, description);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetContacts().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null)
				try {
				//JSONObject jsonObj = new JSONObject(jsonStr);
				JSONArray jsonArr = new JSONArray(jsonStr);
				for (int i = 0; i < jsonArr.length(); i++) {    // Getting JSON Array node
					//contacts = jsonObj.getJSONArray(TAG_CONTACTS);
					persons = jsonArr.getJSONObject(i);
					Log.d("error:", "tfjyfjvfj");
					//Log.d("object  ", persons.getString(TAG_TITLE));
					// looping through All Contacts
					//for (int i = 0; i < contacts.length(); i++) {

					//	JSONObject c = contacts.getJSONObject(i);

					String id = persons.getString(TAG_ID);
					String title = persons.getString(TAG_TITLE);
					String body = persons.getString(TAG_BODY);
					//String address = c.getString(TAG_ADDRESS);
					//String gender = c.getString(TAG_GENDER);


					// tmp hashmap for single contact
					//HashMap<String, String> contact = new HashMap<String, String>();
					HashMap<String, String> person = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					person.put(TAG_ID, id);
					person.put(TAG_TITLE, title);
					person.put(TAG_BODY, body);
					//contact.put(TAG_PHONE_MOBILE, mobile);

					// adding contact to contact list
					personList.add(person);
				}
			}catch (JSONException e) {
					e.printStackTrace();
				}
			 else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, personList,
					R.layout.list_item, new String[] { TAG_ID, TAG_TITLE,
							TAG_BODY }, new int[] { R.id.id,
							R.id.title, R.id.body });

			setListAdapter(adapter);
		}

	}

}
