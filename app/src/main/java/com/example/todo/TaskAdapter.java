package com.example.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<String> taskList;
    private Context context;

    public TaskAdapter(Context context, List<String> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = taskList.get(position);
        holder.taskText.setText(task);

        holder.completeButton.setOnClickListener(v -> {
            if (holder.completeButton.getText().toString().equals("Geri Al")) {
                holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.taskText.setTextColor(Color.BLACK);
                holder.completeButton.setText("Tamamlandı");
            } else {
                holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.taskText.setTextColor(Color.GRAY);
                holder.completeButton.setText("Geri Al");
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Silme işlemi")
                    .setMessage("Bu görevi silmek istediğinizden emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        taskList.remove(position);
                        notifyItemRemoved(position);
                        saveTasks();
                    })
                    .setNegativeButton("Hayır", (dialog, which) -> dialog.dismiss()) // İptal edersek dialog'u kapatıyoruz
                    .create()
                    .show();


            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("task_count", taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            editor.putString("task_" + i, taskList.get(i));
        }
        editor.apply();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskText;
        Button completeButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskText = itemView.findViewById(R.id.task_text);
            completeButton = itemView.findViewById(R.id.complete_button);
        }
    }


}
