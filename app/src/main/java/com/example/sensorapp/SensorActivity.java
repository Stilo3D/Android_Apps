package com.example.sensorapp;

import static com.example.sensorapp.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter = null;
    private boolean countVisible = false;
    private final List<Integer> colouredSensors = Arrays.asList(Sensor.TYPE_GRAVITY, Sensor.TYPE_LIGHT);
    private final int magnetometer = Sensor.TYPE_MAGNETIC_FIELD;
    public static final int SENSOR_DETAILS_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);
        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager (new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //click on show NUMBER
        countVisible = !countVisible;
        String string = getString(R.string.sensors_count, sensorList.size());
        if (countVisible)getSupportActionBar().setSubtitle(string);
        else getSupportActionBar().setSubtitle(null);   //hide number of items
        return true;
    }



    public class SensorHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView iconView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public SensorHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.sensor_name);
            iconView  = itemView.findViewById(R.id.sensor_category);
        }

        public void bind(Sensor sensor) {    //to do sth with particular item on list
            nameTextView.setText(sensor.getName());
            iconView.setImageResource(R.drawable.ic_sensor);
            View itemContainer = itemView.findViewById(R.id.entire_sensor_item);

            if (magnetometer == sensor.getType()){
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, SENSOR_DETAILS_ACTIVITY_REQUEST_CODE);
                });
            }

            if (colouredSensors.contains(sensor.getType())) {
                itemContainer.setBackgroundColor(getResources().getColor(R.color.green_light));  //print green colour if this 2 will be disoplayed
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, SENSOR_DETAILS_ACTIVITY_REQUEST_CODE);
                });
            }
        }


    }


    class SensorAdapter extends  RecyclerView.Adapter<SensorHolder> {

        // ... view holder defined above...

        // Store a member variable for the contacts
        private List<Sensor> sensors;

        // Pass in the contact array into the constructor
        public SensorAdapter(List<Sensor> sens) {
            sensors = sens;
        }

        @Override
        public SensorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.sensor_list_item, parent, false);//////////

            // Return a new holder instance
            SensorHolder sensorHolder = new SensorHolder(contactView);
            return sensorHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(SensorHolder holder, int position) {
            // Get the data model based on position
            Sensor sns = sensors.get(position);
            ImageView imgView = holder.iconView;
            imgView.setImageResource(R.drawable.ic_sensor);
            // Set item views based on your views and data model
            TextView textView = holder.nameTextView;
            textView.setText(sns.getName());
            Logger logger = Logger.getLogger(SensorActivity.class.getName());  //write to logs
            logger.log(Level.WARNING, "Info about sensor: " + sns.getName() + " " + sns.getVendor() + " " + sns.getMaximumRange());
            holder.bind(sns);
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

}