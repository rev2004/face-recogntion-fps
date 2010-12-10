package com.drunkenhamster.TwitterTutorial;


import java.util.List;

import com.drunkenhamster.TwitterTutorial.R;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OAuth extends Activity {
	
	//private static final String APP = "OAUTH";
	
	Twitter twitter = new TwitterFactory().getInstance();
	RequestToken requestToken;
	public final static String consumerKey = 		"hVuMCYLo9q1LdSzgVWk5g";
	public final static String consumerSecret = 	"IFQaH5Sd4mMwg5duhADvzItCjCSvFHyP0mKnbkzHPd8";
	private final String CALLBACKURL =				"twitt://main";
	
	Button buttonLogin;
	Button buttonUpdate;
	EditText tweetText;
	
	/**
	 * Calls the OAuth login method as soon as its started
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("TEST", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//OAuthLogin();
		
		buttonLogin = (Button) findViewById(R.id.ButtonLogin);
		buttonUpdate = (Button) findViewById(R.id.ButtonUpdate);
		tweetText = (EditText) findViewById(R.id.textStatus);
		
		// add login button listener
        buttonLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				OAuthLogin();
			}
		});
        
        // add update button listener
        buttonUpdate.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		String status = tweetText.getText().toString();
        		Log.d("AUTH", "update button");
        		if(twitter.isOAuthEnabled() == true){
        			try {
        				twitter.updateStatus(status);
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
			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
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
































