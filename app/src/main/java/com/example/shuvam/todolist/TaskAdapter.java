package com.example.shuvam.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter {
    ArrayList<Task> items;
    LayoutInflater inflater;

    public TaskAdapter(@NonNull Context context, ArrayList<Task> items) {
        super(context, 0, items);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items=items;

    }
    @Override
    public int getCount(){
        return items.size();
    }

    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View output=convertView;
        if (output==null) {
            output = this.inflater.inflate(R.layout.todol_row_layout, parent, false);

            TextView tasknametv = output.findViewById(R.id.taskname);
            TextView taskSummarytv = output.findViewById(R.id.tasksummary);
            TextView taskDatetv =output.findViewById(R.id.taskdate);
            TextView taskTimetv=output.findViewById(R.id.tasktime);
            TaskViewHolder taskViewHolder = new TaskViewHolder();
            taskViewHolder.task = tasknametv;
            taskViewHolder.taskSummary = taskSummarytv;
            taskViewHolder.taskDate=taskDatetv;
            taskViewHolder.taskTime=taskTimetv;
            output.setTag(taskViewHolder);
        }
        TaskViewHolder taskViewHolder= (TaskViewHolder) output.getTag();

        Task task1 = items.get(position);
        taskViewHolder.task.setText(task1.getTask());
        taskViewHolder.taskSummary.setText(task1.getTaskSummary());
        taskViewHolder.taskDate.setText(task1.getDate());
        taskViewHolder.taskTime.setText(task1.getTime());

        return output;
    }
}


