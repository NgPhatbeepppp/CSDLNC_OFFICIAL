package com.example.dacsdlnc_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText name,email,password;
    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }
        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);

        sharedPreferences = getSharedPreferences("onboardingScreen",MODE_PRIVATE);

        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);

        if (isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime",false);
            editor.commit();

            Intent intent= new Intent(RegistrationActivity.this,OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void signup(View view){
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Enter User Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Enter User PassWord", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() <6)
        {
            Toast.makeText(this, "PassWord too short, enter minimum 6 letters", Toast.LENGTH_SHORT).show();
            return;
        }
        //startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public void signin(View view)
    {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class   ));
    }
}