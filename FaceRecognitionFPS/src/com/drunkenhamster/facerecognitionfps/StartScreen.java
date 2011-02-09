package com.drunkenhamster.facerecognitionfps;

import com.drunkenhamster.facerecognitionfps.R;

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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * StartScreen Activity
 * @author Harrie Essing - harrie.essing@gmail.com
 * 
 * Eerst scherm van het spel. Hier moet de gebruiker een username
 * invullen.
 * 
 */

public class StartScreen extends Activity {
	
	/**
	 * Variabelen 
	 */
	public static final String PREFS_NAME = "GamePreferences";		// Preference File
	static final String TAG = "HeadHunter Login Menu";				// TAG voor het debuggen
	Button goButton, drunkenHamsterButton;							// Buttons
	EditText playerInputUsername;									// EditText om spelernaam in te vullen
	EditText playerInputPassword;									// EditText om wachtwoord in te vullen
	// voor het inloggen via http
	InputStream is;
	String url = "http://facerecognition.twidel.nl/users/login.php";
	ImageView hh;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);
        
        /**
         * Layout elementen definieren
         */
        hh = (ImageView) findViewById(R.id.logo);
        goButton = (Button) findViewById(R.id.goButton);
        drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
        playerInputUsername = (EditText) findViewById(R.id.usernameEditText);
        playerInputPassword = (EditText) findViewById(R.id.passwordEditText);
                      
        goButton.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        		SharedPreferences.Editor editor = settings.edit();
        		
				String result = "";
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", playerInputUsername.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("password", playerInputPassword.getText().toString()));
				
				/**
				 * Check on empty username field
				 */
				if(!playerInputUsername.getText().toString().equals("")){
					// inputveld is niet leeg
					editor.putString("playerUsername", playerInputUsername.getText().toString());
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
					
					// Parse JSON Data
					try {
						JSONArray jArray = new JSONArray(result);					
						for(int i = 0; i < jArray.length(); i++){
							JSONObject data = jArray.getJSONObject(i);
							
							Log.i(TAG, "data: "+data.getString("login"));
							if(data.getString("login").equalsIgnoreCase("success")){
								Log.d(TAG, "inloggen gelukt");
								editor.putString("playerId", data.getString("userid"));
								//editor.putString("sessionId", data.getString("sessionId"));
								// Variabelen doorsturen naar volgende scherm
								editor.commit();
								// Nu pas doorsturen naar het main menu
								Intent intentMain = new Intent(v.getContext(), Main.class);
								startActivityForResult(intentMain, 0);
							}else{
								Log.d(TAG, "inloggen mislukt");
								final AlertDialog alertDialog = new AlertDialog.Builder(StartScreen.this).create();
								alertDialog.setTitle(" ");
								alertDialog.setMessage("Combination username/password doesn't exists.");
								alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										alertDialog.cancel();
									}
								});
								alertDialog.setIcon(R.drawable.icon);
								alertDialog.show();
							}
						}					
					}catch(JSONException e){
						Log.e(TAG, "error parsing json data " +e.toString());
					}
					
					Log.d(TAG, "Click on go button");
				}else{
					final AlertDialog alertDialog = new AlertDialog.Builder(StartScreen.this).create();
					alertDialog.setTitle(" ");
					alertDialog.setMessage("Please fill in an username");
					alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.cancel();
						}
					});
					alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}
				
	        	if(playerInputPassword.getText().toString().equals("")){
					// password field is empty
					final AlertDialog alertDialog = new AlertDialog.Builder(StartScreen.this).create();
					alertDialog.setTitle(" ");
					alertDialog.setMessage("Please fill in a password");
					alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.cancel();
						}
					});
					alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
	        	}
			}
		});
        
        hh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "http://facerecognition.twidel.nl/headhunter/login.php";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				Log.d(TAG, "Click on HeadHunter logo");				
			}
		});
    
        
        drunkenHamsterButton.setOnClickListener(new OnClickListener() {
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
}