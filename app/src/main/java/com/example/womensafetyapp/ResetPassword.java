package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    Button sendResetEmail;
    EditText resetEmail;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();

        sendResetEmail = findViewById(R.id.sendResetPassword);
        resetEmail = findViewById(R.id.etResetPassword);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        sendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("SENDING LINK!!!");
                progressDialog.show();
                String mail = resetEmail.getText().toString().trim();

                if(mail.isEmpty())
                {

                    Toast.makeText(getApplicationContext(), "Please enter a registered Email", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }else
                {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Reset Password link sent to email",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ResetPassword.this, ConfirmPage.class));
                            }else
                            {

                                Toast.makeText(getApplicationContext(), "Error sending Reset Password link sent to email",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                    });
                }
            }
        });

    }
}