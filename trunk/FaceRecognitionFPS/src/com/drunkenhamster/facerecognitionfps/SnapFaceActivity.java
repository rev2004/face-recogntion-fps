/*
 * Copyright (C) 2010 Huan Erdao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* 
 * used and edited by Fontys Hogeschool students Eindhoven for a school project in september2010-januari2011
 * Project name: "Headhunter" (Originally started off as "Face Recognition FPS")
 * Project group name: Drunken Hamsters
 * Code editors: Andrax van den Bogaard; Harrie Essing.
 */
package com.drunkenhamster.facerecognitionfps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* Activity Class
 */
public class SnapFaceActivity extends Activity {
	public static final String PREFS_NAME = "GamePreferences";		// Preference File
    private static final String TAG = "SnapFace";

    private PreviewView camPreview_;
	private int fdetLevel_;
	private int appMode_;
	private GoogleAnalyticsTracker tracker_;
	private boolean calledACTION_GET_CONTENT_ = false;
	private long startTimeMills_ = 0;
	SharedPreferences settings2;
    SharedPreferences.Editor editor;
    
    String url = "http://facerecognition.twidel.nl/users/getPlayerInfo.php";
	String url2 = "http://facerecognition.twidel.nl/users/updateScore.php";
	String playerId; // userId van de speler uit de database
	InputStream is;
	boolean busySending = false;
	ArrayList<NameValuePair> nameValuePairs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
      
        /* GoogleAnalyticsTracker */
		Log.i(TAG,"GoogleAnalytics Setup");
		tracker_ = GoogleAnalyticsTracker.getInstance();
		tracker_.start(getString(R.string.GoogleAnalyticsUA), this);

		/* set Full Screen */
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		/* set window with no title bar */
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* Restore preferences */
		SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
		appMode_ = settings.getInt(getString(R.string.menu_AppMode), 0);
		fdetLevel_ = settings.getInt(getString(R.string.menu_Preferences), 1);

		//implicit intent receiver(GET_CONTENT will be invoked to add contact thumbnail)
        if(Intent.ACTION_GET_CONTENT.equals(getIntent().getAction())) {
            Log.i(TAG,"implicit intent:ACTION_GET_CONTENT");
            calledACTION_GET_CONTENT_ = true;
            appMode_ = 0;
        }
		
		/* create camera view */
		camPreview_ = new PreviewView(this,calledACTION_GET_CONTENT_,tracker_);
		camPreview_.setAppMode(appMode_);
		camPreview_.setfdetLevel(fdetLevel_,true);
		setContentView(camPreview_);
		/* append Overlay */
		addContentView(camPreview_.getOverlay(), new LayoutParams 
				(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	/* onDestroy */
	@Override
	protected void onDestroy() {
		// Stop the tracker when it is no longer needed.
		long curTimeMills = System.currentTimeMillis();
		Date date = new Date(curTimeMills);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		tracker_.trackEvent(getString(R.string.GA_CAT_SYS),getString(R.string.GA_ACT_STOP), simpleDateFormat.format(date), 0);
		tracker_.dispatch();
		tracker_.stop();
		super.onDestroy();
	}

	/* onStart */
	@Override
	protected void onStart() {
        if(calledACTION_GET_CONTENT_)
        	tracker_.trackPageView("/SnapFaceGetContent");
        else
        	tracker_.trackPageView("/SnapFaceStandAlone");
		startTimeMills_ = System.currentTimeMillis();
		Date date = new Date(startTimeMills_);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		tracker_.trackEvent(getString(R.string.GA_CAT_SYS),getString(R.string.GA_ACT_START), simpleDateFormat.format(date), 0);
		tracker_.dispatch();
		super.onStart();
	}

	/* onPause */
	@Override
	protected void onPause() {
		long durationTimeMills = System.currentTimeMillis()-startTimeMills_;
		Date date = new Date(durationTimeMills);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		tracker_.trackEvent(getString(R.string.GA_CAT_SYS),getString(R.string.GA_ACT_PAUSE), simpleDateFormat.format(date), 0);
		tracker_.dispatch();
		super.onPause();
	}

	/* create Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem menu_Preference = menu.add(0,R.id.menu_Preferences,0,R.string.menu_Preferences);
		menu_Preference.setIcon(android.R.drawable.ic_menu_preferences);
		MenuItem menu_AppMode = menu.add(0,R.id.menu_AppMode,0,R.string.menu_AppMode);
		menu_AppMode.setIcon(android.R.drawable.ic_menu_manage);
		return true;
	}

	/* Menu handling */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_Preferences:{
				Log.i(TAG,"GoogleAnalytics trackEvent Menu-Preference");
				tracker_.trackEvent(getString(R.string.GA_CAT_ACT),getString(R.string.GA_ACT_FDETMODE), getString(R.string.GA_LBL_CLICKED) , 1);
				showDialog(R.id.PreferencesDlg);
				break;
			}
			case R.id.menu_AppMode:{
				Log.i(TAG,"GoogleAnalytics trackEvent Menu-AppMode");
				tracker_.trackEvent(getString(R.string.GA_CAT_ACT),getString(R.string.GA_ACT_APPMODE), getString(R.string.GA_LBL_CLICKED) , 1);
				showDialog(R.id.AppModeDlg);
				break;
			}
		}
		return true;
	}

