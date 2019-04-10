package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateParkingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking);

        Spinner payment = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateParkingActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.paymentmethod));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment.setAdapter(myAdapter);
    }
}
