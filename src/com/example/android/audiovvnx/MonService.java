/**
 * 
 * Point de départ -> https://github.com/dnagis/stub_service
 *  
 * 
 * adb uninstall com.example.android.audiovvnx
 * 
 * 
 * adb install out/target/product/mido/system/app/AudioVvnx/AudioVvnx.apk
 * 

 * 
 * 
 * #indispensable, survit au reboot (tant que tu réinstalles pas l'appli), sinon app is in background uid null
 * dumpsys deviceidle whitelist +com.example.android.audiovvnx
 * 
 * pm grant com.example.android.audiovvnx android.permission.RECORD_AUDIO
 * 
 * 
 * am start-service com.example.android.audiovvnx/.MonService  
 * 
 * il FAUT arrêter le MediaRecorder sinon le fichier est illisible
 * 
 * am stop-service com.example.android.audiovvnx/.MonService (OK car fait passer par onDestroy())
 * 
 * am force-stop com.example.android.audiovvnx (Pas OK car ne passe pas par onDestroy()) 
 * 

 * 
 * logcat -s AudioVvnx
 * 
 * adb pull /storage/emulated/0/Android/data/com.example.android.audiovvnx/cache/audiorecordtest.mp4
 * 
 */

package com.example.android.audiovvnx;

import android.app.Service;
import android.util.Log;
import android.os.IBinder;
import android.content.Intent;

import android.media.MediaRecorder;

import java.io.IOException;



public class MonService extends Service {
	
	private static final String TAG = "AudioVvnx";
	
	private MediaRecorder recorder = null;
	private static String fileName = null;
 
    @Override
    public void onCreate() {
		Log.d(TAG, "onCreate");		
				
        //stopSelf(); //j'avais mis ça juste parce que le dev guide disait qu'il fallait faire le ménage soi-même
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "OnStartCommand");
		//stopSelf(); //j'avais mis ça juste parce que le dev guide disait qu'il fallait faire le ménage soi-même
		
		

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.mp4";
		
		recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(fileName);
        Log.d(TAG, "fichier audio record: " + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        recorder.start();
        
		
		return START_NOT_STICKY;
	}

	//am stop-service com.example.android.audiovvnx/.AudioVvnx
    @Override
    public void onDestroy() {		
		Log.d(TAG, "OnDestroy");
		if (recorder != null) {
            recorder.release();
            recorder = null;
        }
		stopSelf();		
	 }
	 
	  @Override
	public IBinder onBind(Intent intent) {
      // We don't provide binding, so return null
      return null;
	}
}

