package com.example.openparking;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PurchaseCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonViewParking;
    Button buttonMainMenu;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_complete);

        buttonViewParking = (Button)findViewById(R.id.btn_viewParking);
        buttonViewParking.setOnClickListener(this);

        buttonMainMenu = (Button) findViewById(R.id.btn_mainMenu);
        buttonMainMenu.setOnClickListener(this);

        myDialog = new Dialog(this);
    }



    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_window);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }




    @Override
    public void onClick(View view)
    {
        if(view == buttonViewParking)
        {
            ShowPopup(view);
        }
        if(view == buttonMainMenu)
        {
            Log.v("!","main menu button clicked" );
            finish();
        }
    }
}
