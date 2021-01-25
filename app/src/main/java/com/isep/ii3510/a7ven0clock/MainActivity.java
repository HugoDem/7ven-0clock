package com.isep.ii3510.a7ven0clock;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment activeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        activeFragment = ClockFragment.getInstance();
        loadFragments("CLOCK");
    }


    public Boolean onNavigationItemSelected(@NonNull MenuItem item){
        if(activeFragment != null){
            activeFragment.onPause();
        }
        String tag = "";
        switch (item.getItemId()){
            case R.id.navigation_clock:
                activeFragment = ClockFragment.getInstance();
                tag = "CLOCK";
                break;
            case R.id.navigation_alarm:
                activeFragment = AlarmFragment.getInstance();
                tag = "ALARM";
                break;
            case R.id.navigation_timer:
                activeFragment = TimerFragment.getInstance();
                tag = "TIMER";
                break;
            case R.id.navigation_settings:
                activeFragment = SettingsFragment.getInstance();
                tag = "SETTINGS";
                break;
        }

        return loadFragments(tag);
    }

    private boolean loadFragments(String tag){
        if(activeFragment == null){
            return false;
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, activeFragment, tag).commit();
            return true;
        }
    }

}