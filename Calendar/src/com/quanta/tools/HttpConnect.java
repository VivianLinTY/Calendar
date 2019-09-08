package com.quanta.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.quanta.calendar.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpConnect {
	
	private static final String DEBUGTAG = "HttpConnect";
	private final static int HTTP_TIMEOUT = 10000;
	
	public static class httpConnectGET extends AsyncTask<Object,Object,Object>{
		Context context;
		Handler mHandler;
		String url;
		public httpConnectGET(Context context, Handler mHandler, String url){
			this.context=context;
			this.mHandler=mHandler;
			this.url=url;
		}
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			HttpURLConnection con = null;
	    	try{
	    		MyLog.Log(DEBUGTAG,"url:"+url,1);
	    		
	            con = (HttpURLConnection) new URL(url).openConnection();
	            con.setConnectTimeout(HTTP_TIMEOUT);
	            con.setReadTimeout(HTTP_TIMEOUT);
	            con.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
	            con.setRequestProperty("Connection", "close");
	            con.connect();
	            MyLog.Log(DEBUGTAG,"getResponseCode is "+con.getResponseCode(),1);
	            if(con.getResponseCode() == 200)
	            {
	            	InputStream instream = con.getInputStream();
	            	String input = convertStreamToString(instream);
	            	MyLog.Log(DEBUGTAG,"input is "+input,1);
		            JSONObject message= new JSONObject(input);
		            Bundle bundle = new Bundle();
		            bundle.putString(StringPool.TAG_JSONObject_USERNAME, message.getString(StringPool.TAG_JSONObject_USERNAME));
		            bundle.putString(StringPool.TAG_JSONObject_USERID, message.getString(StringPool.TAG_JSONObject_USERID));
	            	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.LOGIN_SUCCESS;
					response_msg.setData(bundle);
					mHandler.sendMessage(response_msg);
	            }
	            else
	            {
	            	String message = context.getString(R.string.account_check);
	            	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.LOGIN_FAIL;
					mHandler.sendMessage(response_msg);
	            }
	    	}catch(Exception ex){
	    		ex.printStackTrace();
	    		MyLog.Log(DEBUGTAG,"ex:"+ex,2);
	    		String message = context.getString(R.string.account_check);
	        	Message response_msg = mHandler.obtainMessage(1, message);
				response_msg.what = IntegerPool.LOGIN_FAIL;
				mHandler.sendMessage(response_msg);
	    	}finally {
	    		con.disconnect();
	    		con = null;
	    	}
	    	return null;
		}
	}
	
	public static class httpConnectPOST extends AsyncTask<Object,Object,Object>{
		Context context;
		Handler mHandler;
		String url;
		public httpConnectPOST(Context context, Handler mHandler, String url){
			this.context=context;
			this.mHandler=mHandler;
			this.url=url;
		}
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			HttpURLConnection con = null;
	    	try{
	    		MyLog.Log(DEBUGTAG,"url:"+url,1);
	    		
	            con = (HttpURLConnection) new URL(url).openConnection();
	            con.setConnectTimeout(HTTP_TIMEOUT);
	            con.setReadTimeout(HTTP_TIMEOUT);
	            con.setRequestProperty("Content-Type","text/xml;   charset=utf-8");
	            con.setRequestProperty("Connection", "close");
	            con.setDoOutput(true);
	            con.setDoInput(true);
	            con.setRequestMethod("POST");
	            con.setUseCaches(false);
	            con.connect();
	            MyLog.Log(DEBUGTAG,"getResponseCode is "+con.getResponseCode(),1);
	            if(con.getResponseCode() == 200)
	            {
	            	InputStream instream = con.getInputStream();
	            	String input = convertStreamToString(instream);
	            	MyLog.Log(DEBUGTAG,"input is "+input,1);
		            JSONObject message= new JSONObject(input);
		            Bundle bundle = new Bundle();
		            bundle.putString(StringPool.TAG_JSONObject_USERNAME, message.getString(StringPool.TAG_JSONObject_USERNAME));
		            bundle.putString(StringPool.TAG_JSONObject_USERID, message.getString(StringPool.TAG_JSONObject_USERID));
	            	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.LOGIN_SUCCESS;
					response_msg.setData(bundle);
					mHandler.sendMessage(response_msg);
	            }
	            else
	            {
	            	String message = context.getString(R.string.account_dupicate);
	            	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.LOGIN_FAIL;
					mHandler.sendMessage(response_msg);
	            }
	    	}catch(Exception ex){
	    		ex.printStackTrace();
	    		MyLog.Log(DEBUGTAG,"ex:"+ex,2);
	    		String message = context.getString(R.string.account_check);
	        	Message response_msg = mHandler.obtainMessage(1, message);
				response_msg.what = IntegerPool.LOGIN_FAIL;
				mHandler.sendMessage(response_msg);
	    	}finally {
	    		con.disconnect();
	    		con = null;
	    	}
	    	return null;
		}
	}
	
	public static class httpConnectJsonPOST extends AsyncTask<Object,Object,Object>{
		Context context;
		Handler mHandler;
		String url;
		JSONArray jsonArray;
		public httpConnectJsonPOST(Context context, Handler mHandler, String url, JSONArray jsonArray){
			this.context=context;
			this.mHandler=mHandler;
			this.url=url;
			this.jsonArray=jsonArray;
		}
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			try {
			    StringEntity se = new StringEntity(jsonArray.toString());
			    httpPost.setEntity(se);
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");
			    HttpResponse response = httpClient.execute(httpPost, httpContext); //execute your request and parse response
			    HttpEntity entity = response.getEntity();
			    MyLog.Log(DEBUGTAG,"entity:"+entity,1);
			    String jsonString = EntityUtils.toString(entity); //if response in JSON format
			    MyLog.Log(DEBUGTAG,"jsonString:"+jsonString,1);
			    if(jsonString!=null)
	            {
			    	String message = "";
	            	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.GET_MOBILE_SUCCESS;
//					response_msg.setData(bundle);
					mHandler.sendMessage(response_msg);
	            }
	            else
	            {
	            	String message = "";
		        	Message response_msg = mHandler.obtainMessage(1, message);
					response_msg.what = IntegerPool.GET_MOBILE_FAIL;
					mHandler.sendMessage(response_msg);
	            }
			} catch (Exception e) {
			    e.printStackTrace();
			    MyLog.Log(DEBUGTAG,"ex:"+e,2);
	    		String message = "";
	        	Message response_msg = mHandler.obtainMessage(1, message);
				response_msg.what = IntegerPool.GET_PHONEBOOK_FAIL;
				mHandler.sendMessage(response_msg);
			}
	    	return null;
		}
	}
	
	private static String convertStreamToString(InputStream is){
		BufferedReader reader =new BufferedReader(new InputStreamReader(is));
		StringBuilder sb =new StringBuilder();
		String line =null;
		try{
			while ((line=reader.readLine())!=null){
				sb.append(line+"\n");
			}
		}catch(IOException e){
			MyLog.Log(DEBUGTAG,"convertStreamToString:"+e,2);
			e.printStackTrace();
		}finally{
			try{
				is.close();
				reader.close();
			}catch (IOException e){
				MyLog.Log(DEBUGTAG,"convertStreamToString:"+e,2);
				e.printStackTrace();
			}
		}
    	return sb.toString();
    }
}