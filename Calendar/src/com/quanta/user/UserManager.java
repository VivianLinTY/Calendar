package com.quanta.user;

import com.quanta.tools.StatusRecords;
import com.quanta.tools.StringPool;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
	
	private static final String DEBUGTAG = "UserManager";
	
	public void setUserPreference(Context context, String number, String account, String password, String username, String userid)
	{
		if(context!=null)
		{
			SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
			preference.edit()
			.putString(StringPool.TAG_PREFERENCE_NUMBER, number)
			.putString(StringPool.TAG_PREFERENCE_ACCOUNT, account)
			.putString(StringPool.TAG_PREFERENCE_USERNAME, username)
			.putString(StringPool.TAG_PREFERENCE_USERID, userid)
			.putString(StringPool.TAG_PREFERENCE_PASSWORD, password).commit();
		}
	}
	public String getStringPreferences(Context context, String key, String defaultValue)
	{
		SharedPreferences preference = context.getSharedPreferences(StringPool.TAG_USER_PREFERENCE, context.MODE_PRIVATE);
		return ( null == preference ) ? defaultValue : preference.getString(key, defaultValue);
	}
	public void initUserInfo(Context context)
	{
		StatusRecords.UserID=getStringPreferences(context, StringPool.TAG_PREFERENCE_USERID, "");
	  	StatusRecords.UserName=getStringPreferences(context, StringPool.TAG_PREFERENCE_USERNAME, "");
	  	StatusRecords.UserAccount=getStringPreferences(context, StringPool.TAG_PREFERENCE_ACCOUNT, "");
	  	StatusRecords.UserPassword=getStringPreferences(context, StringPool.TAG_PREFERENCE_PASSWORD, "");
	  	StatusRecords.UserNumber=getStringPreferences(context, StringPool.TAG_PREFERENCE_NUMBER, "");
	}

}
