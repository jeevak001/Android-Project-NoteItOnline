package com.noteitapp.dev.noteit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmNoteActivity extends AppCompatActivity {

    Button dateButton,timeButton,saveButton;
    Typeface helvetica;
    DatePicker datePicker;
    Calendar cal;
    RatingBar ratingbar;
    TimePicker timePicker;
    android.app.AlertDialog dateAlert;
    android.app.AlertDialog timeAlert,noTimeAlert,dataAlert,futureAlert;
    EditText title,content;
    Button dateOk,timeOk;
    GregorianCalendar calender;
    Intent serviceIntent;
    int date,month,year,hour,minute;
    long timeToAlarm = 0;
    boolean timeSet = false,dateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dateButton = (Button) findViewById(R.id.alarm_date);
        saveButton = (Button) findViewById(R.id.alarm_save);
        title = (EditText) findViewById(R.id.alarm_title);
        content = (EditText) findViewById(R.id.alarm_content);
        ratingbar = (RatingBar) findViewById(R.id.alarm_bar);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AlarmNoteActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.date_picker, null);
                Button ok = (Button) dialogView.findViewById(R.id.alert_date_ok);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.alert_date);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("JeevaAlarm","DATE: " + String.valueOf(datePicker.getDayOfMonth()) + ", " + String.valueOf(datePicker.getMonth()) + " ," + String.valueOf(datePicker.getYear()));
                        date = datePicker.getDayOfMonth();
                        month = datePicker.getMonth();
                        year = datePicker.getYear();
                        dateSet = true;
                        dateAlert.dismiss();



                        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AlarmNoteActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.time_picker, null);
                        Button ok = (Button) dialogView.findViewById(R.id.alert_time_ok);
                        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.alert_time);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.d("JeevaAlarm","TIME: "  +  String.valueOf(timePicker.getCurrentHour()) + ", " + String.valueOf(timePicker.getCurrentMinute()) + ", 0");
                                hour = timePicker.getCurrentHour();
                                minute = timePicker.getCurrentMinute();
                                timeSet = true;
                                timeAlert.dismiss();
                            }
                        });

                        dialogBuilder.setView(dialogView);
                        timeAlert = dialogBuilder.create();
                        timeAlert.show();
                    }
                });

                dialogBuilder.setView(dialogView);
                dateAlert = dialogBuilder.create();
                dateAlert.show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(timeSet && dateSet){


                    if(String.valueOf(title.getText()).equalsIgnoreCase("") && String.valueOf(content.getText()).equalsIgnoreCase("") ){

                        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AlarmNoteActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.data_alert, null);
                        Button noDateButton = (Button) dialogView.findViewById(R.id.data_alert_button);

                        noDateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataAlert.dismiss();
                            }
                        });

                        dialogBuilder.setView(dialogView);
                        dataAlert = dialogBuilder.create();
                        dataAlert.show();

                    }else{

                        cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.clear();
                        cal.set(year, month, date, hour, minute,0);


                        if(checkValidForFuture()){

                            AlarmNote note = new AlarmNote();
                            note.setTitle(String.valueOf(title.getText()));
                            note.setContent(String.valueOf(content.getText()));
                            note.setPriority(String.valueOf(ratingbar.getRating()));
                            note.setSet(0);
                            note.setTime(String.valueOf(cal.getTimeInMillis()));

                            MainActivity.helper.showAllAlarmNotes("Before Added");
                            MainActivity.helper.addAlarmNote(note);
                            MainActivity.helper.showAllAlarmNotes("After Added");
                            MainActivity.helper.setAllZero();
                            /*Intent i = new Intent(AlarmNoteActivity.this,MyNoteService.class);
                            i.putExtra("type","first");
                            stopService(i);
                            startService(i);
                            finish();

                            */


                            boolean isToSet = false;
                            AlarmNote noteTmp = new AlarmNote();
                            noteTmp = MainActivity.helper.checkIfTopSet();

                            if(noteTmp.getSet() == 0){

                                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(AlarmNoteActivity.this, AlarmReceiver.class);
                                intent.putExtra("title",noteTmp.getTitle());
                                intent.putExtra("content", noteTmp.getContent());
                                intent.putExtra("rating", noteTmp.getPriority());
                                intent.putExtra("id", noteTmp.getId());

                                Log.d("JeevaAlarm","INTENT_PUT: " + noteTmp.getId() + ", " + noteTmp.getTitle() + " ," +  noteTmp.getContent() + ", " + noteTmp.getSet());
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmNoteActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                //Log.d("JeevaAlarm", "TIME_CHECK: " + String.valueOf(new GregorianCalendar().getTimeInMillis()) + ", " + noteTmp.getTime());
                                //Toast.makeText(AlarmNoteActivity.this, "Timing Note set successfully", Toast.LENGTH_SHORT).show();
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, Long.parseLong(noteTmp.getTime()), pendingIntent);
                                //alarmMgr.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar().getTimeInMillis() + 30000, pendingIntent);
                                MainActivity.helper.setTop(noteTmp.getId());
                                finish();

                            }else{

                                Log.d("JeevaAlarm","Set and Not Starting");
                                finish();

                            }








                        }else{

                            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AlarmNoteActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.future_alert, null);
                            Button noTimeButton = (Button) dialogView.findViewById(R.id.future_alert_button);

                            noTimeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    futureAlert.dismiss();
                                }
                            });

                            dialogBuilder.setView(dialogView);
                            futureAlert = dialogBuilder.create();
                            futureAlert.show();


                        }



                    }


                }else{

                    android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(AlarmNoteActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.no_date_alert, null);
                    Button noTimeButton = (Button) dialogView.findViewById(R.id.no_time_button);

                    noTimeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noTimeAlert.dismiss();
                        }
                    });

                    dialogBuilder.setView(dialogView);
                    noTimeAlert = dialogBuilder.create();
                    noTimeAlert.show();

                }

            }
        });


    }

    public boolean checkValidForFuture(){


        if(new GregorianCalendar().getTimeInMillis() < cal.getTimeInMillis()){
            return true;
        }else{
            return false;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
