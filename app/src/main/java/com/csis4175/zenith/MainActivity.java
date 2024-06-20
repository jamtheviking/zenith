package com.csis4175.zenith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView tvWelcomeName;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        tvWelcomeName = findViewById(R.id.tvWelcomeName);

        user = auth.getCurrentUser();

        if (user == null) {
            Intent intentNoUser = new Intent(getApplicationContext(), Login.class);
            startActivity(intentNoUser);
            finish();
        } else {
            tvWelcomeName.setText(user.getEmail());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intentNoUser = new Intent(getApplicationContext(), Login.class);
                startActivity(intentNoUser);
                finish();
            }
        });

    }
}