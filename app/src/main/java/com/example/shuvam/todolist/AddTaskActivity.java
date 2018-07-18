package com.example.shuvam.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    public static final String TASK_KEY="task";
    public static final String TASKSUMMARY_KEY="tasksummary";
    public static final String date_KEY="date";
    public static String time_KEY="time";

    public static final int ADD_RESULT_CODE=2;

    EditText timeEditText,dateEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todol_row);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        bundle.getString(TASK_KEY);
        bundle.getString(TASKSUMMARY_KEY);
        bundle.getString(date_KEY);
        bundle.getString(time_KEY);


    }

    private void setTime() {

        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeEditText.setText(hourOfDay + ":" + minute);
            }
        },hour,min,false);

        timePickerDialog.show();



    }

    private void setDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                ++month;

                dateEditText.setText(dayOfMonth + "/" + month + "/" + year);
            }
        },year,month,day);

        datePickerDialog.show();
    }

    public void saveTask(View view){
        EditText taskEditText=findViewById(R.id.editTextTask);
        EditText summaryEditText=findViewById(R.id.editTextTaskSummary);
        dateEditText=findViewById(R.id.editTextTaskDate);
        timeEditText=findViewById(R.id.editTextTaskTime);

        String task=taskEditText.getText().toString();
        String taskSummary=summaryEditText.getText().toString();

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();

            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime();
            }
        });






        Intent data=new Intent();
        data.putExtra(TASK_KEY,task);
        data.putExtra(TASKSUMMARY_KEY,taskSummary);
        setResult(ADD_RESULT_CODE,data);
        finish();



    }



}
