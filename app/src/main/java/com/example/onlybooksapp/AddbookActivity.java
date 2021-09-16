package com.example.onlybooksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddbookActivity extends AppCompatActivity {

    private EditText name, price, purl;
    private Button submit, back, add_location;
    private TextView add_latitude, add_longitude ;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longgitude;
    private FirebaseUser uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        price = (EditText) findViewById(R.id.add_price);
        name = (EditText) findViewById(R.id.name) ;
        purl = (EditText) findViewById(R.id.add_purl);
        add_location = (Button) findViewById(R.id.add_location);
        add_latitude = (TextView) findViewById(R.id.set_latitude);
        add_longitude = (TextView) findViewById(R.id.set_longitude);
        uid = FirebaseAuth.getInstance().getCurrentUser() ;


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        back = (Button) findViewById(R.id.add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLoacation();

            }
        });

        submit = (Button) findViewById(R.id.add_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processinsert();

            }
        });
    }

    private void getLoacation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
             Location location = task.getResult();
             if (location != null){

                 try {
                     Geocoder geocoder = new Geocoder( AddbookActivity.this, Locale.getDefault());
                     List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                     latitude = addresses.get(0).getLatitude();
                     Toast.makeText(getApplicationContext(),"Location Added",Toast.LENGTH_LONG).show();
                     longgitude = addresses.get(0).getLongitude();

                     add_latitude.setText(Html.fromHtml(
                             "<font color='6200EE'><b>Latitude: </b><br></font>"
                             +addresses.get(0).getLatitude()
                     ));
                     add_longitude.setText(Html.fromHtml(
                             "<font color='6200EE'><b>Longitude: </b><br></font>"
                                     +addresses.get(0).getLongitude()
                     ));
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
            }
        });
    }

    private void processinsert(){

        Map<String,Object> map=new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("price",price.getText().toString());
        map.put("purl",purl.getText().toString());
        map.put("latitude",latitude);
        map.put("longitude", longgitude);
        map.put("uid",uid.toString());

        FirebaseDatabase.getInstance("https://onlybooks-e1483-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user")
        .push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        name.setText("");
                        price.setText("");
                        purl.setText("");

                        Toast.makeText(getApplicationContext(),"Inserted Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Could not insert",Toast.LENGTH_LONG).show();
                    }
                });

    }
}