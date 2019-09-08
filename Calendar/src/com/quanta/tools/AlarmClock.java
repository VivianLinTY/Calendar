package com.quanta.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.quanta.calendar.HomeActivity;
import com.quanta.calendar.PlayReceiver;

public class AlarmClock {
	private static final String DEBUGTAG = "AlarmClock";
	
	public synchronized void setAlarmClock(Context mContext)
	{
		HomeActivity.setClock=true;
		if(HomeActivity.ClockList!=null && HomeActivity.ClockList.size()>0)
		{
			Calendar cal = null;
			for(int i=0;i<HomeActivity.ClockList.size();i++)
			{
				Calendar cal_temp = Calendar.getInstance();
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				try {
					cal_temp.setTime(sdf.parse(HomeActivity.ClockList.get(i).getDate()+" "+HomeActivity.ClockList.get(i).getScheduleInfoList().get(0).getTime()));
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MyLog.Log(DEBUGTAG, "==>cal.setTime failed!!",1);
				}
				if(cal==null || (cal_temp.getTimeInMillis()>0 && cal_temp.getTimeInMillis()<cal.getTimeInMillis()))
				{
					MyLog.Log(DEBUGTAG, "==>setAlarmClock    cal_temp.getTimeInMillis="+cal_temp.getTimeInMillis(),1);
					cal=cal_temp;
					HomeActivity.i_get = i;
					HomeActivity.alarmMessage = HomeActivity.ClockList.get(i).getScheduleInfoList().get(0).getMessage();
				}
//				else if(cal_temp.getTimeInMillis()>0 && cal_temp.getTimeInMillis()==cal.getTimeInMillis())
//					Message = "1."+Message+"\n2."+HomeActivity.ClockList.get(i).getScheduleInfoList().get(0).getMessage();
			}
			if(cal!=null)
			{
				Intent intent = new Intent(mContext, PlayReceiver.class);
		        intent.putExtra("msg", "play_alarm");
		    	PendingIntent pi = PendingIntent.getBroadcast(mContext, 1, intent, PendingIntent.FLAG_ONE_SHOT);
		    	AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
		        MyLog.Log(DEBUGTAG, "get message = "+HomeActivity.alarmMessage,1);
		    	//      int hour = Integer.valueOf(time.substring(0,2));
		    	//    	int minutes = Integer.valueOf(time.substring(3, 5));
		    	//    	Log.d(DEBUGTAG, "hour:"+hour +", minutes:"+minutes);
		    	//    	Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
		    	//    	intent.putExtra(AlarmClock.EXTRA_MESSAGE, content);
		    	//    	intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
		    	//    	intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
		    	//    	mContext.startActivity(intent);
			}
		}
	}

}
