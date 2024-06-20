package com.csis4175.zenith;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.csis4175.zenith.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    FirebaseAuth auth;
    FirebaseUser user;
    TextView tvWelcomeName;
    Button btnLogout;

    DrawerLayout drawerLayout;
    BottomNavigationView bottomMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();

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

        bottomMenu = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        replaceFragment(new HomeFragment());

        binding.bottomMenu.setBackground(null);
        bottomMenu.setOnClickListener(item -> {

            switch (item.getId()) {
                case R.id.bottom_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.bottom_meditate:
                    replaceFragment(new MeditateFragment());
                    break;
                case R.id.bottom_journal:
                    replaceFragment(new JournalFragment());
                    break;
                case R.id.bottom_routine:
                    replaceFragment(new RoutineFragment());
                    break;
            }

            return true;
        });

    }



    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}