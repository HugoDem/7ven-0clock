package com.isep.ii3510.a7ven0clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                activeFragment = AlarmFragment.newInstance("","");
                tag = "ALARM";
                break;
            case R.id.navigation_timer:
                activeFragment = TimerFragment.getInstance();
                tag = "TIMER";
                break;
            case R.id.navigation_settings:
                activeFragment = SettingsFragment.newInstance("","");
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