package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPersonalDetails extends AppCompatActivity {
    EditText name, surname,age,emgContact,select;
    Button addDetails;
    TextView address;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    String userName, userSurname, userAge,add, contact, motherDaughter;
    ProgressDialog progressDialog;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personal_details);
        getSupportActionBar().hide();

        name = findViewById(R.id.etName);
        surname = findViewById(R.id.etSurname);
        age = findViewById(R.id.etAge);
        address = findViewById(R.id.tvLocation);
        emgContact = findViewById(R.id.etEmergencyContact);
        select = findViewById(R.id.motherDaughter);
        addDetails = findViewById(R.id.buttonAddDetails);
        progressDialog = new ProgressDialog(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Getting Location Please Wait");
                progressDialog.show();
                //check permissions
                if(ActivityCompat.checkSelfPermission(AddPersonalDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();
                }else
                {
                    ActivityCompat.requestPermissions(AddPersonalDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 userName = name.getText().toString();
                 userSurname = surname.getText().toString();
                 userAge = age.getText().toString();
                 add = address.getText().toString();
                 contact = emgContact.getText().toString();
                 motherDaughter = select.getText().toString();

                 if(userName.isEmpty() || userSurname.isEmpty()||
                         userAge.isEmpty()||
                         add.isEmpty()||
                         contact.isEmpty()||
                         motherDaughter.isEmpty())
                 {
                     Toast.makeText(getApplicationContext(), "Please Enter All Required Details", Toast.LENGTH_SHORT).show();
                 }else
                 {
                     progressDialog.setMessage("SAVING INFORMATION!!!");
                     progressDialog.show();
                     sendUserData();
                 }

            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null)
                {
                    progressDialog.dismiss();
                    try {
                        Geocoder geocoder = new Geocoder(AddPersonalDetails.this,
                                Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);

                        address.setText(Html.fromHtml("<font color= '#6200EE'> <b>Address :</b><br></font>"
                                + addresses.get(0).getAddressLine(0) +
                                "<br><font color= '#6200EE'> <b>Locality :</b><br></font>"+ addresses.get(0).getLocality()+
                                "<br><font color= '#6200EE'> <b>Latitude :</b><br></font>"+ addresses.get(0).getLatitude()+
                                "<br><font color= '#6200EE'> <b>Longitude :</b><br></font>"+ addresses.get(0).getLongitude()));

                    } catch (IOException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void sendUserData()
    {
        CollectionReference dbUserData = db.collection("Personal Details");

        UserProfile userProfile = new UserProfile(userName, userSurname, userAge, contact, add, motherDaughter);

        dbUserData.add(userProfile).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AddPersonalDetails.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Saving Details Failed, Try Again ", Toast.LENGTH_SHORT).show();

            }
        });



        /*FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("https://women-safe-eff15-default-rtdb.firebaseio.com/"); //get unique ID
        UserProfile userProfile = new UserProfile(userName, userSurname, userAge, contact, add, motherDaughter);
        myReference.setValue(userProfile);
        progressDialog.dismiss();

         */


    }
}