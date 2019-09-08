package com.quanta.phonebook;

public class PhonebookInfo {
	
	int role = -1;
	String name = "";
	String number = "";
	String image = "";
	String userID = "";
	
	public void setName(String name)
	{
		this.name = name;
	}
	public void setPhoneNumber(String number)
	{
		this.number = number;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
	public void setRole(int role)
	{
		this.role = role;
	}
	
	public String getName()
	{
		return this.name;
	}
	public String getPhoneNumber()
	{
		return this.number;
	}
	public String getImage()
	{
		return this.image;
	}
	public String getUserID()
	{
		return this.userID;
	}
	public int getRole()
	{
		return this.role;
	}
}
