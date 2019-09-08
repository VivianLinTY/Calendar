package com.quanta.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanta.calendar.HomeActivity;
import com.quanta.calendar.R;
import com.quanta.main.AbstractMainActivity;
import com.quanta.tools.HttpConnect.httpConnectGET;
import com.quanta.tools.HttpConnect.httpConnectPOST;
import com.quanta.tools.IntegerPool;
import com.quanta.tools.MyLog;
import com.quanta.tools.StatusRecords;
import com.quanta.tools.StringPool;
import com.quanta.user.UserManager;

public class LoginActivity extends AbstractMainActivity{
    
    private static final String DEBUGTAG = "LoginActivity";
    private boolean clickable = true, register = false;
    String provisionServer = "https://192.168.8.4/thc/provision";
    public static final String MESSAGE = "";
    TelephonyManager telManager;
    String number,account,password,username,userid;
    private EditText mEdtAccountName,mEdtPassWord,mEdtPassWordCon,mEdtUserName;
    private TextView mTxtMessage,empty;
    private RelativeLayout TableRow04,TableRow05;
    private Button saveBtn,NewButton;
    private String login_url;
    ProgressDialog mProgressDialog;
    
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
        	if(mProgressDialog != null && mProgressDialog.isShowing()){
            	 
            	 mProgressDialog.dismiss();
            }
          switch(msg.what)
          {
          		case IntegerPool.LOGIN_SUCCESS:
          			MyLog.Log(DEBUGTAG, "Login success!!", 1);
      				Bundle message_success = msg.getData();
					username = message_success.getString(StringPool.TAG_JSONObject_USERNAME);
					userid = message_success.getString(StringPool.TAG_JSONObject_USERID);
          			writeSharePerence();
          			break;
          		case IntegerPool.LOGIN_FAIL:
          			MyLog.Log(DEBUGTAG, "Login failed!!", 1);
          			String message_fail = (String)msg.obj;
          			mTxtMessage.setText(message_fail);
          			break;                     
          }
        }
    };
    
    private void writeSharePerence() 
    {
    	MyLog.Log(DEBUGTAG, "username is "+username+"   , userid is "+userid, 1);
    	UserManager mUserManager = new UserManager();
    	mUserManager.setUserPreference(LoginActivity.this, number, account, password, username, userid);
		mUserManager.initUserInfo(LoginActivity.this);
		Intent calendar = new Intent();
		calendar.setClass(LoginActivity.this, HomeActivity.class);
		startActivity(calendar);
		finish();
    }

    //touch space and hide soft keyboard
    public void CloseKeyBoard() {
    	mEdtAccountName.clearFocus();
    	mEdtPassWord.clearFocus();
    	mEdtPassWordCon.clearFocus();
    	mEdtUserName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEdtAccountName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mEdtPassWord.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mEdtPassWordCon.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mEdtUserName.getWindowToken(), 0);
    }
    
    //touch space and hide soft keyboard
    public boolean onTouchEvent(MotionEvent event) {
        CloseKeyBoard();
        return super.onTouchEvent(event);
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        setContentView(R.layout.layout_login_setting);
        mProgressDialog = new ProgressDialog(LoginActivity.this);
		mProgressDialog.setMessage(getString(R.string.loading));
        telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mEdtAccountName = (EditText) findViewById(R.id.mEdtAccountName);
        mEdtUserName = (EditText) findViewById(R.id.mEdtUserName);
        mEdtPassWord = (EditText) findViewById(R.id.mEdtPassWord);
        mEdtPassWordCon = (EditText) findViewById(R.id.mEdtPassWordCon);
        mTxtMessage = (TextView) findViewById(R.id.mTxtMessage);
        TableRow04 = (RelativeLayout) findViewById(R.id.TableRow04);
        TableRow05 = (RelativeLayout) findViewById(R.id.TableRow05);
        saveBtn = (Button)findViewById(R.id.saveButton);
        NewButton = (Button)findViewById(R.id.NewButton);
        empty = (TextView) findViewById(R.id.empty);
        OnKeyListener KeyListener = new OnKeyListener() 
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        };
        mEdtAccountName.setOnKeyListener(KeyListener);
        mEdtUserName.setOnKeyListener(KeyListener);
        mEdtPassWord.setOnKeyListener(KeyListener);
        mEdtPassWordCon.setOnKeyListener(KeyListener);
        if(bundle != null){
        	  mTxtMessage.setText(bundle.getString(MESSAGE));
        } else{
        	 mTxtMessage.setText("");
        }
           
        saveBtn.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v){
            	if(clickable)
            	{
            		account = mEdtAccountName.getText().toString();
            		password = mEdtPassWord.getText().toString();
            		String password_confirmed = mEdtPassWordCon.getText().toString();
            		if(telManager!=null)
            			number = telManager.getLine1Number();
            		if(password_confirmed.contentEquals(password) || !register)
            		{
	            		if(account!=null && password!=null && !account.contentEquals("") && !password.contentEquals(""))
	            		{
	            			if(account.startsWith("+"))
	            				account = "0"+account.substring(4);
	            			mProgressDialog.show();
	            			Thread thd = null;
	            			if(!register)
	            			{
	            				login_url = StatusRecords.ServerUrl+"login?PHONE="+account+"&PASSWORD="+password;
	            				thd=new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										httpConnectGET httpGet = new httpConnectGET(LoginActivity.this, mHandler, login_url);
										try {
											httpGet.execute();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
	            			}
	            			else
	            			{
	            				login_url = StatusRecords.ServerUrl+"USERINFO?PHONE="+account+"&PASSWORD="+password+"&USERNAME="+username;
	            				thd=new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										httpConnectPOST httpPost = new httpConnectPOST(LoginActivity.this, mHandler, login_url);
										try {
											httpPost.execute();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
	            			}
	            			mHandler.post(thd);
	            		}
	            		else
	            			mTxtMessage.setText(getString(R.string.account_check));
            		}
            		else
            			mTxtMessage.setText(getString(R.string.password_check));
	            }
            }
        });
        NewButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v){
            	if(clickable)
            	{
            		TableRow04.setVisibility(View.VISIBLE);
            		TableRow05.setVisibility(View.VISIBLE);
            		NewButton.setVisibility(View.GONE);
            		empty.setVisibility(View.GONE);
            		mTxtMessage.setText("");
            		saveBtn.setText(getString(R.string.btn_register));
            		mEdtAccountName.setHint(getString(R.string.new_login_account));
            		mEdtPassWord.setHint(getString(R.string.new_login_password));
            		register = true;
	            }
            }
        });
    }
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		 if (keyCode == KeyEvent.KEYCODE_BACK ) 
	        {
	        	System.exit(0);
	        }	        
		 return super.onKeyDown(keyCode, event);
	 }

}
