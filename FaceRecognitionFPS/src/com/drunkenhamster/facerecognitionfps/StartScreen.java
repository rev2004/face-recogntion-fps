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
import android.widget.EditText;

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
	public static final String PREFS_NAME = "GamePreferences";		// Preference File
	static final String TAG = "FaceRecognitionFPS START MENU";		// TAG voor het debuggen
	Button goButton, drunkenHamsterButton;							// Buttons
	EditText playerInput;											// EditText om spelernaam in te vullen
	
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
        playerInput = (EditText) findViewById(R.id.usernameEditText);    
                      
        goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * TODO tekst afvangen
				 * - tekst afvangen (mag niet leeg zijn)
				 */
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				//String playerUsername = settings.getString("playerUsername", "default");
				SharedPreferences.Editor editor = settings.edit();
								
				if(!playerInput.getText().toString().equals("")){
					// inputveld is niet leeg
					editor.putString("playerUsername", playerInput.getText().toString());
					editor.commit();
					// Nu pas doorsturen naar het main menu
					Intent i = new Intent(v.getContext(), Main.class);
					startActivity(i);
				}else{
					final AlertDialog alertDialog = new AlertDialog.Builder(StartScreen.this).create();
					alertDialog.setTitle("Error");
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
				/*if(!playerInput.getText().toString().equals("")){
					// inputveld niet leeg? 
					editor.putString("playerUsername", playerInput.getText().toString());
					editor.commit();
					// Nu pas doorsturen naar het main menu
					Intent i = new Intent(v.getContext(), Main.class);
					startActivity(i);
				}else{
					final AlertDialog alertDialog = new AlertDialog.Builder(StartScreen.this).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Please fill in an username");
					alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							alertDialog.cancel();
						}
					});
					alertDialog.setIcon(R.drawable.icon);
					alertDialog.show();
				}*/
				Log.d(TAG, "Click on go button");				
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