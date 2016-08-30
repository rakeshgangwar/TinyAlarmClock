package com.rakeshgangwar.tinyalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActiveAlarm extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private List<String> alarms=new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer=MediaPlayer.create(this, R.raw.ringtone);
        listView=(ListView)findViewById(R.id.listView);
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
        fillListView();
        finish();
    }

    public void fillListView(){
        alarms=AlarmDatabase.getAll();
        List<String> mappedList = new ArrayList<>();
        for(String alarm:alarms){
            String[] splitNumber=alarm.split("\\s+");
            if(splitNumber[1].length()==1)
                mappedList.add(alarm.replace(" ",":0"));
            else
                mappedList.add(alarm.replace(" ",":"));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mappedList);
        listView.setAdapter(adapter);
    }

}
