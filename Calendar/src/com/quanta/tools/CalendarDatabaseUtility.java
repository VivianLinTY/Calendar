package com.quanta.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.quanta.calendar.HomeActivity;
import com.quanta.calendar.ScheduleInfoList;
import com.quanta.calendar.ScheduleInfoList.ScheduleInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalendarDatabaseUtility {
	private static final String DEBUGTAG = "CalendarDatabaseUtility";
	private static final String DB_TABLE    = "table_Calendar";
	public static final String KEY_ID       = "_id";
    public static final String KEY_TEXT     = "text";
    public static final String KEY_TIME     = "time";
    public static final String KEY_DATE     = "date";
    public static final String KEY_CLOCK    = "clock";
    private static final String DB_NAME     = "Calendar.db";
//    private static final int    DB_ROW_NUMBER   = 20;
    private static final int DB_VERSION     = 1;
	private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEXT + " TEXT,"  + KEY_TIME + " TEXT,"+ KEY_DATE + " TEXT,"+ KEY_CLOCK + " TEXT)";
	private SQLiteDatabase mSQLiteDatabase = null;
    private DatabaseHelper mDatabaseHelper = null;
    private Context mContext;
    private boolean isOpen = false;
    private ArrayList<ScheduleInfoList> mScheduleList = new ArrayList<ScheduleInfoList>();
	
	private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public CalendarDatabaseUtility(Context context)
    {
        mContext = context;
    }
    
    public DatabaseHelper open(boolean iswritable) throws SQLException
    {
        if (mDatabaseHelper==null)
        {
            mDatabaseHelper = new DatabaseHelper(mContext);
        }
        if (mSQLiteDatabase==null)
        {
            
            if (iswritable)
                mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            else
                mSQLiteDatabase = mDatabaseHelper.getReadableDatabase();
        }
        isOpen = true;
        return mDatabaseHelper;
    }
    public void close()
    {
        if (mSQLiteDatabase!=null && mSQLiteDatabase.isOpen())
        {
            mSQLiteDatabase.close();
        }
        if (mDatabaseHelper!=null && isOpen)
        {
            mDatabaseHelper.close();
        }
        mSQLiteDatabase = null;
        mDatabaseHelper = null;
        mContext = null;
        isOpen = false;
    }

    public long addData(final Date date, final String time, final String content, final boolean setClock)
    {
        SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");
        String day = d.format(date.getTime());
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEXT, content);
        initialValues.put(KEY_DATE, day);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_CLOCK, setClock);
        long key_id = mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
        MyLog.Log(DEBUGTAG, "==>addData - mSQLiteDatabase:"+mSQLiteDatabase+", key_id:"+key_id+", date:"+date+", time:"+time +", content:"+content +", setClock:"+setClock,1);
        ScheduleInfo info_temp = new ScheduleInfo();
        info_temp.setTime(time);
        info_temp.setMessage(content);
        info_temp.setKeyId((int)key_id);
    	ArrayList<ScheduleInfo> ListInfo = new ArrayList<ScheduleInfo>();
    	ListInfo.add(info_temp);
    	ScheduleInfoList mScheduleInfoList = new ScheduleInfoList();
    	mScheduleInfoList.setDate(day);
    	mScheduleInfoList.setScheduleInfoList(ListInfo);
        mScheduleList.add(mScheduleInfoList);
        HomeActivity.mScheduleList = mScheduleList;
        if(setClock)
        {
        	SimpleDateFormat dsf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        	Date today = new Date();
        	Date alarm = null;
        	try {
				alarm = dsf.parse(day+" "+time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	if(alarm!=null && alarm.getTime()>today.getTime())
        	{
	        	HomeActivity.ClockList.add(mScheduleInfoList);
	        	AlarmClock mAlarmClock = new AlarmClock();
	        	mAlarmClock.setAlarmClock(mContext);
        	}
        }
        return key_id;
    }

    public ArrayList<ScheduleInfoList> getdbData()
    {
    	MyLog.Log(DEBUGTAG,"getdbData",1);
        int count = 0;
        Cursor data_cursor =  null;
        if(mSQLiteDatabase != null)
            data_cursor = mSQLiteDatabase.query(DB_TABLE, 
                                                new String[] {  KEY_ID, 
            													KEY_TEXT,
            													KEY_TIME,
            													KEY_DATE,
            													KEY_CLOCK }, 
                                                null, null, null, null, null, null);
        if (data_cursor!=null && data_cursor.getCount()>0)
        {
            count =  data_cursor.getCount();
            while (data_cursor.moveToNext() && count > 0) 
            {
            	int keyId = data_cursor.getInt(data_cursor.getColumnIndex(KEY_ID));
        		String date = data_cursor.getString(data_cursor.getColumnIndex(KEY_DATE));
        		String time = data_cursor.getString(data_cursor.getColumnIndex(KEY_TIME));
        		String content = data_cursor.getString(data_cursor.getColumnIndex(KEY_TEXT));
        		String setClock = data_cursor.getString(data_cursor.getColumnIndex(KEY_CLOCK));
        		MyLog.Log(DEBUGTAG,"date:"+date+", time:"+time +", content:"+content+" keyId:"+keyId,1);
        		ScheduleInfo info_temp = new ScheduleInfo();
                info_temp.setTime(time);
                info_temp.setMessage(content);
                info_temp.setKeyId(keyId);
            	ArrayList<ScheduleInfo> ListInfo = new ArrayList<ScheduleInfo>();
            	ListInfo.add(info_temp);
            	ScheduleInfoList mScheduleInfoList = new ScheduleInfoList();
            	mScheduleInfoList.setDate(date);
            	mScheduleInfoList.setScheduleInfoList(ListInfo);
                mScheduleList.add(mScheduleInfoList);
                MyLog.Log(DEBUGTAG,"size:"+mScheduleList.size()+"   setClock="+setClock,1);
                if(setClock.contentEquals("1"))
                {
                	Calendar now = Calendar.getInstance();
                	Calendar cal = Calendar.getInstance();
        	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        			try {
        				cal.setTime(sdf.parse(date+" "+time));
        			} catch (java.text.ParseException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        				MyLog.Log(DEBUGTAG, "==>cal.setTime failed!!",1);
        			}
                	if(cal.getTime().getTime()>now.getTime().getTime())
                	{
                		HomeActivity.ClockList.add(mScheduleInfoList);
                	}
                }
            }
        }
        if(data_cursor != null)
            data_cursor.close();
        data_cursor=null;
        HomeActivity.mScheduleList = mScheduleList;
        AlarmClock mAlarmClock = new AlarmClock();
    	mAlarmClock.setAlarmClock(mContext);
        return mScheduleList;
    }
    
    public void deactiveation()
	{
		SQLiteDatabase delete_db = mDatabaseHelper.getWritableDatabase();
		delete_db.delete(DB_TABLE, null, null);
		delete_db.close();
	}
    
    public synchronized void deleteData(int keyId)
    {
    	MyLog.Log(DEBUGTAG,"deleteData:"+keyId,1);
        mSQLiteDatabase.execSQL("DELETE FROM " + DB_TABLE + " WHERE " + KEY_ID +"=" + keyId);
        for(int i=0; i<mScheduleList.size();i++)
        {
        	if(mScheduleList.get(i).getScheduleInfoList().get(0).getKeyId()==keyId)
        	{
        		mScheduleList.remove(i);
        		break;
        	}
        }
        for(int i=0; i<HomeActivity.ClockList.size();i++)
        {
        	if(HomeActivity.ClockList.get(i).getScheduleInfoList().get(0).getKeyId()==keyId)
        	{
        		HomeActivity.ClockList.remove(i);
        		break;
        	}
        }
        HomeActivity.setClock=false;
        HomeActivity.mScheduleList = mScheduleList;
    }
}
