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
	static final String TAG = "FaceRecognitionFPS";
	Button  achievementsButton, quitButton, drunkenHamsterButton;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**
         * Buttons
         */
        achievementsButton = (Button) findViewById(R.id.achievementsButton);
        drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
        
              
        achievementsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on achievements button");
			}
		});
        
        /*optionsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on options button");
				Intent i = new Intent(v.getContext(), StartScreen.class);
				startActivityForResult(i, 0);
			}
		});*/
        
        quitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on quit button");
				
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