package com.quanta.calendar;

import com.quanta.main.AbstractMainActivity;
import com.quanta.tools.AlarmClock;
import com.quanta.tools.CalendarDatabaseUtility;
import com.quanta.tools.MyLog;
import com.quanta.tools.Ringtone;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AbstractMainActivity {
  private static final String DEBUGTAG = "HomeActivity";
  private static final int UPDATE_CAL = 0;
  private CalendarPickerView calendar;
  private ListView schedulelist;
  private ImageButton btn_last_month,btn_next_month,btn_add_event,timePickerButton,btn_alarm_on,btn_alarm_off;
  private Button btn_cancel,btn_save;
  private EditText edtContent,edtTime;
  private TextView btn_keyid;
  private LinearLayout list_layout;
  int month_number=0;
  private Calendar thisMonth;
  private boolean setClockAlarm = false;
  float down_x=0,up_x=0;
  Dialog mDialog = null;
  public static ArrayList<ScheduleInfoList> mScheduleList = null;
  public static ArrayList<ScheduleInfoList> ClockList = new ArrayList<ScheduleInfoList>();
  public static Context HomeContext;
  public static boolean setClock=true;
  public static String alarmMessage="";
  public static int i_get=-1;
  List<Date> dateList = null;
  OnTimeSetListener myTimeSetListener;
  String time;
  Date day;
  CalendarDatabaseUtility mCalendarDatabaseUtility;
  
  private Handler mHandler = new Handler()
  {
      public void handleMessage(Message msg)
      {
        switch(msg.what)
        {
            case UPDATE_CAL:
            	list_layout.setVisibility(View.GONE);
            	dateList = null;
            	dateList = new ArrayList<Date>();
            	for(int i=0;i<mScheduleList.size();i++)
            	{
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            		try {
            			dateList.add(sdf.parse(mScheduleList.get(i).getDate()));
            		} catch (ParseException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	Calendar cal = Calendar.getInstance();
            	cal.setTime(day);
            	Date minDay = getFirstMonthDay(cal);
                Date maxDay = getLastMonthDay(cal);
                setCalendarView(minDay,maxDay);
                break;
        }
      }
  };

  private void setAlarmOn(){
	  btn_alarm_off.setVisibility(View.INVISIBLE);
 	  btn_alarm_on.setVisibility(View.VISIBLE);
 	  setClockAlarm=true;
  }
  private void setAlarmOff(){
	  btn_alarm_on.setVisibility(View.INVISIBLE);
 	  btn_alarm_off.setVisibility(View.VISIBLE);
 	  setClockAlarm=false;
  }
  private void showAlarmIcon(){
	  if(setClockAlarm)
	  {
		  btn_alarm_off.setVisibility(View.INVISIBLE);
		  btn_alarm_on.setVisibility(View.VISIBLE);
	  }
	  else
	  {
		  btn_alarm_on.setVisibility(View.INVISIBLE);
	 	  btn_alarm_off.setVisibility(View.VISIBLE);
	  }
  }
  
  @Override
  public void onPause() {
	  super.onPause();
	  Ringtone.stopPlayRing();
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MyLog.Log(DEBUGTAG, "onCreate", 1);
    setContentView(R.layout.layout_home);
    HomeContext = this;
    dateList = new ArrayList<Date>();
    if (mCalendarDatabaseUtility==null)
    {
    	mCalendarDatabaseUtility = new CalendarDatabaseUtility(this);
    	mCalendarDatabaseUtility.open(true);
    	mCalendarDatabaseUtility.getdbData();
    }
    btn_last_month = (ImageButton) findViewById(R.id.btn_last_month);
    btn_next_month = (ImageButton) findViewById(R.id.btn_next_month);
    btn_add_event = (ImageButton) findViewById(R.id.btn_add_event);
    list_layout = (LinearLayout) findViewById(R.id.list_layout);
    schedulelist = (ListView) findViewById(R.id.schedulelist);
    thisMonth = Calendar.getInstance();
    mDialog = new Dialog(HomeActivity.this,R.style.MyDialog);
    mDialog.setContentView(R.layout.dialog_event);
    btn_keyid = (TextView)mDialog.findViewById(R.id.btn_keyid);
//    mDialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
//		@Override
//		public void onDismiss(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				MyLog.Log(DEBUGTAG, "DismissListener", 1);
//				if(!btn_keyid.getText().toString().contentEquals(""))
//				{
//					
//				}
//			}
//		});
    timePickerButton = (ImageButton)mDialog.findViewById(R.id.timePickerButton);
    edtTime = (EditText)mDialog.findViewById(R.id.edtTime);
    edtContent = (EditText)mDialog.findViewById(R.id.edtContent);
    btn_save = (Button)mDialog.findViewById(R.id.btn_save);
    btn_cancel = (Button)mDialog.findViewById(R.id.btn_cancel);
    btn_alarm_off = (ImageButton)mDialog.findViewById(R.id.btn_alarm_off);
    btn_alarm_on = (ImageButton)mDialog.findViewById(R.id.btn_alarm_on);
    myTimeSetListener= new TimePickerDialog.OnTimeSetListener(){
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if(String.valueOf(minute).length()==1)
			{
				if(String.valueOf(hourOfDay).length()==1)
					time = "0"+hourOfDay+":0"+minute;
				else
					time = hourOfDay+":0"+minute;
			}
			else
			{
				if(String.valueOf(hourOfDay).length()==1)
					time = "0"+hourOfDay+":"+minute;
				else
					time = hourOfDay+":"+minute;
			}
    		edtTime.setText(time);
    		}
    	};
    OnClickListener theclick=new OnClickListener(){
		@Override
        public void onClick(View v) {
            synchronized(HomeActivity.this)
            {
            	switch(v.getId()){
	                 case R.id.btn_last_month:
	                	 	showLastMonth();
	          	  			break;
	                 case R.id.btn_next_month:
	                	 	showNextMonth();
	          	  			break;
	                 case R.id.btn_add_event:
	                	 	edtTime.setText("");;
	                	 	edtContent.setText("");
	                	 	btn_keyid.setText("");
	                	 	showAlarmIcon();
	                	 	mDialog.show();
	          	  			break;
	                 case R.id.btn_save:
	                	    if(day!=null && time!=null && !time.contentEquals(""))
	                	    {
	                	    	String keyID = btn_keyid.getText().toString();
	                	    	if(!keyID.contentEquals(""))
	                	    		mCalendarDatabaseUtility.deleteData(Integer.valueOf(keyID));
		                	 	mCalendarDatabaseUtility.addData(day, time, edtContent.getText().toString(), setClockAlarm);
		                	 	mDialog.dismiss();
		                	 	Message msg = new Message();
		                	 	msg.what = UPDATE_CAL;
					            mHandler.sendMessage(msg);
	                	    }
	                	    else
	                	    	mDialog.dismiss();
	                	    edtTime.setText("");
	                	    edtContent.setText("");
	          	  			break;
	                 case R.id.btn_cancel:
	                	 	mDialog.dismiss();
	                 		break;
	                 case R.id.timePickerButton:
	                	 	Calendar c = Calendar.getInstance();
	                	    int myHour = c.get(Calendar.HOUR_OF_DAY);
	                	    int myMinute = c.get(Calendar.MINUTE);
	                	    Dialog timePicker = new TimePickerDialog(HomeActivity.this,myTimeSetListener,myHour, myMinute, true);
	                	    timePicker.show();
	                	 	break;
	                 case R.id.btn_alarm_on:
	                	 	setAlarmOff();
	                	 	break;
	                 case R.id.btn_alarm_off:
	                	 	setAlarmOn();
	                	 	break;
            	}
            }
        }//End of onclick
    };
    btn_add_event.setOnClickListener(theclick);
	btn_save.setOnClickListener(theclick);
	btn_cancel.setOnClickListener(theclick);
	timePickerButton.setOnClickListener(theclick);
	btn_last_month.setOnClickListener(theclick);
	btn_next_month.setOnClickListener(theclick); 
	btn_alarm_off.setOnClickListener(theclick);
	btn_alarm_on.setOnClickListener(theclick);
	
    for(int i=0;i<mScheduleList.size();i++)
	{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			dateList.add(sdf.parse(mScheduleList.get(i).getDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    mHandler.post(setClockCheck);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
	  switch (event.getAction()) {  
	  case MotionEvent.ACTION_DOWN: {
		    down_x = event.getX();
	        break;  
	     }
	  case MotionEvent.ACTION_UP: {
		    up_x = event.getX();
		    if(up_x-down_x>80)
		    	showLastMonth();
		    else if(up_x-down_x<-80)
		    	showNextMonth();
	        break;  
	     }
	  }
      return super.onTouchEvent(event);
  }
  
  
  @Override
  public void onResume() {
    super.onResume();
    thisMonth = getMonth(month_number);
    Date minDay = getFirstMonthDay(thisMonth);
    Date maxDay = getLastMonthDay(thisMonth);
    setCalendarView(minDay,maxDay);
  }
  
  private void setCalendarView(Date minDay,Date maxDay){
	  showCalendar(minDay,maxDay);
	    calendar.setOnTouchListener(new OnTouchListener(){
	        @Override
	        public boolean onTouch(View arg0, MotionEvent motionEvent) {
	        	switch (motionEvent.getAction()) {  
	  		  case MotionEvent.ACTION_DOWN: {
	  			    down_x = motionEvent.getX();
	  		        break;  
	  		     }
	  		  case MotionEvent.ACTION_UP: {
	  			    up_x = motionEvent.getX();
	  			    if(up_x-down_x>80)
	  			    	showLastMonth();
	  			    else if(up_x-down_x<-80)
	  			    	showNextMonth();
	  		        break;  
	  		     }
	  		  }
	        return false;
	        }
	     });
	    calendar.setOnDateSelectedListener(new OnDateSelectedListener()
	    {
			@Override
			public void onDateSelected(Date date) {
				// TODO Auto-generated method stub
				MyLog.Log(DEBUGTAG, "select date is "+date, 1);
				day = date;
				list_layout.setVisibility(View.VISIBLE);
				final ScheduleListAdapter mScheduleListAdapter = new ScheduleListAdapter(HomeActivity.this,mScheduleList,date);
				mScheduleListAdapter.getListView(schedulelist);
				OnItemClickListener itemClick=new OnItemClickListener(){
      	            @Override
      	            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long arg) {
      	            	final Dialog mItemDialog = new Dialog(HomeActivity.this,R.style.MyDialog);
      	            	mItemDialog.setContentView(R.layout.dialog_list_item);
      	            	Button btn_delete = (Button)mItemDialog.findViewById(R.id.btn_delete);
      	            	Button btn_share = (Button)mItemDialog.findViewById(R.id.btn_share);
      	            	Button btn_edit = (Button)mItemDialog.findViewById(R.id.btn_edit);
      	            	Button btn_cancel = (Button)mItemDialog.findViewById(R.id.btn_cancel);
      	            	OnClickListener dialogClick=new OnClickListener(){
      	          		@Override
      	                  public void onClick(View v) {
      	                      synchronized(HomeActivity.this)
      	                      {
      	                      	switch(v.getId()){
      	          	                 case R.id.btn_delete:
	      	          	                	int keyId = mScheduleListAdapter.getListkeyId(position);
	      									mCalendarDatabaseUtility.deleteData(keyId);
	      									mItemDialog.dismiss();
	      									Message msg = new Message();
	      			                	 	msg.what = UPDATE_CAL;
	      						            mHandler.sendMessage(msg);
      	          	          	  			break;
      	          	                 case R.id.btn_share:
      	          	                	 	mItemDialog.dismiss();
      	          	                	 	shareDialog(mScheduleListAdapter.getListString(position));
      	          	          	  			break;
      	          	                 case R.id.btn_edit:
	      	          	                	int KeyId = mScheduleListAdapter.getListkeyId(position);
//	      									mCalendarDatabaseUtility.deleteData(KeyId);
	      									mItemDialog.dismiss();
	      									if(mDialog!=null)
	      									{
	      										time = mScheduleListAdapter.getListInfo(position, "time");
	      										edtTime.setText(time);
	      										edtContent.setText(mScheduleListAdapter.getListInfo(position, "message"));
	      										SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	      					            		try {
	      					            			day = sdf.parse(mScheduleListAdapter.getListInfo(position, "date"));
	      					            		} catch (ParseException e) {
	      					            			// TODO Auto-generated catch block
	      					            			e.printStackTrace();
	      					            			Toast.makeText(HomeActivity.this,"Edit failed",Toast.LENGTH_SHORT).show();
	      					            		}
	      					            		showAlarmIcon();
	      					            		btn_keyid.setText(String.valueOf(KeyId));
	      										mDialog.show();
	      									}
      	          	          	  			break;
      	          	                 case R.id.btn_cancel:
      	          	                	 	mItemDialog.dismiss();
      	          	          	  			break;
      	                      	}
      	                      }
      	                  }//End of onclick
      	              };
      	              btn_delete.setOnClickListener(dialogClick);
      	              btn_share.setOnClickListener(dialogClick);
      	              btn_edit.setOnClickListener(dialogClick);
      	              btn_cancel.setOnClickListener(dialogClick);
      	              TextView list_content = (TextView)mItemDialog.findViewById(R.id.list_content);
      	              list_content.setText(mScheduleListAdapter.getListString(position));
      	              mItemDialog.show();     	            	
    	  	 		}
      	        };
      	      schedulelist.setOnItemClickListener(itemClick);
			}

			@Override
			public void onDateUnselected(Date date) {
				// TODO Auto-generated method stub
				MyLog.Log(DEBUGTAG, "unselect date is "+date, 1);
			}
	    });
  }
  
  private void shareDialog(String shareText) {
	   Intent shareIntent = new Intent();  
	   shareIntent.setAction(Intent.ACTION_SEND);  
	   shareIntent.setType("text/plain"); //文字檔類型  
	   shareIntent.putExtra(Intent.EXTRA_TEXT, shareText); //傳送文字  
	   shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);  
	   //shareIntent.setType("image/png"); //圖片png檔類型  
	  // shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri); //傳送圖片  
	   startActivity(Intent.createChooser(shareIntent, "分享"));  
	 } 
  
  private Date getFirstMonthDay(Calendar calendar) {
	  calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
      return calendar.getTime();
      }
  
  private Date getLastMonthDay(Calendar calendar) {
      calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
      calendar.add(Calendar.DATE, 1);
      return calendar.getTime();
      }
  
  private Calendar getMonth(int number) {
	  thisMonth = Calendar.getInstance();
	  thisMonth.add(Calendar.MONTH, number);
      return thisMonth;
      }
  
  private void showLastMonth() {
	  month_number = month_number-1;
	  Calendar last_calendar = getMonth(month_number);
	  Date minDay = getFirstMonthDay(last_calendar);
      Date maxDay = getLastMonthDay(last_calendar);
      showCalendar(minDay,maxDay);
      list_layout.setVisibility(View.GONE);
      }
  
  private void showNextMonth() {
	  month_number = month_number+1;
  	  Calendar next_calendar = getMonth(month_number);
  	  Date minDay = getFirstMonthDay(next_calendar);
      Date maxDay = getLastMonthDay(next_calendar);
      showCalendar(minDay,maxDay);
      list_layout.setVisibility(View.GONE);
      }
  
  private void showCalendar(Date min, Date max){
	  calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
	  List<Date> dateList_temp = new ArrayList<Date>();
	  for(int i=0;i<dateList.size();i++)
	  {
		  MyLog.Log(DEBUGTAG, "dateList.get(i).getMonth() "+dateList.get(i).getMonth(), 1);
		  if(min.getMonth()==dateList.get(i).getMonth() && min.getYear()==dateList.get(i).getYear())
		  {
			  MyLog.Log(DEBUGTAG, "same", 1);
			  dateList_temp.add(dateList.get(i));
		  }
	  }
	  calendar.init(min, max)
        .inMode(SelectionMode.SINGLE)
        .withHighlightedDates(dateList_temp);
  }
  
  final Runnable setClockCheck = new Runnable() {
      public void run() {
      		if (!setClock)
      		{
      			AlarmClock mAlarmClock = new AlarmClock();
      	    	mAlarmClock.setAlarmClock(HomeActivity.this);
      		}
      		mHandler.postDelayed(setClockCheck, 30000);
      	}
      };
      
      @Override
      public void finish() {
          //super.finish();
          moveTaskToBack(true); //no onDestroy
      }  
  

  
}