package com.quanta.calendar;

import java.util.ArrayList;

public class ScheduleInfoList {
	String date = null;
	public ArrayList<ScheduleInfo> ScheduleInfoList = new ArrayList<ScheduleInfo>();

	public static class ScheduleInfo {
		String Time = "";
		int KeyId = 0;
		String Message = "";
		
		public void setTime(String Time)
		{
			this.Time = Time;
		}
		public void setKeyId(int KeyId)
		{
			this.KeyId = KeyId;
		}
		public void setMessage(String Message)
		{
			this.Message = Message;
		}
		public String getTime()
		{
			return this.Time;
		}
		public int getKeyId()
		{
			return this.KeyId;
		}
		public String getMessage()
		{
			return this.Message;
		}
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	public void setScheduleInfoList(ArrayList<ScheduleInfo> scheduleInfoList)
	{
		this.ScheduleInfoList = scheduleInfoList;
	}
	public String getDate()
	{
		return this.date;
	}
	public ArrayList<ScheduleInfo> getScheduleInfoList()
	{
		return this.ScheduleInfoList;
	}
}