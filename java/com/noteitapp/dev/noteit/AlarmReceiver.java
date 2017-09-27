package com.noteitapp.dev.noteit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Jeeva on 4/10/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    android.app.AlertDialog noteDialog;
    Intent data;
    public static final String ACTION = "com.noteitapp.dev.noteit.alarm";
    public static final int REQUEST_CODE = 12345;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("JeevaAlarm", "Receiver Started");
        Log.d("JeevaAlarm", "MESSAGE_GOT_RECEIVER: " + intent.getExtras().getInt("id") +  ", " +  intent.getExtras().getString("title") + ", " + intent.getExtras().getString("content"));


        Intent serviceIntent = new Intent(context,MyNoteService.class);
        serviceIntent.putExtra("title",intent.getExtras().getString("title"));
        serviceIntent.putExtra("content",intent.getExtras().getString("content"));
        serviceIntent.putExtra("rating",intent.getExtras().getString("rating"));
        serviceIntent.putExtra("id",intent.getExtras().getInt("id"));
        context.startService(serviceIntent);
    }
}
