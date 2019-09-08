package com.quanta.tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class Ringtone {
	
	private static MediaPlayer mMediaPlayer;
	private static Vibrator mVibrator;
	private final static long[] mVibratePattern = {0,800,800};
	
	private static Uri getDefaultRingtoneUri(Context context,int type) {  
	    return RingtoneManager.getActualDefaultRingtoneUri(context, type); 
	}

	public synchronized static void PlayRingTone(Context context,int type){
		if(mMediaPlayer!=null)
		{
			mMediaPlayer.stop();
			mMediaPlayer=null;
		}
		mVibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    mMediaPlayer = MediaPlayer.create(context, 
	            getDefaultRingtoneUri(context,type)); 
	    mMediaPlayer.setLooping(false); 
	    mMediaPlayer.start(); 
	    if (mVibrator!=null)
        {
            mVibrator.cancel();
            mVibrator.vibrate(mVibratePattern,1);
        }
	}
	
	public synchronized static void stopPlayRing(){
		if(mMediaPlayer!=null)
		{
			mMediaPlayer.stop();
			mMediaPlayer=null;
		}
		if (mVibrator!=null)
        {
            mVibrator.cancel();
            mVibrator = null;
        }
	}
}
