package com.csis4175.zenith;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csis4175.zenith.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Test Account
 * test@gmail.com | password
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        manager = getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            navigateToLogin();
            return;
        }

        setupBottomNavigation();
        loadInitialFragment();
    }

    public FirebaseUser getUser() {
        return user;
    }

    private void navigateToLogin() {
        Intent intentNoUser = new Intent(getApplicationContext(), Login.class);
        startActivity(intentNoUser);
        finish();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigationView);
        bottomNavView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.bottom_meditate:
                        selectedFragment = new MeditateFragment();
                        break;
                    case R.id.bottom_journal:
                        selectedFragment = new JournalFragment();
                        break;
                    case R.id.bottom_routine:
                        selectedFragment = new RoutineFragment();
                        break;
                }
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadInitialFragment() {
        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

}