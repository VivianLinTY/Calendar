package com.quanta.phonebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.quanta.calendar.CalendarViewActivity;
import com.quanta.calendar.HomeActivity;
import com.quanta.calendar.R;
import com.quanta.main.AbstractMainActivity;
import com.quanta.tools.HttpConnect.httpConnectGET;
import com.quanta.tools.IntegerPool;
import com.quanta.tools.MyLog;
import com.quanta.tools.StatusRecords;
import com.quanta.tools.StringPool;

public class CalendarListActivity extends AbstractMainActivity{
	
	private static final String DEBUGTAG = "CalendarListActivity";
	ListView mylistview,listview_dialog;
	TextView title_tv,dialog_title_tv;
	ImageButton btn_add_contact,btn_save,Summary_Btn,Phonebook_Btn;
	LinearLayout add_Contact_layout;
	Dialog mDialog = null;
	ProgressDialog mProgressDialog;
	PhonebookListAdapter mPhonebookListAdapter;
	
	 private Handler mHandler = new Handler()
	    {
	        public void handleMessage(Message msg)
	        {
	        	if(mProgressDialog != null && mProgressDialog.isShowing()){
	            	 
	            	 mProgressDialog.dismiss();
	            }
	          switch(msg.what)
	          {
	          		case IntegerPool.GET_CALENDAR_SUCCESS:
	          			MyLog.Log(DEBUGTAG, "Get calendar success!!", 1);
	          			setMyListView();
	          			break;
	          		case IntegerPool.GET_CALENDAR_FAIL:
	          			MyLog.Log(DEBUGTAG, "Get calendar fail!!", 1);
	          			setMyListView();
	          			break;                     
	          }
	        }
	    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_phonebook);
		mProgressDialog = new ProgressDialog(CalendarListActivity.this);
		mProgressDialog.setMessage(getString(R.string.loading));
		//For test
		PhonebookInfo temp = new PhonebookInfo();
		temp.setName("Vivian");
		temp.setRole(IntegerPool.TAG_ROLE_CALENDAR);
		temp.setPhoneNumber("00000");
		temp.setImage("Vivian");
		temp.setUserID("2");
		StatusRecords.Calendar_List.add(temp);
		StatusRecords.Calendar_List.add(temp);
		StatusRecords.Calendar_List.add(temp);
		PhonebookInfo temp2 = new PhonebookInfo();
		temp2.setName("Filber");
		temp2.setRole(IntegerPool.TAG_ROLE_CONTACT);
		temp2.setPhoneNumber("00000");
		temp2.setImage("Vivian");
		temp2.setUserID("3");
		StatusRecords.Calendar_List.add(temp2);
		StatusRecords.Calendar_List.add(temp2);
		mPhonebookListAdapter = new PhonebookListAdapter(CalendarListActivity.this,StatusRecords.Calendar_List,IntegerPool.TAG_ROLE_CALENDAR);
		title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText(getString(R.string.calendar_list));
		mylistview = (ListView) findViewById(R.id.mylistview);
		Phonebook_Btn = (ImageButton) findViewById(R.id.Phonebook);
		Summary_Btn = (ImageButton) findViewById(R.id.Summary);
		btn_add_contact = (ImageButton) findViewById(R.id.btn_add_contact);
		mDialog = new Dialog(CalendarListActivity.this,R.style.MyDialog);
	    mDialog.setContentView(R.layout.dialog_contacts);
	    dialog_title_tv = (TextView) mDialog.findViewById(R.id.dialog_title_tv);
	    dialog_title_tv.setText(getString(R.string.add_calendar));
	    add_Contact_layout = (LinearLayout) mDialog.findViewById(R.id.add_Contact_layout);
		add_Contact_layout.setVisibility(View.GONE);
	    listview_dialog = (ListView) mDialog.findViewById(R.id.listview_dialog);
	    btn_save = (ImageButton) mDialog.findViewById(R.id.btn_save);
	    OnClickListener theclick=new OnClickListener(){
            @Override
            public void onClick(View v) {
                synchronized(CalendarListActivity.this)
                {
                	switch(v.getId()){
  	                 case R.id.Summary:
  	                    	Intent profile = new Intent();
  	                    	profile.setClass(CalendarListActivity.this, HomeActivity.class);
  	        	  			startActivity(profile);
  	        	  			finish();
  	          	  			break;
  	                 case R.id.Phonebook:
  	                    	Intent careplan = new Intent();
  	                    	careplan.setClass(CalendarListActivity.this, PhonebookActivity.class);
  	        	  			startActivity(careplan);
  	        	  			finish();
  	          	  			break;
  	                 case R.id.btn_add_contact:
  	                	 	mDialog.show();
  	          	  			break;
  	                 case R.id.btn_save:
  	                	 	mDialog.dismiss();
  	          	  			break;
                	}
                }
            }//End of onclick
        };
		btn_add_contact.setOnClickListener(theclick);
	    btn_save.setOnClickListener(theclick);
	    Summary_Btn.setOnClickListener(theclick);
	    Phonebook_Btn.setOnClickListener(theclick);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mProgressDialog.show();
  	 	final String get_url=StatusRecords.ServerUrl+"";
  	 	Thread thd=new Thread(new Runnable() {
  	 		@Override
    			public void run() {
    				// TODO Auto-generated method stub
  	 			httpConnectGET httpConnectGet = new httpConnectGET(CalendarListActivity.this, mHandler, get_url);
    				try {
    				httpConnectGet.execute();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
  	 	});
  	 	mHandler.post(thd);
	}
	
	private void setMyListView()
	{
		mPhonebookListAdapter.getListView(mylistview);
		mylistview.setOnItemClickListener(new OnItemClickListener(){
  	 		@Override
  	 		public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
  	 			String calendarID = (String)view.getTag();
  	 			Intent calendarView = new Intent();
  	 			calendarView.putExtra(StringPool.TAG_BUNDLE_USERID, calendarID);
  	 			calendarView.setClass(CalendarListActivity.this, CalendarViewActivity.class);
  	 			startActivity(calendarView);
  	 		}
  	 	});
	}

}
