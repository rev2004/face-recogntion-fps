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
 * Eerst scherm van het spel. Hier moet de gebruiker een username
 * invullen.
 * 
 */

public class StartScreen extends Activity {
	
	/**
	 * Variabelen 
	 */
	static final String TAG = "FaceRecognitionFPS START MENU";
	Button goButton, drunkenHamsterButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);
        
        /**
         * Buttons
         */
        goButton = (Button) findViewById(R.id.goButton);
        drunkenHamsterButton = (Button) findViewById(R.id.drunkenHamsterButton);
                      
        goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/**
				 * TODO tekst afvangen
				 * - tekst afvangen (mag niet leeg zijn)
				 */
				Log.d(TAG, "Click on go button");
				Intent i = new Intent(v.getContext(), Main.class);
				startActivityForResult(i, 0);
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