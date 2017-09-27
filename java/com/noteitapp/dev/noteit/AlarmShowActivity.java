package com.noteitapp.dev.noteit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class AlarmShowActivity extends AppCompatActivity {

    Intent i;
    TextView title,content,rating;
    RatingBar ratingbar;
    String titleT,ratingT,contentT;
    Button dismiss;
    int idT;
    MyDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new MyDBHelper(this);

        i = getIntent();
        titleT = i.getExtras().getString("title");
        contentT = i.getExtras().getString("content");
        ratingT = i.getExtras().getString("rating");
        idT = i.getExtras().getInt("id");

        Log.d("JeevaAlarm", "MESSAGE_GOT_ACTIVITY: " + idT + "," + titleT + "," + contentT + "," + ratingT + ", ");

        helper.showAllAlarmNotes("Before Delete");
        helper.removeAlarm(idT);
        helper.showAllAlarmNotes("After Delete");

        title = (TextView) findViewById(R.id.alarm_title_show);
        content = (TextView) findViewById(R.id.alarm_content_show);
        rating = (TextView) findViewById(R.id.alarm_rating_show);
        ratingbar = (RatingBar) findViewById(R.id.alarm_rating_bar_show);
        dismiss = (Button) findViewById(R.id.alarm_dismiss);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.alarm);
        this.setVolumeControlStream(AudioManager.STREAM_ALARM);
        mp.setLooping(true);
        mp.start();


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                finish();
            }
        });

        title.setText(titleT);
        content.setText(contentT);
        ratingbar.setRating((float)Float.parseFloat(ratingT));

        if(ratingT.equalsIgnoreCase("1.0")){
            rating.setText("R1");
        }else if(ratingT.equalsIgnoreCase("2.0")){
            rating.setText("R2");
        }else if(ratingT.equalsIgnoreCase("3.0")){
            rating.setText("R3");
        }else if(ratingT.equalsIgnoreCase("4.0")){
            rating.setText("R4");
        }else if(ratingT.equalsIgnoreCase("5.0")){
            rating.setText("R5");
        }else{
            rating.setText("R0");
        }


        AlarmNote noteTmp1 = new AlarmNote();
        noteTmp1 = helper.checkIfTopSet();

        if(noteTmp1 != null){

            if(noteTmp1.getSet() == 0 ){

                Log.d("JeevaAlarm","Not Set and Starting");
                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intentTmp = new Intent(AlarmShowActivity.this, AlarmReceiver.class);
                intentTmp.putExtra("title", noteTmp1.getTitle());
                intentTmp.putExtra("content", noteTmp1.getContent());
                intentTmp.putExtra("rating", noteTmp1.getPriority());
                intentTmp.putExtra("id", noteTmp1.getId());


                Log.d("JeevaAlarm","INTENT_PUT: " + noteTmp1.getId() + ", " + noteTmp1.getTitle() + " ," +  noteTmp1.getContent() + ", " + noteTmp1.getSet());


                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmShowActivity.this, 0, intentTmp, PendingIntent.FLAG_UPDATE_CURRENT);

                Log.d("JeevaAlarm", "TIME_CHECK: " + String.valueOf(new GregorianCalendar().getTimeInMillis()) + ", " + noteTmp1.getTime());

                alarmMgr.set(AlarmManager.RTC_WAKEUP, Long.parseLong(noteTmp1.getTime()), pendingIntent);


                //alarmMgr.set(AlarmManager.RTC_WAKEUP, new GregorianCalendar().getTimeInMillis() + 30000, pendingIntent);
                helper.setTop(noteTmp1.getId());

            }else{

                Log.d("JeevaAlarm","Set and Not Starting");

            }
        }



    }

}
