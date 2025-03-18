package com.example.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTaskList;
    private TaskAdapter taskAdapter;
    private ArrayList<String> taskList;
    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTaskList = findViewById(R.id.rvTaskList);
        btnAddTask = findViewById(R.id.btnAddTask);
        taskList = new ArrayList<>();

        loadTasks();

        taskAdapter = new TaskAdapter(this,taskList);
        rvTaskList.setAdapter(taskAdapter);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this));

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences("TaskPrefs", MODE_PRIVATE);
        int taskCount = sharedPreferences.getInt("task_count", 0);
        for (int i = 0; i < taskCount; i++) {
            String task = sharedPreferences.getString("task_" + i, "");
            taskList.add(task);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        taskAdapter.notifyDataSetChanged();
    }
}
