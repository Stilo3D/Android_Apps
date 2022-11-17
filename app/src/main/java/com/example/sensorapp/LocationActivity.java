package com.example.sensorapp;

import static com.example.sensorapp.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 0;
    Button btn;
    private String TAG = "LOKALIZACJA";
    private Location lastLocation;
    private TextView locationTextView, addressTextView;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        btn= findViewById(R.id.location_button);
        locationTextView = findViewById(R.id.location_text);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btn.setOnClickListener(v -> getLocation());         //do after click on button
        addressTextView = findViewById(R.id.last_location_text);
        Button getAddressButton = findViewById(R.id.get_location_button);
        getAddressButton.setOnClickListener(v -> executeGeocoding());
    }

    private String locationGeocoding (Context context, Location location){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
          List<Address> addresses = null;
        String resultMessage = "";
        try{
            addresses = geocoder.getFromLocation(   //return address number
                    location.getLatitude(),
                    location.getLongitude(),
                    1   );
        }
        catch (IOException ioException){
            resultMessage = context.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);
        }

        if (addresses == null || addresses.isEmpty()){
            if(resultMessage.isEmpty()){
                resultMessage = context.getString(R.string.no_address);
                Log.e(TAG, resultMessage);
            }
        }
        else{
            Address address = addresses.get(0);
            List<String> addressParts = new ArrayList<>();
            for(int i = 0; i<=address.getMaxAddressLineIndex();i++)
                addressParts.add(address.getAddressLine(i));
            resultMessage = TextUtils.join("\n", addressParts);
        }

        return resultMessage;
    }

    private void executeGeocoding(){
        if (lastLocation != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();  //get geocoding to other thread
            Future<String> returnedAddress = executor.submit(() -> locationGeocoding(getApplicationContext(), lastLocation));
            //up - get result

            try{
                String result = returnedAddress.get();
                addressTextView.setText(getString(R.string.address_text, result, System.currentTimeMillis()));
            }
            catch (ExecutionException | InterruptedException exception) {
                Log.e(TAG, exception.getMessage(), exception);
                Thread.currentThread().interrupt();
            }

        }
    }

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);  //verify permissions
        }else {

            fusedLocationClient.getLastLocation().addOnSuccessListener(
                    location -> {
                        if(location!=null){
                            lastLocation = location;
                            locationTextView.setText(
                                    getString(R.string.location_text,
                                            location.getLatitude(),
                                            location.getLongitude(),
                                            location.getTime())
                            );
                        }
                        else locationTextView.setText(R.string.no_location);
                    });
    }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);////
        switch(requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) getLocation();
                else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}