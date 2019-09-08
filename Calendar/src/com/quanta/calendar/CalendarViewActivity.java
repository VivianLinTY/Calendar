package com.quanta.calendar;

import com.quanta.calendar.ScheduleInfoList.ScheduleInfo;
import com.quanta.main.AbstractMainActivity;
import com.quanta.tools.CalendarDatabaseUtility;
import com.quanta.tools.MyLog;
import com.quanta.tools.StringPool;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarViewActivity extends AbstractMainActivity {
  private static final String DEBUGTAG = "CalendarViewActivity";
  private CalendarPickerView calendar;
  private ListView schedulelist;
  private ImageButton btn_last_month,btn_next_month,btn_add_event,btn_save,timePickerButton,btn_back;
  EditText edtContent,edtTime;
  private LinearLayout list_layout;
  int month_number=0;
  private Calendar thisMonth;
  float down_x=0,up_x=0;
  Dialog mDialog = null;
  private ArrayList<ScheduleInfoList> mScheduleList = null;
  List<Date> dateList = null;
  OnTimeSetListener myTimeSetListener;
  String calendarID;
  String time;
  Date day;
  CalendarDatabaseUtility mCalendarDatabaseUtility;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_calendar);
    calendarID = getIntent().getStringExtra(StringPool.TAG_BUNDLE_USERID);
    dateList = new ArrayList<Date>();
    if (mCalendarDatabaseUtility==null)
    {
    	mCalendarDatabaseUtility = new CalendarDatabaseUtility(this);
    	mCalendarDatabaseUtility.open(true);
    }
    btn_last_month = (ImageButton) findViewById(R.id.btn_last_month);
    btn_next_month = (ImageButton) findViewById(R.id.btn_next_month);
    btn_add_event = (ImageButton) findViewById(R.id.btn_add_event);
    list_layout = (LinearLayout) findViewById(R.id.list_layout);
    schedulelist = (ListView) findViewById(R.id.schedulelist);
    btn_back = (ImageButton) findViewById(R.id.btn_back);
    thisMonth = Calendar.getInstance();
    mDialog = new Dialog(CalendarViewActivity.this,R.style.MyDialog);
    mDialog.setContentView(R.layout.dialog_event);
    timePickerButton = (ImageButton)mDialog.findViewById(R.id.timePickerButton);
    edtTime = (EditText)mDialog.findViewById(R.id.edtTime);
    edtContent = (EditText)mDialog.findViewById(R.id.edtContent);
    btn_save = (ImageButton)mDialog.findViewById(R.id.btn_save);
    myTimeSetListener= new TimePickerDialog.OnTimeSetListener(){
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		time = hourOfDay+":"+minute;
		edtTime.setText(time);
		}
	};
    OnClickListener theclick=new OnClickListener(){
		@Override
        public void onClick(View v) {
            synchronized(CalendarViewActivity.this)
            {
            	switch(v.getId()){
            		case R.id.btn_back:
		            		finish();
		      	  			break;
	                 case R.id.btn_last_month:
	                	 	showLastMonth();
	                	 	list_layout.setVisibility(View.GONE);
	          	  			break;
	                 case R.id.btn_next_month:
	                	 	showNextMonth();
	                	 	list_layout.setVisibility(View.GONE);
	          	  			break;
	                 case R.id.btn_add_event:
	                	 	mDialog.show();
	          	  			break;
	                 case R.id.btn_save:
	                	 	if(day!=null && time!=null && !time.contentEquals(""))
	                	    {
		                	 	mCalendarDatabaseUtility.addData(day, time, edtContent.getText().toString(), false);
		                	 	mDialog.dismiss();
	                	    }
	                	 	else
	                	 		mDialog.dismiss();
	          	  			break;
	                 case R.id.timePickerButton:
	                	 	Calendar c = Calendar.getInstance();
	                	    int myHour = c.get(Calendar.HOUR_OF_DAY);
	                	    int myMinute = c.get(Calendar.MINUTE);
	                	    Dialog timePicker = new TimePickerDialog(CalendarViewActivity.this,myTimeSetListener,myHour, myMinute, false);
	                	    timePicker.show();
	                	 	break;
            	}
            }
        }//End of onclick
    };
    btn_add_event.setOnClickListener(theclick);
	btn_save.setOnClickListener(theclick);
	timePickerButton.setOnClickListener(theclick);
	btn_last_month.setOnClickListener(theclick);
	btn_next_month.setOnClickListener(theclick);
	btn_back.setOnClickListener(theclick);
    mScheduleList = new ArrayList<ScheduleInfoList>();
    
//    //For test
//    String test_date1 = "2014/12/05";
//    String test_date2 = "2014/12/27";
//    String test_date3 = "2015/01/27";
//    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
//    Date date1=null,date2=null,date3=null;
//	try {
//		date1 = sdFormat.parse(test_date1);
//		date2 = sdFormat.parse(test_date2);
//		date3 = sdFormat.parse(test_date3);
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//    ScheduleInfo info_temp = new ScheduleInfo();
//    info_temp.setName("Vivian");
//    info_temp.setTime("10:13");
//    info_temp.setMessage("sleep");
//	ArrayList<ScheduleInfo> ListInfo = new ArrayList<ScheduleInfo>();
//	ListInfo.add(info_temp);
//	ScheduleInfoList temp = new ScheduleInfoList();
//	temp.setDate(date1);
//	temp.setScheduleInfoList(ListInfo);
//	ScheduleInfoList tem2 = new ScheduleInfoList();
//	tem2.setDate(date2);
//	tem2.setScheduleInfoList(ListInfo);
//	ScheduleInfoList tem3 = new ScheduleInfoList();
//	tem3.setDate(date3);
//	tem3.setScheduleInfoList(ListInfo);
//	mScheduleList.add(temp);
//	mScheduleList.add(temp);
//	mScheduleList.add(temp);
//	mScheduleList.add(tem2);
//	mScheduleList.add(tem2);
//	mScheduleList.add(tem3);
	
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
	
  }
  
  @Override
  public void onResume() {
    super.onResume();
    Date minDay = getFirstMonthDay(thisMonth);
    Date maxDay = getLastMonthDay(thisMonth);
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
			ScheduleListAdapter mScheduleListAdapter = new ScheduleListAdapter(CalendarViewActivity.this,HomeActivity.mScheduleList,date);
			mScheduleListAdapter.getListView(schedulelist);
		}

		@Override
		public void onDateUnselected(Date date) {
			// TODO Auto-generated method stub
			MyLog.Log(DEBUGTAG, "unselect date is "+date, 1);
		}
    });
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
      }
  
  private void showNextMonth() {
	  month_number = month_number+1;
  	  Calendar next_calendar = getMonth(month_number);
  	  Date minDay = getFirstMonthDay(next_calendar);
      Date maxDay = getLastMonthDay(next_calendar);
      showCalendar(minDay,maxDay);
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
  
}