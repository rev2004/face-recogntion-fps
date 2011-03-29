package com.drunkenhamster.facerecognitionfps;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

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
import android.widget.Toast;
import com.drunkenhamster.facerecognitionfps.R;

public class OAuth extends Activity {

	// private static final String APP = "OAUTH";
	public static final String PREFS_NAME = "GamePreferences"; // Preference
	// File
	Twitter twitter = new TwitterFactory().getInstance();
	RequestToken requestToken;
	public final static String consumerKey = "hVuMCYLo9q1LdSzgVWk5g";
	public final static String consumerSecret = "IFQaH5Sd4mMwg5duhADvzItCjCSvFHyP0mKnbkzHPd8";
	private final String CALLBACKURL = "twitt://twitterscreen";

	SharedPreferences settings;
	SharedPreferences.Editor editor;
	AccessToken accessToken;
	Button buttonLogin;
	Button buttonUpdate;
	EditText tweetText;
	public String imagelocation;
	public String verifier;
//	String url = "http://facerecognition.twidel.nl/users/getPlayerInfo.php";
//	String url2 = "http://facerecognition.twidel.nl/users/updateScore.php";
//	String playerId; // userId van de speler uit de database
//	InputStream is;
	boolean busySending = false;
	private boolean scored = false;
//	ArrayList<NameValuePair> nameValuePairs;
	/**
	 * Calls the OAuth login method as soon as its started
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("AA", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitterscreen);
		// OAuthLogin();

		//buttonLogin = (Button) findViewById(R.id.ButtonLogin);
		buttonUpdate = (Button) findViewById(R.id.ButtonUpdate);
		tweetText = (EditText) findViewById(R.id.textStatus);
		settings = getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		imagelocation = settings.getString("lastpic", "default");
		editor.putBoolean("scored", scored);
		editor.commit();
		buttonUpdate.setText("Login at Twitter");
		// add login button listener
//		buttonLogin.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				OAuthLogin();
//			}
//		});
		// tweetText.setText(imagelocation);

		// add update button listener
		buttonUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {onUpdate(v);}
		});
	}
	
	public void onUpdate(View v){
		if (!busySending) {
			String yourtweet = tweetText.getText().toString();
			String status = tweetText.getText().toString();
			tweetText.setText("Busy sending your tweet, please wait a moment");
			if (status.length() > 100) {
				final AlertDialog alertDialog = new AlertDialog.Builder(
						OAuth.this).create();
				alertDialog.setTitle(" ");
				alertDialog
						.setMessage("Your message shouldn'nt be longer than a 100 characters. please shorten it.");
				alertDialog.setButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								alertDialog.cancel();
							}
						});
				alertDialog.setIcon(R.drawable.icon);
				tweetText.setText(yourtweet);
				alertDialog.show();

			} else {
				busySending = true;

				Log.d("AUTH", "update button");
				if (twitter.isOAuthEnabled() == true) {
					try {
						
						Log.d("AA", "update button");
						String pic = postPic(imagelocation);
						twitter.updateStatus("#headhuntergame " + pic + " "
								+ status);
//						updateScore();
						busySending = false;
						finish();
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					tweetText.setText("Logging you in to twitter, post again when this is finished!");
					busySending = false;
					OAuthLogin();
					tweetText.setText(yourtweet);
					//onUpdate(v);
				}
			}
		}
	
	}
	

//	public void updateScore() {
//		List<Status> statuses = null;
//		String result = "";
//		try {
//			settings.getString("playerId", "default");
//
//			nameValuePairs = new ArrayList<NameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("userId", playerId));
//
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(url2);
//			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpClient.execute(httpPost);
//			HttpEntity entity = response.getEntity();
//			is = entity.getContent();
//			try {
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(is, "iso-8859-1"), 8);
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//			is.close();
//
//			result = sb.toString();
//			Log.d("AA", entity.getContent().toString());
//		} catch (Exception e) {
//			Log.e("AA", "error converting result " + e.toString());
//		}
//		}
//			catch (Exception e) {
//				Log.e("AA", "error in http connection " + e.toString());
//			}
////			HttpClient httpClient = new DefaultHttpClient();
////			HttpPost httpPost = new HttpPost(url);
////			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
////			HttpResponse response = httpClient.execute(httpPost);
////			HttpEntity entity = response.getEntity();
////			is = entity.getContent();
////			// Convert response to string
////			try {
////				BufferedReader reader = new BufferedReader(
////						new InputStreamReader(is, "iso-8859-1"), 8);
////				StringBuilder sb = new StringBuilder();
////				String line = null;
////				while ((line = reader.readLine()) != null) {
////					sb.append(line + "\n");
////				}
////				is.close();
////
////				result = sb.toString();
////			} catch (Exception e) {
////				Log.e("AA", "error converting result " + e.toString());
////			}
////		} catch (Exception e) {
////			Log.e("AA", "error in http connection " + e.toString());
////		}
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
//	}

	public String postPic(String imagelocation) {
		try {
			scored = true;
			editor.putBoolean("scored", scored);
			editor.commit();
			File file = new File(imagelocation);
			MediaProvider mProvider = MediaProvider.YFROG;

			// Uri uri = this.getIntent().getData();
			// verifier =
			// uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			// AccessToken accessToken =
			// twitter.getOAuthAccessToken(requestToken, verifier);
			String token = accessToken.getToken(), secret = accessToken
					.getTokenSecret();
			Log.d("AA", token + ", " + secret);
			Properties props = new Properties();
			props.put(PropertyConfiguration.MEDIA_PROVIDER, mProvider);
			props.put(PropertyConfiguration.OAUTH_ACCESS_TOKEN, token);
			props.put(PropertyConfiguration.OAUTH_ACCESS_TOKEN_SECRET, secret);
			props.put(PropertyConfiguration.OAUTH_CONSUMER_KEY, consumerKey);
			props.put(PropertyConfiguration.OAUTH_CONSUMER_SECRET,
					consumerSecret);
			twitter4j.conf.Configuration conf = new PropertyConfiguration(props);
			ImageUploaderFactory factory = new ImageUploaderFactory(conf);
			factory.getInstance(mProvider, twitter.getAuthorization());
			ImageUpload upload = factory.getInstance(mProvider);

			String url = upload.upload(file);
			Toast.makeText(
					this,
					"Tweet placed! Press Back button to continue playing ", Toast.LENGTH_SHORT)
					.show();
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * - Creates object of Twitter and sets consumerKey and consumerSecret -
	 * Prepares the url accordingly and opens the WebView for the user to
	 * provide sign-in details - When user finishes signin-in, WebView opens
	 * your activity back
	 */
	void OAuthLogin() {
		Log.d("test", "OAuthLogin()");
		try {
			// twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			requestToken = twitter.getOAuthRequestToken(CALLBACKURL);
			String authUrl = requestToken.getAuthenticationURL();
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (TwitterException ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
			Log.e("in OAuth.OAuthLogin", ex.getMessage());
		}
		buttonUpdate.setText("Send your photo and message");
	}

	/**
	 * - Called when WebView calls your activity back (when user signed in) -
	 * Extracts the verifier from the URI received - Extracts the token and
	 * secret from the URL
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("test", "onNewIntent(Intent intent)");
		super.onNewIntent(intent);
		Uri uri = intent.getData();

		try {
			verifier = uri
					.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
			String token = accessToken.getToken(), secret = accessToken
					.getTokenSecret();
			displayTimeLine(token, secret); // display the first tweet
		} catch (TwitterException ex) {
			Log.e("OAuth.onNewIntent", "" + ex.getMessage());
		}
	}

	/**
	 * Display the timeline's first tweet
	 * 
	 * @param token
	 * @param secret
	 */
	@SuppressWarnings("deprecation")
	void displayTimeLine(String token, String secret) {
		if (token != null && secret != null) {
			List<Status> statuses = null;
			try {
				twitter.setOAuthAccessToken(token, secret);
				statuses = twitter.getFriendsTimeline();
				Toast.makeText(this, "Logged in to Twitter, please make your post now",
						Toast.LENGTH_LONG).show();
			} catch (Exception ex) {
				Toast.makeText(this, "Error:" + ex.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.d("OAuth.displayTimeLine", "" + ex.getMessage());
			}
		} else {
			Toast.makeText(this, "Not verified", Toast.LENGTH_LONG).show();
		}
	}

}
