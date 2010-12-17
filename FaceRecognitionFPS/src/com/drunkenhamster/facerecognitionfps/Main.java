package com.drunkenhamster.facerecognitionfps;

import com.drunkenhamster.facerecognitionfps.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Main Menu Activity
 * @author Harrie Essing - deluxz@gmail.com
 * 
 * Main menu voor het spel.
 * 
 */

public class Main extends Activity {
	
	/**
	 * Variabelen 
	 */
	static final String TAG = "FaceRecognitionFPS MAIN MENU";
	Button startButton, gameModeButton, achievementsButton, quitButton, drunkenHamsterButton;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**
         * Buttons definieren
         */
        startButton = (Button) findViewById(R.id.startButton);
        gameModeButton = (Button) findViewById(R.id.gameModeButton);
        achievementsButton = (Button) findViewById(R.id.achievementsButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
        
        startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Cick on start button");
			}
		});
        
        gameModeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on gamemode button");
			}
		});
        
        achievementsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on achievements button");
			}
		});
        
        quitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on quit button");
				
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
}