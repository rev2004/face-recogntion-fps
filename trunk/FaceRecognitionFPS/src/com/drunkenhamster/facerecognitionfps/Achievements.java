package com.drunkenhamster.facerecognitionfps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import com.drunkenhamster.facerecognitionfps.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Achievements Activity
 * @author Harrie Essing - harrie.essing@gmail.com
 * 
 * Overzicht van je eigen achievements
 * 
 */

public class Achievements extends Activity {
	
	/**
	 * Variabelen
	 */
	public static final String PREFS_NAME = "GamePreferences";			// Preference File
	static final String TAG = "Headhunter Achievements Menu";			// TAG voor het debuggen
	TextView playerName, playerRank, playerScore;						// Textviews
	// Voor het ophalen van de score
	InputStream is;
	String url = "http://facerecognition.twidel.nl/users/getPlayerInfo.php";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements);
        
        /**
         * Layout elemente definieren
         */
        playerName = (TextView) findViewById(R.id.achievementsPlayerData);
        playerRank = (TextView) findViewById(R.id.achievementsRankData);
        playerScore = (TextView) findViewById(R.id.achievementsScoreData);
        /**
         * SharedPreferences ophalen en username textview aanpassen
         */
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String playerId = settings.getString("playerId", "default");		// userId van de speler uit de database
        
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("userId", playerId));
        
        // HTTP Post
        try{
        	HttpClient httpClient = new DefaultHttpClient();
        	HttpPost httpPost = new HttpPost(url);
        	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	HttpResponse response = httpClient.execute(httpPost);
        	HttpEntity entity = response.getEntity();
        	is = entity.getContent();
        }catch(Exception e){
        	Log.e(TAG, "error in http connection " +e.toString());
        }
        
        // Convert response to string
        try{
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        	StringBuilder sb = new StringBuilder();
        	String line = null;
        	while((line = reader.readLine()) != null){
        		sb.append(line + "\n");
        	}
        	is.close();
        	
        	result = sb.toString();
        }catch(Exception e){
        	Log.e(TAG, "error converting result " +e.toString());
        }
        
        // Parse JSON data
        try{
        	JSONArray jArray = new JSONArray(result);
        	for(int i = 0; i < jArray.length(); i++){
        		JSONObject data = jArray.getJSONObject(i);
        		Log.i(TAG, "username: "+data.getString("username")+ "\n score: "+data.getString("score"));
        		
        		// Put data in textviews
                playerName.setText(data.getString("username"));
                if (data.getInt("score") >= 0 && data.getInt("score") < 50) {
				    playerRank.setText("Private");
				} else if (data.getInt("score") >= 50 && data.getInt("score") < 99) {
				    playerRank.setText("Private 1st Class");
				} else if (data.getInt("score") >= 100 && data.getInt("score") < 149) {
				    playerRank.setText("Lance Corporal");
				} else if (data.getInt("score") >= 150 && data.getInt("score") < 199) {
				    playerRank.setText("Corporal");
				} else if (data.getInt("score") >= 200 && data.getInt("score") < 299) {
				    playerRank.setText("Sergeant");
				} else if (data.getInt("score") >= 300 && data.getInt("score") < 399) {
				    playerRank.setText("Staff Sergeant");
				} else if (data.getInt("score") >= 400 && data.getInt("score") < 499) {
				    playerRank.setText("Warrant Officer");
				} else if (data.getInt("score") >= 500 && data.getInt("score") < 599) {
					playerRank.setText("Officer Cadet");
				} else if (data.getInt("score") >= 600 && data.getInt("score") < 749) {
					playerRank.setText("Second Lieutenant");
				} else if (data.getInt("score") >= 750 && data.getInt("score") < 999) {
					playerRank.setText("Lieutenant");
				} else if (data.getInt("score") >= 1000 && data.getInt("score") < 1249) {
					playerRank.setText("Captain");
				} else if (data.getInt("score") >= 1250 && data.getInt("score") < 1499) {
					playerRank.setText("Major");
				} else if (data.getInt("score") >= 1500 && data.getInt("score") < 1999) {
					playerRank.setText("Lieutenant Colonel");
				} else if (data.getInt("score") >= 2000 && data.getInt("score") < 2499) {
					playerRank.setText("Colonel");
				} else if (data.getInt("score") >= 2500 && data.getInt("score") < 2999) {
					playerRank.setText("Brigadier");
				} else if (data.getInt("score") >= 3000 && data.getInt("score") < 3999) {
					playerRank.setText("Major General");
				} else if (data.getInt("score") >= 4000 && data.getInt("score") < 4999) {
					playerRank.setText("Lieutenant General");
				} else if (data.getInt("score") >= 5000 && data.getInt("score") < 14999) {
					playerRank.setText("General");
				} else if (data.getInt("score") >= 15000) {
					playerRank.setText("Master Headhunter");
				}
                playerScore.setText(data.getString("score"));
        	}
        }catch(JSONException e){
        	Log.e(TAG, "error parsing json data " +e.toString());
        }
        
        
        
    }
	
}
