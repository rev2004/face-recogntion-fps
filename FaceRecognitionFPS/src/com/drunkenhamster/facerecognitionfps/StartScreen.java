package com.drunkenhamster.facerecognitionfps;

import com.drunkenhamster.facerecognitionfps.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Main Menu Activity
 * @author Harrie Essing - deluxz@gmail.com
 * 
 * Options menu voor het spel.
 * 
 */

public class StartScreen extends Activity {
	
	/**
	 * Variabelen 
	 */
	static final String TAG = "FaceRecognitionFPS";
	Button goButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startscreen);
        
        /**
         * Buttons
         */
        goButton = (Button) findViewById(R.id.goButton);
        
              
        goButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "Click on go button");
				Intent i = new Intent(v.getContext(), Main.class);
				startActivityForResult(i, 0);
			}
		});
    }
}