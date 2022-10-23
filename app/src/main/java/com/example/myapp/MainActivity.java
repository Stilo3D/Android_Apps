package com.example.myapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Fragment fragment = createFragment();

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            //protected abstract Fragment createFragment();
            fragment = new TaskFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }*/
    }
    //@Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }
}