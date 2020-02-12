package com.example.android.audiovvnx;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import java.text.SimpleDateFormat;
import java.lang.System;


import android.content.Context;
import android.content.Intent;

public class AudioRec_Activity extends Activity {
	
	private static final String TAG = "AudioVvnx";
	private Button btn_start, btn_stop;
	
	
	 /* manifest attribut d'activity pour prevent passage ici quand rotation:
     * android:configChanges="orientation|screenLayout|screenSize"
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "AudioRec_Activity onCreate");
        super.onCreate(savedInstanceState);

        // Set the layout for this activity.  You can find it res/layout/hello_activity.xml
        View view = getLayoutInflater().inflate(R.layout.mon_layout, null);
        setContentView(view);
        
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
    }
    
    //bouton start
    public void ActionPressBouton_start(View v) {
		Log.d(TAG, "press bouton start");		
		startForegroundService(new Intent(this, MonService.class));
	}
	
	//bouton stop
    public void ActionPressBouton_stop(View v) {
		Log.d(TAG, "press bouton stop");
		stopService(new Intent(this, MonService.class));
	}
	
	
	
}
