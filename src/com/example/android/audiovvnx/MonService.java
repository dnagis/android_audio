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
 * celle là hélas faut la faire à la mano... pas suffisant pm grant hélas
 * 
 * android.permission.WRITE_EXTERNAL_STORAGE
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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;

import android.app.Service;
import android.util.Log;
import android.os.IBinder;
import android.content.Intent;

import android.media.MediaRecorder;

import java.io.IOException;

import java.io.File;
import android.os.Environment;
import java.util.Date;
import java.text.SimpleDateFormat;



public class MonService extends Service {
	
	private static final String TAG = "AudioVvnx";
	
	private MediaRecorder recorder = null;
	private static String fileName = null;
	
	Notification mNotification;

 
    @Override
    public void onCreate() {
		Log.d(TAG, "onCreate");		
				
        //stopSelf(); //j'avais mis ça juste parce que le dev guide disait qu'il fallait faire le ménage soi-même
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "OnStartCommand");
		
		//Il **FAUT** aller mettre la permission à la mano, pas le choix... mkdir /storage/emulated/0/AudioRec/	

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy_HHmmss");
        fileName = "/storage/emulated/0/AudioRec/";
        fileName += sdf.format(new Date());
        fileName += ".mp4";
		
		recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(fileName);
        Log.d(TAG, "fichier audio record: " + fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed: exception: " + e);
        }

        recorder.start();
        
        
        
        //https://developer.android.com/training/notify-user/channels
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        String CHANNEL_ID = "LA_CHAN_ID";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ma_channel", importance);
        channel.setDescription("android_fait_chier_avec_sa_channel");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
		
		// Build the notification object.
        mNotification = new Notification.Builder(this, CHANNEL_ID)  //  The builder requires the context
                .setSmallIcon(R.drawable.icon)  // the status icon
                .setTicker("NotifText")  // the status text
                .setContentTitle("com.example.android.loctrack")  // the label of the entry
                .setContentText("LocTrack")  // the contents of the entry
                .build();	
		
        startForeground(1001,mNotification);        
		
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

