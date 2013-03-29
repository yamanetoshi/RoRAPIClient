package jp.shuri.yamanetoshi.rorapiclient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import jp.shuri.yamanetoshi.json.JSONFunctions;
import android.os.Bundle;
import android.os.Handler;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class RoRClientActivity extends ListActivity {

	private final String TAG = "RoRClientActivity";
	
	private final String BASE_URL = "http://shrouded-tundra-4125.herokuapp.com/";
	private final String TASKS = "tasks.json";
    private final String PLEASE_WAIT = "please wait...";
	
	private DefaultHttpClient mHttpClient = new DefaultHttpClient();
	private Handler mHandler = new Handler();
	
	private ProgressDialog mProgressDialog;
	
	private JSONArray mList;
	private String[] mArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(PLEASE_WAIT);
        
		mProgressDialog.show();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mList = new JSONArray(JSONFunctions.GETfromURL(BASE_URL + TASKS, mHttpClient));
				} catch (Exception e) {
					Log.d(TAG, "e is " + e);
				}
				mHandler.post(new Data2List());
			}
		}).start();	
	}
	
	private class Data2List implements Runnable {

		@Override
		public void run() {
			mArray = new String [mList.length()];
			for (int i = 0; i < mList.length(); i++) {
				try {
					mArray[i] = mList.getJSONObject(i).getString("name");
				} catch (JSONException e) {
					Log.e(TAG, e.toString());
				}
			}
			
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RoRClientActivity.this,
	        		android.R.layout.simple_list_item_1, mArray);
			setListAdapter(adapter);
			
			mProgressDialog.dismiss();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ro_rclient, menu);
		return true;
	}

}
