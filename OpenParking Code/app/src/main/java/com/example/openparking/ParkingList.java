package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ParkingList extends AppCompatActivity {

    RecyclerView recyclerView;
    ElementAdapter adapter;

    List<Element> elementList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list2);
        elementList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adding some items to our list
        elementList.add(
                new Element(
                        1,
                        "1801 Tulane Ave\n"+
                                "Long Beach, CA 90815",
                        "2 parking spaces - Open Monday - Thursday, Pay Per Day",
                        4.3,
                        5,
                        R.drawable.parkingspot1));

        elementList.add(
                new Element(
                        1,
                        "6005 E Atherton St\n" +
                                "Long Beach, CA 90815",
                        "2 parking spaces - Open Monday - Thursday, Pay Per Hour",
                        5.0,
                        0.50,
                        R.drawable.parkingspot2));

        elementList.add(
                new Element(
                        1,
                        "1521 Hackett Ave\n" +
                                "Long Beach, CA 90815",
                        "1 parking spaces - Open Monday - Thursday, Pay Per Day",
                        4.7,
                        6,
                        R.drawable.parkingspot3));

        //creating recyclerview adapter
        ElementAdapter adapter = new ElementAdapter(this, elementList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
