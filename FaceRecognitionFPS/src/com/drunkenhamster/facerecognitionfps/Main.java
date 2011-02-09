package com.drunkenhamster.facerecognitionfps;

import com.drunkenhamster.facerecognitionfps.R;

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
import android.widget.TextView;

/**
 * Main Menu Activity
 * @author Harrie Essing - harrie.essing@gmail.com
 * 
 * Main menu voor het spel.
 * 
 */

public class Main extends Activity {
	
	/**
	 * Variabelen 
	 */
	public static final String PREFS_NAME = "GamePreferences";		// Preference File
	static final String TAG = "Headhunter Main Menu";				// TAG voor het debuggen
	Button startButton, gameModeButton, achievementsButton, 
		   quitButton, drunkenHamsterButton;						// Buttons
	TextView username;												// Textview voor de spelernaam
	//TextView tvSessionId;											// Textview voor sessionId
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        /**
         * Layout elementen definieren
         */
        startButton = (Button) findViewById(R.id.startButton);
        gameModeButton = (Button) findViewById(R.id.gameModeButton);
        achievementsButton = (Button) findViewById(R.id.achievementsButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
        username = (TextView) findViewById(R.id.playerName);
        //tvSessionId = (TextView) findViewById(R.id.tvSessionId);
        /**
         * SharedPreferences ophalen en username textview aanpassen
         */
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final SharedPreferences.Editor editor = settings.edit();
        String playerUsername = settings.getString("playerUsername", "default");
        final String playerId = settings.getString("playerId", "default"); // userId van de speler uit de database
        username.setText(playerUsername);
        //String playerSessionId = settings.getString("sessionId", "default");
        //tvSessionId.setText(playerSessionId);
        editor.putBoolean("scored", false);
        
        startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Nu pas doorsturen naar het main menu
				Intent intentSingleGame = new Intent(v.getContext(), SnapFaceActivity.class);
				startActivityForResult(intentSingleGame, 0);
				Log.d(TAG, "Cick on start button");
			}
		});
        
        gameModeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on teambattle button");
			}
		});
        
        achievementsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// userId doorgeven aan achievementsview, makkelijk om de gegevens op te halen
				editor.putString("playerId", playerId.toString());
				// Nu pas doorsturen naar het main menu
				Intent intentAchievements = new Intent(v.getContext(), Achievements.class);
				startActivityForResult(intentAchievements, 0);
				Log.d(TAG, "Click on achievements button");
			}
		});
        
        quitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on quit button");
				finish();
			}
		});
        
        drunkenHamsterButton.setOnClickListener(new OnClickListener() {
			/**
			 * Bij een onclick op het logo opent een browser de url 
			 * van onze blog
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
    
    @Override
    public void onBackPressed() {
    	final AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();
		alertDialog.setTitle(" ");
		alertDialog.setMessage("Are you sure to logout ?");
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// terugsturen naar het main menu
				alertDialog.hide();
				finish();
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.cancel();
			}
		});
		alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
    }
    
}