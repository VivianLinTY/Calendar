package com.quanta.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.quanta.tools.MyLog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleListAdapter extends BaseAdapter{
	
	private static final String DEBUGTAG = "ScheduleListAdapter";
	private Context mContext;
	protected ArrayList<ScheduleInfoList> mList = null;
	ScheduleListAdapter mScheduleListAdapter;
	public static ArrayList<ScheduleInfoList> mScheduleList = null;
	
	
	public ScheduleListAdapter(Context context, ArrayList<ScheduleInfoList> list, Date date) 
    {
		mScheduleList = list;
		MyLog.Log(DEBUGTAG,"size:"+mScheduleList.size(),1);
        mContext = context;
        mList = new ArrayList<ScheduleInfoList>();
        for(int i=0;i<mScheduleList.size();i++)
        {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        	String day = sdf.format(date);
        	MyLog.Log(DEBUGTAG, "date="+day+"    getDate="+mScheduleList.get(i).getDate(), 1);
        	if(day.equals(mScheduleList.get(i).getDate()))
        		mList.add(mScheduleList.get(i));
        }
        mList = sortList(mList);
        mScheduleListAdapter = this;
    }
	
	private ArrayList<ScheduleInfoList> sortList(ArrayList<ScheduleInfoList> list)
	{
		Collections.sort(list,
                new Comparator<ScheduleInfoList>() {
                    public int compare(ScheduleInfoList o1, ScheduleInfoList o2) {
                    	if(o1.getScheduleInfoList().get(0).getTime()!=null && o2.getScheduleInfoList().get(0).getTime()!=null)
                    		return o2.getScheduleInfoList().get(0).getTime().compareTo(o1.getScheduleInfoList().get(0).getTime());
                    	else
                    		return 0;
                    }
                });
		return list;
	}

    public int getCount() {
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
        tv = new TableView(mContext,
        		mList.get(position).getScheduleInfoList().get(0).getMessage(),
//        		mList.get(position).getScheduleInfoList().get(0).getName(),
        		mList.get(position).getScheduleInfoList().get(0).getTime());
        return tv;
    }
 
    private class TableView extends LinearLayout 
    {
           public TableView(final Context context,final String message, final String time) 
           {
               super(context);
               this.setOrientation(HORIZONTAL);
               this.setPadding(0, 20, 0, 20);
               LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
	 				   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
               margin.setMargins(20, 0, 0, 0);
               TextView Time = new TextView(context);
               Time.setText(time);
               Time.setTextAppearance(context, R.style.content_time);
               addView(Time,margin);
//               TextView Name = new TextView(context);
//               Name.setText(name);
//               Name.setTextAppearance(context, R.style.content_message);
//               addView(Name,margin);
               TextView Message = new TextView(context);
               Message.setText(message);
               Message.setTextAppearance(context, R.style.content_message);
               addView(Message,margin);
           }    
    }
	 public View getListView(ListView view)
	 {
		 if(mList != null)
         {
			 view.setAdapter(mScheduleListAdapter);
			 mScheduleListAdapter.notifyDataSetChanged();
         }
		 return view;
	 }
	 
	 public String getListString(int position)
	 { 
		 return mList.get(position).getDate()+" "+mList.get(position).getScheduleInfoList().get(0).getTime()
				 +"\n"+mList.get(position).getScheduleInfoList().get(0).getMessage();
	 }
	 
	 public String getListInfo(int position, String info_type)
	 { 
		 if(info_type.contentEquals("date"))
			 return mList.get(position).getDate();
		 else if(info_type.contentEquals("time"))
			 return mList.get(position).getScheduleInfoList().get(0).getTime();
		 else
			 return mList.get(position).getScheduleInfoList().get(0).getMessage();
	 }
	 
	 public int getListkeyId(int position)
	 { 
		 return mList.get(position).getScheduleInfoList().get(0).getKeyId();
	 }
}
