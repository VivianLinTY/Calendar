package com.quanta.main;

import java.io.File;

import com.quanta.calendar.HomeActivity;
import com.quanta.login.LoginActivity;
import com.quanta.tools.MyLog;
import com.quanta.tools.StringPool;
import com.quanta.user.UserManager;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	private String STATUS_RECORD_SYSTEM_PATH="/data/data/com.quanta.calendar/shared_prefs/";
	private static final String DEBUGTAG = "MainActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onResume() {
        super.onResume();
		File file = new File(STATUS_RECORD_SYSTEM_PATH+StringPool.TAG_USER_PREFERENCE+".xml");
		UserManager mUserManager = new UserManager();
		if(!file.exists()){
			MyLog.Log(DEBUGTAG, "file isn't exist", 1);
			String phoneNumber = getPhoneNumber();
	    	mUserManager.setUserPreference(MainActivity.this, phoneNumber, phoneNumber, phoneNumber, phoneNumber, phoneNumber);
		}
		mUserManager.initUserInfo(MainActivity.this);
		Intent calendar = new Intent();
		calendar.setClass(this, HomeActivity.class);
		startActivity(calendar);
		finish();
    }
    
    private String getPhoneNumber(){   
        TelephonyManager mTelephonyMgr;   
        mTelephonyMgr = (TelephonyManager)  getSystemService(Context.TELEPHONY_SERVICE);    
        return mTelephonyMgr.getLine1Number();   
    }   

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
}
