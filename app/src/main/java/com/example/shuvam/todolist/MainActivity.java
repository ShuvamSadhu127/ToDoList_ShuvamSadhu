package com.example.shuvam.todolist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ArrayList<Task>  tasks = new ArrayList<>();
    TaskAdapter adapter;
    EditText taskEditText;
    EditText taskSummaryEditText;
    EditText taskDateEditText;
    EditText taskTimeEditText;

    BroadcastReceiver receiver;
    BroadcastReceiver alarmReceiver;
    int year, month, day, hour, min;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fabadd);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

            receiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //TODO
                }
            };

            ListView listView=findViewById(R.id.listview);



        TaskOpenHelper openHelper=TaskOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqLiteDatabase=openHelper.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.query(Contract.todo.TABLE_NAME,null,null,null,null,null,null);
         while(cursor.moveToNext()){
            String task=cursor.getString(cursor.getColumnIndex(Contract.todo.COLUMN_TASKNAME));
             String taskSummary=cursor.getString(cursor.getColumnIndex(Contract.todo.COLUMN_SUMMARY));
             String taskDate=cursor.getString(cursor.getColumnIndex(Contract.todo.COLUMN_date));
             String taskTime=cursor.getString(cursor.getColumnIndex(Contract.todo.COLUMN_time));
             long id=cursor.getLong(cursor.getColumnIndex(Contract.todo.COLUMN_ID));

             Task task1=new Task(task,taskSummary,taskDate,taskTime);
             task1.setId(id);
             tasks.add(task1);
         }
         cursor.close();

        adapter=new TaskAdapter(this,tasks);
        listView.setAdapter(adapter);

        View view=new View(this);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);





    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final Task task = tasks.get(i);
        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setCancelable(false);
        builder.setMessage("Do you really want to delete " + task.getTask() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this,"Ok Presses",Toast.LENGTH_LONG).show();
                TaskOpenHelper openHelper = TaskOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                long id = task.getId();
                String[] selectionArgs = {id + ""};

                database.delete(Contract.todo.TABLE_NAME,Contract.todo.COLUMN_ID + " = ?",selectionArgs);

                //Toast.makeText(MainActivity.this,"Ok Presses",Toast.LENGTH_LONG).show();
                tasks.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId()==R.id.addtask)
            addTask();
        return true;*/

       int id=item.getItemId();
       if (id==R.id.feedback){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            Uri uri=Uri.parse("mailto:shuvam.internet@gmail.com");
            intent.setData(uri);
            startActivity(intent);

       }
       else if (id==R.id.aboutus){
           Intent intent=new Intent(this,AboutUsActivity.class);
           intent.setAction(Intent.ACTION_VIEW);
           startActivity(intent);

       }

        return true;
    }


    private void addTask() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_todol_row,null);
        builder.setView(dialogView);
        taskEditText=dialogView.findViewById(R.id.editTextTask);
        taskSummaryEditText=dialogView.findViewById(R.id.editTextTaskSummary);
        taskDateEditText=dialogView.findViewById(R.id.editTextTaskDate);
        taskTimeEditText=dialogView.findViewById(R.id.editTextTaskTime);

        taskDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yy, int mm, int dd) {

                        year = yy;
                        month = mm;
                        day = dd;

                        taskDateEditText.setText(dd + "/" + (mm+1) + "/" + yy);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });
        taskTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                min=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hour = hourOfDay;
                        min = minute;


                        taskTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                },hour,min,false);

                timePickerDialog.show();
            }
        });

        builder.setTitle("Add Task");


        builder.setPositiveButton("Add this task and Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task,taskSummary,taskDate,taskTime;
                task=taskEditText.getText().toString();
                taskSummary=taskSummaryEditText.getText().toString();

                taskDate=taskDateEditText.getText().toString();
                taskTime=taskTimeEditText.getText().toString();

                Task task1=new Task(task,taskSummary,taskDate,taskTime);


                TaskOpenHelper taskOpenHelper=TaskOpenHelper.getInstance(MainActivity.this);
                SQLiteDatabase sqLiteDatabase=taskOpenHelper.getWritableDatabase();

                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.todo.COLUMN_TASKNAME,task);
                contentValues.put(Contract.todo.COLUMN_SUMMARY,taskSummary);
                contentValues.put(Contract.todo.COLUMN_date,taskDate);
                contentValues.put(Contract.todo.COLUMN_time,taskTime);





                long id=sqLiteDatabase.insert(Contract.todo.TABLE_NAME,null,contentValues);
                if (id>-1L){
                    task1.setId(id);
                    tasks.add(task1);

                    adapter.notifyDataSetChanged();

                }
                alarmReceiver=new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        //TODO
                    }
                };
                //IntentFilter intentFilter=new IntentFilter(android.provider.B);
                //registerReceiver(intentFilter);


                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(MainActivity.this,MyReceiverAlarm.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,1,intent,0);

                Calendar calendar=Calendar.getInstance();
                //calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year,month,day,hour,min);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //TODO

        }
    });
    builder.create().show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {



        final Task task = tasks.get(i);
        final int position = i;
        TaskOpenHelper taskOpenHelper;
        SQLiteDatabase sqLiteDatabase;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("View/Edit this Task");
        builder.setCancelable(false);
        View dialogView = getLayoutInflater().inflate(R.layout.add_todol_row,null);
        builder.setView(dialogView);

        long id =task.getId();
        final String[] selectionArgs = {id + ""};

        taskEditText=dialogView.findViewById(R.id.editTextTask);
        taskSummaryEditText=dialogView.findViewById(R.id.editTextTaskSummary);
        taskDateEditText=dialogView.findViewById(R.id.editTextTaskDate);
        taskTimeEditText=dialogView.findViewById(R.id.editTextTaskTime);

        taskEditText.setText(task.getTask());
        taskSummaryEditText.setText(task.getTaskSummary());
        taskDateEditText.setText(task.getDate());
        taskTimeEditText.setText(task.getTime());

        taskDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        ++month;

                        taskDateEditText.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });
        taskTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                min=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        taskTimeEditText.setText(hourOfDay + ":" + minute);
                    }
                },hour,min,false);

                timePickerDialog.show();
            }
        });


        builder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String task, taskSummary, taskDate, taskTime;

                task = taskEditText.getText().toString();
                taskSummary = taskSummaryEditText.getText().toString();

                taskDate = taskDateEditText.getText().toString();
                taskTime = taskTimeEditText.getText().toString();

                Task task1 = new Task(task, taskSummary, taskDate, taskTime);


                TaskOpenHelper taskOpenHelper = TaskOpenHelper.getInstance(MainActivity.this);
                SQLiteDatabase sqLiteDatabase = taskOpenHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.todo.COLUMN_TASKNAME, task);
                contentValues.put(Contract.todo.COLUMN_SUMMARY, taskSummary);
                contentValues.put(Contract.todo.COLUMN_date, taskDate);
                contentValues.put(Contract.todo.COLUMN_time, taskTime);

                sqLiteDatabase.update(Contract.todo.TABLE_NAME, contentValues, Contract.todo.COLUMN_ID+" = ?", selectionArgs);
                tasks.set(position,task1);
                adapter.notifyDataSetChanged();

                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent=new Intent(MainActivity.this,MyReceiverAlarm.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity.this,1,intent,0);

                Calendar calendar=Calendar.getInstance();
                //calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year,month,day,hour,min);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }

//            alarmReceiver=new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    //TODO
//                }
//            };
            //IntentFilter intentFilter=new IntentFilter(android.provider.B);
            //registerReceiver(intentFilter);


        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO

            }
        });
        builder.create().show();

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.editTextTaskDate){
                setDate((EditText)v);
        }
        else if (v.getId()==R.id.editTextTaskTime){
            setTime((EditText)v);
        }

    }
    public void setTime(final EditText timeEditText) {





    }
    private void setDate(final EditText dateEditText) {


    }
}
