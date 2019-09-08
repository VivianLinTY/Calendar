package com.quanta.phonebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.quanta.calendar.HomeActivity;
import com.quanta.calendar.R;
import com.quanta.main.AbstractMainActivity;
import com.quanta.tools.HttpConnect.httpConnectGET;
import com.quanta.tools.HttpConnect.httpConnectJsonPOST;
import com.quanta.tools.IntegerPool;
import com.quanta.tools.MyLog;
import com.quanta.tools.StatusRecords;

public class PhonebookActivity extends AbstractMainActivity{
	
	private static final String DEBUGTAG = "PhonebookActivity";
	ListView mylistview,listview_dialog;
	TextView title_tv,dialog_title_tv;
	ImageButton btn_add_contact,btn_save,Summary_Btn,Phonebook_Btn,Calendar_Btn;
	Dialog mDialog = null;
	List<String> pickList = null;
	int pickListSize=0;
	ProgressDialog mProgressDialog;
	PhonebookListAdapter mPhonebookListAdapter;
	MobileContactsAdapter mMobileContactsAdapter;
	
	 private Handler mHandler = new Handler()
	    {
	        public void handleMessage(Message msg)
	        {
	        	if(mProgressDialog != null && mProgressDialog.isShowing()){
	            	 
	            	 mProgressDialog.dismiss();
	            }
	          switch(msg.what)
	          {
	          		case IntegerPool.GET_PHONEBOOK_SUCCESS:
	          			MyLog.Log(DEBUGTAG, "Get phonebook success!!", 1);
	          			mPhonebookListAdapter.getListView(mylistview);
	          			break;
	          		case IntegerPool.GET_PHONEBOOK_FAIL:
	          			MyLog.Log(DEBUGTAG, "Get phonebook fail!!", 1);
	          			break;
	          		case IntegerPool.GET_MOBILE_SUCCESS:
	          			MyLog.Log(DEBUGTAG, "Get mobile success!!", 1);
	          			mDialog.show();
	          			setMobileList();
	          			break;
	          		case IntegerPool.GET_MOBILE_FAIL:
	          			MyLog.Log(DEBUGTAG, "Get mobile fail!!", 1);
	          			break;
	          }
	        }
	    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_phonebook);
		mProgressDialog = new ProgressDialog(PhonebookActivity.this);
		mProgressDialog.setMessage(getString(R.string.loading));
		mPhonebookListAdapter = new PhonebookListAdapter(PhonebookActivity.this, StatusRecords.Phonebook_List,IntegerPool.TAG_ROLE_CONTACT);
		mMobileContactsAdapter = new MobileContactsAdapter(PhonebookActivity.this);
		pickList = new ArrayList<String>();
		mylistview = (ListView) findViewById(R.id.mylistview);
		Phonebook_Btn = (ImageButton) findViewById(R.id.Phonebook);
		Phonebook_Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_profile_press));
		Summary_Btn = (ImageButton) findViewById(R.id.Summary);
		Calendar_Btn = (ImageButton) findViewById(R.id.Calendar);
		Calendar_Btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_calendar));
		title_tv = (TextView) findViewById(R.id.title_tv);
		title_tv.setText(getString(R.string.my_contacts));
		btn_add_contact = (ImageButton) findViewById(R.id.btn_add_contact);
		mDialog = new Dialog(PhonebookActivity.this,R.style.MyDialog);
	    mDialog.setContentView(R.layout.dialog_contacts);
	    dialog_title_tv = (TextView) mDialog.findViewById(R.id.dialog_title_tv);
	    dialog_title_tv.setText(getString(R.string.add_contacts));
	    listview_dialog = (ListView) mDialog.findViewById(R.id.listview_dialog);
	    btn_save = (ImageButton)mDialog.findViewById(R.id.btn_save);
	    OnClickListener theclick=new OnClickListener(){
	            @Override
	            public void onClick(View v) {
	                synchronized(PhonebookActivity.this)
	                {
	                	switch(v.getId()){
	  	                 case R.id.Summary:
	  	                    	Intent profile = new Intent();
	  	                    	profile.setClass(PhonebookActivity.this, HomeActivity.class);
	  	        	  			startActivity(profile);
	  	        	  			finish();
	  	          	  			break;
	  	                 case R.id.Calendar:
	  	                    	Intent careplan = new Intent();
	  	                    	careplan.setClass(PhonebookActivity.this, CalendarListActivity.class);
	  	        	  			startActivity(careplan);
	  	        	  			finish();
	  	          	  			break;
	  	                 case R.id.btn_add_contact:
	  	                	 	mProgressDialog.show();
	  	                	 	final String post_url=StatusRecords.ServerUrl+"";
	  	                	 	mMobileContactsAdapter.getMobileContactList(listview_dialog);
	  	                	 	Thread thd=new Thread(new Runnable() {
	  	                	 		@Override
		  	              			public void run() {
		  	              				// TODO Auto-generated method stub
		  	              				httpConnectJsonPOST httpJsonPOST = new httpConnectJsonPOST(PhonebookActivity.this, mHandler, post_url, mMobileContactsAdapter.phonebooklist);
		  	              				MyLog.Log(DEBUGTAG, "phonebooklist:"+mMobileContactsAdapter.phonebooklist, 1);
		  	              				try {
		  	              					httpJsonPOST.execute();
		  	              				} catch (Exception e) {
		  	              					// TODO Auto-generated catch block
		  	              					e.printStackTrace();
		  	              				}
		  	              			}
	  	                	 	});
	  	                	 	mHandler.post(thd);
	  	          	  			break;
	  	                 case R.id.btn_save:
	  	                	 	mDialog.dismiss();
	  	                	 	StatusRecords.checkbox.clear();
	  	                	 	pickList.clear();
	  	                	 	pickListSize=0;
	  	          	  			break;
	                	}
	                }
	            }//End of onclick
	        };
		btn_add_contact.setOnClickListener(theclick);
	    btn_save.setOnClickListener(theclick);
	    Summary_Btn.setOnClickListener(theclick);
	    Calendar_Btn.setOnClickListener(theclick);
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
  	 			httpConnectGET httpConnectGet = new httpConnectGET(PhonebookActivity.this, mHandler, get_url);
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
	
	private void setMobileList()
	{
  	 	mMobileContactsAdapter.getMobileContactList(listview_dialog);
  	 	listview_dialog.setOnItemClickListener(new OnItemClickListener(){
  	 		@Override
  	 		public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg) {
  	 			String number = (String)view.getTag();
  	 			if(number.startsWith("+"))
  	 				number = "0"+number.substring(4);
  	 			MyLog.Log(DEBUGTAG, "number="+number, 1);
  	 			if(pickList.size()==0)
  	 			{
  	 				MyLog.Log(DEBUGTAG, "=>add "+number+"  size="+pickList.size(), 1);
					pickList.add(number);
	  	 			pickListSize = pickListSize+1;
	  	 			mMobileContactsAdapter.setClickItem(position,listview_dialog,true);
  	 			}
  	 			else
  	 			{
  	 				int duplicate = -1;
	  	 			for(int i=0;i<pickList.size();i++)
	  	 			{
	  	 				if(number.contentEquals(pickList.get(i)))
	  	 					duplicate = i;
	  	 			}
	  	 			if(duplicate>-1)
	  	 			{
  	 					pickList.remove(duplicate);
  	 					pickListSize = pickListSize-1;
  	 					MyLog.Log(DEBUGTAG, "=>remove "+number+"  size="+pickList.size(), 1);
  	 					mMobileContactsAdapter.setClickItem(position,listview_dialog,false);
  	 				}
  	 				else
  	 				{
  	 					pickList.add(number);
  	 	  	 			pickListSize = pickListSize+1;
  	 	  	 			MyLog.Log(DEBUGTAG, "=>add "+number+"  size="+pickList.size(), 1);
  	 	  	 			mMobileContactsAdapter.setClickItem(position,listview_dialog,true);
  	 				}
  	 			}
  	 		}
  	 	});
	}
}
