package com.example.openparking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    private EditText editTextEmail;
    private String email;

    private EditText editTextPassword;
    private EditText editTextFirstName;
    //private EditText editTextLastName;

    private TextView textViewSignin;

    ImageView ImgUserPhoto;
    Uri pickedImgUri;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ImgUserPhoto = findViewById(R.id.imgUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22)
                {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        usersRef = mDatabase.getReference();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }


        progressDialog = new ProgressDialog(this);



        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        //editTextLastName = (EditText) findViewById(R.id.editTextLastName);


        textViewSignin = (TextView) findViewById(R.id.textViewSignin);


        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }
    private void updateUserInfo(String name, Uri pickedImgUri, final FirebaseUser currentUser)
    {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded successful

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //url containing user img

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //user info update
                                    showMessage("Register Complete");
                                    updateUI();
                                }
                            }
                        });

                    }
                });

            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String firstName = editTextFirstName.getText().toString().trim();
        //final String lastName = editTextLastName.getText().toString().trim();


        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping any further function execution
            return;
        }
        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping any further function execution
            return;
        }
        if(TextUtils.isEmpty(firstName)) {
            //first name is empty
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            //stopping any further function execution
            return;
        }
        
        //if validations are passed,
        //we will first show a progress bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //store first and last name in user instance!!
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                                    .setDisplayName(firstName)

                                    .build();
                            user.updateProfile(profileUpdates);


                            sendEmailVerification();

                            FirebaseAuth.getInstance().signOut();
                            System.out.println("Signing out");

                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Please verify your email then sign in again.", Toast.LENGTH_SHORT).show();

                            finish();

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Registration Failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }
    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);
        finish();


    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }
    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(SignupActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }
    @Override
    public void onClick(View view) {
        if(view == buttonRegister)
        {
            registerUser();
        }
        if(view == textViewSignin)
        {
            //will open login activity here
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);


        }


    }
}
