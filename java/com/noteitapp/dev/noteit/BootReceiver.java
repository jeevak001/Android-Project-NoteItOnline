package com.noteitapp.dev.noteit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Jeeva on 4/11/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {

            try{
                Log.d("JeevaAlarmPow", "Power Con Called");
                Intent i = new Intent(context,MyNoteService.class);
                i.putExtra("type", "first");
                context.stopService(i);
                MainActivity.helper.setAllZero();
                context.startService(i);
            }catch(Exception e){

            }
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            try {
                Log.d("JeevaAlarmPow", "Power Dis Called");
                Intent i = new Intent(context, MyNoteService.class);
                i.putExtra("type", "first");
                context.stopService(i);
                MainActivity.helper.setAllZero();
                context.startService(i);
            }catch (Exception e){

            }
        }

    }
}
