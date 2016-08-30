package com.rakeshgangwar.tinyalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

public class ActiveAlarm extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer=MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.start();
        setContentView(R.layout.activity_active_alarm);
    }

    public void onClick(View view){
        Calendar calendar=Calendar.getInstance();
        int hours=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(""+hours+minute), intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        AlarmDatabase.deleteAlarm(hours+" "+minute);
        mediaPlayer.stop();
        mediaPlayer.reset();
        finish();
    }

}
