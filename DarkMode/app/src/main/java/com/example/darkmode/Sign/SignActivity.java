package com.example.darkmode.Sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.darkmode.MainActivity;
import com.example.darkmode.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        autoLogin();

        TextView signUp, login;
        signUp = findViewById(R.id.sign_up);
        login = findViewById(R.id.login);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void autoLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            Intent intent = new Intent(SignActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        else {
            FirebaseAuth.getInstance().signOut();
        }
    }
}
