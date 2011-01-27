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
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Achievements Activity
 * 
 * @author Harrie Essing - harrie.essing@gmail.com
 * 
 *         Overzicht van je eigen achievements
 * 
 */

public class Achievements extends Activity {

	/**
	 * Variabelen
	 */
	public static final String PREFS_NAME = "GamePreferences"; // Preference
																// File
	static final String TAG = "Headhunter Achievements Menu"; // TAG voor het
																// debuggen
	TextView playerName, playerRank, playerScore, playerRankFull,
			playerRankScore; // Textviews
	ImageView rankImageLeft, rankImageRight; // Images
	Button drunkenHamsterButton; // Buttons
	// Voor het ophalen van de score
	InputStream is;
	String url = "http://facerecognition.twidel.nl/users/getPlayerInfo.php";
	String url2 = "http://facerecognition.twidel.nl/users/updateScore.php";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements);

		/**
		 * Layout elementen definieren
		 */
		playerName = (TextView) findViewById(R.id.achievementsPlayerData);
		playerRank = (TextView) findViewById(R.id.achievementsRankData);
		playerScore = (TextView) findViewById(R.id.achievementsScoreData);
		drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
		// Scores
		playerRankFull = (TextView) findViewById(R.id.rankNextRankFull);
		playerRankScore = (TextView) findViewById(R.id.rankNextRank);
		rankImageLeft = (ImageView) findViewById(R.id.rankImageLeft);
		rankImageRight = (ImageView) findViewById(R.id.rankImageRight);
		/**
		 * SharedPreferences ophalen en username textview aanpassen
		 */
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String playerId = settings.getString("playerId", "default"); // userId
																		// van
																		// de
																		// speler
																		// uit
																		// de
																		// database

		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userId", playerId));

		// HTTP Post
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Log.e(TAG, "error in http connection " + e.toString());
		}

		// Convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
			Log.d("AA", result);
		} catch (Exception e) {
			Log.e(TAG, "error converting result " + e.toString());
		}

		// Parse JSON data
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject data = jArray.getJSONObject(i);
				Log.i(TAG, "username: " + data.getString("username")
						+ "\n score: " + data.getString("score"));

				// Put data in textviews
				playerName.setText(data.getString("username"));
				if (data.getInt("score") >= 0 && data.getInt("score") < 50) {
					playerRank.setText("Private");

					playerRankFull.setText("Private 1st Class");
					playerRankScore.setText(data.getInt("score") + " / 50");

					rankImageLeft
							.setBackgroundResource(R.drawable.private_class);
					rankImageRight
							.setBackgroundResource(R.drawable.private_first_class);
				} else if (data.getInt("score") >= 50
						&& data.getInt("score") < 99) {
					playerRank.setText("Private 1st Class");

					playerRankFull.setText("Lance Corporal");
					playerRankScore.setText(data.getInt("score") + " / 99");
					rankImageLeft
							.setBackgroundResource(R.drawable.private_first_class);
					rankImageRight
							.setBackgroundResource(R.drawable.lance_corporal);
				} else if (data.getInt("score") >= 100
						&& data.getInt("score") < 149) {
					playerRank.setText("Lance Corporal");

					playerRankFull.setText("Corporal");
					playerRankScore.setText(data.getInt("score") + " / 149");
					rankImageLeft
							.setBackgroundResource(R.drawable.lance_corporal);
					rankImageRight.setBackgroundResource(R.drawable.corporal);
				} else if (data.getInt("score") >= 150
						&& data.getInt("score") < 199) {
					playerRank.setText("Corporal");

					playerRankFull.setText("Sergeant");
					playerRankScore.setText(data.getInt("score") + " / 199");
					rankImageLeft.setBackgroundResource(R.drawable.corporal);
					rankImageRight.setBackgroundResource(R.drawable.sergeant);
				} else if (data.getInt("score") >= 200
						&& data.getInt("score") < 299) {
					playerRank.setText("Sergeant");

					playerRankFull.setText("Staff Sergeant");
					playerRankScore.setText(data.getInt("score") + " / 299");
					rankImageLeft.setBackgroundResource(R.drawable.sergeant);
					rankImageRight
							.setBackgroundResource(R.drawable.staff_sergeant);
				} else if (data.getInt("score") >= 300
						&& data.getInt("score") < 399) {
					playerRank.setText("Staff Sergeant");

					playerRankFull.setText("Warrant Officer");
					playerRankScore.setText(data.getInt("score") + " / 399");
					rankImageLeft
							.setBackgroundResource(R.drawable.staff_sergeant);
					rankImageRight
							.setBackgroundResource(R.drawable.warrant_officer);
				} else if (data.getInt("score") >= 400
						&& data.getInt("score") < 499) {
					playerRank.setText("Warrant Officer");

					playerRankFull.setText("Officer Cadet");
					playerRankScore.setText(data.getInt("score") + " / 499");
					rankImageLeft
							.setBackgroundResource(R.drawable.warrant_officer);
					rankImageRight
							.setBackgroundResource(R.drawable.officer_cadet);
				} else if (data.getInt("score") >= 500
						&& data.getInt("score") < 599) {
					playerRank.setText("Officer Cadet");

					playerRankFull.setText("Second Lieutenant");
					playerRankScore.setText(data.getInt("score") + " / 599");
					rankImageLeft
							.setBackgroundResource(R.drawable.officer_cadet);
					rankImageRight
							.setBackgroundResource(R.drawable.second_lieutenant);
				} else if (data.getInt("score") >= 600
						&& data.getInt("score") < 749) {
					playerRank.setText("Second Lieutenant");

					playerRankFull.setText("Lieutenant");
					playerRankScore.setText(data.getInt("score") + " / 749");
					rankImageLeft
							.setBackgroundResource(R.drawable.second_lieutenant);
					rankImageRight
							.setBackgroundResource(R.drawable.first_lieutenant);
				} else if (data.getInt("score") >= 750
						&& data.getInt("score") < 999) {
					playerRank.setText("Lieutenant");

					playerRankFull.setText("Captain");
					playerRankScore.setText(data.getInt("score") + " / 999");
					rankImageLeft
							.setBackgroundResource(R.drawable.first_lieutenant);
					rankImageRight.setBackgroundResource(R.drawable.captain);
				} else if (data.getInt("score") >= 1000
						&& data.getInt("score") < 1249) {
					playerRank.setText("Captain");

					playerRankFull.setText("Major");
					playerRankScore.setText(data.getInt("score") + " / 1249");
					rankImageLeft.setBackgroundResource(R.drawable.captain);
					rankImageRight.setBackgroundResource(R.drawable.major);
				} else if (data.getInt("score") >= 1250
						&& data.getInt("score") < 1499) {
					playerRank.setText("Major");

					playerRankFull.setText("Lieutenant Colonel");
					playerRankScore.setText(data.getInt("score") + " / 1499");
					rankImageLeft.setBackgroundResource(R.drawable.major);
					rankImageRight
							.setBackgroundResource(R.drawable.lieutenant_colonel);
				} else if (data.getInt("score") >= 1500
						&& data.getInt("score") < 1999) {
					playerRank.setText("Lieutenant Colonel");

					playerRankFull.setText("Colonel");
					playerRankScore.setText(data.getInt("score") + " / 1999");
					rankImageLeft
							.setBackgroundResource(R.drawable.lieutenant_colonel);
					rankImageRight.setBackgroundResource(R.drawable.colonel);
				} else if (data.getInt("score") >= 2000
						&& data.getInt("score") < 2499) {
					playerRank.setText("Colonel");

					playerRankFull.setText("Brigadier");
					playerRankScore.setText(data.getInt("score") + " / 2499");
					rankImageLeft.setBackgroundResource(R.drawable.colonel);
					rankImageRight.setBackgroundResource(R.drawable.brigadier);
				} else if (data.getInt("score") >= 2500
						&& data.getInt("score") < 2999) {
					playerRank.setText("Brigadier");

					playerRankFull.setText("Major General");
					playerRankScore.setText(data.getInt("score") + " / 2999");
					rankImageLeft.setBackgroundResource(R.drawable.brigadier);
					rankImageRight
							.setBackgroundResource(R.drawable.major_general);
				} else if (data.getInt("score") >= 3000
						&& data.getInt("score") < 3999) {
					playerRank.setText("Major General");

					playerRankFull.setText("Lieutenant General");
					playerRankScore.setText(data.getInt("score") + " / 4999");
					rankImageLeft
							.setBackgroundResource(R.drawable.major_general);
					rankImageRight
							.setBackgroundResource(R.drawable.lieutenant_general);
				} else if (data.getInt("score") >= 4000
						&& data.getInt("score") < 4999) {
					playerRank.setText("Lieutenant General");

					playerRankFull.setText("General");
					playerRankScore.setText(data.getInt("score") + " / 4999");
					rankImageLeft
							.setBackgroundResource(R.drawable.lieutenant_general);
					rankImageRight.setBackgroundResource(R.drawable.general);
				} else if (data.getInt("score") >= 5000
						&& data.getInt("score") < 14999) {
					playerRank.setText("General");

					playerRankFull.setText("Master Headhunter");
					playerRankScore.setText(data.getInt("score") + " / 14999");
					rankImageLeft.setBackgroundResource(R.drawable.general);
					// rankImageRight;
				} else if (data.getInt("score") >= 15000) {
					playerRank.setText("Master Headhunter");
				}
				playerScore.setText(data.getString("score"));
			}
		} catch (JSONException e) {
			Log.e(TAG, "error parsing json data " + e.toString());
		}

//		updateScore();

		drunkenHamsterButton.setOnClickListener(new OnClickListener() {
			/**
			 * Bij een onclick op het logo opent een browser de url van onze
			 * blog
			 */
			@Override
			public void onClick(View v) {
				String url = "http://facerecognition.twidel.nl";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

				Log.d(TAG, "Click on drunkenhamster uber logo button");
			}
		});

	}

	public void updateScore() {
		String result = "";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String playerId = settings.getString("playerId", "default");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userId", playerId));
		boolean scored = settings.getBoolean("scored", false);
		if (scored){
			Log.d("AA", "Scored");
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url2);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			// is = entity.getContent();
		} catch (Exception e) {
			Log.e(TAG, "error in http connection " + e.toString());
		}
		}
		// Convert response to string
		// try{
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(is,"iso-8859-1"),8);
		// StringBuilder sb = new StringBuilder();
		// String line = null;
		// while((line = reader.readLine()) != null){
		// sb.append(line + "\n");
		// }
		// is.close();
		//         	
		// result = sb.toString();
		// Log.d("AA", result);
		// }catch(Exception e){
		// Log.e(TAG, "error converting result " +e.toString());
		// }

	}

}
