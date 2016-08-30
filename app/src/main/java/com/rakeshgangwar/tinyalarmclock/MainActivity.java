package com.rakeshgangwar.tinyalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Calendar calendar;
    ListView listView;
    private List<String> alarms=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView=(ListView)findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        calendar=Calendar.getInstance();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar=Calendar.getInstance();
                    new TimePickerDialog(MainActivity.this,
                            t,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false).show();
                }
            });
        }
        AlarmDatabase.init(this);
        fillListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value=(String)parent.getItemAtPosition(position);
                String[] splitNumber=value.split(":");
                String modValue;
                if(splitNumber[1].startsWith("0"))
                    modValue=value.replace(":0"," ");
                else
                    modValue=value.replace(":"," ");

                AlarmDatabase.deleteAlarm(modValue);
                fillListView();
                Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(modValue.replaceAll("\\s+","")), intent, 0);
                AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(getApplicationContext(), "Alarm Deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(getApplicationContext(), "Alarm set for "+hourOfDay+":"+minute, Toast.LENGTH_LONG).show();
            AlarmDatabase.init(getApplicationContext());
            AlarmDatabase.create(""+hourOfDay+" "+minute);
            fillListView();
            AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar now=Calendar.getInstance();
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            if(calendar.before(now))
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent=new Intent(getApplicationContext(), AlarmReceiver.class);
            int _id=Integer.parseInt(""+hourOfDay+minute);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(), _id, intent, 0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
