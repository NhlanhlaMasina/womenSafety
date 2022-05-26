package com.example.womensafetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
        TextView newUser, forgotPassword;
        CheckBox checkBox;
        EditText email, password;
        private Button login;
        ProgressDialog progressDialog;
        FirebaseAuth firebaseAuth;


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        checkBox = findViewById(R.id.checkBox_btn);
        newUser = findViewById(R.id.txtNewUser);
        forgotPassword = findViewById(R.id.txtForgetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        login = (Button) findViewById(R.id.buttonLogin);



        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage.class));
        }

        newUser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this, RegisterPage.class));
               finish();
           }
       });


         checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked)
               {
                   password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               }else
               {
                   password.setTransformationMethod(PasswordTransformationMethod.getInstance());
               }
           }
       });




        login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mail1 = email.getText().toString();
            String pass1 = password.getText().toString();

            if(mail1.isEmpty() || pass1.isEmpty())
            {
                Toast.makeText(MainActivity.this,"Please enter Required field!", Toast.LENGTH_SHORT).show();

            }else
            {
                validate(email.getText().toString(), password.getText().toString());
            }
        }
    });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPassword.class));
            }
        });






}

    private void validate(final String mail, String pass)
    {
         progressDialog.setMessage("PLEASE WAIT!!!");
         progressDialog.show();


            firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomePage.class));
                    finish();


                }
                else
                {
                    Toast.makeText(MainActivity.this, "LOGIN FAILED!!!", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }
            }
        });

    }



}