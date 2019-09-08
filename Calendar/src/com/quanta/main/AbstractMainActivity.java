package com.quanta.main;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public abstract class AbstractMainActivity extends Activity{
	
	@Override
	protected void onResume()
	{
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
//	
//	@Override
//	 public boolean onKeyDown(int keyCode, KeyEvent event) {
//	  if (keyCode == KeyEvent.KEYCODE_BACK ) 
//	  {
//		  Ringtone.stopPlayRing();
//	  }
//	  return super.onKeyDown(keyCode, event);
//	 }

}
