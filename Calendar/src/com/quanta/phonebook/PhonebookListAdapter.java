package com.quanta.phonebook;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanta.calendar.R;
import com.quanta.tools.MyLog;

public class PhonebookListAdapter extends BaseAdapter{
	
	private static final String DEBUGTAG = "PhonebookListAdapter";
	private Context mContext;
	PhonebookListAdapter mPhonebookListAdapter;
	ArrayList<PhonebookInfo> mList = null;
	
	
	public PhonebookListAdapter(Context context,ArrayList<PhonebookInfo> list, int role) 
    {
		MyLog.Log(DEBUGTAG, "list size="+list.size(), 1);
		mList = new ArrayList<PhonebookInfo>();
        mContext = context;
        for(int i=0;i<list.size();i++)
        {
        	if(list.get(i).getRole()==role)
        		mList.add(list.get(i));
        }
        mPhonebookListAdapter = this;
    }

    public int getCount() {
    	MyLog.Log(DEBUGTAG, "size="+mList.size(), 1);
        return mList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TableView tv = null;
        String userID = mList.get(position).getUserID();
        tv = new TableView(mContext,
        		mList.get(position).getImage(),
        		mList.get(position).getName(),
        		mList.get(position).getPhoneNumber());
        tv.setTag(userID);
        return tv;
    }
 
    private class TableView extends LinearLayout 
    {
           public TableView(final Context context, final String image, final String name, final String number) 
           {
               super(context);
               this.setOrientation(HORIZONTAL);
               this.setPadding(0, 20, 0, 20);
               LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
               margin.setMargins(20, 0, 0, 0);
               TextView Time = new TextView(context);
               Time.setText(image);
               Time.setTextAppearance(context, R.style.content_time);
               addView(Time,margin);
               TextView Name = new TextView(context);
               Name.setText(name);
               Name.setTextAppearance(context, R.style.content_message);
               addView(Name,margin);
           }    
    }
	 public View getListView(ListView view)
	 {
		 MyLog.Log(DEBUGTAG, "=>getListView", 1);
		 if(mList != null)
         {
			 view.setAdapter(mPhonebookListAdapter);
			 mPhonebookListAdapter.notifyDataSetChanged();
         }
		 return view;
	 }
}
