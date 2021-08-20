package com.kamila.bpttracker.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kamila.bpttracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    Double tempWeek, tempDaily, bloodPWeek, bloodPDaily;
    int countTempWeek, countTempDaily, countBloodPDaily, countBloodPWeek;
    TextView tvBtWeek, tvBtDaily, tvBpWeek, tvBpDaily;
    String currentDate;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    Button btnAdd;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBtWeek = (TextView) findViewById(R.id.tvBtWeek);
        tvBtDaily = (TextView) findViewById(R.id.tvBtDaily);
        tvBpWeek = (TextView) findViewById(R.id.tvBpWeek);
        tvBpDaily = (TextView) findViewById(R.id.tvBpDaily);
        btnAdd = findViewById(R.id.btnAdd);

        tempWeek = 0.0;
        tempDaily = 0.0;
        bloodPWeek = 0.0;
        bloodPDaily = 0.0;
        countTempDaily = 0;
        countTempWeek = 0;
        countBloodPDaily = 0;
        countBloodPWeek = 0;


        FirebaseApp.initializeApp(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        System.out.println("email " + email);
        ;


        db.collection(email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //System.out.println("TEMPERATURE "  + document.get("temperature"));;
                        //System.out.println("bloodpressure " + document.get("bloodpressure"));
                        String temp = (String) document.get("temperature");
                        String bloodP = (String) document.get("bloodpressure");
                        String docDate = (String) document.get("date");
                        currentDate = df.format(new Date());
                        if (currentDate.equals(docDate)) {
                            countTempDaily++;
                            countBloodPDaily++;
                            tempDaily = tempDaily + Double.parseDouble(temp);
                            bloodPDaily = bloodPDaily + Double.parseDouble(bloodP);
                        }
                        tempWeek = tempWeek + Double.parseDouble((String) document.get("temperature"));
                        bloodPWeek = bloodPWeek + Double.parseDouble((String) document.get("bloodpressure"));
                        countTempWeek++;
                        countBloodPWeek++;
                    }

                    tvBtWeek.setText(String.format("%.2f", tempWeek / countTempWeek));
                    tvBpWeek.setText(String.format("%.2f", bloodPWeek / countBloodPWeek));

                    tvBtDaily.setText(String.format("%.2f", tempDaily / countTempDaily));
                    tvBpDaily.setText(String.format("%.2f", bloodPDaily / countBloodPDaily));
                } else {
                    System.out.println("error" + task.getException());
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BloodPressBodyTemp.class));
                finish();

            }
        });
    }
}