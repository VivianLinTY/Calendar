package com.quanta.calendar;

import com.quanta.tools.MyLog;
import com.quanta.tools.Ringtone;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayReceiver extends BroadcastReceiver
{
	private Dialog dialog = new Dialog(HomeActivity.HomeContext,R.style.MyDialog);;
	private static final String DEBUGTAG = "PlayReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bData = intent.getExtras();
        if(bData.get("msg").equals("play_alarm"))
        {
        	MyLog.Log(DEBUGTAG, "==>Had a AlarmClock!!",1);
    		dialog.setContentView(R.layout.dialog_alarm);
    		dialog.setCanceledOnTouchOutside(false);
    		dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
    			@Override
    			public void onDismiss(DialogInterface dialog) {
    					// TODO Auto-generated method stub
    					MyLog.Log(DEBUGTAG, "DismissListener", 1);
    					Ringtone.stopPlayRing();
    				}
    		});
        	TextView dialog_content = (TextView)dialog.findViewById(R.id.dialog_content);
        	dialog_content.setText(HomeActivity.alarmMessage);
        	Button btn_close = (Button)dialog.findViewById(R.id.btn_close);
        	btn_close.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
		    });
        	dialog.show();
        	HomeActivity.ClockList.remove(HomeActivity.i_get);
          	HomeActivity.setClock=false;
          	Intent home=new Intent(context, HomeActivity.class);
          	home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(home);
            Ringtone.PlayRingTone(HomeActivity.HomeContext, RingtoneManager.TYPE_RINGTONE);
        }
		
	}

}