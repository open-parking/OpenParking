package com.example.openparking;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openparking.Config.RecyclerTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MyParkingList extends AppCompatActivity{
    private static final String TAG = "MyParkingList";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mImagePrice = new ArrayList<>();

    List<ParkingSpace> parkingSpaceList;  // List of parking spaces that user owns


    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private ParkingSpace ps;
    private User owner;
    private Dialog myDialog;

    private RatingBar mRatingBar;

    private Intent create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerv_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        parkingSpaceList = new ArrayList<>();

        // [START initialize_database_ref]
        Log.v(TAG, "getting database reference");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, parkingSpaceList, mImageUrls);
        recyclerView.setAdapter(mAdapter);

        ps = new ParkingSpace();
        owner = new User();
        myDialog = new Dialog(this);

        create = new Intent(MyParkingList.this, CreateParkingInstanceActivity.class);

        //FireBase
        //loadImagesFromFireBase(); TODO: implement this function
        loadParkingSpacesFromDataBase("90815");

        //Test Info
        setTestImages(); // TODO: replace this function with loadImagesFromFireBase();
        //setTestData();

        //initRecyclerView();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ps = parkingSpaceList.get(position);
                System.out.println("Parking Space: " + ps.getAddress());

                retrieveOwner();



            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setTestData()
    {
        mNames.add("132 Fake St.");
        mImagePrice.add("1 Dollars");

        mNames.add("1530 Hackett Ave");
        mImagePrice.add("2 Dollars");

        mNames.add("1541 Hackett Ave");
        mImagePrice.add("3 Dollars");

        mNames.add("Rocky Mountain");
        mImagePrice.add("4 Dollars");

        mNames.add("Mahahual");
        mImagePrice.add("5 Dollars");

        mNames.add("Frozen Lake");
        mImagePrice.add("6 Dollars");

        mNames.add("White Sands Desert");
        mImagePrice.add("7 Dollars");

        mNames.add("Austrailia");
        mImagePrice.add("8 Dollars");

        mNames.add("Washington");
        mImagePrice.add("9 Dollars");
    }

    //TODO: replace this with a call to the database
    private void setTestImages()
    {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        //MyAdapter adapter = new MyAdapter(this, mNames, mImageUrls);//TO BE REPLACED
        MyAdapter adapter = new MyAdapter(this, parkingSpaceList, mImageUrls);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadParkingSpacesFromDataBase(String zipCode)
    {
        Log.v(TAG, "loadParkingSpacesFromDataBase");

        mDatabase.child("ParkingSpaces").child(zipCode).addChildEventListener( //OLD TABLE NAME WAS "ZipCodes"
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.v(TAG, "onChildAdded:" + dataSnapshot.getKey());

                        // A new parking space has been added, add it to the displayed list
                        //ParkingSpace ps = new ParkingSpace();
                        ps = dataSnapshot.getValue(ParkingSpace.class);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser fuser = firebaseAuth.getCurrentUser();
                        User user = new User();
                        user.setId(fuser.getUid());

                        if(!ps.equals(null))
                        {
                            if(ps.getOwnerID().equals(user.getId())) {
                                Log.d(TAG, "onChildAdded: " + "ps is good");

                                parkingSpaceList.add(ps);
                                Log.v(TAG, "onChildAdded:" + ps.getAddress());
                                mAdapter.notifyDataSetChanged();

                                //String address = ps.getAddress();
                                //String hours = "Hours: " + ps.getOpentime() + " to " + ps.getClosetime();
                            }
                        }
                        else
                        {
                            Log.d(TAG, "onChildAdded: " +  "ps is null");
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so displayed the changed comment.
                        //Comment newComment = dataSnapshot.getValue(Comment.class);
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                        // A comment has changed, use the key to determine if we are displaying this
                        // comment and if so remove it.
                        // commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                        // A comment has changed position, use the key to determine if we are
                        // displaying this comment and if so move it.
                        //Comment movedComment = dataSnapshot.getValue(Comment.class);
                        //String commentKey = dataSnapshot.getKey();

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                        //Toast.makeText(mContext, "Failed to load comments.",
                        //Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void loadImagesFromFireBase()
    {

    }

    private void retrieveOwner()
    {
        DatabaseReference ref = mDatabase.child("users").child(ps.getOwnerID());

        //Retrieve seller information using ps.getOwnerID()
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                owner = dataSnapshot.getValue(User.class);
                Log.d("TAG", "Read successful, Owner: " + owner.toString());

                //pass parking space object to next activity
                create.putExtra("parkingSpace", ps);


                showPopup();
            }

            @Override
            public void onStart() {
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("ONFAILURE", "Failed");
            }
        });
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener){
        System.out.println("Reached READDATA function");
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    public void showPopup() {
        TextView txtclose;
        Button btnFollow;
        TextView txtSellerName;
        TextView txtAddress;
        TextView txtIsAvailable;
        TextView txtOpenClose;
        TextView txtCost;
        RatingBar mRatingBar;

        myDialog.setContentView(R.layout.custom_window2);

        mRatingBar = (RatingBar) myDialog.findViewById(R.id.ratingBar3);
        displayRating(mRatingBar);

        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        txtSellerName = (TextView) myDialog.findViewById(R.id.txtSellerName);
        txtSellerName.setText(owner.getName());

        txtAddress = (TextView) myDialog.findViewById(R.id.txtAddress);
        txtAddress.setText(ps.getAddress() + ", " + ps.getZipcode());

        txtIsAvailable = (TextView) myDialog.findViewById(R.id.txtIsAvailable);
        if(ps.getReservedStatus())
            txtIsAvailable.setText("Sold");
        else
            txtIsAvailable.setText("Available");

        txtOpenClose = (TextView) myDialog.findViewById(R.id.txtOpenClose);
        txtOpenClose.setText("From " + ps.getOpentime() + " to " + ps.getClosetime());

        txtCost = (TextView) myDialog.findViewById(R.id.txtCost);
        txtCost.setText("$" + ps.getCost() + "0");

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void displayRating(RatingBar mRatingBar)
    {
        double rating = owner.getContributorRating();
        int numRated = owner.getTimesContributorRated();
        if(numRated == 0)
        {
            Toast.makeText(this, "This seller has not been rated.", Toast.LENGTH_SHORT).show();
        }
        else if(rating >= 4.5)
        {
            mRatingBar.setNumStars(5);
        }
        else if(rating >= 3.5)
        {
            mRatingBar.setNumStars(4);
        }
        else if(rating >= 2.5)
        {
            mRatingBar.setNumStars(3);
        }
        else if(rating >= 1.5)
        {
            mRatingBar.setNumStars(2);
        }
        else if(rating >= 0.5)
        {
            mRatingBar.setNumStars(1);
        }
        else
        {
            mRatingBar.setNumStars(0);
        }
    }
}
