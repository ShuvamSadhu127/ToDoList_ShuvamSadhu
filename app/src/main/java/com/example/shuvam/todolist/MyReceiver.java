package com.example.shuvam.todolist;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class MyReceiver extends BroadcastReceiver {
    public static boolean CURRENT= true;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        if (CURRENT) {
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    long timestampMillis = currentMessage.getTimestampMillis();
                    Calendar c=Calendar.getInstance();
                    c.setTimeInMillis(timestampMillis);
                    Date d=c.getTime();
                    Date t=c.getTime();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("HH:mm");
                    String dateCreated= simpleDateFormat.format(d);
                    String timeCreated=simpleDateFormat1.format(t);

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    TaskOpenHelper openHelper = TaskOpenHelper.getInstance(context);
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.todo.COLUMN_TASKNAME, phoneNumber);
                    contentValues.put(Contract.todo.COLUMN_SUMMARY, message);
                    contentValues.put(Contract.todo.COLUMN_date,dateCreated);
                    contentValues.put(Contract.todo.COLUMN_time, timeCreated);



                    database.insert(Contract.todo.TABLE_NAME, null, contentValues);

                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);

            }
        }


    }
}
