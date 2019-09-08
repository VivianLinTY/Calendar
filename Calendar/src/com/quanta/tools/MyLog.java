package com.quanta.tools;

import android.util.Log;

public class MyLog {
	
	public static final boolean Show_DebugTAG = false;
	public static final String tag = "CDRC";
	
	public static void Log(String tag_activity, String msg,int level){
		switch (level) {
		case 0:
			if(Show_DebugTAG) Log.i(tag,tag_activity+" "+msg);
			break;
		case 1:
			if(Show_DebugTAG) Log.d(tag,tag_activity+" "+msg);
			break;
		case 2:
			if(Show_DebugTAG) Log.e(tag,tag_activity+" "+msg);
			break;
		default:
			break;
		}
	}

}
