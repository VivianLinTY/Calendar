package com.quanta.phonebook;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanta.calendar.R;
import com.quanta.tools.MyLog;
import com.quanta.tools.StatusRecords;

public class MobileContactsAdapter extends BaseAdapter{
	
	private static final String DEBUGTAG = "PhonebookListAdapter";
	private Context mContext;
	MobileContactsAdapter mMobileContactsAdapter;
	private Cursor cursor;
	public JSONArray phonebooklist;
	
	
	public MobileContactsAdapter(Context context) 
    {
		mContext = context;
		cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null, null, null);
        mMobileContactsAdapter = this;
        phonebooklist = new JSONArray();
    }

    public int getCount() {
    	for(int i=0;i<cursor.getCount();i++)
    	{
    		cursor.moveToPosition(i);
	    	String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "");
	    	phonebooklist.put(number);
    	}
    	return cursor.getCount();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	cursor.moveToPosition(position);
    	String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "");
    	String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
    	boolean checkbox = false;
    	for(int i=0; i<StatusRecords.checkbox.size();i++)
    	{
    		if(StatusRecords.checkbox.get(i)==position)
    			checkbox=true;
    	}
        TableView tv = null;
        tv = new TableView(mContext,name,number,checkbox);
        tv.setTag(number);
        return tv;
    }
 
    private class TableView extends LinearLayout 
    {
           public TableView(final Context context, final String name, final String number, final boolean checkbox) 
           {
               super(context);
               this.setOrientation(HORIZONTAL);
               this.setPadding(0, 20, 0, 20);
               LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
               margin.setMargins(20, 0, 0, 0);
               ImageView checkboxView = new ImageView(context);
               if(checkbox)
            	   checkboxView.setBackgroundResource(R.drawable.checkbox_checked);
               else
            	   checkboxView.setBackgroundResource(R.drawable.checkbox);
               addView(checkboxView,margin);
               TextView Name = new TextView(context);
               Name.setText(name);
               Name.setTextAppearance(context, R.style.content_message);
               addView(Name,margin);
           }    
    }
	 public View getMobileContactList(ListView view)
	 {
		 MyLog.Log(DEBUGTAG, "=>getMobileContactList", 1);
		 view.setAdapter(mMobileContactsAdapter);
		 mMobileContactsAdapter.notifyDataSetChanged();
		 return view;
	 }
	 
	 public void setClickItem(int position,ListView view,boolean click)
	 {
		 MyLog.Log(DEBUGTAG, "=>setClickItem", 1);
		 if(click)
			 StatusRecords.checkbox.add(position);
		 else
			 StatusRecords.checkbox.remove(position);
		 mMobileContactsAdapter.notifyDataSetChanged();
	 }
}
