package com.isep.ii3510.a7ven0clock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String IS_CONNECTED = "isConnected";
    private static final String ARG_PARAM2 = "param2";

    private static TimerFragment timerInstance;
    private boolean spotConnection;
    private String mParam2;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param spotConnection boolean to tell if connected.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment getInstance(boolean spotConnection, String param2) {
        if(timerInstance == null){
            timerInstance = new TimerFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_CONNECTED, spotConnection);
            args.putString(ARG_PARAM2, param2);
            timerInstance.setArguments(args);
        }
        return timerInstance;
    }

    public static TimerFragment getInstance(){return getInstance(false,"");}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotConnection = getArguments().getBoolean(IS_CONNECTED);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    private void refreshTime(){
        Handler handler = new Handler();
        final Runnable r = new Runnable(){
            @Override
            public void run(){
                updateTimer();
            }
        };

        handler.postDelayed(r, 1000);
    }

    private void updateTimer(){

    }

}