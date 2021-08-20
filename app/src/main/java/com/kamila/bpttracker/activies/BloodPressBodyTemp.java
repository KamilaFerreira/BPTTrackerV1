package com.kamila.bpttracker.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kamila.bpttracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BloodPressBodyTemp extends AppCompatActivity {

    EditText editTextBodyTemp, editTextBloodPress;
    FloatingActionButton floatingActionButCheck;
    CalendarView calendarView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String dateSelected;
    SharedPreferences sharedPreferences;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_press_body_temp);

        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        editTextBodyTemp = findViewById(R.id.editTextBodyTemp);
        editTextBloodPress = findViewById(R.id.editTextBloodPress);
        calendarView = findViewById(R.id.calendarView);
        floatingActionButCheck = findViewById(R.id.floatingActionButCheck);


        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");
        System.out.println("email "  +email );

        floatingActionButCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bodyTempField = editTextBodyTemp.getText().toString();
                String bloodPressField = editTextBloodPress.getText().toString();
                dateSelected = sdf.format(new Date(calendarView.getDate()));


                Map<String, String> data = new HashMap<>();
                data.put("bloodpressure" , bloodPressField);
                data.put("temperature" , bodyTempField);
                data.put("date" , dateSelected );


                db.collection(email).document().set(data, SetOptions.merge())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Data Not Saved "+e);
                                backToMainActivity();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("Data Saved");
                                backToMainActivity();
                            }
                        });
            }
        });

    }

    private void validateInputNumbers() {

        String validBodyTemp = editTextBodyTemp.getText().toString();
        String validBloodP = editTextBloodPress.getText().toString();
        String validDate = String.valueOf(calendarView.getDate());


        if (validBodyTemp.isEmpty()) {
            if (validBloodP.isEmpty()) {
                if(validDate.isEmpty()){


                }else{
                    Toast.makeText(BloodPressBodyTemp.this, "Please, Select the Date field", //Toast displays a message to user fill up the value
                            Toast.LENGTH_LONG).show();

                }


            } else {
                Toast.makeText(BloodPressBodyTemp.this, "Please, fill up Blood Pressure field", //Toast displays a message to user fill up the value
                        Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(BloodPressBodyTemp.this, "Please, fill up Body Temperature field", //Toast displays a message to user fill up the value
                    Toast.LENGTH_LONG).show();


        }
    }

    private void backToMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish(); //this method closes the Login Activity
    }
}