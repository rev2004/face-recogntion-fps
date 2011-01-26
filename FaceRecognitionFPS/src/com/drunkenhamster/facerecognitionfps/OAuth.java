package com.drunkenhamster.facerecognitionfps;

import java.io.File;
import java.util.List;
import java.util.Properties;


import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploaderFactory;
import twitter4j.media.MediaProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.drunkenhamster.facerecognitionfps.R;
public class OAuth extends Activity {
	
	//private static final String APP = "OAUTH";
	public static final String PREFS_NAME = "GamePreferences";		// Preference File
	Twitter twitter = new TwitterFactory().getInstance();
	RequestToken requestToken;
	public final static String consumerKey = 		"hVuMCYLo9q1LdSzgVWk5g";
	public final static String consumerSecret = 	"IFQaH5Sd4mMwg5duhADvzItCjCSvFHyP0mKnbkzHPd8";
	private final String CALLBACKURL =				"twitt://twitterscreen";
	
	SharedPreferences settings;
    SharedPreferences.Editor editor;
    AccessToken accessToken;
	Button buttonLogin;
	Button buttonUpdate;
	EditText tweetText;
	public String imagelocation;
	public String verifier;
	/**
	 * Calls the OAuth login method as soon as its started
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("AA", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitterscreen);
		//OAuthLogin();
		
		buttonLogin = (Button) findViewById(R.id.ButtonLogin);
		buttonUpdate = (Button) findViewById(R.id.ButtonUpdate);
		tweetText = (EditText) findViewById(R.id.textStatus);
		settings = getSharedPreferences(PREFS_NAME, 0);
	    editor = settings.edit();
		imagelocation = settings.getString("lastpic", "default");
		
		// add login button listener
        buttonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				OAuthLogin();
			}
		});
        //tweetText.setText(imagelocation);
        
        // add update button listener
        buttonUpdate.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		String status = tweetText.getText().toString();
        		Log.d("AUTH", "update button");
        		if(twitter.isOAuthEnabled() == true){
        			try {
        				Log.d("AA", "update button");
        				String pic = postPic(imagelocation);
        				twitter.updateStatus(pic);
        			} catch (TwitterException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}else{
        			OAuthLogin();
        		}
        	}
        });
	}

    public String postPic(String imagelocation){
    	try{
    		Log.d("AA", "post reached");
    		File file = new File(imagelocation);
    		MediaProvider mProvider = MediaProvider.YFROG;
    		
//    		Uri uri = this.getIntent().getData();
    		//verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
//			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			String token = accessToken.getToken(), secret = accessToken.getTokenSecret();
			Log.d("AA", token + ", " + secret);
			Properties props = new Properties();
			props.put(PropertyConfiguration.MEDIA_PROVIDER, mProvider);
			props.put(PropertyConfiguration.OAUTH_ACCESS_TOKEN, token);
			props.put(PropertyConfiguration.OAUTH_ACCESS_TOKEN_SECRET, secret);
			props.put(PropertyConfiguration.OAUTH_CONSUMER_KEY, consumerKey);
			props.put(PropertyConfiguration.OAUTH_CONSUMER_SECRET, consumerSecret);
			twitter4j.conf.Configuration conf = new PropertyConfiguration(props);
			Log.d("AA", "Intent?");
			ImageUploaderFactory factory = new ImageUploaderFactory(conf);
			factory.getInstance(mProvider,twitter.getAuthorization());
			ImageUpload upload = factory.getInstance(mProvider);
			
			Log.d("AA", "Intent?");
			String url = upload.upload(file);
			return url;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
	
	/**
	 * - Creates object of Twitter and sets consumerKey and consumerSecret
	 * - Prepares the url accordingly and opens the WebView for the user to provide sign-in details
	 * - When user finishes signin-in, WebView opens your activity back
	 */
	void OAuthLogin() {
		Log.d("test", "OAuthLogin()");
		try {
			//twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
			String authUrl = requestToken.getAuthenticationURL();
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (TwitterException ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			Log.e("in OAuth.OAuthLogin", ex.getMessage());
		}
	}
	
	/**
	 * - Called when WebView calls your activity back (when user signed in)
	 * - Extracts the verifier from the URI received
	 * - Extracts the token and secret from the URL
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("test", "onNewIntent(Intent intent)");
		super.onNewIntent(intent);
		Uri uri = intent.getData();
		
		try {
			verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			String token =  accessToken.getToken(), secret = accessToken.getTokenSecret();
			displayTimeLine(token, secret); // display the first tweet
		} catch (TwitterException ex) {
			Log.e("OAuth.onNewIntent", "" + ex.getMessage());
		}
	}
	
	/**
	 * Display the timeline's first tweet
	 * @param token
	 * @param secret
	 */
	@SuppressWarnings("deprecation")
	void displayTimeLine(String token, String secret) {
		if(token != null && secret != null) {
			List<Status> statuses = null;
			try {
				twitter.setOAuthAccessToken(token, secret);
				statuses = twitter.getFriendsTimeline();
				Toast.makeText(this, statuses.get(0).getText(), Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				Toast.makeText(this, "Error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
				Log.d("OAuth.displayTimeLine", "" + ex.getMessage());
			}
		} else {
			Toast.makeText(this, "Not verified", Toast.LENGTH_LONG).show();
		}
	}
		
}
































