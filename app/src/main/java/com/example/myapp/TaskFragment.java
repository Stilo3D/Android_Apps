package com.example.myapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class TaskFragment extends Fragment {

    Task task;
    private static String ARG_TASK_ID = "ARG_TASK_ID" ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_task);

        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        task = TaskStorage.getInstance().getTask(taskId);

    }

    public static TaskFragment newInstance(UUID taskId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID,taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        //Task task = new Task();


        EditText nameField;
        Button dateButton = null;
        CheckBox doneCheckBox = null;

        nameField = view.findViewById(R.id.task_name);
        dateButton = view.findViewById(R.id.task_date);
        doneCheckBox = view.findViewById(R.id.task_done);

        nameField.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                task.setName(s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setName(s.toString());

            }
        });

        if(dateButton != null){
            dateButton.setText(task.getDate().toString());
            dateButton.setEnabled(false);
        }


        if(doneCheckBox != null){
            doneCheckBox.setChecked(task.isDone());
            doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->{
                task.setDone(isChecked);
            });
        }

        return view;
        //return inflater.inflate(R.layout.fragment_task, container, false);
            }
}
