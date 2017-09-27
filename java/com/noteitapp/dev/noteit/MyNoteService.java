package com.noteitapp.dev.noteit;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyNoteService extends Service {

    android.app.AlertDialog noteDialog;
    MyDBHelper helper;
    ArrayList<PendingIntent> intents;

    public MyNoteService() {

        intents = new ArrayList<>();
        helper = new MyDBHelper(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        MyNoteService getService() {
            return MyNoteService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("JeevaAlarmPow", "Service ONSTARTCOMMAND");

        Log.d("JeevaAlarm", "Intent Size: " + String.valueOf(intents.size()));


        if(String.valueOf(intent.getExtras().getString("type")).equalsIgnoreCase("first")) {


            Log.d("JeevaAlarmPow", "Service FIRST");
            AlarmNote noteTmp = new AlarmNote();
            noteTmp = MainActivity.helper.checkIfTopSet();

            helper.showAllAlarmNotes("Before Service");

            if(noteTmp != null){

                if (noteTmp.getSet() == 0) {

                    helper.setAllZero();

                    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent intentTmp = new Intent(MyNoteService.this, AlarmReceiver.class);
                    intentTmp.putExtra("title", noteTmp.getTitle());
                    intentTmp.putExtra("content", noteTmp.getContent());
                    intentTmp.putExtra("rating", noteTmp.getPriority());
                    intentTmp.putExtra("id", noteTmp.getId());

                    Log.d("JeevaAlarm", "INTENT_PUT: " + noteTmp.getId() + ", " + noteTmp.getTitle() + " ," + noteTmp.getContent() + ", " + noteTmp.getSet());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MyNoteService.this, 0, intentTmp, PendingIntent.FLAG_UPDATE_CURRENT);
                    intents.add(pendingIntent);
                    //Log.d("JeevaAlarm", "TIME_CHECK: " + String.valueOf(new GregorianCalendar().getTimeInMillis()) + ", " + noteTmp.getTime());
                    //Toast.makeText(AlarmNoteActivity.this, "Timing Note set successfully", Toast.LENGTH_SHORT).show();
                    alarmMgr.set(AlarmManager.RTC_WAKEUP, Long.parseLong(noteTmp.getTime()), pendingIntent);
                    //alarmMgr.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar().getTimeInMillis() + 30000, pendingIntent);
                    MainActivity.helper.setTop(noteTmp.getId());

                    helper.showAllAlarmNotes("After Service");

                } else {

                    Log.d("JeevaAlarm", "Set and Not Starting");

                }

            }

        }else {

            Log.d("JeevaAlarmPow", "Service NOT FIRST");
            Log.d("JeevaAlarm", "Service Started");
            Log.d("JeevaAlarm", "MESSAGE_GOT_SERVICE: " + intent.getExtras().getInt("id") + ", " + intent.getExtras().getString("title") + ", " + intent.getExtras().getString("content"));

            try {

                Intent i = new Intent();
                i.putExtra("title", intent.getExtras().getString("title"));
                i.putExtra("content", intent.getExtras().getString("content"));
                i.putExtra("rating", intent.getExtras().getString("rating"));
                i.putExtra("id", intent.getExtras().getInt("id"));


                AlarmNote noteTmp = new AlarmNote();
                noteTmp = MainActivity.helper.checkIfTopSet();

                if (noteTmp.getId() == intent.getExtras().getInt("id")) {

                    Log.d("JeevaAlarm", "Top is same as Sent");
                    i.setClass(this, AlarmShowActivity.class);
                    i.setAction(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cn = new ComponentName(this, AlarmShowActivity.class);
                    i.setComponent(cn);
                    startActivity(i);

                } else {

                    helper.setZero(intent.getExtras().getInt("id"));
                }


            } catch (Exception e) {

            }
        }


            return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("JeevaAlarmPow", "Service Created");
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("JeevaAlarm","Service Destroyed");
        for (PendingIntent intent:intents){
            intent.cancel();
        }
        Log.d("JeevaAlarm", "INtents Cleared: " + String.valueOf(intents.size()));


    }

    @Override
    public void onStart(final Intent intent, int startId) {

        Log.d("JeevaAlarmPow", "Service ONSTART");
        super.onStart(intent, startId);



    }
}
