package com.example.openparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConSignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonUpdate;
    private EditText editStreetAddress;
    private EditText editCity;
    private EditText editZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_signup);


        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        editStreetAddress = (EditText) findViewById(R.id.editStreetAddress);
        editCity = (EditText) findViewById(R.id.editCity);
        editZipCode = (EditText) findViewById(R.id.editZipCode);

        buttonUpdate.setOnClickListener(this);
    }

    private void contributorUpdate()
    {

    }

    @Override
    public void onClick(View view) {
        if(view == buttonUpdate)
        {
            contributorUpdate();
        }
}
