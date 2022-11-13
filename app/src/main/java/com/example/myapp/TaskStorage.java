package com.example.myapp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks;

    public static TaskStorage getInstance(){
        return taskStorage;
    }

    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i=1;i<=10;i++){
            Task task = new Task();
            task.setName("pilne zadanie numer" + i);
            task.setDone(i%3==0);
            if (i % 3 == 0)
                task.setCategory(Category.STUDIA);
            else
                task.setCategory(Category.DOM);
            tasks.add(task);
        }

    }

    public void addTask(Task tsk){
        tasks.add(tsk);
    }

    public Task getTask(UUID ID){
        for( Task tsk : tasks)
            if(tsk.getId().equals(ID))
                return tsk;
        return null;
    }


    public List<Task> getTasks() {
        return tasks;
    }
}
