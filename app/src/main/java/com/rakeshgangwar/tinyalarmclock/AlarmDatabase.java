package com.rakeshgangwar.tinyalarmclock;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabase extends SQLiteOpenHelper {

    static AlarmDatabase instance = null;
    static SQLiteDatabase database = null;

    static final String databaseName = "AlarmDB";
    static final int databaseVersion = 1;

    public static final String tableAlarm = "alarm";
    public static final String columnAlarmTime = "alarm_time";

    public static void init(Context context){
        if(null == instance){
            instance=new AlarmDatabase(context);
        }
    }

    public static SQLiteDatabase getDatabase() {
        if(database == null) {
            database=instance.getWritableDatabase();
        }
        return database;
    }

    public static long create(String time){
        ContentValues contentValues=new ContentValues();
        contentValues.put(columnAlarmTime, time);

        return getDatabase().insert(tableAlarm, null, contentValues);
    }

    public static int deleteAlarm(String time){
        return getDatabase().delete(tableAlarm, columnAlarmTime+"=\""+time+"\"",null);
    }

    public static Cursor getCursor() {
        String[] columns=new String[]{columnAlarmTime};
        return getDatabase().query(tableAlarm,columns,null, null, null, null,
                null);
    }

    AlarmDatabase(Context context) { super(context,databaseName,null,databaseVersion); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableAlarm + " ( "
                + columnAlarmTime + " TEXT primary key NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableAlarm);
        onCreate(db);
    }

    public static List<String> getAll() {
        List<String> alarms=new ArrayList<>();
        Cursor cursor=AlarmDatabase.getCursor();
        if(cursor.moveToFirst()) {
            do{
                String alarm=cursor.getString(0);
                alarms.add(alarm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }
}
