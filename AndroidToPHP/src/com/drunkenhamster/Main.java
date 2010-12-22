package com.drunkenhamster;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity {
	
	/**
	 * Variabelen 
	 */
	static final String TAG = "FaceRecognitionFPS";
	InputStream is;
	String url = "http://facerecognition.twidel.nl/users/login.php";
	Button goButton;
	EditText editTextUsername;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**
         * Buttons
         */
        goButton = (Button) findViewById(R.id.btnLogin);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
              
        goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String result = "";
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", editTextUsername.getText().toString()));
				
				// http Post
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
				
				// convert response to string
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
				
				// parse json data
				try {
					JSONArray jArray = new JSONArray(result);
					for(int i=0;i<jArray.length();i++){
						JSONObject data = jArray.getJSONObject(i);						
						
						Log.i(TAG, "id: " +data.getInt("id")+
								", name: " +data.getString("username")+
								", password: " +data.getString("password")+
								", picture: " +data.getString("picture")
						);
					}
				}catch(JSONException e){
					Log.e(TAG, "error parsing json data " +e.toString());
				}
				
				Log.d(TAG, "Click on go button");
			}
		});
    }
}



























