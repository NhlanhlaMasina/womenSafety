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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {
    CheckBox checkBox;
    EditText email, password1, confirmPass;
    TextView back;
    Button register;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String mail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getSupportActionBar().hide();

        email = findViewById(R.id.etEmailReg);
        checkBox = findViewById(R.id.checkBoxShowPassword);
        password1 = findViewById(R.id.etpasswordReg);
        confirmPass = findViewById(R.id.etpasswordRegConfirm);
        register = findViewById(R.id.buttonRegistrUser);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else
                {
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    //upload data to database
                    String userMail = email.getText().toString().trim();
                    String userPass = password1.getText().toString().trim();

                    progressDialog.setMessage("PLEASE WAIT!!!");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(userMail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                progressDialog.dismiss();
                                Toast.makeText(RegisterPage.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterPage.this, HomePage.class));
                                finish();

                            }else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterPage.this,"Registration Failed",Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }

            }
        });



        back = findViewById(R.id.tvLogin);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, MainActivity.class));
                finish();
            }
        });
    }

    private Boolean validate()
    {
        Boolean result =false;
         mail = email.getText().toString();
         pass = password1.getText().toString();
        String passCon = confirmPass.getText().toString();


        if(mail.isEmpty() || pass.isEmpty() || passCon.isEmpty()) {
            Toast.makeText(this, "Pleasse enter all the details or check if password match", Toast.LENGTH_SHORT).show();

        }else
        {
            result = true;
        }

        return result;
    }




}