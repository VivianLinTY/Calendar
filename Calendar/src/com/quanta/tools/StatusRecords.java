package com.quanta.tools;

import java.util.ArrayList;
import java.util.List;

import com.quanta.phonebook.PhonebookInfo;

public class StatusRecords {
	
	public static String UserID = "";
	public static String UserName= "";
	public static String UserAccount = "";
	public static String UserPassword= "";
	public static String UserNumber= "";
	public static String ServerUrl= "http://192.168.6.119:8080/SharedCalendar/UserInfoServlet/";
	
	public static List<Integer> checkbox = new ArrayList<Integer>();
	public static ArrayList<PhonebookInfo> Calendar_List = new ArrayList<PhonebookInfo>();
	public static ArrayList<PhonebookInfo> Phonebook_List = new ArrayList<PhonebookInfo>();
}