	/* onCreateDialog */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			// create progress dialog for search
			// create map selection dialog and change modes
			case R.id.PreferencesDlg: {
				return new AlertDialog.Builder(this)
				.setTitle(R.string.PreferencesDlgTitle)
				.setSingleChoiceItems(R.array.select_fdetlevel, fdetLevel_, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						fdetLevel_ = whichButton;
						camPreview_.setfdetLevel(fdetLevel_,false);
						SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt(getString(R.string.menu_Preferences), whichButton);
						editor.commit();
						dismissDialog(R.id.PreferencesDlg);
						tracker_.trackEvent(getString(R.string.GA_CAT_ACT),getString(R.string.GA_ACT_FDETMODE), Integer.toString(whichButton) , 1);
					}
				})
				.create();
			}
			case R.id.AppModeDlg: {
				return new AlertDialog.Builder(this)
				.setTitle(R.string.PreferencesDlgTitle)
				.setSingleChoiceItems(R.array.select_appmode, appMode_, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						camPreview_.setAppMode(whichButton);
						SharedPreferences settings = getSharedPreferences(getString(R.string.SnapFacePreference), 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putInt(getString(R.string.menu_AppMode), whichButton);
						editor.commit();
						dismissDialog(R.id.AppModeDlg);
						tracker_.trackEvent(getString(R.string.GA_CAT_ACT),getString(R.string.GA_ACT_APPMODE), Integer.toString(whichButton) , 1);
					}
				})
				.create();
			}
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			updateScore();
	        if(calledACTION_GET_CONTENT_){
	        	setResult(RESULT_CANCELED);
				tracker_.trackEvent(getString(R.string.GA_CAT_ACT), getString(R.string.GA_ACT_GET_CONTENT), getString(R.string.GA_LBL_CANCEL) , 1);
	        	finish();
	        }
		} 
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(calledACTION_GET_CONTENT_){
			if(resultCode == RESULT_OK && data != null ){
				Uri uri = data.getData();
				Intent intent = new Intent();
				intent.setData(uri);
				setResult(RESULT_OK,intent);
				tracker_.trackEvent(getString(R.string.GA_CAT_ACT), getString(R.string.GA_ACT_GET_CONTENT), getString(R.string.GA_LBL_SUCCEED) , 1);
			}
        	setResult(resultCode);
        	finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void openTwitter(String pic){
		settings2 = getSharedPreferences(PREFS_NAME, 0);
	    editor = settings2.edit();
	    Log.d("AA","snap+" + pic);
		editor.putString("lastpic", pic.toString());
		editor.commit();
		String test = settings2.getString("lastpic", "default");
		Log.d("AA", test);
		Intent intentSingleGame = new Intent(camPreview_.getContext(), OAuth.class);
		Log.d("AA", "intent made");
		startActivityForResult(intentSingleGame, 0);
		Log.d("AA", "activity started");
		updateScore();
	}
	
	public void updateScore() {
		List<Status> statuses = null;
		String result = "";
		try {
			settings2 = getSharedPreferences(PREFS_NAME, 0);
		    editor = settings2.edit();
			playerId = settings2.getString("playerId", "default");
			boolean scored = settings2.getBoolean("scored", false);
			if(!scored){return;}
			Log.d("AA", "Scored");
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("userId", playerId));
			Log.d("AA", nameValuePairs.get(0).getValue());
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url2);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
		}catch (Exception e) {
			Log.e(TAG, "error in http connection " + e.toString());
		}
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(url);
//			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			// Convert response to string
//			try {
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(is, "iso-8859-1"), 8);
//				StringBuilder sb = new StringBuilder();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//				is.close();
//
//				result = sb.toString();
//			} catch (Exception e) {
//				Log.e("AA", "error converting result " + e.toString());
//			}
//		} catch (Exception e) {
//			Log.e("AA", "error in http connection " + e.toString());
//		}
//		try {
//			statuses = twitter.getUserTimeline();
//			Toast.makeText(
//					this,
//					"Tweet placed! Press Back button to continue playing :"
//							+ statuses.get(0).getText(), Toast.LENGTH_SHORT)
//					.show();
//			boolean found = false;
//			for (Status st : statuses) {
//				if (st.getText().startsWith("#headhunter", 0)) {
//					Log.d("AA", "headhunter found! :D");// HTTP Post
//					found = true;
//					
//				}
//			}
//			if(found){
////			nameValuePairs.add(new BasicNameValuePair("score", "10"));
//			
//			}
//		} catch (Exception ex) {
//			Toast.makeText(this, "Error:" + ex.getMessage(), Toast.LENGTH_LONG)
//					.show();
//			Log.d("OAuth.displayTimeLine", "" + ex.getMessage());
//		}
	}
	
}

