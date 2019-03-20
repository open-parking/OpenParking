package com.example.openparking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.w3c.dom.Text;

public class PasswordRecovery extends AppCompatActivity {
    private EditText editTextEmail;
    private Button buttonRecover;
    private TextView textViewReturn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonRecover = (Button) findViewById(R.id.buttonRecover);
        textViewReturn= (TextView) findViewById(R.id.textViewReturn);
        mAuth = FirebaseAuth.getInstance();

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = editTextEmail.getText().toString();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(PasswordRecovery.this, "Please enter your email before clicking the Reset Button", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(PasswordRecovery.this,"Please check the email account", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PasswordRecovery.this, LoginActivity.class));
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(PasswordRecovery.this,"ERROR : "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        textViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PasswordRecovery.this, LoginActivity.class));
            }
        });

    }
}
